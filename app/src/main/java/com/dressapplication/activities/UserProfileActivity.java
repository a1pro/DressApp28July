package com.dressapplication.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.GetProfileModel;
import com.dressapplication.Model.GetUserByName;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.adapters.ViewpagerAdapterProfile;
import com.google.android.material.tabs.TabLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView iv_back,iv_dot;
    private LinearLayout ll_following;
    private int likescount;
    private Button btn_edit_profile;
    String Userid;
    private ProgressBar progressBar;
    private boolean followunfollow;
    private TextView tvTitle,tv_username,number_following,no_followers,number_likes,tv_videocount;
    private CircleImageView profile_image;
    private ViewPager view_pager;
    private ViewpagerAdapterProfile viewPagerAdapter;
    private TabLayout tablayout;
    private LinearLayout layout_follower,layout_like;
    private String user;
    private int[] tabIcons = {R.mipmap.filter,R.mipmap.like};
    private String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initUI();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
         Userid=getIntent().getStringExtra("userid_");
         user=getIntent().getStringExtra("GetProfile");


        if (user!=null){
            String newUser=  user.replaceAll("@","");
            getdata(newUser);
        }

        if (Userid!=null){
            GetProfile(Userid);
            setViewpager(Userid);
        }else {
        //    Toast.makeText(this, "userid null", Toast.LENGTH_SHORT).show();
        }

      //  Log.e("Userid",""+Userid);



    }

    private void initUI() {
        tv_videocount=findViewById(R.id.tv_videocount);
        layout_like=findViewById(R.id.layout_like);
        layout_follower=findViewById(R.id.layout_follower);
        view_pager = findViewById(R.id.view_pager);
        profile_image=findViewById(R.id.profile_image);
        progressBar=findViewById(R.id.progressBar);
        tvTitle=findViewById(R.id.tvTitle);
        tv_username=findViewById(R.id.tv_username);
        number_following=findViewById(R.id.number_following);
        no_followers=findViewById(R.id.no_followers);
        number_likes=findViewById(R.id.number_likes);

        toolbar=findViewById(R.id.toolbar);
        iv_dot=findViewById(R.id.iv_dot);
        iv_back=findViewById(R.id.iv_back);

        ll_following=findViewById(R.id.ll_following);
        btn_edit_profile=findViewById(R.id.btn_edit_profile);
        ll_following.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_dot.setOnClickListener(this);
        btn_edit_profile.setOnClickListener(this);
        layout_follower.setOnClickListener(this);
        layout_like.setOnClickListener(this);
        profile_image.setOnClickListener(this);

    }

    private  void setViewpager(String user){
        viewPagerAdapter = new ViewpagerAdapterProfile(getSupportFragmentManager(),user);
        view_pager.setAdapter(viewPagerAdapter);
        tablayout =findViewById(R.id.tablayout);
        tablayout.setupWithViewPager(view_pager);

        if (tablayout!=null){
            if (tablayout.getTabAt(0)!=null)
                tablayout.getTabAt(0).setIcon(tabIcons[0]);

            if (tablayout.getTabAt(1)!=null)
                tablayout.getTabAt(1).setIcon(tabIcons[1]);

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_following:
               Intent i2=new Intent(getApplicationContext(), FollowingActivity.class);
                i2.putExtra("userid",Userid);
                startActivity(i2);
                break;
            case R.id.btn_edit_profile:
                FollowUnfollow(getuserid(),Userid);
                break;

            case R.id.layout_like:
                ShoWLikeDialog(likescount);
                break;

            case R.id.layout_follower:
                Intent intent=new Intent(UserProfileActivity.this,FollowersActivity.class);
                intent.putExtra("userid",Userid);
                startActivity(intent);
                break;

            case R.id.profile_image:
                if (imageurl!=null){
                    Intent intent1=new Intent(UserProfileActivity.this,SeeFullImageActivity.class);
                    intent1.putExtra("image_url",imageurl);
                    startActivity(intent1);
                }
                break;
        }
    }



    private void FollowUnfollow(String followeby,String followto) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(UserProfileActivity.this);
            apiInteface.FollowUnfollow(followeby,followto).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        ResponseData data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                Toast.makeText(UserProfileActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();

                                if (followunfollow == false) {
                                    btn_edit_profile.setText("UnFollow");
                                    followunfollow = true;
                                } else {
                                    btn_edit_profile.setText("Follow");
                                    followunfollow = false;
                                }
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(UserProfileActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UserProfileActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Follow failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Userid!=null){
            GetProfile(Userid);
            setViewpager(Userid);

        }
    }

    private void GetProfile(String Userid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(UserProfileActivity.this);
            apiInteface.GetProfile(Userid,getuserid()).enqueue(new Callback<GetProfileModel>() {
                @Override
                public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                    if (response.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        GetProfileModel data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                Toast.makeText(UserProfileActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();

                                if (data.getUserData()!=null){
                                    String Username=data.getUserData().get(0).getUsername();
                                    if (Username!=null){
                                        tvTitle.setText(""+Username);
                                        tv_username.setText(Username);
                                    }

                                    if (data.getPostData().size()>0){
                                        tv_videocount.setText(data.getPostData().size()+" Videos");
                                    }
                                    if(data.getUserData().get(0).getFollowersCount()!=null){
                                        no_followers.setText(""+data.getUserData().get(0).getFollowersCount());
                                    }
                                    else {
                                        no_followers.setText("0");
                                    }
                                    if(data.getUserData().get(0).getFollowingCount()!=null){
                                        number_following.setText(""+data.getUserData().get(0).getFollowingCount());
                                    }
                                    else {
                                        number_following.setText("0");
                                    }
                                    if(data.getUserData().get(0).getPostLikeCount()!=null){
                                        number_likes.setText(""+data.getUserData().get(0).getPostLikeCount());
                                         likescount=data.getUserData().get(0).getPostLikeCount();
                                    }
                                    else {
                                        number_likes.setText("0");
                                    }
                                    if (data.getUserData().get(0).getProfileImage()!=null && !data.getUserData().get(0).getProfileImage().isEmpty()){
                                         imageurl=data.getUserData().get(0).getProfileImage();
                                        Glide.with(UserProfileActivity.this).load(ApiUtils.IMAGE_BASE_URL+data.getUserData().get(0).getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_image);
                                    }


                                        if (data.getUserData().get(0).getIsfollowing().equalsIgnoreCase("1")){
                                        btn_edit_profile.setText("UnFollow");
                                        followunfollow=false;
                                        }else {
                                            btn_edit_profile.setText("Follow");
                                            followunfollow=true;
                                        }


                                }

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(UserProfileActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UserProfileActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("view profile failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getdata(String username) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(UserProfileActivity.this);
            apiInteface.getUsername(username).enqueue(new Callback<GetUserByName>() {
                @Override
                public void onResponse(Call<GetUserByName> call, Response<GetUserByName> response) {
                    if (response.isSuccessful()){

                        progressBar.setVisibility(View.GONE);
                        GetUserByName data=response.body();
                        if (data!=null){

                            if (data.getCode().equalsIgnoreCase("201")){

                                if (data.getUserData()!=null){

                                    if (Userid==null){
                                        Userid=data.getUserData().get(0).getId();
                                        setViewpager(Userid);
                                    }

                                    if (data.getPostData().size()>0){
                                        tv_videocount.setText(data.getPostData().size()+" Videos");
                                    }

                                    String Username=data.getUserData().get(0).getUsername();
                                    if (Username!=null){
                                        tvTitle.setText(""+Username);
                                        tv_username.setText(Username);
                                    }
                                    if(data.getUserData().get(0).getFollowersCount()!=null){
                                        no_followers.setText(""+data.getUserData().get(0).getFollowersCount());
                                    }
                                    else {
                                        no_followers.setText("0");
                                    }
                                    if(data.getUserData().get(0).getFollowingCount()!=null){
                                        number_following.setText(""+data.getUserData().get(0).getFollowingCount());
                                    }
                                    else {
                                        number_following.setText("0");
                                    }
                                    if(data.getUserData().get(0).getPostLikeCount()!=null){
                                        number_likes.setText(""+data.getUserData().get(0).getPostLikeCount());
                                        likescount=data.getUserData().get(0).getPostLikeCount();
                                    }
                                    else {
                                        number_likes.setText("0");
                                    }
                                    if (data.getUserData().get(0).getProfileImage()!=null && !data.getUserData().get(0).getProfileImage().isEmpty()){
                                         imageurl=data.getUserData().get(0).getProfileImage();
                                        Glide.with(getApplicationContext()).load(ApiUtils.IMAGE_BASE_URL+data.getUserData().get(0).getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_image);
                                    }

                                    if (data.getUserData().get(0).getIsfollowing().equalsIgnoreCase("1")){
                                        btn_edit_profile.setText("UnFollow");
                                        followunfollow=false;
                                    }else {
                                        btn_edit_profile.setText("Follow");
                                        followunfollow=true;
                                    }
                                }

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(UserProfileActivity.this, "Profile"+data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetUserByName> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UserProfileActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("follower failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ShoWLikeDialog(int like) {
        final Dialog dialog = new Dialog(UserProfileActivity.this);
        dialog.setContentView(R.layout.likedialog);
        dialog.setCanceledOnTouchOutside(false);
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        TextView tv_likes_dialog=dialog.findViewById(R.id.tv_likes_dialog);
        tv_likes_dialog.setText(" Total recevied " +like+ " Likes");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // set the custom dialog components - text, image and button
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UserProfileActivity.this);
        return preferences.getString("userid", null);
    }


}

