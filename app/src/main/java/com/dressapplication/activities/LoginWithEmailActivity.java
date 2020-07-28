package com.dressapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.LoginData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.SharedPrefrence.DataProccessor;
import com.dressapplication.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginWithEmailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back,img_login;
    private TextView tv_forgot_password;
    private String forgot_password_string;
    private RelativeLayout rl_login_with_phone;
    private EditText et_email,et_password;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        clickListener();

        et_email.addTextChangedListener(manageAcWatcher);
        et_password.addTextChangedListener(manageAcWatcher);
    }

    private void initUI() {
        progressBar=findViewById(R.id.progressBar);
        img_login=findViewById(R.id.img_login);
        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        iv_back=findViewById(R.id.iv_back);
        rl_login_with_phone=findViewById(R.id.rl_login_with_phone);
        tv_forgot_password=findViewById(R.id.tv_forgot_password);
        forgot_password_string=("Forgot password?\n"
                + "<font color=\"#FF0084\"> Get help signing in. </font>");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_forgot_password.setText(Html.fromHtml(forgot_password_string, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tv_forgot_password.setText(Html.fromHtml(forgot_password_string));
        }

    }

    private void clickListener(){
        img_login.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_forgot_password.setOnClickListener(this);
        rl_login_with_phone.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_forgot_password:
                Toast.makeText(getApplicationContext(),"Forgot password",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_login_with_phone:
                Intent intent=new Intent(getApplicationContext(),LoginWithPhoneActivity.class);
                startActivity(intent);
                break;

            case R.id.img_login:
                if (NetworkUtils.isConnected(LoginWithEmailActivity.this)){
                    if (validation()){
                        LoginApi(et_email.getText().toString(),et_password.getText().toString());
                    }
                }
                else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void LoginApi(String email,String password) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(this);
            apiInteface.LoginApi(email,password,"1",getDeviceid()).enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                    if (response.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        LoginData data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                Toast.makeText(LoginWithEmailActivity.this, "Login"+data.getStatus(), Toast.LENGTH_SHORT).show();
                                DataProccessor dataProccessor = new DataProccessor(LoginWithEmailActivity.this);
                                dataProccessor.setEmail("email",data.getData().get(0).getEmail());
                                dataProccessor.setUserid("userid",data.getData().get(0).getId());
                                dataProccessor.setpassword("password",data.getData().get(0).getPassword());
                                dataProccessor.setusername("username",data.getData().get(0).getUsername());
                                savelogindata(data.getData().get(0).getEmail());
                                saveuserid(data.getData().get(0).getId());
                                Intent intent=new Intent(LoginWithEmailActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginWithEmailActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginWithEmailActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("register failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean validation(){
        if (et_email.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (et_password.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginWithEmailActivity.this);
        return preferences.getString("registration_id", null);
    }

    private TextWatcher manageAcWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email = et_email.getText().toString().trim();
            String password = et_password.getText().toString().trim();


            if (!email.isEmpty() && !password.isEmpty() ) {
                img_login.setImageDrawable(getResources().getDrawable(R.mipmap.red_tick));
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
