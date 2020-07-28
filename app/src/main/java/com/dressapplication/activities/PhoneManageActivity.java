package com.dressapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.dressapplication.utils.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneManageActivity extends AppCompatActivity {
    private TextView et_phone, et_email, et_location;
    private ImageView img_done,iv_back;
    private ProgressBar progressBar;
    private MyGps gpsTracker;
    private String latitud, longitud, address1,address2,address3, zip, city, countr;
    private int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean setValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_manage);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_location = findViewById(R.id.et_location);
        img_done = findViewById(R.id.img_done);
        progressBar = findViewById(R.id.progressBar);
        iv_back=findViewById(R.id.iv_back);


        et_phone.addTextChangedListener(manageAcWatcher);
        et_email.addTextChangedListener(manageAcWatcher);
        et_location.addTextChangedListener(manageAcWatcher);

        img_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected(PhoneManageActivity.this)) {
                    if (validatiion()) {
                        managesave(et_phone.getText().toString()
                                , address1, longitud, latitud, countr, city, "", getuserid());
                    }
                } else {
                    Toast.makeText(PhoneManageActivity.this, "No Internet Connectiion", Toast.LENGTH_SHORT).show();
                }

            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private TextWatcher manageAcWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String phone = et_phone.getText().toString().trim();
            String email = et_email.getText().toString().trim();
            String location = et_location.getText().toString().trim();

            if (!phone.isEmpty() && !email.isEmpty() && !location.isEmpty()) {
                img_done.setImageDrawable(getResources().getDrawable(R.mipmap.red_tick));
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private void managesave(String phoneno, String address, String longi, String lati, String count, String city, String zip, String userids) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(PhoneManageActivity.this);
            apiInteface.manageAccount(phoneno, address, longi, lati, count, city, zip, userids).enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        ResponseData data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {

                                Toast.makeText(PhoneManageActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(PhoneManageActivity.this, "" + data.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PhoneManageActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("manage failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (getemail() != null) {
            et_email.setText(getemail());
        }
        gpsTracker = new MyGps(PhoneManageActivity.this);
        if (checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }
        et_location.setText(address1);
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(PhoneManageActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(PhoneManageActivity.this,
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
                            ActivityCompat.requestPermissions(PhoneManageActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i("tag", "Requesting permission");
            ActivityCompat.requestPermissions(PhoneManageActivity.this,
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
                        latitud = String.valueOf(latitude);
                        longitud = String.valueOf(longitude);


                        if (addresses.get(0).getAddressLine(0) != null) {
                            address1 = addresses.get(0).getAddressLine(0);
                            Log.e("address1",""+address1);

                        }
                        if (addresses.get(0).getLocality() != null) {
                            address2 = addresses.get(0).getLocality();
                            Log.e("address2",""+address2);

                        } else {
                            if (addresses.get(0).getSubAdminArea() != null) {
                                address3 = addresses.get(0).getSubAdminArea();
                            } else {
                                address3 = "";
                            }
                        }
//                        if (addresses.get(0).getAdminArea() != null) {
//                            myLocation.setUserState(addresses.get(0).getAdminArea());
//                        }
                        if (addresses.get(0).getCountryName() != null) {
                            countr = addresses.get(0).getCountryName();
                        }
//                        if (addresses.get(0).getCountryCode() != null) {
//                            myLocation.setUserCountryCode(addresses.get(0).getCountryCode());
//                        }

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


    private boolean validatiion() {

        if (et_phone.getText().toString().isEmpty()) {
            Toast.makeText(this, "phone is Empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_email.getText().toString().isEmpty()) {
            Toast.makeText(this, "Email is Empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_location.getText().toString().isEmpty()) {
            Toast.makeText(this, "Location is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String getuserid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PhoneManageActivity.this);
        return preferences.getString("userid", null);
    }


    private String getemail() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PhoneManageActivity.this);
        return preferences.getString("email", null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gpsTracker != null) {
            gpsTracker.stopUsingGPS();
        }
    }

}
