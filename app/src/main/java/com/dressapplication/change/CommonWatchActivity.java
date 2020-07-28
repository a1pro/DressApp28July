package com.dressapplication.change;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.SharedPrefrence.DataProccessor;
import com.dressapplication.activities.Signup_chooseActivity;
import com.dressapplication.activities.UserProfileActivity;
import com.dressapplication.utils.Home_Get_Set;
import com.dressapplication.utils.Variables;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CommonWatchActivity extends AppCompatActivity implements Player.EventListener,
        View.OnClickListener, Fragment_Data_Send {

    RecyclerView recyclerView;
    ArrayList<Home_Get_Set> data_list;
    int position = 0;
    int currentPage = -1;
    LinearLayoutManager layoutManager;
    Watch_Videos_Adapter adapter;
    ProgressBar p_bar;
    RelativeLayout write_layout;
    Context context;
    EditText message_edit;
    ImageButton send_btn;
    ProgressBar send_progress;
    ImageView Goback;
    String myuserid;
    boolean likestatus=false;


    String video_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_watch);
        context = this;
        DataProccessor dataProccessor=new DataProccessor(context);
        myuserid= dataProccessor.getUserid("userid");
        write_layout = findViewById(R.id.write_layout);
        p_bar = findViewById(R.id.p_bar);
        Goback = findViewById(R.id.Goback);
        Goback.setOnClickListener(this);


        send_progress = findViewById(R.id.send_progress);


        Intent bundle = getIntent();
        if (bundle != null) {


            data_list = (ArrayList<Home_Get_Set>) bundle.getSerializableExtra("arraylist");
            position = bundle.getIntExtra("position", 0);



            Set_Adapter();


        }

    }

    public void Set_Adapter() {
        recyclerView = findViewById(R.id.recylerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        adapter = new Watch_Videos_Adapter(context, data_list, new Watch_Videos_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, final Home_Get_Set item, View view) {

                switch (view.getId()) {

                    case R.id.user_pic:
                        onPause();
                        OpenProfile(item,false);
                        break;

                    case R.id.username:
                        openprofilebyusername(item);
                        break;

                    case R.id.layout_map:
                        if (item.lat!=null && item.longi!=null && !item.lat.equalsIgnoreCase("") && !item.longi.equalsIgnoreCase("")){
                            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr="+item.lat+","+item.longi);
                            Intent mapintent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(mapintent);
                        }else {
                            Toast.makeText(CommonWatchActivity.this, "No Location Found", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.like_layout:
                        if (myuserid!=null){
                           AddLikeonVideo(item,postion,item.video_id,item.userid,myuserid,"1");
                        }else {
                            Intent intent=new Intent(CommonWatchActivity.this, Signup_chooseActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.comment_layout:
                        OpenComment(item);
                        break;

//                    case R.id.shared_layout:
//                        final VideoAction_F fragment = new VideoAction_F(item.video_id, new Fragment_Callback() {
//                            @Override
//                            public void Responce(Bundle bundle) {
//
//                                if(bundle.getString("action").equals("save")){
//                                    Save_Video(item);
//                                }
//                            }
//                        });
//                        fragment.show(getSupportFragmentManager(), "");
//                        break;



                }

            }
        });

        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);


        // this is the scroll listener of recycler view which will tell the current item number
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no = scrollOffset / height;

                if (page_no != currentPage) {
                    currentPage = page_no;

                    Privious_Player();
                    Set_Player(currentPage);
                }

            }
        });

        recyclerView.scrollToPosition(position);

    }

    private void AddLikeonVideo(final Home_Get_Set home_get_set,int position,String videoid, String videOwnerId, String Userid, String Likestatus) {
        try {


            p_bar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(CommonWatchActivity.this);
            apiInteface.AddLikeOnVideo(videoid, videOwnerId, Userid, Likestatus).enqueue(new retrofit2.Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        p_bar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                                //Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();

                                if (likestatus==false){
                                    home_get_set.like_count="1";
                                    home_get_set.liked="1";
                                    likestatus=true;
                                }else {
                                    home_get_set.like_count="0";
                                    home_get_set.liked="0";
                                    likestatus=false;
                                }






                                data_list.remove(position);
                                data_list.add(position,home_get_set);
                                adapter.notifyDataSetChanged();



                            } else {
                                p_bar.setVisibility(View.GONE);
                                Toast.makeText(CommonWatchActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(CommonWatchActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("comment failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OpenComment(Home_Get_Set item) {
        Fragment_Data_Send fragment_data_send=this;

        Comment_F comment_f = new Comment_F(fragment_data_send);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("video_id",item.video_id);
        args.putString("user_id",item.userid);
        comment_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.WatchVideo_F, comment_f).commit();
    }

    private void openprofilebyusername(Home_Get_Set item) {
        Intent intent=new Intent(CommonWatchActivity.this, UserProfileActivity.class);
        intent.putExtra("GetProfile",item.first_name);
        startActivity(intent);
    }

    private void OpenProfile(Home_Get_Set item, boolean b) {
        Intent intent=new Intent(CommonWatchActivity.this, UserProfileActivity.class);
        intent.putExtra("userid_",item.userid);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Goback:
                finish();
                break;
        }
    }


    public void Set_Player(final int currentPage) {

        final Home_Get_Set item = data_list.get(currentPage);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "DressApp"));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(item.video_url));

        Log.d(Variables.tag, item.video_url);


        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);


        View layout = layoutManager.findViewByPosition(currentPage);
        PlayerView playerView = layout.findViewById(R.id.playerviewcommon);
        playerView.setPlayer(player);


        player.setPlayWhenReady(true);
        privious_player = player;


        final RelativeLayout mainlayout = layout.findViewById(R.id.mainlayout);
        playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);
                    float deltaX = e1.getX() - e2.getX();
                    float deltaXAbs = Math.abs(deltaX);
                    // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                    if ((deltaXAbs > 100) && (deltaXAbs < 1000)) {
                        if (deltaX > 0) {
                        //   OpenProfile(item,true);
                        }
                    }


                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);
                    if (!player.getPlayWhenReady()) {
                        privious_player.setPlayWhenReady(true);
                    } else {
                        privious_player.setPlayWhenReady(false);
                    }


                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
//                    Show_video_option(item);

                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if (!player.getPlayWhenReady()) {
                        privious_player.setPlayWhenReady(true);
                    }

//                        if (myuserid!=null){
//                            Show_heart_on_DoubleTap(item, mainlayout, e);
//                            AddLikeonVideo(item,currentPage,item.video_id,item.userid,myuserid,"1");
//                        }else {
//                            Intent intent=new Intent(CommonWatchActivity.this, Signup_chooseActivity.class);
//                            startActivity(intent);
//                        }


                    return super.onDoubleTap(e);

                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


//        Call_Api_For_Singlevideos(currentPage);
    }





    @Override
    public void onDataSent(String yourData) {
        int comment_count = Integer.parseInt(yourData);
        Home_Get_Set item = data_list.get(currentPage);
        item.video_comment_count = "" + comment_count;
        data_list.remove(currentPage);
        data_list.add(currentPage, item);
        adapter.notifyDataSetChanged();


    }


    public boolean check_permissions() {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 2);
        } else {

            return true;
        }

        return false;
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    // when we swipe for another video this will relaese the privious player
    SimpleExoPlayer privious_player;

    public void Privious_Player() {
        if (privious_player != null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (privious_player != null) {
            privious_player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (privious_player != null) {
            privious_player.release();
        }


    }


    // Bottom all the function and the Call back listener of the Expo player
    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING) {
            p_bar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            p_bar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }


    @Override
    public void onSeekProcessed() {


    }


}
