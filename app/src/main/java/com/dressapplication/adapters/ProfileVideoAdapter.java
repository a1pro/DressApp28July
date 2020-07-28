package com.dressapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Interface.CommonPlayInterface;
import com.dressapplication.Model.PostDatum;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;

import java.util.List;

public class ProfileVideoAdapter extends RecyclerView.Adapter<ProfileVideoAdapter.ViewHolder> {
    private Context context;
    List<PostDatum> list;
    CommonPlayInterface commonPlayInterface;

    public ProfileVideoAdapter(Context context, List<PostDatum> list, CommonPlayInterface commonPlayInterface) {
        this.context=context;
        this.list=list;
        this.commonPlayInterface=commonPlayInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_profile,parent,false);
        return new ProfileVideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
  //      Log.e("listsize",""+list.size());
  //      Log.e("videothumb",""+list.get(position).getVideoThumImg());
        if (list.get(position).getVideoThumImg()!=null ){
            Glide.with(context).load(ApiUtils.VIDEO_BASE_URL+list.get(position).getVideoThumImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_videothumbnails);
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
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_videothumbnails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_videothumbnails=itemView.findViewById(R.id.img_videothumbnails);
        }
    }
}
