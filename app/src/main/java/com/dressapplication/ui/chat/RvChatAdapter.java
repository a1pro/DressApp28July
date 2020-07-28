package com.dressapplication.ui.chat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Interface.AddchatUser;
import com.dressapplication.Model.ChatData;
import com.dressapplication.Model.FriendData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.SharedPrefrence.DataProccessor;
import com.dressapplication.activities.ChatActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RvChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_CHAT_MESSAGE = 0;
    final int VIEW_FREINDS_LIST = 1;

    Context context;
    List<FriendData> list;
    AddchatUser addchatUser;
    List<ChatData> list2;


    public RvChatAdapter(Context context, List<FriendData> list, List<ChatData> list2, AddchatUser addchatUser) {
        this.context = context;
        this.list = list;
        this.addchatUser = addchatUser;
        this.list2 = list2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_chat, viewGroup, false);
        if (viewtype == VIEW_CHAT_MESSAGE) {
            return new ChatMessage(view);
        }

        if (viewtype == VIEW_FREINDS_LIST) {
            return new RvChatAdapterHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < list2.size()) {
            return VIEW_CHAT_MESSAGE;
        }

        if (position - list.size() < list2.size()) {
            return VIEW_FREINDS_LIST;
        }

        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ChatMessage) {
            ((ChatMessage) holder).populate(list2.get(position));
        }

        if (holder instanceof RvChatAdapterHolder) {
            ((RvChatAdapterHolder) holder).populate(list.get(position - list2.size()));
        }
//        if (list.get(i).getUsername()!=null){
//            holder.tv_name.setText(list.get(i).getUsername());
//            holder.tv_id.setText("@"+list.get(i).getUsername());
//        }
//
//        if (list.get(i).getProfileImage()!=null){
//            Glide.with(context).load(ApiUtils.IMAGE_BASE_URL+list.get(i).getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_user_profile);
//        }
//
//        holder.tv_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (list.size()>0) {
//                    if (list.get(i).getId() != null) {
//                        addchatUser.id(list.get(i).getId());
//                    }
//                }
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return list.size() + list2.size();
    }

    public class RvChatAdapterHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_click;
        private TextView tv_name, tv_id, tv_add;
        private CircleImageView iv_user_profile;

        public RvChatAdapterHolder(@NonNull View itemView) {
            super(itemView);
            iv_user_profile = itemView.findViewById(R.id.iv_user_profile);
            layout_click = itemView.findViewById(R.id.layout_click);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_add = itemView.findViewById(R.id.tv_add);
        }


        public void populate(FriendData friendData) {
            if (friendData.getUsername() != null) {
                tv_name.setText(friendData.getUsername());
                tv_id.setText("@" + friendData.getUsername());
            }

            if (friendData.getProfileImage() != null) {
                Glide.with(context).load(ApiUtils.IMAGE_BASE_URL + friendData.getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_user_profile);
            }

            tv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (friendData.getId() != null) {
                        addchatUser.id(friendData.getId());

                    }
                }
            });
        }

    }


    public class ChatMessage extends RecyclerView.ViewHolder {
        RelativeLayout layout_click;
        private TextView tv_name, tv_id, tv_add;
        private CircleImageView iv_user_profile;

        public ChatMessage(@NonNull View itemView) {
            super(itemView);
            iv_user_profile = itemView.findViewById(R.id.iv_user_profile);
            layout_click = itemView.findViewById(R.id.layout_click);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_add = itemView.findViewById(R.id.tv_add);
        }

        public void populate(ChatData chatData) {
            if (chatData.getUsername() != null) {
                tv_name.setText(chatData.getUsername());

            }
            if (chatData.getMessage() != null) {
                tv_id.setText("" + chatData.getMessage());
            }

            if (chatData.getProfileImage() != null) {
                Glide.with(context).load(ApiUtils.IMAGE_BASE_URL + chatData.getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_user_profile);
            }

            tv_add.setVisibility(View.GONE);

            layout_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataProccessor dataProccessor=new DataProccessor(context);
                    String username=dataProccessor.getusername("username");
                    Intent intent=new Intent(context, ChatActivity.class);
                    intent.putExtra("senderid",getuserid());
                    intent.putExtra("reciverid",chatData.getReceiverId());
                    intent.putExtra("name",username);
                    intent.putExtra("recivername",chatData.getUsername());
                    intent.putExtra("chatroom",chatData.getMasterId());
                    context.startActivity(intent);
                }
            });
        }

    }

    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("userid", null);
    }
}

