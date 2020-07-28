package com.dressapplication.ui.camera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.dressapplication.R;
import com.dressapplication.utils.CameraPreview;
import com.dressapplication.utils.NetworkUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraFragment extends Fragment {

    NavController navController;
    private ImageView img_profile, img_flash;
    private Camera myCamera;
    private MyCameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;
    SurfaceHolder surfaceHolder;
    boolean recording;
    private Button myButton;
    boolean flashlight;
    private ToggleButton toggleButton;
    private CameraManager mCameraManager;
    private String mCameraId;
    Camera.Parameters params;
    CameraPreview mpreview;
    private boolean cameraFront = false;
    private String mVideoFileName;
    private ProgressBar progressBar;
    private File imageFile;
    private File f;
    private Size mImageSize;
    private ImageReader mImageReader;
    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_VIDEO = 2;
    private int type1 = TYPE_VIDEO;
    private ProgressBar progressbar;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        recording = false;
        flashlight = false;
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        img_profile = view.findViewById(R.id.img_profile);
        progressbar = view.findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);


        boolean isFlashAvailable = getActivity().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {
            showNoFlashError();
        }

        toggleButton = view.findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    turnOnFlash();
                } else {
                    turnOffFlash();
                }
            }
        });


        myCamera = getCameraInstance();
        myCamera.setDisplayOrientation(90);
        if (myCamera == null) {
            Toast.makeText(getContext(),
                    "Fail to get Camera",
                    Toast.LENGTH_LONG).show();
        }

        myCameraSurfaceView = new MyCameraSurfaceView(getContext(), myCamera);
        FrameLayout myCameraPreview = (FrameLayout) view.findViewById(R.id.videoview);
        myCameraPreview.addView(myCameraSurfaceView);


        myButton = (Button) view.findViewById(R.id.mybutton);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    if (recording) {
                        myCamera = getCameraInstance();
                        myCamera.setDisplayOrientation(90);
                        // stop recording and release camera
                        mediaRecorder.stop();  // stop the recording
                        releaseMediaRecorder(); // release the MediaRecorder object

                        //Exit after saved
                        //finish();
                        myButton.setText("REC");
                        recording = false;
                        videoupload();
                    } else {

                        //Release Camera before MediaRecorder start
                        releaseCamera();

                        if (!prepareMediaRecorder()) {
                            Toast.makeText(getContext(),
                                    "Fail in prepareMediaRecorder()!\n - Ended -",
                                    Toast.LENGTH_LONG).show();
                            // finish();
                        }

                        mediaRecorder.start();
                        recording = true;
                        myButton.setText("STOP");


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.navigation_profile);
            }
        });


        return view;
    }


    public void videoupload() {
        if (NetworkUtils.isConnected(getContext())) {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Uploading....", Toast.LENGTH_SHORT).show();
            File videoFile = new File(mVideoFileName);
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

            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/Silicompressor/videos");
            if (f.mkdirs() || f.isDirectory())
                //compress and output new video specs
                new VideoCompressAsyncTask(getContext()).execute(videoFile.getAbsolutePath(), f.getPath());

        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getContext(), "Check your network", Toast.LENGTH_SHORT).show();
        }

    }


    public void showNoFlashError() {
        final AlertDialog alert = new AlertDialog.Builder(getContext())
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash not available in this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void turnOnFlash() {

        if (myCamera == null || params == null) {
            return;
        }
        myCamera = getCameraInstance();
        params = myCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        myCamera.setParameters(params);
        myCamera.startPreview();
    }

    private void turnOffFlash() {

        if (myCamera == null || params == null) {
            return;
        }
        myCamera = getCameraInstance();
        params = myCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        myCamera.setParameters(params);
        myCamera.stopPreview();


    }


    private String getFileName_CustomFormat() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }


    private boolean prepareMediaRecorder() {
        myCamera = getCameraInstance();
        mediaRecorder = new MediaRecorder();
        myCamera.setDisplayOrientation(90);
        myCamera.unlock();
        mediaRecorder.setCamera(myCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//
//
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_HIGH));
//        mediaRecorder.setCaptureRate(30);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        mediaRecorder.setOutputFile(mVideoFileName);
        //mediaRecorder.setOutputFile("/sdcard/myvideo1.mp4");
//        mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
//        mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M

        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = new MediaRecorder();
            myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (myCamera != null) {
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }

    public class MyCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder mHolder;
        private Camera mCamera;

        public MyCameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int weight,
                                   int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null) {
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                // ignore: tried to stop a non-existent preview
            }

            // make any resize, rotate or reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e) {
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }
    }


    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
            // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
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
        }

        @Override
        protected String doInBackground(String... paths) {
            String filePath = null;
//            try {
//
//               // filePath = SiliCompressor.with(mContext).compressVideo(paths[0], paths[1]);
//
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
            return filePath;

        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            imageFile = new File(compressedFilePath);
            float length = imageFile.length() / 1024f; // Size in KB
            String value;
            if (length >= 1024)
                value = length / 1024f + " MB";
            else
                value = length + " KB";
            if (NetworkUtils.isConnected(getContext())) {
                getThmbnail(imageFile);
            } else {
                Toast.makeText(getContext(), "Check your network", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void getThmbnail(File video) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mVideoFileName, MediaStore.Video.Thumbnails.MINI_KIND);
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
       // uplaodVideo(video, f);
    }

//    private void uplaodVideo(File videoFile, File fileThumbnail) {
//        try {
//            String name = System.currentTimeMillis() + "_video.jpeg";
//
//            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), getuserid());
//
//
//            RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), fileThumbnail);
//            MultipartBody.Part userImage1 = MultipartBody.Part.createFormData("videoThumImg", name, requestFile1);
//
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
//            MultipartBody.Part uservideo = MultipartBody.Part.createFormData("addVideo", videoFile.getName(), requestFile);
//
//
//            progressBar.setVisibility(View.VISIBLE);
//            ApiInterface apiInteface;
//            apiInteface = ApiUtils.getAPIService(getContext());
//            apiInteface.uploadStory(userId, uservideo, userImage1).enqueue(new Callback<ResponseData>() {
//                @Override
//                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
//                    if (response.isSuccessful()) {
//                        progressBar.setVisibility(View.GONE);
//                        ResponseData data = response.body();
//                        if (data != null) {
//                            if (data.getCode().equalsIgnoreCase("201")) {
//
//                                Toast.makeText(getContext(), "Login" + data.getStatus(), Toast.LENGTH_SHORT).show();
//
//
//                            } else {
//                                progressBar.setVisibility(View.GONE);
//                                Toast.makeText(getContext(), "" + data.getStatus(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseData> call, Throwable t) {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.e("upload failure", "" + t.getMessage());
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return preferences.getString("userid", null);
    }


}