package com.dressapplication.change;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Interface.CommentLikeInterface;
import com.dressapplication.Model.GetSingleVideoComment;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.Model.SinglevideoCommentData;
import com.dressapplication.Model.VideoData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.SharedPrefrence.DataProccessor;
import com.dressapplication.adapters.RvCommentAdapter;
import com.dressapplication.utils.NetworkUtils;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Comment_F extends RootFragment {
private ProgressBar progressBar;
private EditText et_leave_omment;
private RecyclerView recylerview;
private ImageView send_btn;
    List<SinglevideoCommentData> list2=new ArrayList<>();
    RvCommentAdapter rvCommentAdapter;
    List<VideoData> list = new ArrayList<>();
    TextView comment_count_txt;
    FrameLayout comment_screen;
    int VideoCommentCount;
    String video_id;
    String user_id;
    String userid;



    public static int comment_count=0;


    public Comment_F() {
        // Required empty public constructor
    }

    Fragment_Data_Send fragment_data_send;

    @SuppressLint("ValidFragment")
    public Comment_F( Fragment_Data_Send fragment_data_send){

        this.fragment_data_send=fragment_data_send;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_comment_, container, false);
        progressBar=view.findViewById(R.id.progressBar);
        et_leave_omment=view.findViewById(R.id.et_leave_omment);
        recylerview=view.findViewById(R.id.recylerview);
        comment_count_txt=view.findViewById(R.id.comment_count);
        send_btn=view.findViewById(R.id.send_btn);
        DataProccessor dataProccessor=new DataProccessor(getContext());
        userid=dataProccessor.getUserid("userid");




        comment_screen=view.findViewById(R.id.comment_screen);
        comment_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getActivity().onBackPressed();

            }
        });

        view.findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            getActivity().onBackPressed();

            }
        });


        Bundle bundle=getArguments();
        if(bundle!=null){
            video_id=bundle.getString("video_id");
            user_id=bundle.getString("user_id");
        }

        getAlldataApi();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected(getContext())) {
                    if (validation()) {
                        AddComment(video_id, user_id, userid, et_leave_omment.getText().toString());
                    }
                } else {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }


    @Override
    public void onDetach() {
        Functions.hideSoftKeyboard(getActivity());

        super.onDetach();
    }



    private void AddComment(String videoid, String videOwnerId, String Userid, String UserComment) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.AddcommentApi(videoid, videOwnerId, Userid, UserComment).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        list.clear();
                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                                Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                et_leave_omment.getText().clear();
                                getAlldataApi();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("comment failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean validation() {
        if (et_leave_omment.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please Leave a Comment", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void getAlldataApi() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.GetSingleVideocomment(user_id,video_id,userid).enqueue(new Callback<GetSingleVideoComment>() {
                @Override
                public void onResponse(Call<GetSingleVideoComment> call, Response<GetSingleVideoComment> response) {
                    if (response.isSuccessful()) {
                        list2.clear();
                        progressBar.setVisibility(View.GONE);
                        GetSingleVideoComment data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                list2.addAll(data.getData());

                                if (list2.size()>0){
                                    comment_count_txt.setText("" + list2.size() + " Comments");
                                }else {
                                    comment_count_txt.setText("No  Comments Found");
                                }

                                rvCommentAdapter = new RvCommentAdapter(getContext(), list2,commentLikeInterface);
                                recylerview.setLayoutManager(new LinearLayoutManager(getContext()));
                                recylerview.setAdapter(rvCommentAdapter);

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "" + data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetSingleVideoComment> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("commentApi failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    CommentLikeInterface commentLikeInterface =new CommentLikeInterface() {
        @Override
        public void LikeComment(int pos, String commentid, String CommentOwnerid) {


            if (userid!=null){
                AddLikeOnComment(userid,commentid,userid);
            }

        }


    };



    private void AddLikeOnComment(String Userid,String commentid,String CommentOwnerid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.AddLikeOnComment(Userid,commentid,CommentOwnerid).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                 //                  Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            rvCommentAdapter.notifyDataSetChanged();
                        //        getAlldataApi();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("comment failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
