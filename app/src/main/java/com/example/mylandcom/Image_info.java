package com.example.mylandcom;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mylandcom.Adapter.PostAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Image_info extends AppCompatActivity {

    //downloading of the images
    public class Background_task extends AsyncTask<String,Void , String>{

        Vibrator vibrator;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("tag","on pre");
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("tag","on post");
            Toast.makeText(Image_info.this,"Image downlaoded",Toast.LENGTH_SHORT).show();
            vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(300);

        }

        @Override
        protected String doInBackground(String... strings) {
                Uri images;
                ContentResolver contentResolver =  getContentResolver();

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
                {
                    images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                }
                else
                {
                    images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }

                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,System.currentTimeMillis()+".jpg");
                contentValues.put(MediaStore.Images.Media.MIME_TYPE,"images/*");
                Uri uri = contentResolver.insert(images,contentValues);

                try {

                    BitmapDrawable  bitmapDrawable = (BitmapDrawable) post.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    OutputStream outputStream = null;
                    try {
                        outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return "something went wrong";
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    Objects.requireNonNull(outputStream);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            return null;
        }
    }




    CircleImageView profile;
    ScaleGestureDetector scaleGestureDetector;
    ImageView post,download,share;
    TextView user,phone,email,lat,lng,details,img_type,sq_feet,caption,date;
    Uri u_profile,u_post;
    Button approach,get_loc,listen;
    ScrollView scrollView;
    TextToSpeech textToSpeech;
    String s;
    private static final int REQUEST_CODE=1;

    OutputStream outputStream;
    int PERMISSION_ID = 44;
    int flag=1;

    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_info);

        user = findViewById(R.id.user);
        profile = findViewById(R.id.profile_pic);
        approach = findViewById(R.id.approach);
        post = findViewById(R.id.post);
        download = findViewById(R.id.download);
        listen = findViewById(R.id.listen);
        sq_feet = findViewById(R.id.sq_feet);
        lat = findViewById(R.id.lat);
        lng = findViewById(R.id.lng);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        details = findViewById(R.id.details);
        caption = findViewById(R.id.caption);
        get_loc = findViewById(R.id.location);
        scrollView =findViewById(R.id.scrollview);
        share = findViewById(R.id.share);


        Intent intent = getIntent();
        user.setText(intent.getStringExtra("user"));
        phone.setText(intent.getStringExtra("phone"));
        email.setText(intent.getStringExtra("email"));
        lat.setText(intent.getStringExtra("latitude"));
        lng.setText(intent.getStringExtra("longitude"));
        sq_feet.setText(intent.getStringExtra("sq_feet"));
        details.setText(intent.getStringExtra("details"));
        caption.setText(intent.getStringExtra("caption"));

        u_profile = Uri.parse(intent.getStringExtra("profile"));
        u_post = Uri.parse(intent.getStringExtra("post"));

        Glide.with(this).load(u_profile).into(profile);
        Glide.with(this).load(u_post).into(post);

        //background task
        //Text to speech
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i==TextToSpeech.SUCCESS)
                {
                    int lang= textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        Context context = getApplicationContext();

        //listen
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(flag==1)
                {
                   s = intent.getStringExtra("details");
//                    Intent intent = new Intent(Image_info.this,TTs.class);
//                    intent.putExtra("string",s);
//                    context.startService(new Intent(context, TTs.class));
                    textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);
                    flag=0;
                    listen.setText("STOP");
                }
                else
                {
//                    stopService(new Intent(Image_info.this, TTs.class));
                    if (textToSpeech != null) {
                        textToSpeech.stop();
                    }
                    flag=1;
                    listen.setText("Listen");
                }

            }
        });


            download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(Image_info.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                {
//                    saveImage();
                      new Background_task().execute();
                }
                else
                {
                    ActivityCompat.requestPermissions(Image_info.this,new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },REQUEST_CODE);

                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share_Image();
            }
        });



//

        //image download
        ActivityCompat.requestPermissions(Image_info.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(Image_info.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);



        approach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = intent.getStringExtra("phone");
                Toast.makeText(Image_info.this, "tel"+phone, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
            }
        });


        get_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String latitude = lat.getText().toString();
                String longitude = lng.getText().toString();
                String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude+ " (" + "Location" + ")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

    }


    public void share_Image()
    {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) post.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Uri uri = getImageToShare(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,uri);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent,"Share Image"));

    }

    public Uri getImageToShare(Bitmap bitmap)
    {
        File folder = new File(getCacheDir(),"images");
        Uri uri = null;
        try {
            folder.mkdir();
            File file = new File(folder, "shared_image.jpg");
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(this,"com.example.mylandcom",file);

        }
        catch (Exception e)
        {
            Toast.makeText(this," "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return uri;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Background_task my_task = new Background_task();
        if(requestCode == REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
//                saveImage();
//                my_task.execute();
            }
            else
            {
                Toast.makeText(Image_info.this, "provide the reequest permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void  saveImage()
    {
        Uri images;
        ContentResolver contentResolver =  getContentResolver();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
        {
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        }
        else
        {
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,System.currentTimeMillis()+".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"images/*");
        Uri uri = contentResolver.insert(images,contentValues);

        try {

            BitmapDrawable  bitmapDrawable = (BitmapDrawable) post.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            OutputStream outputStream = null;
            try {
                outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            Objects.requireNonNull(outputStream);

            Toast.makeText(Image_info.this,"image saved successfully",Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void onStop()
    {
        super.onStop();

        if(textToSpeech != null){
            textToSpeech.shutdown();
        }
    }

}

