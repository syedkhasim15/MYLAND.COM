package com.example.mylandcom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Profile extends AppCompatActivity {

    private CircleImageView pro;
    TextView name,email;
    private EditText Username,First,Last,Email,Gender,Phone,Bio;
    private Button mSaveBtn;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private String Uid;
    private Uri mImageUri = null;
    private ProgressBar progressBar;
    String susername ;
    String sfirst;
    String slast;
    String semail ;
    String sgender ;
    String sbio;
    String snum ;
    private boolean isPhotoSelected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_set_up);
        setContentView(R.layout.my_profile);

        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();
//
        pro = findViewById(R.id.profile);
        Username = findViewById(R.id.e1);
        First = findViewById(R.id.e2);
        Last = findViewById(R.id.e3);
        Email = findViewById(R.id.e4);
        Bio = findViewById(R.id.e6);
        Phone = findViewById(R.id.n1);
        mSaveBtn = findViewById(R.id.Save);
        progressBar = findViewById(R.id.progressbar);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        progressBar.setVisibility(View.VISIBLE);
////
//
        firestore.collection("Users").document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    if (task.getResult().exists()){
                        susername = task.getResult().getString("User");
                        sfirst = task.getResult().getString("First");
                        slast = task.getResult().getString("Last");
                        semail = task.getResult().getString("Email");
                        sbio = task.getResult().getString("Bio");
                        snum =  task.getResult().getString("Phone");
                        String imageUrl = task.getResult().getString("image");

                        Username.setText(susername);
                        name.setText(susername);
                        First.setText(sfirst);
                        Last.setText(slast);
                        Email.setText(semail);
                        email.setText(semail);
                        Bio.setText(sbio);
                        Phone.setText(snum);
                        mImageUri = Uri.parse(imageUrl);
//
////                        Glide.with(User_Profile.this).load(imageUrl).into(circleImageView);
                        Glide.with(User_Profile.this).load(imageUrl).into(pro);
//                        pro.setImageURI(mImageUri);
                    }
                }
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                susername = Username.getText().toString();
                sfirst = First.getText().toString();
                slast = Last.getText().toString();
                semail = Email.getText().toString();
                sbio = Bio.getText().toString();
                snum =  Phone.getText().toString();
//
                StorageReference imageRef = storageReference.child("Profile_pics").child(Uid + ".jpg");
                progressBar.setVisibility(View.VISIBLE);
                if (isPhotoSelected) {
                    if (!susername.isEmpty() && mImageUri != null) {
                        imageRef.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Toast.makeText(User_Profile.this, "if", Toast.LENGTH_SHORT).show();
                                            saveToFireStore(task, uri);
                                        }
                                    });

                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(User_Profile.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(User_Profile.this, "Please Select picture and write your name", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(User_Profile.this, "else", Toast.LENGTH_SHORT).show();
                    saveToFireStore(null ,mImageUri);
                }
            }
        });
//
//        circleImageView.setOnClickListener(new View.OnClickListener() {
          pro.setOnClickListener(new View.OnClickListener()
         {
            @Override
            public void onClick(View v) {
                    Intent galleryIntent = new Intent();
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent , 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            mImageUri = data.getData();
            isPhotoSelected = true;
//            circleImageView.setImageURI(mImageUri);
            pro.setImageURI(mImageUri);

        }
    }


    private void saveToFireStore(Task<UploadTask.TaskSnapshot> task, Uri downloadUri) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("User", susername);
        map.put("First", sfirst);
        map.put("Last", slast);
        map.put("Email", semail);
        map.put("Gender", sgender);
        map.put("Phone", snum);
        map.put("Bio", sbio);
        map.put("image", downloadUri.toString());

        firestore.collection("Users").document(Uid).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(User_Profile.this, "Profile Settings Saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(User_Profile.this, Homepage.class));
                    finish();
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(User_Profile.this, "came in firestore", Toast.LENGTH_SHORT).show();
                    Toast.makeText(User_Profile.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}