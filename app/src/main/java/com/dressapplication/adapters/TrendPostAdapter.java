package com.dressapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Interface.CommonPlayInterface;
import com.dressapplication.Model.AllPost;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;

import java.util.List;

public class TrendPostAdapter extends RecyclerView.Adapter<TrendPostAdapter.ViewHolder> {
    Context context;
    List<AllPost> list2;
    CommonPlayInterface commonPlayInterface;

    public TrendPostAdapter(Context context, List<AllPost> list2, CommonPlayInterface commonPlayInterface) {
        this.list2=list2;
        this.context=context;
        this.commonPlayInterface=commonPlayInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trend_items,parent,false);
        return new TrendPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list2.get(position).getVideoThumImg()!=null ){
            Glide.with(context).load(ApiUtils.VIDEO_BASE_URL+list2.get(position).getVideoThumImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_videothumbnails);
        }

        holder.img_videothumbnails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonPlayInterface.getWatch(position);
            }
        });
    }





    @Override
    public int getItemCount() {
        return list2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_videothumbnails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_videothumbnails=itemView.findViewById(R.id.img_videothumbnails);
        }
    }
}
