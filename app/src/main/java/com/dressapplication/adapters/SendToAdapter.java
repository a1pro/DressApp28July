package com.dressapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Model.FollowingData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SendToAdapter extends RecyclerView.Adapter<SendToAdapter.ViewHolder> {
    List<FollowingData> followinglist;
    Context context;
    public SendToAdapter(Context context, List<FollowingData> followinglist) {
        this.followinglist=followinglist;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_items, parent, false);
        return new SendToAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     if (followinglist.get(position).getProfileImage()!=null && !followinglist.get(position).getProfileImage().isEmpty()){
         Glide.with(context).load(ApiUtils.IMAGE_BASE_URL+followinglist.get(position).getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_icon);
     }else {
         holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.mipmap.app_icon));
     }

     if (followinglist.get(position).getUsername()!=null){
         holder.tv_name.setText(followinglist.get(position).getUsername());
     }

    }


    @Override
    public int getItemCount() {
        return followinglist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_icon;
        private TextView tv_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_icon = itemView.findViewById(R.id.img_icon);
        }
    }
}
