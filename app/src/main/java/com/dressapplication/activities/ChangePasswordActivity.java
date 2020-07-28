package com.dressapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.SharedPrefrence.DataProccessor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageView img_done,iv_back;
    private EditText et_oldpassword, et_newpassword, et_conpassword;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        progressBar = findViewById(R.id.progressBar);
        et_oldpassword = findViewById(R.id.et_oldpassword);
        et_newpassword = findViewById(R.id.et_newpassword);
        et_conpassword = findViewById(R.id.et_conpassword);
        img_done = findViewById(R.id.img_done);
        iv_back=findViewById(R.id.iv_back);
        DataProccessor dataProccessor = new DataProccessor(ChangePasswordActivity.this);
        String password = dataProccessor.getPassword("password");
        userid=dataProccessor.getUserid("userid");
        Log.e("password", password);

//        if (password != null) {
//            et_oldpassword.setText(password);
//        }

        et_oldpassword.addTextChangedListener(manageAcWatcher);
        et_newpassword.addTextChangedListener(manageAcWatcher);
        et_conpassword.addTextChangedListener(manageAcWatcher);

        img_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validations()) {
                    managesave(et_oldpassword.getText().toString()
                            ,et_newpassword.getText().toString()
                            ,et_conpassword.getText().toString(),userid);
                }
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    private TextWatcher manageAcWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String olpassword = et_oldpassword.getText().toString().trim();
            String newpassword = et_newpassword.getText().toString().trim();
            String conpassword = et_conpassword.getText().toString().trim();

            if (!olpassword.isEmpty() && !newpassword.isEmpty() && !conpassword.isEmpty()) {
                img_done.setImageDrawable(getResources().getDrawable(R.mipmap.red_tick));
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private void managesave(String oldpassword, String newpass, String conf, String Userid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(ChangePasswordActivity.this);
            apiInteface.changePassword(newpass, conf, oldpassword, Userid).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                Toast.makeText(ChangePasswordActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ChangePasswordActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ChangePasswordActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("manage failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean Validations() {
        String conpassword = et_conpassword.getText().toString();

        if (et_oldpassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Old Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_newpassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter Old Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!conpassword.equalsIgnoreCase(et_newpassword.getText().toString())) {
            Toast.makeText(this, "Password and Confirm password are not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
