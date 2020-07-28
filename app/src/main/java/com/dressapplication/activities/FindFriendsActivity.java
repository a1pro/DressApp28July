package com.dressapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dressapplication.R;

public class FindFriendsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private RelativeLayout layout_invite,layout_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        initUI();
        clickListener();
    }

    private void initUI() {
        layout_invite=findViewById(R.id.layout_invite);
        iv_back=findViewById(R.id.iv_back);
        layout_phone=findViewById(R.id.layout_phone);
        iv_back.setOnClickListener(this);
        layout_invite.setOnClickListener(this);
        layout_phone.setOnClickListener(this);
    }

    private void clickListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;

            case R.id.layout_invite:
                Intent intent=new Intent(FindFriendsActivity.this,InviteFriendsActivity.class);
                startActivity(intent);
                break;

            case R.id.layout_phone:
                Intent intent2=new Intent(FindFriendsActivity.this,InviteFriendsActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
