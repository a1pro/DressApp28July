package com.dressapplication.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.FollowingData;
import com.dressapplication.Model.GetFollowing;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.adapters.RvFollowingAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rv_following;
    private RvFollowingAdapter adapter;
    private ImageView iv_back;
    private ProgressBar progressBar;
    List<FollowingData> list =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        initUI();
        clickListener();
        String userid=getIntent().getStringExtra("userid");
        if (userid!=null){
            Getfollowing(userid);
        }

    }

    private void clickListener() {
        iv_back.setOnClickListener(this);
    }

    private void initUI() {
        progressBar=findViewById(R.id.progressBar);
        rv_following=findViewById(R.id.rv_following);
        iv_back=findViewById(R.id.iv_back);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }



    private void Getfollowing(String Userid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(FollowingActivity.this);
            apiInteface.GetFollowing(Userid).enqueue(new Callback<GetFollowing>() {
                @Override
                public void onResponse(Call<GetFollowing> call, Response<GetFollowing> response) {
                    if (response.isSuccessful()){
                        list.clear();
                        progressBar.setVisibility(View.GONE);
                        GetFollowing data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                list.addAll(data.getData());

                              //     Toast.makeText(FollowingActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                                   if (list.size()>0){
                                       adapter=new RvFollowingAdapter(FollowingActivity.this,list);
                                       rv_following.setLayoutManager(new LinearLayoutManager(FollowingActivity.this));
                                       rv_following.setAdapter(adapter);
                                   }

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(FollowingActivity.this, "Profile"+data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetFollowing> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FollowingActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("follower failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
