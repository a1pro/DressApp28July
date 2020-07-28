package com.dressapplication.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Model.FollowingData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RvFollowingAdapter extends RecyclerView.Adapter<RvFollowingAdapter.RvFollowingAdapterHolder> {
    List<FollowingData> list;
    Context context;
    public RvFollowingAdapter(Context context, List<FollowingData> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RvFollowingAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_following,viewGroup,false);
        return new RvFollowingAdapter.RvFollowingAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvFollowingAdapterHolder holder, int position) {

        if (list.get(position).getProfileImage() != null && !list.get(position).getProfileImage().isEmpty()) {
            Glide.with(context).load(ApiUtils.IMAGE_BASE_URL + list.get(position).getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileImage);
        }else {
            holder.profileImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.app_icon));
        }

        String name = list.get(position).getUsername();

        if (name != null) {
            holder.tv_name.setText(name);
            holder.tv_username.setText("@" + name);
        }

        if (list.get(position).getFriendsStatus()!=null){
           if (list.get(position).getFriendsStatus().equalsIgnoreCase("1")){
               holder.btn_follow.setText("Freinds");
           }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RvFollowingAdapterHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView tv_name, tv_username;
        private Button btn_follow;
        public RvFollowingAdapterHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_username = itemView.findViewById(R.id.tv_username);
            btn_follow =itemView.findViewById(R.id.btn_follow);

        }
    }
}
