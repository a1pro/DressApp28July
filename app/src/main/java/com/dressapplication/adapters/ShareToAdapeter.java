package com.dressapplication.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShareToAdapeter extends RecyclerView.Adapter<ShareToAdapeter.ViewHolder> {
    int images[]={R.mipmap.whatsapp,R.mipmap.insta_icon,R.mipmap.message,R.mipmap.other};
    String names []={"WhatsApp","Instagram","Messages","Other"};
    Context context;
    public ShareToAdapeter(Context context) {
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_items, parent, false);
        return new ShareToAdapeter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img_icon.setImageResource(images[position]);
        holder.tv_name.setText(names[position]);


       holder.img_icon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (holder.getLayoutPosition()==0){
                   String video= ApiUtils.VIDEO_BASE_URL+ "userVideo6QSbU.mp4";
                   Intent sendIntent = new Intent();
                   sendIntent.setAction(Intent.ACTION_SEND);
                   sendIntent.putExtra(Intent.EXTRA_TEXT, video);
                   sendIntent.setType("text/plain");
                   sendIntent.setPackage("com.whatsapp");
                   context.startActivity(sendIntent);
               }

               if (holder.getLayoutPosition()==1){

                   String uri = ApiUtils.VIDEO_BASE_URL+ "userVideo6QSbU.mp4";
                   Intent likeIng = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                   likeIng.setPackage("com.instagram.android");

                   try {
                      context.startActivity(likeIng);
                   } catch (ActivityNotFoundException e) {
                       context.startActivity(new Intent(Intent.ACTION_VIEW,
                               Uri.parse("http://instagram.com/xxx")));
                   }
               }

               if (holder.getLayoutPosition()==2){
                   String uri1 = ApiUtils.VIDEO_BASE_URL+ "userVideo6QSbU.mp4";
                   Uri uri = Uri.parse("smsto:12345");
                   Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                   intent.putExtra("DressApp video", uri1);
                   context.startActivity(intent);
               }

               if (holder.getLayoutPosition()==3){
                   String uri1 = ApiUtils.VIDEO_BASE_URL+ "userVideo6QSbU.mp4";
                   Intent sendIntent = new Intent();
                   sendIntent.setAction(Intent.ACTION_SEND);
                   sendIntent.putExtra(Intent.EXTRA_TEXT, uri1);
                   sendIntent.setType("text/plain");

                   Intent shareIntent = Intent.createChooser(sendIntent, null);
                   context.startActivity(shareIntent);
               }

           }
       });

    }


    @Override
    public int getItemCount() {
        return 4;
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

