package com.dressapplication.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

public class LoginWithPhoneActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rl_login_with_email;
    private ImageView iv_tick;
    private EditText phoneNumber,et_password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_phone);
        initUI();
        phoneNumber.addTextChangedListener(manageAcWatcher);
        et_password.addTextChangedListener(manageAcWatcher);
    }

    private void initUI() {
        progressBar=findViewById(R.id.progressBar);
        et_password=findViewById(R.id.et_password);
        phoneNumber=findViewById(R.id.phone_number);
        ImageView iv_back = findViewById(R.id.iv_back);
        rl_login_with_email=findViewById(R.id.rl_login_with_email);
        iv_tick=findViewById(R.id.iv_tick);
        iv_tick.setOnClickListener(this);
        rl_login_with_email.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
//            case R.id.rl_login_with_email:
                finish();
                break;
            case R.id.iv_tick:
            if (NetworkUtils.isConnected(LoginWithPhoneActivity.this)){
                if (Validations()){
                    LoginApi(phoneNumber.getText().toString(),et_password.getText().toString());
                }
            }else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
                break;
        }
    }

    private boolean Validations (){
        if (phoneNumber.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (et_password.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return  true;
    }
//    private void sendtOtp() {
//            if(phoneNumber.getText().toString().isEmpty() || phoneNumber.getText().toString().length() < 10){
//                phoneNumber.setError("Enter a valid mobile");
//                phoneNumber.requestFocus();
//                return;
//            }
//            Intent intent = new Intent(LoginWithPhoneActivity.this, EnterOtpActivity.class);
//            intent.putExtra("mobile",phoneNumber.getText().toString().trim());
//            startActivity(intent);
//        }


    private void LoginApi(String phone,String password) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(this);
            apiInteface.LoginApi(phone,password,"1",getDeviceid()).enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                    if (response.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        LoginData data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                Toast.makeText(LoginWithPhoneActivity.this, "Login"+data.getStatus(), Toast.LENGTH_SHORT).show();
                                DataProccessor dataProccessor = new DataProccessor(LoginWithPhoneActivity.this);
                                dataProccessor.setEmail("email",data.getData().get(0).getEmail());
                                dataProccessor.setUserid("userid",data.getData().get(0).getId());
                                dataProccessor.setpassword("password",data.getData().get(0).getPassword());
                                dataProccessor.setusername("username",data.getData().get(0).getUsername());
                                savelogindata(data.getData().get(0).getEmail());
                                saveuserid(data.getData().get(0).getId());
                                Intent intent=new Intent(LoginWithPhoneActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginWithPhoneActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginWithPhoneActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Login phone failure",""+t.getMessage());
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginWithPhoneActivity.this);
        return preferences.getString("registration_id", null);
    }

    private TextWatcher manageAcWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phone = phoneNumber.getText().toString().trim();
            String password = et_password.getText().toString().trim();


            if (!phone.isEmpty() && !password.isEmpty() ) {
                iv_tick.setImageDrawable(getResources().getDrawable(R.mipmap.red_tick));
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}
