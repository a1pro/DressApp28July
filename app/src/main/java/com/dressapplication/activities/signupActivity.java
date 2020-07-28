package com.dressapplication.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.utils.NetworkUtils;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class signupActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout linear_email, linear_phone;
    private Button btn_email, btn_phone;
    private String signupas = "Email";
    private EditText et_username, et_email, et_dob, et_password, et_con_password, et_phone;
    private ImageView img_signup, iv_back;
    private ProgressBar progressBar;
    private RelativeLayout rl_login_with_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        rl_login_with_phone = findViewById(R.id.rl_login_with_phone);
        progressBar = findViewById(R.id.progressBar);
        iv_back = findViewById(R.id.iv_back);
        linear_email = findViewById(R.id.linear_email);
        linear_phone = findViewById(R.id.linear_phone);
        btn_email = findViewById(R.id.btn_email);
        btn_phone = findViewById(R.id.btn_phone);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_dob = findViewById(R.id.et_dob);
        et_password = findViewById(R.id.et_password);
        et_con_password = findViewById(R.id.et_con_password);
        et_phone = findViewById(R.id.et_phone);
        img_signup = findViewById(R.id.img_signup);
        img_signup.setOnClickListener(this);
        btn_email.setOnClickListener(this);
        btn_phone.setOnClickListener(this);
        rl_login_with_phone.setOnClickListener(this);
        et_dob.setOnClickListener(this);
        iv_back.setOnClickListener(this);


        et_username.addTextChangedListener(manageAcWatcher);
        et_email.addTextChangedListener(manageAcWatcher);
        et_dob.addTextChangedListener(manageAcWatcher);
        et_password.addTextChangedListener(manageAcWatcher);
        et_con_password.addTextChangedListener(manageAcWatcher);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_email:
                linear_phone.setVisibility(View.GONE);
                linear_email.setVisibility(View.VISIBLE);
                btn_email.setBackgroundDrawable(getResources().getDrawable(R.drawable.pink_btn_back));
                btn_email.setTextColor(Color.WHITE);
                btn_phone.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_btn_back));
                signupas = "Email";
                break;

            case R.id.btn_phone:
                linear_email.setVisibility(View.GONE);
                linear_phone.setVisibility(View.VISIBLE);
                btn_email.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_btn_back));
                btn_phone.setBackgroundDrawable(getResources().getDrawable(R.drawable.pink_btn_back));
                btn_phone.setTextColor(Color.WHITE);
                signupas = "Phone";
                break;

            case R.id.img_signup:
                if (NetworkUtils.isConnected(this)) {
                    if (signupas.equalsIgnoreCase("Email")) {
                        if (Validation_for_Email()) {
                            HitSignupApiForEmail(et_username.getText().toString(),
                                    et_email.getText().toString(),
                                    et_dob.getText().toString(),
                                    et_password.getText().toString(),
                                    et_con_password.getText().toString());
                        }
                    } else {
                        sendtOtp();
                    }

                } else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.rl_login_with_phone:
                Intent intent = new Intent(signupActivity.this, LoginWithEmailActivity.class);
                startActivity(intent);
                //  finish();
                break;

            case R.id.et_dob:
                showCalener();
                break;

            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void showCalener() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(signupActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                et_dob.setText(dayOfMonth + "-"
                        + (month + 1) + "-" + year);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void sendtOtp() {
        if (et_phone.getText().toString().isEmpty() || et_phone.getText().toString().length() < 10) {
            et_phone.setError("Enter a valid mobile");
            et_phone.requestFocus();
            return;
        }
        Intent intent = new Intent(signupActivity.this, EnterOtpActivity.class);
        intent.putExtra("mobile", et_phone.getText().toString().trim());
        startActivity(intent);
    }

    private void HitSignupApiForEmail(String username, final String email, String dob, String password, String con_password) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(this);
            apiInteface.RegisterApi(username, email, dob, password, con_password).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                Toast.makeText(signupActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                savelogindata(email);
                                Intent intent = new Intent(signupActivity.this, LoginWithEmailActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(signupActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(signupActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("register failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean Validation_for_Email() {
        String password = et_password.getText().toString();
        if (et_username.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_dob.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter D.O.B", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_con_password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!et_con_password.getText().toString().equals(password)) {
            Toast.makeText(this, "Password & Confirm password doen't match", Toast.LENGTH_SHORT).show();
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

    private TextWatcher manageAcWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String username = et_username.getText().toString().trim();
            String dob = et_dob.getText().toString().trim();
            String email = et_email.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            String confirmpass = et_con_password.getText().toString();


            if (!email.isEmpty() && !password.isEmpty() && !username.isEmpty() && !dob.isEmpty() && !confirmpass.isEmpty()) {
                img_signup.setImageDrawable(getResources().getDrawable(R.mipmap.red_tick));
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
