package com.dressapplication.ui.myProfile;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Interface.CommonPlayInterface;
import com.dressapplication.Model.LikedData;
import com.dressapplication.Model.LikedVideos;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.change.CommonWatchActivity;
import com.dressapplication.utils.Home_Get_Set;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeVideoFragment extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    RvProfileAdapter adapter;
    List<LikedData> list=new ArrayList<>();
    String userid;
    private TextView tv_novideo;
    List<Home_Get_Set> data_list=new ArrayList<>();
    public LikeVideoFragment(String userid) {

        this.userid=userid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_like_video, container, false);
        progressBar=view.findViewById(R.id.progressBar);
        tv_novideo=view.findViewById(R.id.tv_novideo);
//        DataProccessor dataProccessor=new DataProccessor(getContext());
//        String userid=dataProccessor.getUserid("userid");

        recyclerView=view.findViewById(R.id.recyclerView);


        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (userid!=null){
            GetProfile(userid);
        }





        return view;
    }


    private void GetProfile(String Userid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.getVideolikeByMe(Userid).enqueue(new Callback<LikedVideos>() {
                @Override
                public void onResponse(Call<LikedVideos> call, Response<LikedVideos> response) {
                    if (response.isSuccessful()){
                        list.clear();
                        progressBar.setVisibility(View.GONE);
                        LikedVideos data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                //      Toast.makeText(getContext(), "Liked"+data.getStatus(), Toast.LENGTH_SHORT).show();
                                list.addAll(data.getData());
                                if(list.size()>0){
                                    adapter=new RvProfileAdapter(getContext(),list,commonPlayInterface);
                                    recyclerView.setAdapter(adapter);
                                }else {
                                    tv_novideo.setVisibility(View.VISIBLE);
                                }
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
//                                Toast.makeText(getContext(), ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<LikedVideos> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("view profile failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    CommonPlayInterface commonPlayInterface=new CommonPlayInterface() {
        @Override
        public void getWatch(int pos) {
            data_list.clear();
            for (int i=0;i<list.size();i++){
                Home_Get_Set item=new Home_Get_Set();
                item.video_url=ApiUtils.VIDEO_BASE_URL+list.get(i).getVideoName();
                item.video_description=list.get(i).getVideoDescribe();
                item.userid=list.get(i).getUserId();
                item.video_id=list.get(i).getVid();
                item.longi=list.get(i).getLongitude();
                item.lat=list.get(i).getLatitude();
                item.liked=list.get(i).getLoginUserIsLiked();
                item.profile_pic=ApiUtils.IMAGE_BASE_URL+list.get(i).getProfile_image();
                item.first_name=list.get(i).getUsername();
                data_list.add(item);
            }
            OpenWatchVideo(pos);

        }
    };



    private void OpenWatchVideo(int postion) {

        Intent intent=new Intent(getActivity(), CommonWatchActivity.class);
        intent.putExtra("arraylist", (Serializable) data_list);
        intent.putExtra("position",postion);
        startActivity(intent);

    }
}