package com.dressapplication.ui.camera.CaptureVideo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.dressapplication.R;
import com.dressapplication.activities.VideoDescriptionActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {
    private VideoView videoview;
    private ProgressBar progressBar;
    private Button done;
    File file;
    String thumb_base_64;
    File  thumbfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        videoview=findViewById(R.id.videoview);
        progressBar=findViewById(R.id.progressBar);
        done=findViewById(R.id.done);

        if (getIntent()!=null) {

            file = new File(getIntent().getStringExtra("path"));
            String filename = getIntent().getStringExtra("path");
          Uri  uri = Uri.parse(filename);

            Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(uri.getPath(),
                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

            Bitmap bmThumbnail_resized = Bitmap.createScaledBitmap(bmThumbnail,(int)(bmThumbnail.getWidth()*0.4), (int)(bmThumbnail.getHeight()*0.4), true);

            thumb_base_64 = Bitmap_to_base64( bmThumbnail_resized);


         //   Log.e("Videopath", file.getAbsolutePath());
     //       Log.e("Thumbpath", thumbfile.getAbsolutePath());


            // Set media controller
            videoview.setVideoURI(Uri.parse(file.getAbsolutePath()));
            videoview.setMediaController(new MediaController(VideoPlayActivity.this));
            videoview.requestFocus();
            videoview.setVideoPath(file.getAbsolutePath());
            videoview.seekTo(100);
            videoview.start();
            videoview.setOnCompletionListener(mp -> {

            });
        }

        done.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(VideoPlayActivity.this, VideoDescriptionActivity.class);
                intent.putExtra("videofile",file.toString());
               // intent.putExtra("thumbfile",thumb_base_64);
                startActivity(intent);
              //  uplaodVideo();
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(VideoPlayActivity.this);
        return preferences.getString("userid", null);
    }

    public  String Bitmap_to_base64( Bitmap imagebitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] byteArray = baos .toByteArray();
        String base64= Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }


}
