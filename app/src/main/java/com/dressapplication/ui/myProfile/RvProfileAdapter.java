package com.dressapplication.ui.myProfile;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Interface.CommonPlayInterface;
import com.dressapplication.Model.LikedData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import java.util.List;

public class RvProfileAdapter extends RecyclerView.Adapter<RvProfileAdapter.rvProfileAdapterHolder> {
    private Context context;
    List<LikedData> list;
    CommonPlayInterface commonPlayInterface;


    public RvProfileAdapter(Context context, List<LikedData> list, CommonPlayInterface commonPlayInterface) {
        this.context=context;
        this.list=list;
        this.commonPlayInterface=commonPlayInterface;

    }

    @NonNull
    @Override
    public rvProfileAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_profile,viewGroup,false);
        return new RvProfileAdapter.rvProfileAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull rvProfileAdapterHolder holder, int i) {
        Log.e("videothumb",""+list.get(i).getVideoThumImg());
        if (list.get(i).getVideoThumImg()!=null){
            Glide.with(context).load(ApiUtils.VIDEO_BASE_URL+list.get(i).getVideoThumImg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img_videothumbnails);
        }

        holder.img_videothumbnails.setOnClickListener(new View.OnClickListener() {
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

    public class rvProfileAdapterHolder extends RecyclerView.ViewHolder {
        private ImageView img_videothumbnails;
        public rvProfileAdapterHolder(@NonNull View itemView) {
            super(itemView);
            img_videothumbnails=itemView.findViewById(R.id.img_videothumbnails);
        }
    }
}
