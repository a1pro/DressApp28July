package com.dressapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Interface.Getusers;
import com.dressapplication.Model.FriendData;
import com.dressapplication.Model.TrendData;
import com.dressapplication.Model.UserNameData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetUserAdapter extends RecyclerView.Adapter<GetUserAdapter.ViewHolder> {
    Context context;
    List<FriendData> list;
    Getusers getusers;

    public GetUserAdapter(Context context, List<FriendData> list, Getusers getusers) {
    this.list=list;
    this.context=context;
    this.getusers=getusers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item, parent, false);
        return new GetUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (list.get(position).getProfileImage() != null) {
            Glide.with(context).load(ApiUtils.IMAGE_BASE_URL + list.get(position).getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profileImage);
        }

        String name = list.get(position).getUsername();
        String username="@"+list.get(position).getUsername();

        if (name != null) {
            holder.tv_name.setText(name);
            holder.tv_username.setText("@" + name);
        }

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getusers.getUsername("@"+list.get(position).getUsername());
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView tv_name, tv_username;
        private LinearLayout linear;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linear=itemView.findViewById(R.id.linear);
            profileImage = itemView.findViewById(R.id.profileImage);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_username = itemView.findViewById(R.id.tv_username);
        }
    }

    public void filterList(ArrayList<FriendData> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }
}
