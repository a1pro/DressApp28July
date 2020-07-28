package com.dressapplication.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.Chat;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.adapters.ChatAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {
    private Query query;
    private FirebaseDatabase databaseReference;
    private DatabaseReference myRef;
    private List<Chat> list = new ArrayList<>();
    private EditText message;
    private ImageView sendImg;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChatAdapter adapter;
    private TextView user_name;
    private ImageView delete;
    private ImageView back_click;
    private String chatroom,senderid,reciverid,username,lastMessage,recicvername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        back_click = findViewById(R.id.back_click);

        message = findViewById(R.id.msg);
        // delete = findViewById(R.id.delete);
        sendImg = findViewById(R.id.send);
        user_name = findViewById(R.id.user_name);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        chatroom = getIntent().getStringExtra("chatroom");
        senderid=getIntent().getStringExtra("senderid");
        reciverid=getIntent().getStringExtra("reciverid");
        username=getIntent().getStringExtra("name");
        recicvername=getIntent().getStringExtra("recivername");

        if (recicvername!=null){
            user_name.setText(recicvername);
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chats");
        if (chatroom != null) {
            query = myRef.child(chatroom)
                    .limitToLast(30);
            query.addChildEventListener(childEventListener);
        }


        sendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().length() > 0) {
                    if (chatroom != null) {
                        saveChatDatabase( String.valueOf(System.currentTimeMillis()),
                                message.getText().toString(),
                                reciverid,
                                username,
                                senderid,
                                chatroom);
                        message.setText("");
                    }
                }
            }
        });


        back_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Chat userChat = dataSnapshot.getValue(Chat.class);
            list.add(userChat);
            if (list.size()>0){
                adapter = new ChatAdapter(ChatActivity.this, list);
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }else {
                Toast.makeText(ChatActivity.this, "Empty Chat", Toast.LENGTH_SHORT).show();
            }

//            if (list.size() > 0) {
//                lastMessage = list.get(adapter.getItemCount() - 1).getMessage();
//                //    Toast.makeText(ChatActivty.this, lastMessage, Toast.LENGTH_SHORT).show();
//            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("databaseError",databaseError.getMessage());
        }
    };

    public void saveChatDatabase(String timestamp,String msg,  String reciver_id, String senderName, String sender_id,String ChatRoom) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chats");


        Chat chat = new Chat();
        chat.setMessageTime(timestamp);
        chat.setName(senderName);
        chat.setReceiver_id(reciver_id);
        chat.setSender_id(sender_id);
        chat.setText(msg);

        lastMessage = msg;

        databaseReference.child(ChatRoom).child(String.valueOf(System.currentTimeMillis())).setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (chatroom != null) {
                    AddLastMessage(sender_id,reciver_id,msg);
                 //   Toast.makeText(ChatActivity.this, "sent", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void AddLastMessage(String senderid, String reciverid,String message) {
        try {

            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(ChatActivity.this);
            apiInteface.LastMessage(senderid, reciverid,message).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {

                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                         //       Toast.makeText(ChatActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();


                            } else {

                             //   Toast.makeText(ChatActivity.this, "Add" + data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {

                    Toast.makeText(ChatActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Addchat failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        AddLastMessage(senderid,reciverid,lastMessage);
    }
}
