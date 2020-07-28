package com.dressapplication.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.R;
import com.dressapplication.activities.HashTagActivity;
import com.dressapplication.activities.UserProfileActivity;
import com.dressapplication.utils.Home_Get_Set;
import com.dressapplication.utils.PatternEditableBuilder;
import com.google.android.exoplayer2.ui.PlayerView;
import java.util.ArrayList;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;



public class Home_Adapter extends RecyclerView.Adapter<Home_Adapter.CustomViewHolder > {

    public Context context;
    private Home_Adapter.OnItemClickListener listener;
    private ArrayList<Home_Get_Set> dataList;



    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon,Home_Get_Set item, View view);
    }



    public Home_Adapter(Context context, ArrayList<Home_Get_Set> dataList, Home_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public Home_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        Home_Adapter.CustomViewHolder viewHolder = new Home_Adapter.CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
       return dataList.size();
    }



    @Override
    public void onBindViewHolder(final Home_Adapter.CustomViewHolder holder, final int i) {
        final Home_Get_Set item= dataList.get(i);
        holder.setIsRecyclable(false);

//                 Glide.with(context).load(item.thum).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.videothumb);
        holder.sound_name.setSelected(true);


        if (item.first_name!=null){
            holder.username.setText("@"+item.first_name);
            holder.sound_name.setText("Sound original - "+item.first_name+"  ");
        }


        if (item.first_name!=null){
            holder.tv_username.setText(item.first_name);
        }


        if (item.like_count.equalsIgnoreCase("null")){
            holder.like_txt.setText("0");
        }else {
            holder.like_txt.setText(item.like_count);
        }


        if (item.video_comment_count.equalsIgnoreCase("null")){
            holder.comment_txt.setText("0");

        }else {
            holder.comment_txt.setText(item.video_comment_count);
        }

        if (item.video_description!=null){
            holder.desc_txt.setText(item.video_description);
        }

        if (item.isloginliked!=null){
            if (item.isloginliked.equalsIgnoreCase("1")){
                holder.like_image.setImageDrawable(context.getResources().getDrawable(R.mipmap.like));
            }else {
                holder.like_image.setImageDrawable(context.getResources().getDrawable(R.mipmap.unlike_icon));
            }
        }


        if (item.profile_pic!=null){
            Glide.with(context).
                    load(item.profile_pic).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.user_pic);
        }else {
            holder.user_pic.setImageDrawable(context.getDrawable(R.mipmap.app_icon));
        }


        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), Color.WHITE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                if (text!=null && !text.isEmpty()){
                                    Intent intent6=new Intent(context, HashTagActivity.class);
                                    intent6.putExtra("hashvalue",text);
                                    context.startActivity(intent6);

                                }
                            }
                        }).into(holder.desc_txt);

        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.WHITE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                if (text!=null && !text.isEmpty()){
                                    Intent intent6=new Intent(context, UserProfileActivity.class);
                                    intent6.putExtra("GetProfile",text);
                                    context.startActivity(intent6);

                                }
                            }
                        }).into(holder.desc_txt);



        try {

        holder.bind(i,item,listener);


        }catch (Exception e){

        }
   }



     class CustomViewHolder extends RecyclerView.ViewHolder {

        PlayerView playerview;
        TextView username,desc_txt,sound_name;
        ImageView sound_image,videothumb;
        CircleImageView user_pic;

        LinearLayout like_layout,comment_layout,shared_layout,sound_image_layout,layout_map;
        ImageView comment_image;
         ImageView like_image;
        TextView like_txt,comment_txt,tv_username;


        public CustomViewHolder(View view) {
            super(view);

            playerview=view.findViewById(R.id.playerview);
            desc_txt=view.findViewById(R.id.desc_txt);
            videothumb=view.findViewById(R.id.videothumb);
            username=view.findViewById(R.id.username);
            user_pic=view.findViewById(R.id.user_pic);
            sound_name=view.findViewById(R.id.sound_name);
            tv_username=view.findViewById(R.id.tv_username);
            layout_map=view.findViewById(R.id.layout_map);


            like_layout=view.findViewById(R.id.like_layout);
            like_image=view.findViewById(R.id.like_image);
            like_txt=view.findViewById(R.id.like_txt);




            comment_layout=view.findViewById(R.id.comment_layout);
            comment_image=view.findViewById(R.id.comment_image);
            comment_txt=view.findViewById(R.id.comment_txt);



            shared_layout=view.findViewById(R.id.shared_layout);
        }

        public void bind(final int postion, final Home_Get_Set item, final Home_Adapter.OnItemClickListener listener) {

            layout_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });


            user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);

//                    Intent intent=new Intent(context,UserProfileActivity.class);
//                    intent.putExtra("userid_",item.userid);
//                    context.startActivity(intent);
                }
            });

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });




            like_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              //      like_image.setImageDrawable(context.getResources().getDrawable(R.mipmap.like));

                    listener.onItemClick(postion,item,v);
                }
            });


            comment_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });

            shared_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });

            sound_image_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });


        }


    }


}