package com.dressapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAddActvity extends AppCompatActivity {
    private TextView tv_message;
    private Button bt_submit;
    private EditText et_report;
    private ProgressBar progressBar;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_add_actvity);
        tv_message = findViewById(R.id.tv_message);
        bt_submit = findViewById(R.id.bt_submit);
        et_report = findViewById(R.id.et_report);
        progressBar = findViewById(R.id.progressBar);
        iv_back=findViewById(R.id.iv_back);

        String message = getIntent().getStringExtra("message");
        String ownerid = getIntent().getStringExtra("ownerid");
        String videoid = getIntent().getStringExtra("videoid");
        if (message != null) {
            tv_message.setText(message);
        }

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected(ReportAddActvity.this)) {
                    if (ownerid != null && videoid != null) {
                        Getfollowing(videoid, ownerid, message, et_report.getText().toString());
                    }
                } else {
                    Toast.makeText(ReportAddActvity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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


    private void Getfollowing(String videoid, String videoownerid, String title, String content) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(ReportAddActvity.this);
            apiInteface.reportUser(videoid, videoownerid, title, content).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {

                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                Toast.makeText(ReportAddActvity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ReportAddActvity.this, "Profile" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ReportAddActvity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("follower failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
