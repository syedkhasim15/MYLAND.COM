package com.example.mylandcom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Images_uploading extends AppCompatActivity {


    private ImageView img1,img2,img3,img4;
    Animation topAnim,bottomAnim;
    public static  final  String s_image = "img";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_uploading);
        full_screen();

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        img1.setAnimation(topAnim);
        img2.setAnimation(topAnim);
        img3.setAnimation(bottomAnim);
        img4.setAnimation(bottomAnim);

        Toast.makeText(this, "coming", Toast.LENGTH_SHORT).show();
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_activity("House");
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_activity("Land");
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_activity("Crop");
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goto_activity("Apt");
            }
        });
    }

    private void goto_activity(String img_type)
    {
        Intent intent = new Intent(Images_uploading.this, Product_uploading.class);
        intent.putExtra(s_image,img_type);
        startActivity(intent);
    }

    public void full_screen()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

}