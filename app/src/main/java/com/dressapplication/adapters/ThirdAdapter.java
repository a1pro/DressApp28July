package com.dressapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dressapplication.R;
import com.dressapplication.activities.ReportActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class ThirdAdapter extends RecyclerView.Adapter<ThirdAdapter.ViewHolder> {
    int images[]={R.mipmap.report};
    String names []={"Report"};
    Context context;
    String ownerid;
    String videoid;

    public ThirdAdapter(Context context,String ownerid,String videoid) {
        this.context=context;
        this.ownerid=ownerid;
        this.videoid=videoid;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_items, parent, false);
        return new ThirdAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img_icon.setImageResource(images[position]);
        holder.tv_name.setText(names[position]);

        holder.img_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (holder.getLayoutPosition()==0){
                Intent intent=new Intent(context, ReportActivity.class);
                intent.putExtra("ownerid",ownerid );
                intent.putExtra("videoid", videoid );
                context.startActivity(intent);
                }


            }
        });
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_icon;
        private TextView tv_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            img_icon=itemView.findViewById(R.id.img_icon);
        }
    }
}
