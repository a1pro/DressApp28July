package com.dressapplication.ui.home;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.Interface.CommentLikeInterface;
import com.dressapplication.Model.FollowingData;
import com.dressapplication.Model.GetFollowing;
import com.dressapplication.activities.HashTagActivity;
import com.dressapplication.adapters.SendToAdapter;
import com.dressapplication.adapters.ShareToAdapeter;
import com.dressapplication.adapters.ThirdAdapter;
import com.dressapplication.utils.PatternEditableBuilder;
import com.dressapplication.utils.VideoPlayerConfig;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.GetAlldataWithComment;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.Model.VideoComment;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.activities.MainActivity;
import com.dressapplication.activities.UserProfileActivity;
import com.dressapplication.activities.signupActivity;
import com.dressapplication.adapters.RvCommentAdapter;
import com.dressapplication.utils.NetworkUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static com.google.android.exoplayer2.Player.STATE_READY;

public class ChildFragment extends Fragment implements View.OnClickListener {
    BottomSheetBehavior sheetBehavior;
    BottomSheetBehavior sheetBehavior2;
    CardView bottomSheet,bottom_sheet_share;
    RecyclerView recyclerview_bottom_sheet,recyclerview_sendto,recyclerview_shareto,recylerview_report;
    RvCommentAdapter rvCommentAdapter;
    private ImageView iv_cross;
    private TextView tv_comment, tv_share, tv_username, tvTitle,tv_likes,tv_home_username,tv_home_hashtags;
    private ImageView  videothumb, img_send, iv_likes,iv_comment,img_plus,iv_share,iv_map;
    private CircleImageView iv_profile;
    private EditText et_leave_omment;
    SimpleExoPlayerView exoPlayerView;
    public static SimpleExoPlayer exoPlayer;
    String videobaseurl = "https://a1professionals.net/dressApp/assets/videos/";
    int VideoCommentCount;
    private ProgressBar progressBar, exoprogress;
    String Videoownerid;
    List<VideoComment> list = new ArrayList<>();
    private static String LOG_TAG = "PLAYER";
    String videoid, userid, videothumbname;
    Boolean likeorunlike = false;
    String videourl;
    private NavController navController;
    private String likedcount;
    String isliked,username,Videodescribe,userimage;
    String latitude,longitude;
    private boolean followunfollow=false;
    int maxBytes = 100;
    private Button bt_cancel;
    List<FollowingData> followinglist =new ArrayList<>();



    public ChildFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);




        Bundle bundle = getArguments();
        videourl = bundle.getString("videourl");
         username = bundle.getString("username");
     //   VideoCommentCount = bundle.getInt("VideoCommentCount");
     //   Videoownerid = bundle.getString("videoOwnerId");
        videothumbname = bundle.getString("Videothumbnail");
        videoid = bundle.getString("Videoid");
        userid = bundle.getString("userid");
          isliked = bundle.getString("isliked");
        likedcount=bundle.getString("likedcount");
        Videodescribe=bundle.getString("videoDescribe");
         userimage=bundle.getString("userimage");
        latitude=bundle.getString("lati");
        longitude=bundle.getString("long");

        Log.e("Videodescribe",""+userid);

        initUI(view);
        Setdata();
        initializePlayer(videourl);
        Getfollowing();


        return view;
    }

    private void Setdata() {

        if (Videodescribe!=null){
            tv_home_hashtags.setText(Videodescribe);
        }else {
            tv_home_hashtags.setText("");
        }



        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), Color.WHITE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                if (text!=null && !text.isEmpty()){
                                    Intent intent6=new Intent(getContext(),HashTagActivity.class);
                                    intent6.putExtra("hashvalue",text);
                                    startActivity(intent6);

                                }
                            }
                        }).into(tv_home_hashtags);

        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.WHITE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                if (text!=null && !text.isEmpty()){
                                    Intent intent6=new Intent(getContext(),UserProfileActivity.class);
                                    intent6.putExtra("GetProfile",text);
                                    startActivity(intent6);

                                }
                            }
                        }).into(tv_home_hashtags);

//        tv_comment.setText(""+VideoCommentCount);

        if (likedcount!=null){
            tv_likes.setText("" +likedcount );
        }else {
            tv_likes.setText("0");
        }


        if (isliked!=null){
            if (isliked.equalsIgnoreCase("1")){
                iv_likes.setImageDrawable(getResources().getDrawable(R.drawable.like));
            }
            else {
                iv_likes.setImageDrawable(getResources().getDrawable(R.drawable.unlike_icon));
            }
        }

        if (username != null) {
            tv_username.setText("@"+username);
            tv_home_username.setText("@"+username);
        }

        if (videothumbname != null) {
            Glide.with(getContext()).load(videobaseurl + videothumbname).diskCacheStrategy(DiskCacheStrategy.ALL).into(videothumb);
        }

        if (userimage!=null){
            Glide.with(getContext()).load(ApiUtils.IMAGE_BASE_URL + userimage).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_profile);
        }else {
            iv_profile.setImageDrawable(getResources().getDrawable(R.mipmap.app_icon));
        }



    }


    private void initUI(View view) {
        bt_cancel=view.findViewById(R.id.bt_cancel);
        exoPlayerView = view.findViewById(R.id.exo_player_view);
        exoprogress = view.findViewById(R.id.exoprogress);
        iv_map=view.findViewById(R.id.iv_map);
        img_plus=view.findViewById(R.id.img_plus);
        iv_share=view.findViewById(R.id.iv_share);
        tv_home_username=view.findViewById(R.id.tv_home_username);
        tv_home_hashtags=view.findViewById(R.id.tv_home_hashtags);
        iv_comment=view.findViewById(R.id.iv_comment);
        iv_likes = view.findViewById(R.id.iv_likes);
        tv_likes=view.findViewById(R.id.tv_likes);
        et_leave_omment = view.findViewById(R.id.et_leave_omment);
        img_send = view.findViewById(R.id.img_send);
        videothumb = view.findViewById(R.id.videothumb);
        tv_comment = view.findViewById(R.id.tv_comment);
        tv_share = view.findViewById(R.id.tv_share);
        iv_profile = view.findViewById(R.id.iv_profile);
        tv_username = view.findViewById(R.id.tv_username);
        tv_share.setOnClickListener(this);
        iv_profile.setOnClickListener(this);
        img_send.setOnClickListener(this);
        iv_likes.setOnClickListener(this);
        et_leave_omment.setOnClickListener(this);
        iv_comment.setOnClickListener(this);
        tv_home_username.setOnClickListener(this);
        tv_home_hashtags.setOnClickListener(this);
        img_plus.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_map.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);

        tvTitle = view.findViewById(R.id.tvTitle);
        iv_cross = view.findViewById(R.id.iv_cross);
        iv_cross.setOnClickListener(this);

        bottomSheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setHideable(true);//Important to add
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        progressBar = view.findViewById(R.id.progressBar);

        bottom_sheet_share = view.findViewById(R.id.bottom_sheet_share);
        sheetBehavior2 = BottomSheetBehavior.from(bottom_sheet_share);
        sheetBehavior2.setHideable(true);//Important to add
        sheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);

        recyclerview_bottom_sheet = view.findViewById(R.id.recyclerview_bottom_sheet);
        recyclerview_sendto=view.findViewById(R.id.recyclerview_sendto);
        recyclerview_shareto=view.findViewById(R.id.recyclerview_shareto);
        recylerview_report=view.findViewById(R.id.recylerview_report);
    }


    private void initializePlayer(String path) {


        LoadControl loadControl = new DefaultLoadControl(
                new DefaultAllocator(true, 16),
                VideoPlayerConfig.MIN_BUFFER_DURATION,
                VideoPlayerConfig.MAX_BUFFER_DURATION,
                VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);

        // Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Initialize the player
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector,loadControl);

        //Initialize simpleExoPlayerView

        exoPlayerView.setPlayer(exoPlayer);
        exoPlayerView.setUseController(false);


        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "DressApp"));

        Cache cache = new SimpleCache(getContext().getCacheDir(),
                new LeastRecentlyUsedCacheEvictor(maxBytes));
        dataSourceFactory = new CacheDataSourceFactory(cache,
                dataSourceFactory);

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        Uri videoUri = Uri.parse(videobaseurl + path);


        if (path != null) {
            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.
            LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);
            exoPlayer.prepare(loopingSource);
            exoPlayer.prepare(videoSource);
            exoPlayer.setPlayWhenReady(true);
            exoPlayerView.setUseController(false);


            exoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {


                }

                @Override
                public void onLoadingChanged(boolean isLoading) {
                    if (isLoading) {
                        exoprogress.setVisibility(View.VISIBLE);
                    } else {
                        exoprogress.setVisibility(View.INVISIBLE);
                    }

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case STATE_BUFFERING:
                            exoprogress.setVisibility(View.VISIBLE);
                            videothumb.setVisibility(View.GONE);
                            break;
                        case STATE_READY:
                            exoprogress.setVisibility(View.GONE);
                            videothumb.setVisibility(View.GONE);
                            break;
                        case STATE_IDLE:
                            exoprogress.setVisibility(View.GONE);
                            videothumb.setVisibility(View.GONE);
                            break;

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
            });


        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_comment:

                if (getemail() != null) {
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        ((MainActivity) getActivity()).visiblityoff();

                        getAlldataApi();

                    } else {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                        ((MainActivity) getActivity()).visiblityon();
                    }
                }else {
                    Intent intent2 = new Intent(getContext(), signupActivity.class);
                    startActivity(intent2);
                }
                break;

            case R.id.iv_share:
                if (getemail() != null) {
                    if (sheetBehavior2.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);

                        ((MainActivity) getActivity()).visiblityoff();

                        setAdapeters();

                    } else {
                        sheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);

                        ((MainActivity) getActivity()).visiblityon();
                    }
                } else {
                    Intent intent = new Intent(getContext(), signupActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.iv_profile:
                Intent intent=new Intent(getContext(),UserProfileActivity.class);
                intent.putExtra("userid_",userid);
                startActivity(intent);
                break;

            case R.id.iv_cross:
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                    ((MainActivity) getActivity()).visiblityon();
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                 //   MainActivity.btm_frame.setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).visiblityon();
                }
                break;

            case R.id.img_send:
                if (NetworkUtils.isConnected(getContext())) {
                    if (validation()) {
                        AddComment(videoid, userid, userid, et_leave_omment.getText().toString());
                    }
                } else {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_likes:
                if (getemail() != null) {
                    AddLikeonVideo(videoid, userid, userid, "1");
                } else {
                    Intent intent3 = new Intent(getContext(), signupActivity.class);
                    startActivity(intent3);
                }

                break;

            case R.id.et_leave_omment:
                if (getemail() == null) {
                    Intent intent2 = new Intent(getContext(), signupActivity.class);
                    startActivity(intent2);
                }
                break;

            case R.id.tv_home_username:
                Intent intent1=new Intent(getContext(),UserProfileActivity.class);
                intent1.putExtra("userid_",userid);
                startActivity(intent1);
                break;


            case R.id.img_plus:
                FollowUnfollow(getuserid(),userid);
                break;

            case R.id.iv_map:
                if (latitude!=null && longitude!=null && !latitude.equalsIgnoreCase("") && !longitude.equalsIgnoreCase("")){

                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr="+latitude+","+longitude);
                    Intent mapintent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(mapintent);
                }else {
                    Toast.makeText(getContext(), "No Location", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_cancel:
                if (sheetBehavior2.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
                //    MainActivity.btm_frame.setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).visiblityoff();
                } else {
                    sheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
                 //   MainActivity.btm_frame.setVisibility(View.VISIBLE);
                    ((MainActivity) getActivity()).visiblityon();
                }
                break;

        }
    }

    private void setAdapeters() {

        recyclerview_sendto.setHasFixedSize(true);
        recyclerview_shareto.setHasFixedSize(true);
        recylerview_report.setHasFixedSize(true);

        SendToAdapter sendToAdapter = new SendToAdapter(getContext(),followinglist);
        recyclerview_sendto.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerview_sendto.setAdapter(sendToAdapter);

        ShareToAdapeter shareToAdapeter = new ShareToAdapeter(getContext());
        recyclerview_shareto.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerview_shareto.setAdapter(shareToAdapeter);


        ThirdAdapter thirdAdapter = new ThirdAdapter(getContext(),userid,videoid);
        recylerview_report.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recylerview_report.setAdapter(thirdAdapter);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void imHiddenNow() {
        releasePlayer();
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
            exoPlayerView.setPlayer(null);
        }
    }

    public void imVisibleNow() {
        initializePlayer(videourl);
    }


    @Override
    public void onResume() {
        super.onResume();
        //   Toast.makeText(getContext(), "resume", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onStart() {
        super.onStart();
        // Toast.makeText(getContext(), "start", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
            exoPlayer.seekTo(0);
        }

    }

    private String getemail() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("email", null);
    }


    private void getAlldataApi() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.getAlldatawithComment().enqueue(new Callback<GetAlldataWithComment>() {
                @Override
                public void onResponse(Call<GetAlldataWithComment> call, Response<GetAlldataWithComment> response) {
                    if (response.isSuccessful()) {
                        list.clear();
                        progressBar.setVisibility(View.GONE);
                        GetAlldataWithComment data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                                String username = data.getData().get(0).getUsername();

                                        for (int i = 0; i < data.getData().size(); i++) {
                                            if (videoid!=null){
                                                if (videoid.equalsIgnoreCase(data.getData().get(i).getVid())){
                                                    list.addAll(data.getData().get(i).getVideoComments());
                                                    VideoCommentCount=list.size();
                                                }
                                            }
                                        }

//                                rvCommentAdapter = new RvCommentAdapter(getContext(), list, username,commentLikeInterface);
//                                recyclerview_bottom_sheet.setLayoutManager(new LinearLayoutManager(getContext()));
//                                recyclerview_bottom_sheet.setAdapter(rvCommentAdapter);

                                if (VideoCommentCount>0){
                                    tvTitle.setText("" + VideoCommentCount + " Comments");
                                }else {
                                    tvTitle.setText("0 Comments");
                                }




                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "" + data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetAlldataWithComment> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("register failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    CommentLikeInterface commentLikeInterface =new CommentLikeInterface() {
        @Override
        public void LikeComment(int pos, String commentid, String CommentOwnerid) {

            AddLikeOnComment(getuserid(),commentid,CommentOwnerid);
        }


    };


    private void AddComment(String videoid, String videOwnerId, String Userid, String UserComment) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.AddcommentApi(videoid, videOwnerId, Userid, UserComment).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        list.clear();
                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                                Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                et_leave_omment.getText().clear();
                                getAlldataApi();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("comment failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean validation() {
        if (et_leave_omment.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please Leave a Comment", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void AddLikeonVideo(String videoid, String videOwnerId, String Userid, String Likestatus) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.AddLikeOnVideo(videoid, videOwnerId, Userid, Likestatus).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                                //Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                if (likeorunlike == false) {
                                    iv_likes.setImageDrawable(getResources().getDrawable(R.drawable.like));
                                    likeorunlike = true;
                                } else {
                                    iv_likes.setImageDrawable(getResources().getDrawable(R.drawable.unlike_icon));
                                    likeorunlike = false;
                                }

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("comment failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AddLikeOnComment(String Userid,String commentid,String CommentOwnerid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.AddLikeOnComment(Userid,commentid,CommentOwnerid).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                           //     Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();


                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("comment failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void FollowUnfollow(String followeby,String followto) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.FollowUnfollow(followeby,followto).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        ResponseData data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                Toast.makeText(getContext(), ""+data.getMessage(), Toast.LENGTH_SHORT).show();

                                if (followunfollow == false) {
                                   img_plus.setVisibility(View.GONE);
                                    followunfollow = true;
                                } else {
                                    img_plus.setVisibility(View.VISIBLE);
                                    followunfollow = false;
                                }
                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Follow failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getuserid () {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("userid", null);
    }

    private void Getfollowing() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(getContext());
            apiInteface.GetFollowing(getuserid()).enqueue(new Callback<GetFollowing>() {
                @Override
                public void onResponse(Call<GetFollowing> call, Response<GetFollowing> response) {
                    if (response.isSuccessful()){
                        followinglist.clear();
                        progressBar.setVisibility(View.GONE);
                        GetFollowing data=response.body();
                        if (data!=null){
                            if (data.getCode().equalsIgnoreCase("201")){

                                followinglist.addAll(data.getData());

                                //     Toast.makeText(FollowingActivity.this, ""+data.getStatus(), Toast.LENGTH_SHORT).show();


                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                             //   Toast.makeText(getContext(), ""+data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetFollowing> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("follower failure",""+t.getMessage());
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
