package com.dressapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Interface.CommonPlayInterface;
import com.dressapplication.Model.GetHashTag;
import com.dressapplication.Model.HashData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.adapters.RvHashTagAdapter;
import com.dressapplication.change.CommonWatchActivity;
import com.dressapplication.utils.Home_Get_Set;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HashTagActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rv_hashtag;
    private RvHashTagAdapter rvHashTagAdapter;
    private ImageView iv_back;
    private ProgressBar progressBar;
    List<HashData> list=new ArrayList<>();
    private TextView tv_hashtag;
    private ImageView img_video;
    List<Home_Get_Set> data_list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_tag);
        initUI();
        clickListener();

        String hash=getIntent().getStringExtra("hashvalue");
        if (hash!=null){
            gethashtag(hash);
            tv_hashtag.setText(hash);
        }

        rv_hashtag.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    img_video.setVisibility(View.GONE);
                } else {
                    // Scrolling down
                    img_video.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void initUI() {
        img_video=findViewById(R.id.img_video);
        tv_hashtag=findViewById(R.id.tv_hashtag);
        iv_back=findViewById(R.id.iv_back);
        rv_hashtag=findViewById(R.id.rv_hashtag);
        progressBar=findViewById(R.id.progressBar);
        img_video.setOnClickListener(this);

    }
    private void clickListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_video:
                Intent intent=new Intent(HashTagActivity.this,MyCameraActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    private void gethashtag (String hashtag) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(HashTagActivity.this);
            apiInteface.getHashTagVideoData(hashtag).enqueue(new Callback<GetHashTag>() {
                @Override
                public void onResponse(Call<GetHashTag> call, Response<GetHashTag> response) {
                    if (response.isSuccessful()){
                        list.clear();
                        progressBar.setVisibility(View.GONE);
                        GetHashTag data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                list.addAll(data.getData());
                             //     Toast.makeText(HashTagActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();


                                rvHashTagAdapter=new RvHashTagAdapter(HashTagActivity.this,list,commonPlayInterface);
                                rv_hashtag.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
                                rv_hashtag.setAdapter(rvHashTagAdapter);

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(HashTagActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetHashTag> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(HashTagActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("hashtag failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    CommonPlayInterface commonPlayInterface=new CommonPlayInterface() {
        @Override
        public void getWatch(int pos) {
            data_list.clear();
            for (int i=0;i<list.size();i++){
                Home_Get_Set item=new Home_Get_Set();
                item.video_url=ApiUtils.VIDEO_BASE_URL+list.get(i).getVideoName();
                item.thum=ApiUtils.VIDEO_BASE_URL+list.get(i).getVideoThumImg();
                item.userid=list.get(i).getUserId();
                item.video_id=list.get(i).getVid();
                item.video_description=list.get(i).getVideoDescribe();
                item.longi=list.get(i).getLongitude();
                item.lat=list.get(i).getLatitude();
                item.liked=list.get(i).getLoginUserIsLiked();
                item.profile_pic=ApiUtils.IMAGE_BASE_URL+list.get(i).getProfile_image();
                item.first_name=list.get(i).getUsername();
                data_list.add(item);
            }

            OpenWatchVideo(pos);
        }
    };


    private void OpenWatchVideo(int postion) {
        Intent intent=new Intent(HashTagActivity.this, CommonWatchActivity.class);
        intent.putExtra("arraylist", (Serializable) data_list);
        intent.putExtra("position",postion);
        startActivity(intent);

    }

}
