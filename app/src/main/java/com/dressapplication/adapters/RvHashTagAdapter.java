package com.dressapplication.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Interface.CommonPlayInterface;
import com.dressapplication.Model.HashData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;

import java.util.List;

public class RvHashTagAdapter extends RecyclerView.Adapter<RvHashTagAdapter.RvHashTagAdapterHolder> {
    Context context;
    List<HashData> list;
    CommonPlayInterface commonPlayInterface;
    public RvHashTagAdapter(Context context, List<HashData> list, CommonPlayInterface commonPlayInterface) {
        this.context=context;
        this.list=list;
        this.commonPlayInterface=commonPlayInterface;

    }

    @NonNull
    @Override
    public RvHashTagAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_hashtag,viewGroup,false);
        return new RvHashTagAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvHashTagAdapterHolder holder, int i) {
        if (list.get(i).getVideoThumImg()!=null){
            Glide.with(context).load(ApiUtils.VIDEO_BASE_URL+list.get(i).getVideoThumImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_thumbnail);
        }
        else {

        }

        holder.img_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonPlayInterface.getWatch(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RvHashTagAdapterHolder extends RecyclerView.ViewHolder {
        private ImageView img_thumbnail;
        public RvHashTagAdapterHolder(@NonNull View itemView) {
            super(itemView);
            img_thumbnail=itemView.findViewById(R.id.img_thumbnail);

        }
    }
}
