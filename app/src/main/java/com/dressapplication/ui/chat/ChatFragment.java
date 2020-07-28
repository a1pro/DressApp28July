package com.dressapplication.ui.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.dressapplication.Interface.AddchatUser;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.ChatData;
import com.dressapplication.Model.ChatMessage;
import com.dressapplication.Model.FriendData;
import com.dressapplication.Model.GetFriendList;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.activities.Signup_chooseActivity;
import com.dressapplication.adapters.TapChatAdapter;
import com.dressapplication.utils.NetworkUtils;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatFragment extends Fragment {
    private ChatViewModel dashboardViewModel;
    private RecyclerView rv_chat, recyclerView;
    RvChatAdapter rvChatAdapter;
    TapChatAdapter adapter;
    private LinearLayout layout_alert,layout_main;
    private ProgressBar progressBar;
    List<FriendData> list = new ArrayList<>();
    List<ChatData> list2 = new ArrayList<>();
    private Button bt_signup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        initUI(root);
        if (getuserid()!=null){
            GetChatMessage(getuserid());
            Getfollowing(getuserid());
            layout_main.setVisibility(View.VISIBLE);
            layout_alert.setVisibility(View.GONE);
        }else {

            layout_alert.setVisibility(View.VISIBLE);
            layout_main.setVisibility(View.GONE);
        }

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Signup_chooseActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }

    private void initUI(View root) {
        bt_signup=root.findViewById(R.id.bt_signup);
        layout_main=root.findViewById(R.id.layout_main);
        layout_alert=root.findViewById(R.id.layout_alert);
        recyclerView = root.findViewById(R.id.recyclerView);
        rv_chat = root.findViewById(R.id.rv_chat);
        progressBar = root.findViewById(R.id.progressBar);

    }


    private void Getfollowing(String Userid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.GetFriendList(Userid).enqueue(new Callback<GetFriendList>() {
                @Override
                public void onResponse(Call<GetFriendList> call, Response<GetFriendList> response) {
                    if (response.isSuccessful()) {
                        list.clear();
                        progressBar.setVisibility(View.GONE);
                        GetFriendList data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                                list.addAll(data.getData());

                                //     Toast.makeText(FollowingActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                                if (list.size() > 0) {
                                    rvChatAdapter = new RvChatAdapter(getContext(), list,list2, addchatUser);
                                    rv_chat.setLayoutManager(new LinearLayoutManager(getContext()));
                                    rv_chat.setAdapter(rvChatAdapter);
                                }else {
                                    Toast.makeText(getContext(), "No Friends", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "No Friends Yet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetFriendList> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("follower failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetChatMessage(String Userid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.GetChatMessage(Userid).enqueue(new Callback<ChatMessage>() {
                @Override
                public void onResponse(Call<ChatMessage> call, Response<ChatMessage> response) {
                    if (response.isSuccessful()) {
                        list2.clear();
                        progressBar.setVisibility(View.GONE);
                        ChatMessage data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                list2.addAll(data.getData());
                                //     Toast.makeText(FollowingActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();
//                                if (list2.size() > 0) {
//                                    adapter = new TapChatAdapter(getContext(), list2);
//                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                                    recyclerView.setAdapter(adapter);
//
//                                }

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "No User Yet" , Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChatMessage> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
               //     Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("follower failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AddchatUser addchatUser = new AddchatUser() {
        @Override
        public void id(String id) {
            if (NetworkUtils.isConnected(getContext())){
                if (id!=null){
                    Log.e("reciverid",""+id);
                    Addchatuser(getuserid(),id);
                }
            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void Addchatuser(String senderid, String reciverid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.AddChat(senderid, reciverid).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                Getfollowing(getuserid());
                                GetChatMessage(getuserid());

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Add" + data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Addchat failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("userid", null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getuserid()!=null){
            GetChatMessage(getuserid());
            Getfollowing(getuserid());
            layout_main.setVisibility(View.VISIBLE);
            layout_alert.setVisibility(View.GONE);
        }else {

            layout_alert.setVisibility(View.VISIBLE);
            layout_main.setVisibility(View.GONE);
        }
    }
}


