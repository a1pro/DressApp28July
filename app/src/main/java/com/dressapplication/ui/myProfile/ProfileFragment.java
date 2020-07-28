package com.dressapplication.ui.myProfile;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.activities.EditProfileActivity;
import com.dressapplication.activities.FindFriendsActivity;
import com.dressapplication.activities.FollowersActivity;
import com.dressapplication.activities.FollowingActivity;
import com.dressapplication.activities.PrivacyAndSettings;
import com.dressapplication.activities.SeeFullImageActivity;
import com.dressapplication.activities.Signup_chooseActivity;
import com.dressapplication.adapters.ViewpagerAdapterProfile;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private TextView tv_name;
    private ProfileViewModel profileViewModel;
    private RecyclerView rv_profile_likes;
    private ImageView iv_add_user,iv_dot;
    private LinearLayout ll_following,layout_alert,layout_main;
    private Intent i;
    private Button btn_edit_profile,bt_signup;
    private TextView tvTitle,tv_username,number_following,no_followers,number_likes,tv_videocount;
    private ProgressBar progressBar;
    private CircleImageView profile_image;
    private ViewPager view_pager;
    private ViewpagerAdapterProfile viewPagerAdapter;
    private TabLayout tablayout;
    private LinearLayout layout_follower;
    private int[] tabIcons = {R.mipmap.filter,R.mipmap.like};
    private LinearLayout layout_like;
    private String Username;
    private int likes;
    private String imageurl;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        initUI(root);
        clickListerner();

        if (getuserid()!=null){
            GetProfile(getuserid());
            layout_main.setVisibility(View.VISIBLE);
            layout_alert.setVisibility(View.GONE);
        }else {
            layout_alert.setVisibility(View.VISIBLE);
            layout_main.setVisibility(View.GONE);
        }

//        Toast.makeText(getContext(), ""+getuserid(), Toast.LENGTH_SHORT).show();


        return root;
    }
    private void initUI(View root) {
        tv_videocount=root.findViewById(R.id.tv_videocount);
        bt_signup=root.findViewById(R.id.bt_signup);
        layout_main=root.findViewById(R.id.layout_main);
        layout_alert=root.findViewById(R.id.layout_alert);
        layout_like=root.findViewById(R.id.layout_like);
        layout_follower=root.findViewById(R.id.layout_follower);
        view_pager = root.findViewById(R.id.view_pager);
        profile_image=root.findViewById(R.id.profile_image);
        progressBar=root.findViewById(R.id.progressBar);
        tvTitle=root.findViewById(R.id.tvTitle);
        tv_username=root.findViewById(R.id.tv_username);
        number_following=root.findViewById(R.id.number_following);
        no_followers=root.findViewById(R.id.no_followers);
        number_likes=root.findViewById(R.id.number_likes);

        iv_dot=root.findViewById(R.id.iv_dot);
        iv_add_user=root.findViewById(R.id.iv_add_user);
        ll_following=root.findViewById(R.id.ll_following);
        btn_edit_profile=root.findViewById(R.id.btn_edit_profile);
        btn_edit_profile.setOnClickListener(this);
        ll_following.setOnClickListener(this);
        layout_like.setOnClickListener(this);

        viewPagerAdapter = new ViewpagerAdapterProfile(getChildFragmentManager(),getuserid());
        view_pager.setAdapter(viewPagerAdapter);
        tablayout = root.findViewById(R.id.tablayout);
        tablayout.setupWithViewPager(view_pager);

        if (tablayout!=null){
            if (tablayout.getTabAt(0)!=null)
                tablayout.getTabAt(0).setIcon(tabIcons[0]);

            if (tablayout.getTabAt(1)!=null)
                tablayout.getTabAt(1).setIcon(tabIcons[1]);

        }
    }

    private void clickListerner() {
        iv_add_user.setOnClickListener(this);
        iv_dot.setOnClickListener(this);
        btn_edit_profile.setOnClickListener(this);
        layout_follower.setOnClickListener(this);
        bt_signup.setOnClickListener(this);
        profile_image.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_user:
                i=new Intent(getContext(), FindFriendsActivity.class);
                startActivity(i);
                break;
            case R.id.iv_dot:
                i=new Intent(getContext(), PrivacyAndSettings.class);
                startActivity(i);
                break;
            case R.id.ll_following:
               Intent i2=new Intent(getContext(), FollowingActivity.class);
                i2.putExtra("userid",getuserid());
                startActivity(i2);
                break;
            case R.id.btn_edit_profile:
                    i=new Intent(getContext(), EditProfileActivity.class);
                    startActivity(i);
                break;
            case R.id.layout_follower:
                Intent intent=new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("userid",getuserid());
                startActivity(intent);
                break;
                
            case R.id.layout_like:
                ShoWLikeDialog(likes);
                break;

            case R.id.bt_signup:
                Intent intent1=new Intent(getContext(), Signup_chooseActivity.class);
                startActivity(intent1);
                break;

            case R.id.profile_image:
                if (imageurl!=null){
                    Intent intent3=new Intent(getContext(), SeeFullImageActivity.class);
                    intent3.putExtra("image_url",imageurl);
                    startActivity(intent3);
                }
                break;
        }
    }

    private void ShoWLikeDialog(int like) {
        final Dialog dialog = new Dialog(getContext());
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


    private void GetProfile(String Userid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.GetProfile(Userid,getuserid()).enqueue(new Callback<GetProfileModel>() {
                @Override
                public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                    if (response.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        GetProfileModel data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                             //   Toast.makeText(getContext(), ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                                if (data.getPostData().size()>0){
                                    tv_videocount.setText(data.getPostData().size()+" Videos");
                                }else {
                                    tv_videocount.setText("0 Videos");
                                }

                                if (data.getUserData()!=null){
                                     Username=data.getUserData().get(0).getUsername();
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
                                        likes=data.getUserData().get(0).getPostLikeCount();
                                        number_likes.setText(""+data.getUserData().get(0).getPostLikeCount());
                                    }
                                    else {
                                        number_likes.setText("0");
                                    }
                                    if (data.getUserData().get(0).getProfileImage()!=null && !data.getUserData().get(0).getProfileImage().isEmpty()){
                                        imageurl=data.getUserData().get(0).getProfileImage();
                                        Glide.with(getApplicationContext()).load(ApiUtils.IMAGE_BASE_URL+data.getUserData().get(0).getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_image);
                                    }
                                }
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                              //  Toast.makeText(getContext(), "Profile"+data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("view profile failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("userid", null);
    }


    @Override
    public void onResume() {
        super.onResume();
            GetProfile(getuserid());

    }

    @Override
    public void onStart() {
        super.onStart();
            GetProfile(getuserid());
    }






}
