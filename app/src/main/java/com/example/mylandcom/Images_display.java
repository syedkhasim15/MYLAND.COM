package com.example.mylandcom;

import  androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.mylandcom.Adapter.PostAdapter;
import com.example.mylandcom.Model.Post;
import com.example.mylandcom.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class Images_display extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    Animation topAnim,bottomAnim,left,right;
    private RecyclerView mRecyclerView;
    private PostAdapter adapter;
    private List<Post> list;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private List<Users> usersList;
    int num;
    Homepage_frag homepage_frag = new Homepage_frag();

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images_display);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Images_display.this));
        list = new ArrayList<>();
        usersList = new ArrayList<>();
        adapter = new PostAdapter(Images_display.this , list, usersList,getApplicationContext());

        mRecyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        num = intent.getIntExtra(homepage_frag.s_key,1);



        left = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        swipeRefreshLayout.setAnimation(left);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                usersList.clear();
                refresh();
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });

        if (firebaseAuth.getCurrentUser() != null){

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom = !mRecyclerView.canScrollVertically(1);
                    if (isBottom)
                        Toast.makeText(Images_display.this, "Reached Bottom", Toast.LENGTH_SHORT).show();
                }
            });

            if(num ==1)
                query = firestore.collection("posts").whereEqualTo("Img_type","House");
            else if(num==2)
                query = firestore.collection("posts").whereEqualTo("Img_type","Land");
            else if(num==3)
                query = firestore.collection("posts").whereEqualTo("Img_type","Crop");
            else if(num==4)
                query = firestore.collection("posts").whereEqualTo("Img_type","Apt");


//            query = firestore.collection("Posts").orderBy("time" , Query.Direction.DESCENDING);
//            query = firestore.collection("posts").orderBy("time" , Query.Direction.DESCENDING);
//            query = firestore.collection("posts").whereEqualTo("Img_type","House");
                refresh();

        }

    }


    public void refresh()
    {
        listenerRegistration = query.addSnapshotListener(Images_display.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String postId = doc.getDocument().getId();
                        Post post = doc.getDocument().toObject(Post.class).withId(postId);
                        String postUserId = doc.getDocument().getString("user");
                        firestore.collection("Users").document(postUserId).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            Users users = task.getResult().toObject(Users.class);
                                            usersList.add(users);
                                            list.add(post);
                                            adapter.notifyDataSetChanged();
                                        }else{
                                            Toast.makeText(Images_display.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }else{
                        adapter.notifyDataSetChanged();
                    }
                }
                listenerRegistration.remove();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null){
            startActivity(new Intent(Images_display.this , Login_page.class));
            finish();
        }else{
            String currentUserId = firebaseAuth.getCurrentUser().getUid();
            firestore.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if (!task.getResult().exists()){
                            startActivity(new Intent(Images_display.this , User_Profile.class));
                            finish();
                        }
                    }
                }
            });
        }

    }

}