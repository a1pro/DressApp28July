package com.dressapplication.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.ResponseData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.utils.MyGps;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMyAcActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_delete_account, tv_phone_number, tv_email, tv_password, tv_country_value, tv_state_value;
    private ProgressBar progressBar;
    MyGps gpsTracker;
    private boolean setValue = false;
    private int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private String countr, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_ac);
        initUI();
        clickListerner();


    }

    private void initUI() {
        tv_state_value = findViewById(R.id.tv_state_value);
        tv_country_value = findViewById(R.id.tv_country_value);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_email = findViewById(R.id.tv_email);
        tv_password = findViewById(R.id.tv_password);
        tv_delete_account = findViewById(R.id.tv_delete_account);
        iv_back = findViewById(R.id.iv_back);
        progressBar = findViewById(R.id.progressBar);
    }

    private void clickListerner() {
        iv_back.setOnClickListener(this);
        tv_delete_account.setOnClickListener(this);
        tv_phone_number.setOnClickListener(this);
        tv_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_password:
                Intent intent1 = new Intent(ManageMyAcActivity.this, ChangePasswordActivity.class);
                startActivity(intent1);
                break;

            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_delete_account:
                deleteAccountFunction("5");
                break;

            case R.id.tv_phone_number:
                Intent intent = new Intent(ManageMyAcActivity.this, PhoneManageActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void deleteAccountFunction(String userid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageMyAcActivity.this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete account");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                ApiInterface apiInteface;
                apiInteface = ApiUtils.getAPIService(ManageMyAcActivity.this);
                apiInteface.deleteAccount(userid).enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            ResponseData responseData = response.body();
                            if (responseData != null) {
                                if (responseData.getCode().equalsIgnoreCase("201")) {
                                    Toast.makeText(ManageMyAcActivity.this, "" + responseData.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ManageMyAcActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ManageMyAcActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        gpsTracker = new MyGps(ManageMyAcActivity.this);
        if (checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }

        if (countr != null) {
            tv_country_value.setText(countr);
        }

        if (state != null) {
            tv_state_value.setText(state);
        }


    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(ManageMyAcActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(ManageMyAcActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("tag", "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(ManageMyAcActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i("tag", "Requesting permission");
            ActivityCompat.requestPermissions(ManageMyAcActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }


    private void startLocationUpdates() {
        if (gpsTracker.getLocation() != null) {
            if (gpsTracker.getLatitude() != 0 && gpsTracker.getLongitude() != 0) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && addresses.size() > 0) {


                        if (addresses.get(0).getCountryName() != null) {
                            countr = addresses.get(0).getCountryName();
                        }

                        if (addresses.get(0).getAdminArea() != null) {
                            state = addresses.get(0).getAdminArea();
                        }


                        setValue = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                setValue = false;
            }
        } else {

            gpsTracker.showSettingsAlert();
            setValue = false;
        }
    }


}
