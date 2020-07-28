package com.dressapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Model.FollowersData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowesAdapter extends RecyclerView.Adapter<FollowesAdapter.ViewHolder> {
    Context context;
    List<FollowersData> list;

    public FollowesAdapter(Context context, List<FollowersData> list) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_following, parent, false);
        return new FollowesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (list.get(position).getProfileImage() != null ) {
            Glide.with(context).load(ApiUtils.IMAGE_BASE_URL + list.get(position).getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileImage);
        }else {
            holder.profileImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.app_icon));
        }

        String name = list.get(position).getUsername();

        if (name != null) {
            holder.tv_name.setText(name);
            holder.tv_username.setText("@" + name);
        }
        holder.btn_follow.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView tv_name, tv_username;
        private Button btn_follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_username = itemView.findViewById(R.id.tv_username);
            btn_follow =itemView.findViewById(R.id.btn_follow);


        }
    }
}
