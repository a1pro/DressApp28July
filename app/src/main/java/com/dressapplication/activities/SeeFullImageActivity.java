package com.dressapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;

public class SeeFullImageActivity extends AppCompatActivity {
    ImageButton close_gallery;
    ImageView single_image;
    String image_url;
    ProgressBar p_bar;


    int width,height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_full_image);
        p_bar=findViewById(R.id.p_bar);
        p_bar.setVisibility(View.VISIBLE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
       SeeFullImageActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        image_url=getIntent().getStringExtra("image_url");

        close_gallery=findViewById(R.id.close_gallery);
        close_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });





        single_image=findViewById(R.id.single_image);

        if (image_url!=null){
            Glide.with(getApplicationContext()).load(ApiUtils.IMAGE_BASE_URL+image_url).diskCacheStrategy(DiskCacheStrategy.ALL).into(single_image);
        }



        p_bar.setVisibility(View.GONE);


    }
}
