package com.dressapplication.ui.camera.CaptureVideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;
import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.dressapplication.R;
import com.dressapplication.ui.camera.BitmapUtils;
import com.dressapplication.utils.NetworkUtils;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Mp4TrackImpl;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends CameraVideoFragment {

    private static final String TAG = "CameraFragment";
    private static final String VIDEO_DIRECTORY_NAME = "DressApp";
    @BindView(R.id.mTextureView)
    AutoFitTextureView mTextureView;
    @BindView(R.id.mRecordVideo)
    ImageView mRecordVideo;
    @BindView(R.id.mVideoView)
    VideoView mVideoView;
    @BindView(R.id.mPlayVideo)
    ImageView mPlayVideo;
    Unbinder unbinder;
    private String mOutputFilePath;
    private   File f,f1;
    private ProgressBar progressBar;
    private ImageView done,imf_flash;
    private   File file;
    private Button bt_done;
    private ImageView img_switchcamera;
    private int REQUEST_TAKE_GALLERY_VIDEO = 2;
    private int REQUEST_TAKE_GALLERY_VIDEO2 = 36;
    private Button add_videoGallery;
    private static final int REQUEST_TAKE_VIDEO = 200;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;
    private int type1 = TYPE_VIDEO;
    private File Galleryvideo;
    private LinearLayout linear_capture;




    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */


    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camer2, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressBar=view.findViewById(R.id.progressBar);
        bt_done=view.findViewById(R.id.bt_done);
        imf_flash=view.findViewById(R.id.imf_flash);
        img_switchcamera=view.findViewById(R.id.img_switchcamera);
        add_videoGallery=view.findViewById(R.id.add_videoGallery);
        linear_capture=view.findViewById(R.id.linear_capture);

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected(getActivity())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (Galleryvideo!=null){
                            Log.e("galleryvideopath",Galleryvideo.getAbsolutePath());

                            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
                            if (f.mkdirs() || f.isDirectory())
                                //compress and output new video specs
                                new VideoCompressAsyncTask2(getContext()).execute(Galleryvideo.getAbsolutePath(), f.getPath());
                        }
                        else {
                            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
                            if (f.mkdirs() || f.isDirectory())
                                new VideoCompressAsyncTask(getContext()).execute(file.getAbsolutePath(), f.getPath());
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Check your network", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imf_flash.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                switchFlash();
            }
        });

        img_switchcamera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    switchCamera();
                    //startRecordingVideo();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        add_videoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               chooseVideoFromGallary();
            }
        });

        return view;
    }

    @Override
    public int getTextureResource() {
        return R.id.mTextureView;
    }

    @Override
    protected void setUp(View view) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.mRecordVideo, R.id.mPlayVideo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mRecordVideo:
                /**
                 * If media is not recoding then start recording else stop recording
                 */
                if (mIsRecordingVideo) {
                    try {
                        stopRecordingVideo();
                        prepareViews();
                        file=new File(mOutputFilePath);
                        bt_done.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    startRecordingVideo();
                    mRecordVideo.setImageResource(R.mipmap.camera_circle);
                    //Receive out put file here
                    mOutputFilePath = getCurrentFile().getAbsolutePath();
                }
                break;
            case R.id.mPlayVideo:
                Log.e("thumbname",""+f.getName());
               // Toast.makeText(getContext(), ""+mOutputFilePath, Toast.LENGTH_SHORT).show();
                mVideoView.setVisibility(View.VISIBLE);
                mVideoView.start();
                mPlayVideo.setVisibility(View.GONE);
                break;
        }
    }

    private void prepareViews() {
        if (mVideoView.getVisibility() == View.GONE) {
           // mVideoView.setVisibility(View.VISIBLE);
          //  mPlayVideo.setVisibility(View.VISIBLE);
         //   mTextureView.setVisibility(View.GONE);
//            try {
//                setMediaForRecordVideo();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void setMediaForRecordVideo() throws IOException {
        File file=new File(mOutputFilePath);
//        mOutputFilePath = parseVideo(mOutputFilePath);
        // Set media controller
        mVideoView.setVideoURI(Uri.parse(file.getName()));
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.requestFocus();
        mVideoView.setVideoPath(mOutputFilePath);
        mVideoView.seekTo(100);
        mVideoView.setOnCompletionListener(mp -> {
            // Reset player
           // mVideoView.setVisibility(View.GONE);
          //  mTextureView.setVisibility(View.VISIBLE);
          //  mPlayVideo.setVisibility(View.GONE);
         //   mRecordVideo.setImageResource(R.mipmap.camera_circle);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String parseVideo(String mFilePath) throws IOException {
        DataSource channel = new FileDataSourceImpl(mFilePath);
        IsoFile isoFile = new IsoFile(channel);
        List<TrackBox> trackBoxes = isoFile.getMovieBox().getBoxes(TrackBox.class);
        boolean isError = false;
        for (TrackBox trackBox : trackBoxes) {
            TimeToSampleBox.Entry firstEntry = trackBox.getMediaBox().getMediaInformationBox().getSampleTableBox().getTimeToSampleBox().getEntries().get(0);
            // Detect if first sample is a problem and fix it in isoFile
            // This is a hack. The audio deltas are 1024 for my files, and video deltas about 3000
            // 10000 seems sufficient since for 30 fps the normal delta is about 3000
            if (firstEntry.getDelta() > 10000) {
                isError = true;
                firstEntry.setDelta(3000);
            }
        }
        File file = getOutputMediaFile();
        String filePath = file.getAbsolutePath();
        if (isError) {
            Movie movie = new Movie();
            for (TrackBox trackBox : trackBoxes) {
                movie.addTrack(new Mp4TrackImpl(channel.toString() + "[" + trackBox.getTrackHeaderBox().getTrackId() + "]", trackBox));
            }
            movie.setMatrix(isoFile.getMovieBox().getMovieHeaderBox().getMatrix());
            Container out = new DefaultMp4Builder().build(movie);

            //delete file first!
            FileChannel fc = new RandomAccessFile(filePath, "rw").getChannel();
            out.writeContainer(fc);
            fc.close();
            Log.d(TAG, "Finished correcting raw video");
            return filePath;
        }
        return mFilePath;
    }

    /**
     * Create directory and return file
     * returning video file
     */
    private File getOutputMediaFile() {
        // External sdcard file location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
                VIDEO_DIRECTORY_NAME);
        // Create storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + VIDEO_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");

        return mediaFile;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getThmbnail(File video) {

        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mOutputFilePath, MediaStore.Video.Thumbnails.MINI_KIND);
        String name = System.currentTimeMillis() + "_video.jpeg";
        final String path = BitmapUtils.insertImage(getActivity().getContentResolver(), thumb, name, null);
        if (!TextUtils.isEmpty(path)) {
            f = new File(getActivity().getCacheDir(), name);
            try {
                f.createNewFile();
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
                fos = new FileOutputStream(f);
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
        try {
            Intent intent=new Intent(getContext(), VideoPlayActivity.class);
            intent.putExtra("path",video.toString());
            intent.putExtra("thumbnail",f.toString());
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

      //  uplaodVideo(video, f);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getThmbnail2(File video) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mOutputFilePath, MediaStore.Video.Thumbnails.MINI_KIND);
        String name = System.currentTimeMillis() + "_video.jpeg";
        final String path = BitmapUtils.insertImage(getActivity().getContentResolver(), thumb, name, null);
        if (!TextUtils.isEmpty(path)) {
            f1 = new File(getActivity().getCacheDir(), name);
            try {
                f.createNewFile();
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
                fos = new FileOutputStream(f);
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
        try {
            Intent intent=new Intent(getContext(),VideoPlayActivity.class);
            intent.putExtra("path",video.toString());
            intent.putExtra("thumbnail",f.toString());
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

        //  uplaodVideo(video, f);
    }


    class VideoCompressAsyncTask extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(mContext, "Please wait", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
//            try {
//
//                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);
//
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
            return filePath;

        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
          File  Videofile = new File(compressedFilePath);
            float length = Videofile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            getThmbnail(Videofile);



        }
    }

    class VideoCompressAsyncTask2 extends AsyncTask<String, String, String> {

        Context mContext;

        public VideoCompressAsyncTask2(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(mContext, "Please wait", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
//            try {
//
//                filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);
//
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
            return filePath;

        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            File  Videofile = new File(compressedFilePath);
            float length = Videofile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            getThmbnail2(Videofile);

        }
    }


    public void chooseVideoFromGallary() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
        openGalleryIntent.setType("video/*");
        startActivityForResult(openGalleryIntent, REQUEST_TAKE_GALLERY_VIDEO);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
            if (data != null) {

                Uri uri = data.getData();
                if (uri != null) {
                    String selectedPathVideo = getRealPathFromURIPath(uri, getActivity());
                    if (selectedPathVideo != null) {
                        Galleryvideo = new File(selectedPathVideo);
                        bt_done.setVisibility(View.VISIBLE);
                    }
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = (type1 == TYPE_IMAGE) ? "JPEG_" + timeStamp + "_" : "VID_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        type1 == TYPE_IMAGE ? Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_MOVIES);
                File file1 = null;
                try {
                    file1 = File.createTempFile(
                            fileName,  /* prefix */
                            type1 == TYPE_IMAGE ? ".jpg" : ".mp4",         /* suffix */
                            storageDir      /* directory */
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
//                if (f.mkdirs() || f.isDirectory())
//                    //compress and output new video specs
//                    new VideoCompressAsyncTask2(getContext()).execute(Galleryvideo.getAbsolutePath(), f.getPath());

            } else {
                if (requestCode == REQUEST_TAKE_GALLERY_VIDEO2) {
                    if (data != null) {

                        Uri uri = data.getData();
                        if (uri != null) {
                            String selectedPathVideo = getRealPathFromURIPath(uri, getActivity());
                            if (selectedPathVideo != null) {
                                Galleryvideo = new File(selectedPathVideo);
                            } else {
                                Toast.makeText(getActivity(), "selectedPathVideo2 null", Toast.LENGTH_SHORT).show();

                            }
                        }

                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String fileName = (type1 == TYPE_IMAGE) ? "JPEG_" + timeStamp + "_" : "VID_" + timeStamp + "_";
                        File storageDir = Environment.getExternalStoragePublicDirectory(
                                type1 == TYPE_IMAGE ? Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_MOVIES);
                        File file1 = null;
                        try {
                            file1 = File.createTempFile(
                                    fileName,  /* prefix */
                                    type1 == TYPE_IMAGE ? ".jpg" : ".mp4",         /* suffix */
                                    storageDir      /* directory */
                            );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
//                        if (f.mkdirs() || f.isDirectory())
//                            //compress and output new video specs
//                            new VideoCompressAsyncTask2(getContext()).execute(Galleryvideo.getAbsolutePath(), f.getPath());

                    }
                }
            }
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