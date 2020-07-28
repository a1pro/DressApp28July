package com.dressapplication.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Interface.CommentLikeInterface;
import com.dressapplication.Model.SinglevideoCommentData;
import com.dressapplication.Model.VideoComment;
import com.dressapplication.Model.VideoData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RvCommentAdapter extends RecyclerView.Adapter<RvCommentAdapter.RvChatAdapterHolder> {
    private Context context;
    List<SinglevideoCommentData> datalist;
    String usernam;
    CommentLikeInterface commentLikeInterface;
    private boolean likeorunlike=false;

    public RvCommentAdapter(Context context, List<SinglevideoCommentData> datalist, CommentLikeInterface commentLikeInterface){
        this.datalist= datalist;
        this.context=context;
        this.commentLikeInterface=commentLikeInterface;

    }

    @NonNull
    @Override
    public RvCommentAdapter.RvChatAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_comment,viewGroup,false);
        return new RvCommentAdapter.RvChatAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvCommentAdapter.RvChatAdapterHolder holder, int i) {

        try {
//            Log.e("id",datalist.get(i).getId());
//            Log.e("videoid",datalist.get(i).getVideoId());
//            Log.e("videoownerid",datalist.get(i).getVideoOwnerId());
//            Log.e("userid",datalist.get(i).getUserId());
            if (datalist.get(i).getIslikedCommnet()!=null){
                if (datalist.get(i).getIslikedCommnet().equalsIgnoreCase("1")){
                    holder.iv_like.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                }
            }


                holder.tv_comment_like_count.setText(""+datalist.get(i).getLikCommnetCount());


            if (datalist.get(i).getCommnetedUserProfileIm()!=null && !datalist.get(i).getCommnetedUserProfileIm().isEmpty()){
                Glide.with(context).load(ApiUtils.IMAGE_BASE_URL+datalist.get(i).getCommnetedUserProfileIm()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profile_img);
            }else {
                holder.profile_img.setImageDrawable(context.getResources().getDrawable(R.drawable.profile_image_placeholder));
            }


                String comment=datalist.get(i).getUserComments();
                if (comment!=null){
                    holder.tv_comment.setText(comment);
                }


            if (datalist.get(i).getCommnetedUserName()!=null){
                holder.tv_name.setText(datalist.get(i).getCommnetedUserName());
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }



        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                  commentLikeInterface.LikeComment(i,datalist.get(i).getId(),datalist.get(i).getUserId());

                if (likeorunlike == false) {
                    holder.iv_like.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                    likeorunlike = true;
                } else {
                    holder.iv_like.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike_icon));
                    likeorunlike = false;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class RvChatAdapterHolder extends RecyclerView.ViewHolder {
        private TextView tv_name,tv_comment,tv_comment_like_count;
        private ImageView iv_like;
        private CircleImageView profile_img;
        public RvChatAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tv_comment_like_count=itemView.findViewById(R.id.tv_comment_like_count);
            profile_img=itemView.findViewById(R.id.profile_img);
            iv_like=itemView.findViewById(R.id.iv_like);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_comment=itemView.findViewById(R.id.tv_comment);
//            tv_time=itemView.findViewById(R.id.tv_time);
        }
    }
}
