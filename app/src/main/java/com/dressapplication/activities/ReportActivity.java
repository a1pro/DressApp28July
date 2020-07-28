package com.dressapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dressapplication.R;

public class ReportActivity extends AppCompatActivity {
private ListView listview;
private String messages []={"Dangerous organizations and individuals","Illegal activities and regulated goods","Violent and graphic content","Animal cruelty","Suicide or self-harm","Hate speech","Harassment or bullying","Pornography and nudity","Minor safety","Spam","Intellectual property infringment","Other"};
private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        iv_back=findViewById(R.id.iv_back);
        String ownerid=getIntent().getStringExtra("ownerid");
        String videoid=getIntent().getStringExtra("videoid");
        listview=findViewById(R.id.listview);

        ArrayAdapter adapter = new ArrayAdapter<String>(ReportActivity.this,android.R.layout.simple_list_item_1, messages);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mess=messages[position];
                if (ownerid!=null && videoid !=null) {
                    Intent intent = new Intent(ReportActivity.this, ReportAddActvity.class);
                    intent.putExtra("message", mess);
                    intent.putExtra("ownerid", ownerid);
                    intent.putExtra("videoid", videoid);
                    startActivity(intent);
                }else {
                    Toast.makeText(ReportActivity.this, "Empty Video & Owner ID", Toast.LENGTH_SHORT).show();
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
}
