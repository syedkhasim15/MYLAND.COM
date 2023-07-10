package com.example.mylandcom.Adapter;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mylandcom.Image_info;
import com.example.mylandcom.Model.Post;
import com.example.mylandcom.Model.Users;
import com.example.mylandcom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.Console;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter  extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {


    private List<Post> mList;
    private List<Users> usersList;
    private Activity context;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    public ImageView img;
    public static final  String key = "PostAdapter";

    Context app_context;
    public PostAdapter(Activity context , List<Post> mList , List<Users> usersList, Context app_context){
        this.mList = mList;
        this.context = context;
        this.usersList = usersList;
        this.app_context = app_context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.each_post , parent , false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        Post post = mList.get(position);
        long milliseconds = post.getTime().getTime();
        String date  = DateFormat.format("MM/dd/yyyy" , new Date(milliseconds)).toString();

        holder.setPostPic(post.getImage());
        holder.setPostCaption(post.getCaption());
        holder.setPostDate(date);

        Users user = usersList.get(position);
        holder.setPostUsername(user.getUser());
        holder.setProfilePic(user.getImage());

        holder.postPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,Image_info.class);

                intent.putExtra("user",user.getUser());
                intent.putExtra("profile",user.getImage());
                intent.putExtra("email",user.getEmail());
                intent.putExtra("phone",user.getPhone());
                intent.putExtra("bio",user.getBio());
                intent.putExtra("date",date);
                intent.putExtra("first",user.getFirst());
                intent.putExtra("last",user.getLast());
                intent.putExtra("post",post.getImage());
                intent.putExtra("caption",post.getCaption());
                intent.putExtra("latitude",post.getLat());
                intent.putExtra("longitude",post.getLng());
                intent.putExtra("details",post.getDetails());
                intent.putExtra("img_type",post.getImg_type());
                intent.putExtra("date",post.getTime());
                intent.putExtra("sq_feet",post.getSq_feet());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView  postPic,deleteBtn;
        CircleImageView profilePic ;
        TextView postUsername , postDate , postCaption,postEmail;
        View mView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            postPic = mView.findViewById(R.id.user_post);
            profilePic = mView.findViewById(R.id.profile_pic);
            postUsername = mView.findViewById(R.id.username_tv);
            postEmail = mView.findViewById(R.id.email);
            postDate = mView.findViewById(R.id.date_tv);
            postCaption = mView.findViewById(R.id.caption_tv);
        }

        public void setPostUsername(String username){
            postUsername.setText(username);
        }

        public void setProfilePic(String urlProfile){
            Glide.with(context).load(urlProfile).into(profilePic);
        }

        public void setPostEmail(String email)
        {
            postEmail.setText(email);
        }

        public void setPostPic(String urlPost){
            Glide.with(context).load(urlPost).into(postPic);
        }


        public void setPostDate(String date){
            postDate.setText(date);
        }
        public void setPostCaption(String caption){
            postCaption.setText(caption);
        }


    }
}