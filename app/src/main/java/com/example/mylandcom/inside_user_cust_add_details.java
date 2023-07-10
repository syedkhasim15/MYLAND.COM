package com.example.mylandcom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class inside_user_cust_add_details extends AppCompatActivity {

    TextView t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_user_cust_add_details);

        Intent intent = getIntent();
        int i = intent.getIntExtra("khasim",0);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);

        if(i==0)
        {
            t1.setText("TERMS AND CONDITIONS");
            t2.setText(getResources().getString(R.string.terms));
        }
        else if(i==1)
        {
            t1.setText("WHO WE ARE");
            t2.setText(getResources().getString(R.string.terms));
        }
        else if(i==2)
        {
            t1.setText("CUSTOMER CARE");
            t2.setText(getResources().getString(R.string.terms));
        }
        else if(i==3)
        {
            t1.setText("CUSTOMER CARE");
            t2.setText(getResources().getString(R.string.terms));
        }



        Toast.makeText(this, " "+i+" ", Toast.LENGTH_SHORT).show();

    }
}