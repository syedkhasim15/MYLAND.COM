package com.example.mylandcom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_page extends AppCompatActivity {

    Button bt1;
    TextView register_acc;
    EditText email,password;
    FirebaseAuth auth;
    SharedPreferences sharedPreferences;
    int autoSave;
    public  static final String s_eamil="hahah";
    private long pressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_new);
          full_screen();
        bt1 = findViewById(R.id.button1);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        register_acc= findViewById(R.id.register_acc);
        auth = FirebaseAuth.getInstance();


        //"autoLogin" is a unique string to identify the instance of this shared preference
        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_email = email.getText().toString();
                String txt_pass= password.getText().toString();
//                Intent intent = new Intent(Login_page.this,Homepage.class);
//                intent.putExtra(s_eamil,txt_email);
//                startActivity(intent);
//                startActivity(new Intent(Login_page.this, Homepage.class));
                login_user(txt_email, txt_pass);
            }
        });

        register_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_page.this,Register_activity.class));
                finish();
            }
        });

   }
    public void login_user(String email,String pass)
    {

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Please enter the credentials correctly", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, email+pass, Toast.LENGTH_SHORT).show();
            auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //LOGIN ONCE
                    autoSave = 1;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("key", autoSave);
                    editor.apply();

                    startActivity(new Intent(Login_page.this, Homepage.class));
                    finish();
                }
            });
            Toast.makeText(this, "waiting", Toast.LENGTH_SHORT).show();
        }

    }
    public void full_screen()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }


    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();

    }

    }
