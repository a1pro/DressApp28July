package com.dressapplication.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dressapplication.Model.Chat;
import com.dressapplication.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    public static final String DATE_FORMAT_1 = "hh:mm a";

    private Context context;
    private List<Chat> list;


    public ChatAdapter(Context context, List<Chat> list) {

        this.list = list;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (list != null) {
            if (getuserid().equalsIgnoreCase(list.get(position).getSender_id())) {
                SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
                String dateString = formatter.format(new Date(Long.parseLong(list.get(position).getMessageTime())));

                holder.messageSend.setVisibility(View.VISIBLE);
                holder.messageRecived.setVisibility(View.GONE);
                holder.messageSend.setText(list.get(position).getText());
                holder.msg_rec_time.setVisibility(View.GONE);
                holder.msg_send_time.setVisibility(View.VISIBLE);
                holder.msg_send_time.setText(dateString);

            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
                String dateString = formatter.format(new Date(Long.parseLong(list.get(position).getMessageTime())));

                holder.messageSend.setVisibility(View.GONE);
                holder.messageRecived.setVisibility(View.VISIBLE);
                holder.messageRecived.setText(list.get(position).getText());
                holder.msg_rec_time.setVisibility(View.VISIBLE);
                holder.msg_send_time.setVisibility(View.GONE);
                holder.msg_rec_time.setText(dateString);
            }
        }

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private TextView messageRecived, messageSend,msg_rec_time,msg_send_time;

        public ViewHolder(View itemView) {

            super(itemView);

            msg_rec_time=itemView.findViewById(R.id.msg_rec_time);
            msg_send_time=itemView.findViewById(R.id.msg_send_time);
            messageRecived = (TextView) itemView.findViewById(R.id.messageRecived);
            messageSend = (TextView) itemView.findViewById(R.id.messageSend);

        }
    }

    public String dateget(String datemy) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");
        SimpleDateFormat formatterOut = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        try {
            date = format.parse(datemy);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatterOut.format(date).toString();
    }




    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("userid", null);
    }

}