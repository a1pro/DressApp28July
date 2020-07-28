package com.dressapplication.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dressapplication.BuildConfig;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.GetProfileModel;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.FileProvider.getUriForFile;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back, img_select;
    private ProgressBar progressBar;
    private TextView tv_name, tv_username,tv_save;
    private EditText tv_bio,tv_instagram, tv_youtube;
    private CircleImageView img_profile;
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_GALLERY_IMAGE = 1;
    private boolean lockAspectRatio = false, setBitmapMaxWidthHeight = false;
    private int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 9, bitmapMaxWidth = 800, bitmapMaxHeight = 800;
    private int IMAGE_COMPRESSION = 85;
    private String currentPhotoPath;
    Context context;
    File file = null;
    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initUI();
        GetProfile(getuserid());
    }

    private void initUI() {
        tv_save=findViewById(R.id.tv_save);
        img_select = findViewById(R.id.img_select);
        img_profile = findViewById(R.id.img_profile);
        tv_instagram = findViewById(R.id.tv_instagram);
        tv_youtube = findViewById(R.id.tv_youtube);
        tv_bio = findViewById(R.id.tv_bio);
        tv_username = findViewById(R.id.tv_username);
        // tv_name = findViewById(R.id.tv_name);
        progressBar = findViewById(R.id.progressBar);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        img_select.setOnClickListener(this);
        tv_save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.img_select:
                setImage();
                break;

            case R.id.tv_save:
                setChangeImage(tv_bio.getText().toString(),tv_instagram.getText().toString(),tv_youtube.getText().toString());
                break;
        }
    }

    private void setImage() {
        final CharSequence[] option = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Photo !");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (option[i].equals("Take Photo")) {
                    takeCameraImage();
                } else if (option[i].equals("Choose from Gallery")) {
                    chooseImageFromGallery();
                } else {
                    dialog.cancel();
                }
            }
        });
        builder.show();
    }

    private void chooseImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            pictureIntent.setType("image/*");
                            pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
                                pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                            }
                            startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), REQUEST_GALLERY_IMAGE);

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void takeCameraImage() {
        Dexter.withActivity(EditProfileActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file;
                            try {
                                file = getImageFile(); // 1
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                            Uri uri;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
                                uri = getUriForFile(EditProfileActivity.this, BuildConfig.APPLICATION_ID.concat(".provider"), file);
                            else
                                uri = Uri.fromFile(file); // 3
                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
                            startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void GetProfile(String Userid) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(EditProfileActivity.this);
            apiInteface.GetProfile(Userid,getuserid()).enqueue(new Callback<GetProfileModel>() {
                @Override
                public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        GetProfileModel data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                        //        Toast.makeText(EditProfileActivity.this, "" + data.getStatus(), Toast.LENGTH_SHORT).show();

                                if (data.getUserData() != null) {
                                    String Username = data.getUserData().get(0).getUsername();
                                    if (Username != null) {
                                        //   tv_name.setText("" + Username);
                                        tv_username.setText("@" + Username);
                                    }
                                    if (data.getUserData().get(0).getNoBioYet() != null) {
                                        tv_bio.setText(data.getUserData().get(0).getNoBioYet());
                                    }

                                    if (data.getUserData().get(0).getYoutube() != null) {
                                        tv_youtube.setText(data.getUserData().get(0).getYoutube());
                                    }

                                    if (data.getUserData().get(0).getIntagram() != null) {
                                        tv_instagram.setText(data.getUserData().get(0).getIntagram());
                                    }

                                    if (data.getUserData().get(0).getProfileImage() != null && !data.getUserData().get(0).getProfileImage().isEmpty()) {
                                        Glide.with(EditProfileActivity.this).load(ApiUtils.IMAGE_BASE_URL + data.getUserData().get(0).getProfileImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_profile);
                                    }

                                }

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(EditProfileActivity.this, "" + data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileModel> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("register failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void cropImage(Uri sourceUri, Uri destinationUri) {
//        Uri destinationUri = Uri.fromFile(new File(getActivity().getCacheDir(), queryName(getActivity().getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(IMAGE_COMPRESSION);
        // options.setMaxBitmapSize(10000);


//        // applying UI theme
        options.setToolbarColor(ContextCompat.getColor(EditProfileActivity.this, R.color.pink));
        options.setStatusBarColor(ContextCompat.getColor(EditProfileActivity.this, R.color.pink));
        options.setActiveWidgetColor(ContextCompat.getColor(EditProfileActivity.this, R.color.pink));

//
        if (lockAspectRatio)
            options.withAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y);
//
        if (setBitmapMaxWidthHeight)
            options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);
//
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }

    private void handleUCropResult(Intent data) {
        if (data == null) {
            setResultCancelled();
            return;
        }
        final Uri resultUri = UCrop.getOutput(data);
        setResultOk(resultUri);
    }

    private void setResultOk(Uri imagePath) {


        try {
            String path = String.valueOf(imagePath);
            file = new File(new URI(path));
           byteArray = getStreamByteFromImage(file);


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void setChangeImage(String bio,String insta,String youtub) {
            try {

//                byte[] byteArray = byteArray1;

                MultipartBody.Part profile_image=null;

                RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), getuserid());

                RequestBody no_bio_yet = RequestBody.create(MediaType.parse("text/plain"), bio);

                RequestBody intagram = RequestBody.create(MediaType.parse("text/plain"), insta);

                RequestBody youtube = RequestBody.create(MediaType.parse("text/plain"), youtub);

                if (file!=null){
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                     profile_image = MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);
                }
                else {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                    profile_image = MultipartBody.Part.createFormData("profile_image", "", requestFile);

                }


                progressBar.setVisibility(View.VISIBLE);
                ApiInterface apiInteface;
                apiInteface = ApiUtils.getAPIService(this);
                apiInteface.EditProfile(userId,no_bio_yet,intagram,youtube,profile_image).enqueue(new Callback<ResponseData>() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            ResponseData data = response.body();
                            if (data != null) {
                                if (data.getCode().equalsIgnoreCase("201")) {

                                    Toast.makeText(EditProfileActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(EditProfileActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EditProfileActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("upload failure", "" + t.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

    }


        private void setResultCancelled () {
            Toast.makeText(EditProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
        }

        private Uri getCacheImagePath (String fileName){
            File path = new File(EditProfileActivity.this.getExternalCacheDir(), "camera");
            if (!path.exists()) path.mkdirs();
            File image = new File(path, fileName);
            return getUriForFile(EditProfileActivity.this, EditProfileActivity.this.getPackageName() + ".provider", image);
        }

        private static String queryName (ContentResolver resolver, Uri uri){
            Cursor returnCursor =
                    resolver.query(uri, null, null, null, null);
            assert returnCursor != null;
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = returnCursor.getString(nameIndex);
            returnCursor.close();
            return name;
        }

        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent data){
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    if (resultCode == RESULT_OK) {
                        try {
                            //  cropImage(getCacheImagePath(fileName));
                            Uri uri = Uri.parse(currentPhotoPath);
                            Log.e("currentPhotoPath", "" + currentPhotoPath);
                            cropImage(uri, uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //  Toast.makeText(getContext(), fileName.toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        setResultCancelled();
                    }
                    break;
                case REQUEST_GALLERY_IMAGE:
                    if (resultCode == RESULT_OK) {
                        try {
                            Uri sourceUri = data.getData();
                            File file = getImageFile();
                            Log.e("Source", "" + file.getAbsolutePath());
                            Uri destinationUri = Uri.fromFile(file);
                            cropImage(sourceUri, destinationUri);
                        } catch (Exception e) {
                            Log.e("ss", "Please select another image");
                        }

                    } else {
                        setResultCancelled();
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    if (resultCode == RESULT_OK) {
                        handleUCropResult(data);
                        if (file != null) {
                            Glide.with(this).load(file).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_profile);
                            String name = System.currentTimeMillis() + "_video.jpeg";
                        }
                    } else {
                        setResultCancelled();
                    }
                    break;
                case UCrop.RESULT_ERROR:
                    final Throwable cropError = UCrop.getError(data);
                    Log.e("error", "Crop error: " + cropError);
                    setResultCancelled();
                    break;
                default:
                    setResultCancelled();
            }
        }


        public static byte[] getStreamByteFromImage ( final File imageFile){

            Bitmap photoBitmap = BitmapFactory.decodeFile(imageFile.getPath());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            int imageRotation = getImageRotation(imageFile);

            if (imageRotation != 0)
                photoBitmap = getBitmapRotatedByDegree(photoBitmap, imageRotation);

            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);

            return stream.toByteArray();
        }

        private File getImageFile () throws IOException {
            String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
            File storageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DCIM
                    ), "Camera"
            );
            System.out.println(storageDir.getAbsolutePath());
            if (storageDir.exists())
                System.out.println("File exists");
            else
                System.out.println("File not exists");
            File file = File.createTempFile(
                    imageFileName, ".jpg", storageDir
            );
            currentPhotoPath = "file:" + file.getAbsolutePath();
            return file;
        }


        private static int getImageRotation ( final File imageFile){

            ExifInterface exif = null;
            int exifRotation = 0;

            try {
                exif = new ExifInterface(imageFile.getPath());
                exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (exif == null)
                return 0;
            else
                return exifToDegrees(exifRotation);
        }

        private static int exifToDegrees ( int rotation){
            if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
                return 90;
            else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
                return 180;
            else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
                return 270;

            return 0;
        }

        private static Bitmap getBitmapRotatedByDegree (Bitmap bitmap,int rotationDegree){
            Matrix matrix = new Matrix();
            matrix.preRotate(rotationDegree);

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }


        private String getuserid () {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(EditProfileActivity.this);
            return preferences.getString("userid", null);
        }

    }





