package com.dressapplication.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.dressapplication.adapters.Home_Adapter;
import com.dressapplication.change.ApiRequest;
import com.dressapplication.change.Callback;
import com.dressapplication.change.Comment_F;
import com.dressapplication.change.CommonSharedFragment;
import com.dressapplication.change.Fragment_Data_Send;
import com.dressapplication.change.RootFragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends RootFragment implements Player.EventListener, Fragment_Data_Send {
    View view;
    Context context;
    RecyclerView recyclerView;
    ArrayList<Home_Get_Set> data_list;
    int currentPage=-1;
    LinearLayoutManager layoutManager;
    ProgressBar p_bar;
    int swipe_count=0;
    String myuserid;
    private SwipeRefreshLayout swiperefresh;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        context=getContext();

        DataProccessor dataProccessor=new DataProccessor(context);
        myuserid= dataProccessor.getUserid("userid");
        p_bar=view.findViewById(R.id.p_bar);

        recyclerView=view.findViewById(R.id.recylerview);
        swiperefresh=view.findViewById(R.id.swiperefresh);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        SnapHelper snapHelper =  new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

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
                int page_no=scrollOffset / height;

                if(page_no!=currentPage ){
                    currentPage=page_no;

                    Release_Privious_Player();
                    Set_Player(currentPage);

                }
            }
        });

        swiperefresh.setProgressViewOffset(false, 0, 200);

        swiperefresh.setColorSchemeResources(R.color.black);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage=-1;
                Call_Api_For_get_Allvideos();
            }
        });

        Call_Api_For_get_Allvideos();





        return view;
    }



    Home_Adapter adapter;
    public void Set_Adapter(){

        adapter=new Home_Adapter(context, data_list, new Home_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int positon, Home_Get_Set item, View view) {

                switch(view.getId()) {

                    case R.id.user_pic:
                        onPause();
                        OpenProfile(item,false);
                        break;

                    case R.id.username:
                        onPause();
                        openProfilebyUserName(item);
                        break;

                    case R.id.like_layout:
                        if (myuserid!=null){
                            AddLikeonVideo(item,positon,item.video_id,item.userid,getuserid(),"1");
                        }else {
                            Intent intent=new Intent(getContext(), Signup_chooseActivity.class);
                            startActivity(intent);
                        }

                        break;

                    case R.id.comment_layout:
                        if (myuserid!=null){
                            OpenComment(item);
                        }else {
                            Intent intent=new Intent(getContext(), Signup_chooseActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case R.id.shared_layout:

                        if (myuserid!=null){
                            openSharedoption(item);
                        }else {
                            Intent intent=new Intent(getContext(), Signup_chooseActivity.class);
                            startActivity(intent);
                        }

                        break;

                    case R.id.layout_map:
                        if (item.lat!=null && item.longi!=null && !item.lat.equalsIgnoreCase("") && !item.longi.equalsIgnoreCase("")){

                            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr="+item.lat+","+item.longi);
                            Intent mapintent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(mapintent);
                        }else {
                            Toast.makeText(getContext(), "No Location Found", Toast.LENGTH_SHORT).show();
                        }
                        break;


                }

            }
        });

        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

    }

    private void openSharedoption(Home_Get_Set item) {

        Fragment_Data_Send fragment_data_send=this;

        CommonSharedFragment comment_f = new CommonSharedFragment(fragment_data_send);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("video_id",item.video_id);
        args.putString("user_id",item.userid);
        comment_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, comment_f).commit();
    }

    private void openProfilebyUserName(Home_Get_Set item) {
    //    Toast.makeText(context, "username", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getContext(), UserProfileActivity.class);
        intent.putExtra("GetProfile",item.first_name);
        startActivity(intent);
    }

    private String getuserid () {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("userid", null);
    }




    // Bottom two function will call the api and get all the videos form api and parse the json data
    private void Call_Api_For_get_Allvideos() {

        DataProccessor dataProccessor=new DataProccessor(getContext());
        String userid=dataProccessor.getUserid("userid");

//
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("loginId",userid);
//            parameters.put("token",MainMenuActivity.token);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.showAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                swiperefresh.setRefreshing(false);
                Parse_data(resp);

            }
        });



    }

    public void Parse_data(String responce){

        data_list=new ArrayList<>();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("201")){
                JSONArray msgArray=jsonObject.getJSONArray("data");
                for (int i=0;i<msgArray.length();i++) {
                    JSONObject itemdata = msgArray.optJSONObject(i);
                    Home_Get_Set item=new Home_Get_Set();
//                    item.fb_id=itemdata.optString("fb_id");
//
//                    JSONObject user_info=itemdata.optJSONObject("user_info");
//
                    item.first_name=itemdata.optString("username");
//                    item.last_name=user_info.optString("last_name","User");
//                    item.profile_pic=user_info.optString("profile_pic","null");
//
//                    JSONObject sound_data=itemdata.optJSONObject("sound");
//                    item.sound_id=sound_data.optString("id");
//                    item.sound_name=sound_data.optString("sound_name");
//                    item.sound_pic=sound_data.optString("videoThumImg");

                    item.userid=itemdata.optString("userId");
                    item.lat=itemdata.optString("latitude");
                    item.longi=itemdata.optString("longitude");

//                    JSONObject count=itemdata.optJSONObject("count");
//                    item.like_count=count.optString("like_count");
                    item.isloginliked=itemdata.optString("loginUserIsLiked");

                    item.profile_pic=ApiUtils.IMAGE_BASE_URL+itemdata.optString("profile_image");

                    item.video_id=itemdata.optString("vid");
                    item.like_count=itemdata.optString("likedCount");
                    item.liked=itemdata.optString("isLiked");
                    item.video_url=ApiUtils.VIDEO_BASE_URL+itemdata.optString("videoName");
                    item.video_description=itemdata.optString("videoDescribe");

                    item.thum=ApiUtils.VIDEO_BASE_URL+itemdata.optString("videoThumImg");
                    item.video_comment_count=itemdata.optString("commentCount");

                    data_list.add(item);
                    Collections.reverse(data_list);
                }

                Set_Adapter();

            }else {
                Toast.makeText(context, ""+jsonObject.optString("status"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // this will call when swipe for another video and
    // this function will set the player to the current video
    public void Set_Player(final int currentPage){

        final Home_Get_Set item= data_list.get(currentPage);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "DressApp"));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(item.video_url));

        Log.d("resp",item.video_url);


        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);


        View layout=layoutManager.findViewByPosition(currentPage);
        final PlayerView playerView=layout.findViewById(R.id.playerview);
        playerView.setPlayer(player);




        player.setPlayWhenReady(true);
        privious_player=player;




        final RelativeLayout mainlayout = layout.findViewById(R.id.mainlayout);
        playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);
                    float deltaX = e1.getX() - e2.getX();
                    float deltaXAbs = Math.abs(deltaX);
                    // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                    if((deltaXAbs > 100) && (deltaXAbs < 1000)) {
                        if(deltaX > 0)
                        {
                            OpenProfile(item,true);
                        }
                    }


                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);
                    if(!player.getPlayWhenReady()){
                        privious_player.setPlayWhenReady(true);
                    }else{
                        privious_player.setPlayWhenReady(false);
                    }


                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                  //  Show_video_option(item);

                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if(!player.getPlayWhenReady()){
                        privious_player.setPlayWhenReady(true);
                    }


                    if(getuserid()!=null) {
                        Show_heart_on_DoubleTap(item, mainlayout, e);
                        AddLikeonVideo(item,currentPage,item.video_id,item.userid,getuserid(),"1");
                    }else {
                        Toast.makeText(context, "Please Login into app", Toast.LENGTH_SHORT).show();
                    }
                    return super.onDoubleTap(e);

                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });



    }


    public void Show_heart_on_DoubleTap(Home_Get_Set item,final RelativeLayout mainlayout,MotionEvent e){

        int x = (int) e.getX()-100;
        int y = (int) e.getY()-100;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ImageView iv = new ImageView(getApplicationContext());
        lp.setMargins(x, y, 0, 0);
        iv.setLayoutParams(lp);
        if(item.liked.equals("1"))
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.unlike_icon));
        else
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.like));

        mainlayout.addView(iv);
        Animation fadeoutani = AnimationUtils.loadAnimation(context,R.anim.fade_out);

        fadeoutani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainlayout.removeView(iv);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(fadeoutani);

    }





    @Override
    public void onDataSent(String yourData) {
        int comment_count =Integer.parseInt(yourData);
        Home_Get_Set item=data_list.get(currentPage);
        item.video_comment_count=""+comment_count;
        data_list.remove(currentPage);
        data_list.add(currentPage,item);
        adapter.notifyDataSetChanged();
    }



    // this will call when go to the home tab From other tab.
    // this is very importent when for video play and pause when the focus is changes
    boolean is_visible_to_user;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        is_visible_to_user=isVisibleToUser;

        if(privious_player!=null && isVisibleToUser){
            privious_player.setPlayWhenReady(true);
        }else if(privious_player!=null && !isVisibleToUser){
            privious_player.setPlayWhenReady(false);
        }
    }



    // when we swipe for another video this will relaese the privious player
    SimpleExoPlayer privious_player;
    public void Release_Privious_Player(){
        if(privious_player!=null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }



    private void AddLikeonVideo(final Home_Get_Set home_get_set,int position,String videoid, String videOwnerId, String Userid, String Likestatus) {
        try {
            p_bar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.AddLikeOnVideo(videoid, videOwnerId, Userid, Likestatus).enqueue(new retrofit2.Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        p_bar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                          //      Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();

                                String action=home_get_set.isloginliked;

                                if(action.equals("1")){
                                    action="0";
                                    try {
                                        home_get_set.like_count=""+(Integer.parseInt(home_get_set.like_count) -1);
                                    }catch (NumberFormatException e){

                                    }

                                }else {
                                    action="1";
                                    try {
                                        home_get_set.like_count=""+(Integer.parseInt(home_get_set.like_count) +1);
                                    }catch (NumberFormatException e){

                                    }

                                }

                                data_list.remove(position);
                                home_get_set.isloginliked=action;
                                data_list.add(position,home_get_set);
                                adapter.notifyDataSetChanged();



                            } else {
                                p_bar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("comment failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    // this will open the comment screen
    private void OpenComment(Home_Get_Set item) {


        Fragment_Data_Send fragment_data_send=this;

        Comment_F comment_f = new Comment_F(fragment_data_send);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("video_id",item.video_id);
        args.putString("user_id",item.userid);
        comment_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, comment_f).commit();


    }



//     this will open the profile of user which have uploaded the currenlty running video
    private void OpenProfile(Home_Get_Set item,boolean from_right_to_left) {

    //    Toast.makeText(context, "userid="+item.userid, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(context, UserProfileActivity.class);
        intent.putExtra("userid_",item.userid);
        startActivity(intent);

        }


    public void Delete_file_no_watermark(Home_Get_Set item){
        File file=new File(Environment.getExternalStorageDirectory() +"/DressApp/"+item.video_id+"no_watermark"+".mp4");
        if(file.exists()){
            file.delete();
        }
    }

    public void Scan_file(Home_Get_Set item){
        MediaScannerConnection.scanFile(getActivity(),
                new String[] { Environment.getExternalStorageDirectory() +"/DressApp/"+item.video_id+".mp4" },
                null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {

                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }



    public boolean is_fragment_exits(){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if(fm.getBackStackEntryCount()==0){
            return false;
        }else {
            return true;
        }

    }

    // this is lifecyle of the Activity which is importent for play,pause video or relaese the player
    @Override
    public void onResume() {
        super.onResume();
        if((privious_player!=null && is_visible_to_user) && !is_fragment_exits() ){
            privious_player.setPlayWhenReady(true);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(privious_player!=null){
            privious_player.release();
        }
    }



    public boolean check_permissions() {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            requestPermissions(PERMISSIONS, 2);
        }else {

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

        if(playbackState==Player.STATE_BUFFERING){
            p_bar.setVisibility(View.VISIBLE);
        }
        else if(playbackState==Player.STATE_READY){
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
