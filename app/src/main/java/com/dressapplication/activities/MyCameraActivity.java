package com.dressapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.coremedia.iso.boxes.Container;
import com.dressapplication.ProgressBarClass.ProgressBarListener;
import com.dressapplication.ProgressBarClass.SegmentedProgressBar;
import com.dressapplication.R;
import com.dressapplication.change.Functions;
import com.dressapplication.change.Merge_Video_Audio;
import com.dressapplication.ui.camera.CaptureVideo.VideoPlayActivity;
import com.dressapplication.utils.Variables;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyCameraActivity extends AppCompatActivity implements View.OnClickListener{
    CameraView cameraView;
    File file;
    int number=0;
    String outputFilePath=null;
    private int REQUEST_TAKE_GALLERY_VIDEO = 2;
    ArrayList<String> videopaths=new ArrayList<>();

    ImageButton record_image;
    ImageButton done_btn;
    boolean is_recording=false;
    boolean is_flash_on=false;

    ImageButton flash_btn;

    SegmentedProgressBar video_progress;

    LinearLayout camera_options;

    ImageButton rotate_camera;
    private ImageView img_gallery_video;
    public static int Sounds_list_Request_code=1;

    private final static int READ_EXTERNAL_STORAGE_PERMISSION_RESULT = 0;
    private static final String VIDEO_DIRECTORY_NAME = "DressApp";
    TextView add_sound_txt;


    int sec_passed=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);
        chechReadExternalstorage();
        video_progress=findViewById(R.id.video_progress);
        img_gallery_video=findViewById(R.id.img_gallery_video);

        Variables.Selected_sound_id="null";
        Variables.recording_duration=Variables.max_recording_duration;



        cameraView = findViewById(R.id.camera);
        camera_options=findViewById(R.id.camera_options);

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
            }

            @Override
            public void onError(CameraKitError cameraKitError) {
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });


        record_image=findViewById(R.id.record_image);


        findViewById(R.id.upload_layout).setOnClickListener(this);


        done_btn=findViewById(R.id.done);
        done_btn.setEnabled(false);
        done_btn.setOnClickListener(this);


        rotate_camera=findViewById(R.id.rotate_camera);
        rotate_camera.setOnClickListener(this);
        flash_btn=findViewById(R.id.flash_camera);
        flash_btn.setOnClickListener(this);

        findViewById(R.id.Goback).setOnClickListener(this);

        add_sound_txt=findViewById(R.id.add_sound_txt);
        add_sound_txt.setOnClickListener(this);


        Intent intent=getIntent();
        if(intent.hasExtra("sound_name")){
            add_sound_txt.setText(intent.getStringExtra("sound_name"));
            Variables.Selected_sound_id=intent.getStringExtra("sound_id");
            PreparedAudio();
        }



        // this is code hold to record the video
        final Timer[] timer = {new Timer()};
        record_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    timer[0] =new Timer();

                    timer[0].schedule(new TimerTask() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!is_recording)
                                        Start_or_Stop_Recording();
                                }
                            });

                        }
                    }, 200);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    timer[0].cancel();
                    if(is_recording){
                        Start_or_Stop_Recording();
                    }
                }
                return false;
            }

        });

        initlize_Video_progress();
    }


    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.rotate_camera:
                RotateCamera();
                break;

            case R.id.upload_layout:
                Intent upload_intent=new Intent(this, GalleryVideosActivity.class);
                startActivity(upload_intent);
                overridePendingTransition(R.anim.in_from_bottom,R.anim.out_to_top);
                break;

            case R.id.done:
                append();
                break;

            case R.id.record_image:
                Start_or_Stop_Recording();
                break;

            case R.id.flash_camera:

                if(is_flash_on){
                    is_flash_on=false;
                    cameraView.setFlash(0);
                    flash_btn.setImageDrawable(getResources().getDrawable(R.drawable.flash_icon));

                }else {
                    is_flash_on=true;
                    cameraView.setFlash(CameraKit.Constants.FLASH_TORCH);
                    flash_btn.setImageDrawable(getResources().getDrawable(R.drawable.flash_icon));
                }

                break;

            case R.id.Goback:
                onBackPressed();
                break;

            case R.id.add_sound_txt:
//                Intent intent =new Intent(this,SoundList_Main_A.class);
//                startActivityForResult(intent,Sounds_list_Request_code);
//                overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                break;
//            case R.id.img_gallery_video:
//                chooseVideoFromGallary();
//                break;

        }


    }



    // this will apped all the videos parts in one  fullvideo
    private boolean append() {
        final ProgressDialog progressDialog=new ProgressDialog(MyCameraActivity.this);
        new Thread(new Runnable() {
            @Override
            public void run() {


                runOnUiThread(new Runnable() {
                    public void run() {

                        progressDialog.setMessage("Please wait..");
                        progressDialog.show();
                    }
                });

                ArrayList<String> video_list=new ArrayList<>();
                for (int i=0;i<videopaths.size();i++){

                    File file=new File(videopaths.get(i));
                    if(file.exists()) {

                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(MyCameraActivity.this, Uri.fromFile(file));
                        String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
                        boolean isVideo = "yes".equals(hasVideo);

                        if (isVideo && file.length() > 3000) {
                            Log.d("resp", videopaths.get(i));
                            video_list.add(videopaths.get(i));
                        }else {
                            Toast.makeText(MyCameraActivity.this, "More than 3 sec", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Log.e("dfodsfjidjfosd","file not exists");
                    }
                }



                try {

                    Movie[] inMovies = new Movie[video_list.size()];
                    try {

                        for (int i = 0; i < video_list.size(); i++) {

                            inMovies[i] = MovieCreator.build(video_list.get(i));
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("erroriscoming",e.getMessage()+"  ,  "+e.getLocalizedMessage());
                    }


                    List<Track> videoTracks = new LinkedList<Track>();
                    List<Track> audioTracks = new LinkedList<Track>();
                    for (Movie m : inMovies) {
                        for (Track t : m.getTracks()) {
                            if (t.getHandler().equals("soun")) {
                                audioTracks.add(t);
                            }
                            if (t.getHandler().equals("vide")) {
                                videoTracks.add(t);
                            }
                        }
                    }
                    Movie result = new Movie();
                    if (audioTracks.size() > 0) {
                        result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
                    }
                    if (videoTracks.size() > 0) {
                        result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
                    }

                    Container out = new DefaultMp4Builder().build(result);


                    if(audio!=null){
                        outputFilePath=Variables.outputfile;
                    }else {
                        outputFilePath=Variables.outputfile2;
                    }

                    FileOutputStream fos = new FileOutputStream(new File(outputFilePath));
                    out.writeContainer(fos.getChannel());
                    fos.close();

                    runOnUiThread(new Runnable() {
                        public void run() {
                          progressDialog.dismiss();

                            if(audio!=null){
                                Merge_withAudio();
                            }
                            else {
                                Go_To_preview_Activity();
                            }

                        }
                    });
                } catch (Exception e) {
                    Log.e("execptionaarahihe",""+e.getMessage());
                }
            }
        }).start();



        return true;
    }

    // this will add the select audio with the video
    public void Merge_withAudio(){

        String root = Environment.getExternalStorageDirectory().toString();
        String audio_file;
        audio_file =Variables.app_folder+Variables.SelectedAudio_AAC;

        String video = root + "/"+"output.mp4";
        String finaloutput = root + "/"+"output2.mp4";

        Merge_Video_Audio merge_video_audio=new Merge_Video_Audio(MyCameraActivity.this);
        merge_video_audio.doInBackground(audio_file,video,finaloutput);

    }

    public void Go_To_preview_Activity(){
   //     File file=new File(outputFilePath);
        Intent intent =new Intent(this, VideoPlayActivity.class);
        intent.putExtra("path",file.toString());
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void RotateCamera(){
        cameraView.toggleFacing();
    }

    public void Start_or_Stop_Recording(){

        if (!is_recording && sec_passed<(Variables.recording_duration/1000)-1) {
            number=number+1;

            is_recording=true;

             file = new File(Variables.root + "/" + "myvideo"+(number)+".mp4");
            videopaths.add(Variables.root + "/" + "myvideo"+(number)+".mp4");
            cameraView.captureVideo(file);


            if(audio!=null)
                audio.start();



            video_progress.resume();


            done_btn.setBackgroundResource(R.mipmap.tick);
            done_btn.setEnabled(true);

            record_image.setImageDrawable(getResources().getDrawable(R.drawable.circle_btn));

            camera_options.setVisibility(View.GONE);
            add_sound_txt.setClickable(false);
            rotate_camera.setVisibility(View.GONE);

        }

        else if (is_recording) {

            is_recording=false;

            video_progress.pause();
            video_progress.addDivider();

            if(audio!=null)
                audio.pause();

            cameraView.stopVideo();


            if(sec_passed>((Variables.recording_duration/1000)/3)) {
                done_btn.setBackgroundResource(R.mipmap.tick);
                done_btn.setEnabled(true);
            }

            record_image.setImageDrawable(getResources().getDrawable(R.mipmap.camera_circle));
            camera_options.setVisibility(View.VISIBLE);

        }

        else if(sec_passed>(Variables.recording_duration/1000)){
            Functions.Show_Alert(this,"Alert","Video only can be a "+(int)Variables.recording_duration/1000+" S");
        }



    }


    // this will play the sound with the video when we select the audio
    MediaPlayer audio;
    public  void PreparedAudio(){
        File file=new File(Variables.app_folder+ Variables.SelectedAudio_AAC);
        if(file.exists()) {
            audio = new MediaPlayer();
            try {
                audio.setDataSource(Variables.app_folder + Variables.SelectedAudio_AAC);
                audio.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this, Uri.fromFile(file));
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Integer.parseInt(durationStr);

            if(file_duration<Variables.max_recording_duration){
                Variables.recording_duration=file_duration;
                initlize_Video_progress();
            }

        }


    }



    public void initlize_Video_progress(){
        sec_passed=0;
        video_progress=findViewById(R.id.video_progress);
        video_progress.enableAutoProgressView(Variables.recording_duration);
        video_progress.setDividerColor(Color.WHITE);
        video_progress.setDividerEnabled(true);
        video_progress.setDividerWidth(4);
        video_progress.setShader(new int[]{Color.CYAN, Color.CYAN, Color.CYAN});

        video_progress.SetListener(new ProgressBarListener() {
            @Override
            public void TimeinMill(long mills) {
                sec_passed = (int) (mills/1000);

                if(sec_passed>(Variables.recording_duration/1000)){
                    Start_or_Stop_Recording();
                }

            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            if (audio != null) {
                audio.stop();
                audio.reset();
                audio.release();
            }
            cameraView.stop();

        }catch (Exception e){

        }
    }

    private void chechReadExternalstorage() {


        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) || (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)) {
             //   Log.v(TAG,"Permission is granted1");
            } else {

             //   Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_RESULT);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
          //  Log.v(TAG,"Permission is granted1");
          //  return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_RESULT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }else {
                    Toast.makeText(this, "Please allow permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}