package com.example.mylandcom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_activity extends AppCompatActivity {

    EditText email,password;
    Button register;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_act);
        full_screen();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_pass = password.getText().toString();
                
                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pass))
                {
                    Toast.makeText(Register_activity.this, "Please enter the credentials correctly", Toast.LENGTH_SHORT).show();
                }
                else if(txt_pass.length()<6)
                {
                    Toast.makeText(Register_activity.this, "password length should be greater then 6", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    acc_registration(txt_email,txt_pass);
                }
               
            }
        });

    }

    public void acc_registration(String email,String password)
    {
        Toast.makeText(this, email+password, Toast.LENGTH_SHORT).show();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register_activity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    Intent intent = new Intent(Register_activity.this, User_Profile.class);
                    Toast.makeText(Register_activity.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(Register_activity.this, "Registration is failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void full_screen()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    public void onBackPressed() {
        startActivity(new Intent(Register_activity.this,Login_page.class));
    }
}