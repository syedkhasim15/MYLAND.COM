package com.example.mylandcom;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Homepage_frag extends Fragment {


    ImageView img1,img2,img3,img4;
    Animation topAnim,bottomAnim,left,right;
    public static  final  String s_key = "key";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view;
        view = inflater.inflate(R.layout.image_downloading, container, false);
        img1 = view.findViewById(R.id.house);
        img2 = view.findViewById(R.id.land);
        img3 = view.findViewById(R.id.field);
        img4= view.findViewById(R.id.building);



        topAnim = AnimationUtils.loadAnimation(getContext(), R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(getContext(),R.anim.bottom_animation);
        left =AnimationUtils.loadAnimation(getContext(), R.anim.left_to_right);
        right = AnimationUtils.loadAnimation(getContext(), R.anim.right_to_left);

        img1.setAnimation(left);
        img2.setAnimation(right);
        img3.setAnimation(left);
        img4.setAnimation(right);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_activity(1);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_activity(2);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_activity(3);
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_activity(4);
            }
        });

        return view;
    }

    void open_activity(int con)
    {
        Intent intent = new Intent(getContext(), Images_display.class);
        intent.putExtra(s_key,con);
        startActivity(intent);
    }
}