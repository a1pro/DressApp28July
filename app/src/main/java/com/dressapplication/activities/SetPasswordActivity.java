package com.dressapplication.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.SetPasswordModel;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.SharedPrefrence.DataProccessor;
import com.dressapplication.utils.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_done,iv_back;
    private String mobile;
    private ProgressBar progressBar;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        initUI();
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        Log.e("mobile","+91 "+mobile);
    }

    private void initUI() {
        et_password=findViewById(R.id.et_password);
        iv_done=findViewById(R.id.iv_done);
        iv_back=findViewById(R.id.iv_back);
        progressBar=findViewById(R.id.progressBar);
        iv_done.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_done:

                if (NetworkUtils.isConnected(this)){
                    if (Validation()){
                        setpasswordApi();
                    }
                }else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_back:

                Intent intent=new Intent(SetPasswordActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private boolean Validation(){
        if (et_password.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setpasswordApi() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(this);
            apiInteface.setPhone(mobile,et_password.getText().toString(),"30.2512","37.2512").enqueue(new Callback<SetPasswordModel>() {
                @Override
                public void onResponse(Call<SetPasswordModel> call, Response<SetPasswordModel> response) {
                    if (response.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        SetPasswordModel data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){
                                DataProccessor dataProccessor=new DataProccessor(SetPasswordActivity.this);
                                dataProccessor.setEmail("email",data.getData().get(0).getEmail());
                                dataProccessor.setUserid("userid",data.getData().get(0).getId());
                                dataProccessor.setpassword("password",data.getData().get(0).getPassword());
                                dataProccessor.setusername("username",data.getData().get(0).getUsername());
                                savelogindata(data.getData().get(0).getEmail());
                                saveuserid(data.getData().get(0).getId());

                                Intent intent=new Intent(SetPasswordActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(SetPasswordActivity.this, "Phone no is Already register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<SetPasswordModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SetPasswordActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("register failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    private void savelogindata(String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.apply();

    }

    private void saveuserid(String userid) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userid", userid);
        editor.apply();

    }

    private String getDeviceid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SetPasswordActivity.this);
        return preferences.getString("registration_id", null);
    }

}
