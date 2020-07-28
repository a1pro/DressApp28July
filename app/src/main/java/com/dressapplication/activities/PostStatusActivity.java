package com.dressapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.dressapplication.R;

public class PostStatusActivity extends AppCompatActivity implements View.OnClickListener{
    String postStatus=null;

private ImageView iv_back,img_public,img_freinds,img_private;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_status);

        iv_back=findViewById(R.id.iv_back);
        img_public=findViewById(R.id.img_public);
        img_freinds=findViewById(R.id.img_freinds);
        img_private=findViewById(R.id.img_private);

        iv_back.setOnClickListener(this);
        img_public.setOnClickListener(this);
        img_freinds.setOnClickListener(this);
        img_private.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.img_public:
                postStatus="1";
                img_public.setImageDrawable(getResources().getDrawable(R.mipmap.red_tick));
                img_freinds.setImageDrawable(getResources().getDrawable(R.mipmap.checked_iicon));
                img_private.setImageDrawable(getResources().getDrawable(R.mipmap.checked_iicon));
               Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",postStatus);
                        setResult(RESULT_OK,returnIntent);
                        finish();
                    }
                },500);

                break;

            case R.id.img_freinds:
                postStatus="2";

                img_public.setImageDrawable(getResources().getDrawable(R.mipmap.checked_iicon));
                img_freinds.setImageDrawable(getResources().getDrawable(R.mipmap.red_tick));
                img_private.setImageDrawable(getResources().getDrawable(R.mipmap.checked_iicon));
                Handler handler1=new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent returnIntent1 = new Intent();
                        returnIntent1.putExtra("result",postStatus);
                        setResult(RESULT_OK,returnIntent1);
                        finish();
                    }
                },500);


                break;

            case R.id.img_private:
            postStatus="3";
                img_public.setImageDrawable(getResources().getDrawable(R.mipmap.checked_iicon));
                img_freinds.setImageDrawable(getResources().getDrawable(R.mipmap.checked_iicon));
                img_private.setImageDrawable(getResources().getDrawable(R.mipmap.red_tick));
                Handler handler2=new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent returnIntent2 = new Intent();
                        returnIntent2.putExtra("result",postStatus);
                        setResult(RESULT_OK,returnIntent2);
                        finish();
                    }
                },500);

                break;
        }
    }


}
