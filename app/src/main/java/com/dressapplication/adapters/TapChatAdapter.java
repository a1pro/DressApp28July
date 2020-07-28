package com.dressapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Model.ChatData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.SharedPrefrence.DataProccessor;
import com.dressapplication.activities.ChatActivity;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class TapChatAdapter extends RecyclerView.Adapter<TapChatAdapter.ViewHolder> {
    Context context;
    List<ChatData> list;

    public TapChatAdapter(Context context, List<ChatData> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_tab_item,parent,false);
        return new TapChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        if (list.get(i).getUsername()!=null){
            holder.tv_name.setText(list.get(i).getUsername());
        }

        if (list.get(i).getMessage()!=null){
            holder.tv_id.setText(""+list.get(i).getMessage());
        }

        if (list.get(i).getProfileImage()!=null){
            Glide.with(context).load(ApiUtils.IMAGE_BASE_URL+list.get(i).getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_user_profile);
        }

        holder.layout_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataProccessor dataProccessor=new DataProccessor(context);
                String username=dataProccessor.getusername("username");

                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("senderid",getuserid());
                intent.putExtra("reciverid",list.get(i).getId());
                intent.putExtra("name",username);
                intent.putExtra("chatroom",list.get(i).getMasterId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_click;
        private TextView tv_name,tv_id;
        private CircleImageView iv_user_profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_user_profile=itemView.findViewById(R.id.iv_user_profile);
            layout_click=itemView.findViewById(R.id.layout_click);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_id=itemView.findViewById(R.id.tv_id);

        }
    }

    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("userid", null);
    }
}
