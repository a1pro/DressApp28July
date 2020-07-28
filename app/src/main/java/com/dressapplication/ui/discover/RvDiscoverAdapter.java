package com.dressapplication.ui.discover;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dressapplication.Interface.CommonPlayInterface;
import com.dressapplication.Model.TrendData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.activities.HashTagActivity;
import com.dressapplication.adapters.TrendPostAdapter;
import com.dressapplication.change.CommonWatchActivity;
import com.dressapplication.utils.Home_Get_Set;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RvDiscoverAdapter extends RecyclerView.Adapter<RvDiscoverAdapter.RvDiscoverAdapterHolder> {
    Context context;
    List<TrendData> list;

    List<Home_Get_Set> data_list=new ArrayList<>();


    public RvDiscoverAdapter(Context context, List<TrendData> list ) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public RvDiscoverAdapter.RvDiscoverAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_discover, viewGroup, false);
        return new RvDiscoverAdapter.RvDiscoverAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvDiscoverAdapter.RvDiscoverAdapterHolder holder, int i) {

        if (list.get(i).getHashtag() != null) {
            holder.tv_challenge.setText(list.get(i).getHashtag());
        }
        if (list.get(i).getPostCount() != null) {
            holder.tv_count.setText("" + list.get(i).getPostCount());
        }


        holder.rl_hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,HashTagActivity.class);
                intent.putExtra("hashvalue",list.get(i).getHashtag());
                context.startActivity(intent);
            }
        });


        CommonPlayInterface commonPlayInterface=new CommonPlayInterface() {
            @Override
            public void getWatch(int pos) {
                data_list.clear();
                for (int j=0;j<list.get(i).getAllPost().size();j++){
                    Home_Get_Set item=new Home_Get_Set();
                    item.video_url= ApiUtils.VIDEO_BASE_URL+list.get(i).getAllPost().get(j).getVideoName();
                    item.video_description=list.get(i).getAllPost().get(j).getVideoDescribe();
                    item.userid=list.get(i).getAllPost().get(j).getUserId();
                    item.video_id=list.get(i).getAllPost().get(j).getVid();
                    item.longi=list.get(i).getAllPost().get(j).getLongitude();
                    item.lat=list.get(i).getAllPost().get(j).getLatitude();
                    item.liked=list.get(i).getAllPost().get(j).getLoginUserIsLiked();
                    item.profile_pic=ApiUtils.IMAGE_BASE_URL+list.get(i).getAllPost().get(j).getProfile_image();
                    item.first_name=list.get(i).getAllPost().get(j).getUsername();
                    data_list.add(item);
                }

                OpenWatchVideo(pos);
            }
        };


        holder.recyclerView.setHasFixedSize(true);
        TrendPostAdapter itemListDataAdapter = new TrendPostAdapter(context, list.get(i).getAllPost(),commonPlayInterface);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(itemListDataAdapter);


    }

    private void OpenWatchVideo(int postion) {

        Intent intent=new Intent(context, CommonWatchActivity.class);
        intent.putExtra("arraylist", (Serializable) data_list);
        intent.putExtra("position",postion);
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RvDiscoverAdapterHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_hashtag;
        private TextView tv_challenge, tv_count;
        private RecyclerView recyclerView;

        RvDiscoverAdapterHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            tv_count = itemView.findViewById(R.id.tv_count);
            rl_hashtag = itemView.findViewById(R.id.rl_hashtag);
            tv_challenge = itemView.findViewById(R.id.tv_challenge);

        }

    }

    public void filterList(ArrayList<TrendData> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}
