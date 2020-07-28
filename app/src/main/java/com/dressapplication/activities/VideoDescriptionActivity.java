package com.dressapplication.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.ui.camera.BitmapUtils;
import com.dressapplication.ui.camera.CaptureVideo.VideoPlayActivity;
import com.dressapplication.utils.Variables;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoDescriptionActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RelativeLayout layout_poststatus;
    private File videofile, thumbfile;
    private Switch switch_comment;
    private ImageView img_thumbnail;
    private String comstatus = "1";
    private String posStatus = "1";
    private ListView listview;
    private LinearLayout layout_controls;
    private Button bt_hashtag, bt_post, bt_friends;
    private EditText et_hashtag;
    private TextView tv_comments, tv_visiblity;
    private ImageView iv_back;
    private String video_path;
    File videogalleryfile;
    private String[] hashtags = {"#trending", "#COVID19", "#photooftheday", "#love", "#nature", "#selfie", "#followme", "#travel", "#tourist", "#ilovemygadgets", "#apple", "#mobile", "#fashion", "#happy", "#cute", "#smile", "#style", "#fun", "#girl", "#repost", "#friends", "#art", "#summer", "#like4like", "#boy"};

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_description);
        progressBar = findViewById(R.id.progressBar);
        iv_back = findViewById(R.id.iv_back);
        img_thumbnail = findViewById(R.id.img_thumbnail);
        switch_comment = findViewById(R.id.switch_comment);
        layout_poststatus = findViewById(R.id.layout_poststatus);
        listview = findViewById(R.id.listview);
        layout_controls = findViewById(R.id.layout_controls);
        bt_hashtag = findViewById(R.id.bt_hashtag);
        et_hashtag = findViewById(R.id.et_hashtag);
        tv_comments = findViewById(R.id.tv_comments);
        bt_post = findViewById(R.id.bt_post);
        tv_visiblity = findViewById(R.id.tv_visiblity);
        bt_friends = findViewById(R.id.bt_friends);

        video_path = Variables.output_filter_file;

//        getIntent().getStringExtra("galleryvideo");

        if (video_path!=null){
            videogalleryfile=new File(video_path);
            Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(video_path,
                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

            if (bmThumbnail != null) {
                img_thumbnail.setImageBitmap(bmThumbnail);
                getThumbnail(videogalleryfile);
            } else {
            }
        }

        try {
            videofile = new File(getIntent().getStringExtra("videofile"));
            String filename = getIntent().getStringExtra("videofile");
            Uri uri = Uri.parse(filename);
            Bitmap bmThumbnail;
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(uri.getPath(),
                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

            if (bmThumbnail != null) {
                img_thumbnail.setImageBitmap(bmThumbnail);
                 getThumbnail(videofile);


            } else {
            }
            // thumbfile=new File(getIntent().getStringExtra("thumbfile"));
        } catch (Exception e) {
            e.printStackTrace();
        }


//        if (thumbfile!=null){
//            Glide.with(VideoDescriptionActivity.this).load(thumbfile).into(img_thumbnail);
//        }

        switch_comment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    comstatus = "1";
                    tv_comments.setText("Comment On");
                } else {
                    comstatus = "0";
                    tv_comments.setText("Comment Off");
                }
            }
        });


        layout_poststatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoDescriptionActivity.this, PostStatusActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        bt_hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_controls.setVisibility(View.GONE);
                listview.setVisibility(View.VISIBLE);
                ArrayAdapter adapter = new ArrayAdapter<String>(VideoDescriptionActivity.this, android.R.layout.simple_list_item_1, hashtags);
                listview.setAdapter(adapter);

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = et_hashtag.getText().toString();
                et_hashtag.setText(data + " " + hashtags[position]);
                layout_controls.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);

            }
        });


        bt_post.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (videofile!=null){
                    uplaodVideo(getuserid(), comstatus, posStatus, et_hashtag.getText().toString(),videofile);
                }else {
                    uplaodVideo(getuserid(), comstatus, posStatus, et_hashtag.getText().toString(),videogalleryfile);
                }

            }
        });

        bt_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoDescriptionActivity.this, GetUsersByNameActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void uplaodVideo(String userid, String commStatus, String poststatus_, String videodesc_,File file) {

        try {

            String name = System.currentTimeMillis() + "_video.jpeg";

            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), userid);

            RequestBody commentStatus = RequestBody.create(MediaType.parse("text/plain"), commStatus);

            RequestBody postStatus = RequestBody.create(MediaType.parse("text/plain"), poststatus_);

            RequestBody videoDescribe = RequestBody.create(MediaType.parse("text/plain"), videodesc_);


            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), thumbfile);
            MultipartBody.Part userImage1 = MultipartBody.Part.createFormData("videoThumImg", name, requestFile1);


            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part uservideo = MultipartBody.Part.createFormData("addVideo", file.getName(), requestFile);


            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(this);
            apiInteface.uploadStory(userId,commentStatus,postStatus,videoDescribe,uservideo, userImage1).enqueue(new Callback<ResponseData>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {

                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                Toast.makeText(VideoDescriptionActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(VideoDescriptionActivity.this,MainActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(VideoDescriptionActivity.this, "200" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(VideoDescriptionActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("upload failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("exeption", "" + e.getLocalizedMessage() + "  ,  " + e.getMessage());
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                posStatus = data.getStringExtra("result");
                if (posStatus.equalsIgnoreCase("1")) {
                    tv_visiblity.setText("Public");
                } else if (posStatus.equalsIgnoreCase("2")) {
                    tv_visiblity.setText("Friends");
                } else if (posStatus.equalsIgnoreCase("3")) {
                    tv_visiblity.setText("Private");
                }
                //   Toast.makeText(this, ""+result, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String usernames2 = data.getStringExtra("username");
                // Toast.makeText(this, ""+usernames2, Toast.LENGTH_SHORT).show();
                String userdata = et_hashtag.getText().toString();
                et_hashtag.setText(userdata + " " + usernames2);

            }

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(VideoDescriptionActivity.this);
        return preferences.getString("userid", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void getThumbnail(File imageFile) {
        Uri uri = Uri.parse(imageFile.getPath());
        if (uri != null) {
            final String selectedPathVideo = getRealPathFromURIPath(uri, VideoDescriptionActivity.this);
            if (selectedPathVideo != null) {
                //uploadVideoToServer(selectedPathVideo);
                try {
                    final Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedPathVideo, MediaStore.Video.Thumbnails.MINI_KIND);
                    String name = System.currentTimeMillis() + "_video.jpeg";
                    final String path = BitmapUtils.insertImage(VideoDescriptionActivity.this.getContentResolver(), thumb, name, null);
                    if (!TextUtils.isEmpty(path)) {
                        thumbfile = new File(VideoDescriptionActivity.this.getCacheDir(), name);
                        try {
                            thumbfile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//Convert bitmap to byte array

                        Bitmap bitmap = thumb;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(thumbfile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(bitmapdata);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    // uploadVideoToServer(selectedPathVideo, f);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
          //      Toast.makeText(VideoDescriptionActivity.this, "selectedPathVideo null", Toast.LENGTH_SHORT).show();
            }
        } else {
      //      Toast.makeText(VideoDescriptionActivity.this, "uri null", Toast.LENGTH_SHORT).show();
        }
    }


    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}
