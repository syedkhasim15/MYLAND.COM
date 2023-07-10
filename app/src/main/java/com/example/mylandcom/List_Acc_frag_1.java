package com.example.mylandcom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class List_Acc_frag_1 extends ArrayAdapter<String> {

    private String[] arr;
    public List_Acc_frag_1(@NonNull Context context, int resource, @NonNull String[] arr) {
        super(context, resource, arr);
        this.arr = arr;
    }         //CONSTRUCTOR WE SHOULD INCLUDE........ JUST PRESS ALT PRESS ENTER AFTER CREATING THE CLASS

    @Nullable
    @Override
    public String getItem(int position) {
        return arr[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list1_layout , parent , false) ;
        TextView t = convertView.findViewById(R.id.textView);
        t.setText(getItem(position));
        return convertView;
    }
}


