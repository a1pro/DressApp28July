package com.dressapplication.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.SharedPrefrence.DataProccessor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PrivacyAndSettings extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView iv_back;
    private TextView tv_manage_my_account,tv_push_notification,tv_logout;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_and_settings);
        initUI();
        ClickLister();
        /*toolbar.setTitle("");
        setSupportActionBar(toolbar);*/
    }


    private void initUI() {
        tv_logout=findViewById(R.id.tv_logout);
        tv_push_notification=findViewById(R.id.tv_push_notification);
        toolbar=findViewById(R.id.toolbar);
        iv_back = findViewById(R.id.iv_back);
        toolbar = findViewById(R.id.toolbar);
        tv_manage_my_account=findViewById(R.id.tv_manage_my_account);
    }
    private void ClickLister() {
        iv_back.setOnClickListener(this);
        tv_manage_my_account.setOnClickListener(this);
        tv_push_notification.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_manage_my_account:
                 i=new Intent(getApplicationContext(),ManageMyAcActivity.class);
                startActivity(i);
                break;
            case R.id.tv_push_notification:
                 i=new Intent(getApplicationContext(),PushNotificationActivity.class);
                startActivity(i);
                break;
            case R.id.tv_logout:
                hitLogoutApi("4");
                break;
        }
    }
    private void hitLogoutApi(String userid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PrivacyAndSettings.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               // progressBar.setVisibility(View.VISIBLE);
                ApiInterface apiInteface;
                apiInteface = ApiUtils.getAPIService(PrivacyAndSettings.this);
                apiInteface.logout(userid).enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.isSuccessful()) {
                            //progressBar.setVisibility(View.GONE);
                            ResponseData responseData = response.body();
                            if (responseData != null) {
                                if (responseData.getCode().equalsIgnoreCase("201")) {
                                    Toast.makeText(PrivacyAndSettings.this, "" + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                                    i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                    DataProccessor dataProccessor=new DataProccessor(PrivacyAndSettings.this);
                                    dataProccessor.setUserid("userid",null);
                                    saveuserid(null);
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                      //  progressBar.setVisibility(View.GONE);
                        Toast.makeText(PrivacyAndSettings.this, "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).show();
    }

    private void saveuserid(String userid) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userid", userid);
        editor.apply();

    }

}
