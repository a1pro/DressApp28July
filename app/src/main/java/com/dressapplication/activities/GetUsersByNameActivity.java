package com.dressapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Interface.Getusers;
import com.dressapplication.Model.FriendData;
import com.dressapplication.Model.GetFriendList;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.SharedPrefrence.DataProccessor;
import com.dressapplication.adapters.GetUserAdapter;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUsersByNameActivity extends AppCompatActivity {
private ProgressBar progressBar;
private RecyclerView recyclerView;
private GetUserAdapter adapter;
private EditText et_username;
List<FriendData> list=new ArrayList<>();
private TextView tv_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_users_by_name);
        progressBar=findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        et_username=findViewById(R.id.et_username);


        DataProccessor dataProccessor=new DataProccessor(GetUsersByNameActivity.this);
        String userid=dataProccessor.getUserid("userid");

        if (userid!=null){
            getdata(userid);
        }


        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void filter(String text) {
        ArrayList<FriendData> filteredList = new ArrayList<>();
        for (FriendData item : list) {
            if (item.getUsername().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    private boolean validation(){
        if (et_username.getText().toString().isEmpty()){
            Toast.makeText(this, "Please Enter a Username", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private void getdata(String userid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(GetUsersByNameActivity.this);
            apiInteface.GetFriendList(userid).enqueue(new Callback<GetFriendList>() {
                @Override
                public void onResponse(Call<GetFriendList> call, Response<GetFriendList> response) {
                    if (response.isSuccessful()){
                        list.clear();
                        progressBar.setVisibility(View.GONE);
                        GetFriendList data=response.body();
                        if (data!=null){

                            if (data.getCode().equalsIgnoreCase("201")){

                                list.addAll(data.getData());
                                //   Toast.makeText(getContext(), ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                                adapter=new GetUserAdapter(GetUsersByNameActivity.this,list,getusers);
                                recyclerView.setLayoutManager(new LinearLayoutManager(GetUsersByNameActivity.this));
                                recyclerView.setAdapter(adapter);

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(GetUsersByNameActivity.this, ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetFriendList> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(GetUsersByNameActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("follower failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    Getusers getusers=new Getusers() {
        @Override
        public void getUsername(String name) {
         //   Log.e("name",""+name);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("username",name);
            setResult(RESULT_OK,returnIntent);
            finish();
        }
    };


}
