package com.example.mylandcom;

import androidx.appcompat.app.AppCompatActivity;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_screen extends AppCompatActivity {

    Animation topAnim,bottomAnim,left,right;
    ImageView imageView;
    TextView app_name;
    int autoSave;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        full_screen();
//////
//////                    // on below line we are
//////                    // starting a new activity.
//                    startActivity(i);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_splash);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        left =AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        right = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        imageView = findViewById(R.id.imageView2);
        app_name = findViewById(R.id.appName);

        imageView.setAnimation(topAnim);
        app_name.setAnimation(bottomAnim);

        //LOGIN ONCE
        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        int j = sharedPreferences.getInt("key", 0);


        if(j>0)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // on below line we are
                    // creating a new
                    Intent i = new Intent(Splash_screen.this, Homepage.class);
                    // on below line we are
                    // starting a new activity
                    startActivity(i);
                    // on the below line we are finishing
                    // our current activity.
                    finish();
                }
            }, 3000);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // on below line we are
                    // creating a new intent
                    Intent i = new Intent(Splash_screen.this, Login_page.class);
//                    Pair[] pairs = new Pair[2];
//                pairs[0] = new Pair(imageView,"splash_image");
//                pairs[1] = new Pair(app_name,"splash_text");
//
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splash_screen.this,pairs);
                    // on below line we are
                    // starting a new activity
//                    startActivity(i,options.toBundle());
                    startActivity(i);

                    // on the below line we are finishing
                    // our current activity.
                    finish();
                }
            }, 4000);
        }
//         on below line we are calling handler to run a task
//         for specific time interval

    }
//
        public void full_screen ()
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        }


}