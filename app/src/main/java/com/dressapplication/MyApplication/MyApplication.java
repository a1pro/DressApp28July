package com.dressapplication.MyApplication;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MyApplication extends Application {
    private static boolean sIsChatActivityOpen = false;



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.KITKAT) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            Log.e("kitkat", "Bulid");
        }
        getDeviceID();
    }


    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        MyApplication.sIsChatActivityOpen = isChatActivityOpen;
    }

    // Get new Instance ID token
    private void getDeviceID() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("Log", "getInstanceId failed", task.getException());
                            return;
                        }
                        if (task.getResult() != null) {
                            String token = task.getResult().getToken();
                            if (!token.isEmpty()) {
                                Log.e("token",""+token);
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("registration_id", token);
                                editor.apply();
                            }
                        } else {
                            Log.e("Log", "getInstanceId failed", task.getException());
                        }
                    }
                });

    }
}