package com.example.mylandcom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Homepage extends AppCompatActivity {

    ImageView I1,I2,I3,I4;
    SharedPreferences sharedPreferences;
    private long pressedTime;
    BottomNavigationView bottomNav;
    int con=0;
    public static  final  String s_key = "key";

    private BroadcastReceiver mNetworkReceiver;
    static TextView tv_check_connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Intent intetBackground = new Intent(this, FirebasePush.class);
        startService(intetBackground);

        Intent intent = getIntent();
        String email = intent.getStringExtra(Login_page.s_eamil);
        tv_check_connection=(TextView)findViewById(R.id.connect);
        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();
//        full_screen();

        //bottom navigation part
        sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        final String[] it = new String[1];

        bottomNav = findViewById(R.id.bottomNav);
        frag_imp(new Homepage_frag());
        bottomNav.setOnNavigationItemSelectedListener(item -> {

            it[0] =item.getTitle().toString();

            if(it[0].equals("Home"))
                frag_imp(new Homepage_frag());
            else if (it[0].equals("Upload"))
            {
                Intent in = new Intent(this, Images_uploading.class);
                startActivity(in);
            }
            else if(it[0].equals("Account"))
                startActivity(new Intent(Homepage.this, User_Profile.class));
            else if(it[0].equals("Guide"))
                frag_imp(new User_frag());
            return true;
        });

    }

    public void frag_imp(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.frag , fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                if(s.charAt(0)=='H' || s.charAt(0)=='h')
                    con=1;
                else if(s.charAt(0)=='L' || s.charAt(0)=='l')
                    con=2;
                else if(s.charAt(0)=='C' || s.charAt(0)=='c')
                    con=3;
                else if(s.charAt(0)=='A' || s.charAt(0)=='a' || s.charAt(0)=='f' || s.charAt(0)=='F')
                    con=4;

                Intent intent = new Intent(Homepage.this, Images_display.class);
                intent.putExtra(s_key,con);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.myprofile)
        {
            startActivity(new Intent(Homepage.this, User_Profile.class));
        }
        else if(item.getItemId()==R.id.search)
        {

        }
        else if(item.getItemId()==R.id.notify)
        {

        }
        else if(item.getItemId()==R.id.signout)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("key", 0);
            editor.apply();
            startActivity(new Intent(Homepage.this,Login_page.class));
        }
        return super.onOptionsItemSelected(item);
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

    public static void dialog(boolean value){

        if(value){
            tv_check_connection.setText("We are back !!!");
            tv_check_connection.setBackgroundColor(Color.GREEN);
            tv_check_connection.setTextColor(Color.WHITE);

            Handler handler = new Handler();
            Runnable delayrunnable = new Runnable() {
                @Override
                public void run() {
                    tv_check_connection.setVisibility(View.GONE);
                }
            };
            handler.postDelayed(delayrunnable, 3000);
        }else {
            tv_check_connection.setVisibility(View.VISIBLE);
            tv_check_connection.setText("Could not connect to internet");
            tv_check_connection.setBackgroundColor(Color.RED);
            tv_check_connection.setTextColor(Color.WHITE);
        }
    }


    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
}

//    public void full_screen()
//    {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
//    }

