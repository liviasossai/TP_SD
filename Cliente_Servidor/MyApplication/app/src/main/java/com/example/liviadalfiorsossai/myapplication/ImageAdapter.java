package com.example.liviadalfiorsossai.myapplication;

/**
 * Created by liviadalfiorsossai on 3/21/15.
 */

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private Integer[] mThumbIds = {
            R.drawable.f1, R.drawable.f2, R.drawable.f3, R.drawable.f4,
            R.drawable.f5, R.drawable.f6, R.drawable.f7, R.drawable.f8,
            R.drawable.f9, R.drawable.f10, R.drawable.f11, R.drawable.f12,
            R.drawable.f13, R.drawable.f14, R.drawable.f15, R.drawable.f16,
            R.drawable.f17, R.drawable.f18, R.drawable.f19, R.drawable.f20,
            R.drawable.f21,

    };

   //public  static int[] pos;

    public int[] cover;



    private int NUM_PARES = 12;

    public ImageAdapter(Context c) {
        //pos = imagensRandomicas();
        mContext = c;


    }

    @Override
    public int getCount() {
        return cover.length;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }



    public void init(int NUM, int [] pos){

        NUM_PARES = NUM;

        cover = new int[2*NUM_PARES];
        for(int i = 0; i < 2*NUM_PARES; i++){
            if(pos[i] == 100) {
                cover[i] = R.drawable.f31;
            }
            else if(pos[i] == 200) {
                cover[i] = R.drawable.f30;
            }
            else{
                cover[i] = R.drawable.f0;
            }
        }
    }

    public void renderiza(int NUM, int [] pos){

        NUM_PARES = NUM;

        cover = new int[2*NUM_PARES];
        for(int i = 0; i < 2*NUM_PARES; i++){
            if(pos[i] == 100) {
                cover[i] = R.drawable.f31;
            }
            else if(pos[i] == 200) {
                cover[i] = R.drawable.f30;
            }
            else if(pos[i] == -10) {
                cover[i] = R.drawable.f0;
            }
            else{
                cover[i] = mThumbIds[pos[i]];
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(122, 101));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 2, 0, 2);
        } else {
            imageView = (ImageView) convertView;
        }

            imageView.setImageResource(cover[position]);


        return imageView;
    }

}

