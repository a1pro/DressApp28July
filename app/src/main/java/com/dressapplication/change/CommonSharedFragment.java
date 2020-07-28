package com.dressapplication.change;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.FollowingData;
import com.dressapplication.Model.GetFollowing;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.adapters.SendToAdapter;
import com.dressapplication.adapters.ShareToAdapeter;
import com.dressapplication.adapters.ThirdAdapter;
import com.dressapplication.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommonSharedFragment extends Fragment {
    RecyclerView recyclerview_bottom_sheet,recyclerview_sendto,recyclerview_shareto,recylerview_report;
    private ProgressBar progressBar;
    List<FollowingData> followinglist =new ArrayList<>();
    String video_id;
    String user_id;
    private TextView bt_cancel;


    public CommonSharedFragment() {
        // Required empty public constructor
    }


    Fragment_Data_Send fragment_data_send;

    @SuppressLint("ValidFragment")
    public CommonSharedFragment( Fragment_Data_Send fragment_data_send){

        this.fragment_data_send=fragment_data_send;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_common_shared, container, false);
        progressBar=view.findViewById(R.id.progressBar);
        bt_cancel=view.findViewById(R.id.bt_cancel);
        recyclerview_sendto=view.findViewById(R.id.recyclerview_sendto);
        recyclerview_shareto=view.findViewById(R.id.recyclerview_shareto);
        recylerview_report=view.findViewById(R.id.recylerview_report);

        Bundle bundle=getArguments();
        if(bundle!=null){
            video_id=bundle.getString("video_id");
            user_id=bundle.getString("user_id");
        }
        if (NetworkUtils.isConnected(getContext())) {
            Getfollowing();
        }else {
            Toast.makeText(getContext(), "No Internet Coninection", Toast.LENGTH_SHORT).show();
        }


        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        return view;


    }

    private void setAdapeters() {

        recyclerview_sendto.setHasFixedSize(true);
        recyclerview_shareto.setHasFixedSize(true);
        recylerview_report.setHasFixedSize(true);

        SendToAdapter sendToAdapter = new SendToAdapter(getContext(),followinglist);
        recyclerview_sendto.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerview_sendto.setAdapter(sendToAdapter);

        ShareToAdapeter shareToAdapeter = new ShareToAdapeter(getContext());
        recyclerview_shareto.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerview_shareto.setAdapter(shareToAdapeter);


        ThirdAdapter thirdAdapter = new ThirdAdapter(getContext(),user_id,video_id);
        recylerview_report.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recylerview_report.setAdapter(thirdAdapter);

    }

    private String getuserid () {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("userid", null);
    }

    private void Getfollowing() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.GetFollowing(getuserid()).enqueue(new Callback<GetFollowing>() {
                @Override
                public void onResponse(Call<GetFollowing> call, Response<GetFollowing> response) {
                    if (response.isSuccessful()){
                        followinglist.clear();
                        progressBar.setVisibility(View.GONE);
                        GetFollowing data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                followinglist.addAll(data.getData());

                                //     Toast.makeText(FollowingActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                                setAdapeters();

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                //   Toast.makeText(getContext(), ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetFollowing> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("follower failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
