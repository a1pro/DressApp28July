package com.dressapplication.ui.myProfile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Interface.CommonPlayInterface;
import com.dressapplication.Model.GetProfileModel;
import com.dressapplication.Model.PostDatum;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.adapters.ProfileVideoAdapter;
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
public class ProfileVideoFragment extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    RvProfileAdapter rvProfileAdapter;
    ProfileVideoAdapter adapter;
    List<PostDatum> list=new ArrayList<>();
    String userid;
    private TextView tv_novideo;
    List<Home_Get_Set> data_list=new ArrayList<>();



    public ProfileVideoFragment(String userid) {
    this.userid=userid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_video, container, false);
        progressBar=view.findViewById(R.id.progressBar);
        recyclerView=view.findViewById(R.id.recyclerView);
        tv_novideo=view.findViewById(R.id.tv_novideo);

//        DataProccessor dataProccessor=new DataProccessor(getContext());
//        String userid=dataProccessor.getUserid("userid");


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
            apiInteface.GetProfile(Userid,getuserid()).enqueue(new Callback<GetProfileModel>() {
                @Override
                public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                    if (response.isSuccessful()){
                        list.clear();
                        progressBar.setVisibility(View.GONE);
                        GetProfileModel data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                         //       Toast.makeText(getContext(), ""+data.getStatus(), Toast.LENGTH_SHORT).show();
                                list.addAll(data.getPostData());
                                if (list.size()>0){
                                    adapter=new ProfileVideoAdapter(getContext(),list,commonPlayInterface);
                                    recyclerView.setAdapter(adapter);
                                }else {
                                    tv_novideo.setVisibility(View.VISIBLE);
                                }


                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                              //  Toast.makeText(getContext(), "Profile Video"+data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileModel> call, Throwable t) {
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

    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("userid", null);
    }

    private void OpenWatchVideo(int postion) {

        Intent intent=new Intent(getActivity(), CommonWatchActivity.class);
        intent.putExtra("arraylist", (Serializable) data_list);
        intent.putExtra("position",postion);
        startActivity(intent);

    }
}
