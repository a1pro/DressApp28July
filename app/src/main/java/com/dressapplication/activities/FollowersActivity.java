package com.dressapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.FollowersData;
import com.dressapplication.Model.GetFollowers;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.adapters.FollowesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private FollowesAdapter adapter;
    private ImageView iv_back;
    private ProgressBar progressBar;
    List<FollowersData> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        String userid=getIntent().getStringExtra("userid");

        initUI();
        clickListener();
        if (userid!=null){
            GetFollowers(userid);
        }


    }

    private void clickListener() {
        iv_back.setOnClickListener(this);
    }

    private void initUI() {
        progressBar=findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
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


    private void GetFollowers(String Userid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(FollowersActivity.this);
            apiInteface.getFollowers(Userid).enqueue(new Callback<GetFollowers>() {
                @Override
                public void onResponse(Call<GetFollowers> call, Response<GetFollowers> response) {
                    if (response.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        GetFollowers data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                list.addAll(data.getData());
                                //   Toast.makeText(getContext(), ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                                adapter=new FollowesAdapter(FollowersActivity.this,list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(FollowersActivity.this));
                                recyclerView.setAdapter(adapter);

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(FollowersActivity.this, "Profile"+data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetFollowers> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FollowersActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("follower failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
