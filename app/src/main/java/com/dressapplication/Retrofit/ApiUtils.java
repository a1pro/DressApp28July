package com.dressapplication.Retrofit;

import android.content.Context;
import com.dressapplication.Interface.ApiInterface;


public class ApiUtils {
    private ApiUtils() {
    }


    public static final String BASE_URL = "https://a1professionals.net/dressApp/";
    public static final String IMAGE_BASE_URL="https://a1professionals.net/dressApp/assets/profileImages/";

    public static final String VIDEO_BASE_URL = "https://a1professionals.net/dressApp/assets/videos/";

//   public static final String BASE_URL = "http://srv1.a1professionals.net/dressApp/";
//    public static final String IMAGE_BASE_URL="http://srv1.a1professionals.net/dressApp/assets/profileImages/";
//    public static final String VIDEO_BASE_URL = "http://srv1.a1professionals.net/dressApp/assets/videos/";


    public static ApiInterface getAPIService(Context context) {

            return RetrofitClient.getClient(BASE_URL).create(ApiInterface.class);

    }








}
