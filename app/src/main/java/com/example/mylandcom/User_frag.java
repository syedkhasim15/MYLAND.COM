package com.example.mylandcom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class User_frag extends Fragment {


    ListView listView;
    TextView Username,email;
    String[] listItem;
    String su_name,semail;
    ImageView img;
    Uri mImageUri;
    FirebaseFirestore firestore;
    private FirebaseAuth auth;
    String Uid;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_user_frag, container, false);
        listView = (ListView) view.findViewById(R.id.list1);
        listItem = getResources().getStringArray(R.array.my_account);
//        progressBar = (ProgressBar)view.findViewById(R.id.progressbar);
        Username = view.findViewById(R.id.u_name);
        email = view.findViewById(R.id.email);
        img = view.findViewById(R.id.profile);


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();

        List_Acc_frag_1 ad = new List_Acc_frag_1(getContext(),R.layout.list1_layout,listItem);   //custom adopter
        listView.setAdapter(ad);

        listView. setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                open_activity(i);
            }
        });

        firestore.collection("Users").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
//                    progressBar.setVisibility(View.INVISIBLE);
                    if (task.getResult().exists()){
                        su_name = task.getResult().getString("First")+task.getResult().getString("Last");
                        semail = task.getResult().getString("Email");
                        String imageUrl = task.getResult().getString("image");
                        Username.setText(su_name);
                        email.setText(semail);
                        mImageUri = Uri.parse(imageUrl);
                        Glide.with(User_frag.this).load(imageUrl).into(img);
                    }
                }
            }
        });

        return view;
    }


    public void open_activity(int con)
    {

        if(con==0)
        {
            Intent intent = new Intent(getContext() ,inside_user_cust_add_details.class);
            intent.putExtra("khasim",con);
            startActivity(intent);
        }
        if(con==1)
        {
            Intent intent = new Intent(getContext() , inside_user_cust_add_details.class);
            intent.putExtra("khasim",con);
            startActivity(intent);
        }
        if(con==2)
        {
            Intent intent = new Intent(getContext() , inside_user_cust_add_details.class);
            intent.putExtra("khasim",con);
            startActivity(intent);
        }
        if(con==3)
        {
            Intent intent = new Intent(getContext() ,  Settings.class);
            startActivity(intent);
        }


    }

}