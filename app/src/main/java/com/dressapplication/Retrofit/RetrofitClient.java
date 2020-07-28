package com.dressapplication.Retrofit;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

//        OkHttpClient client = new OkHttpClient();


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }


    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    okhttp3.Response response = chain.proceed(request);
                    if (response.code() == 500) {
                        Log.e("Net:", "Error: 500");
                        return response;
                    }
                    if (response.cacheControl() != null) {
                        Log.e("Net:", "Error: cacheControl ");
                    } else if (response.networkResponse() != null) {
                        Log.e("Net:", "Error: networkResponse1 ");
                    } else {
                        Log.e("Net:", "Error: networkResponse2 ");
                    }
                    Log.e("Net:", "Error: " + response.code());
                    long tx = response.sentRequestAtMillis();
                    long rx = response.receivedResponseAtMillis();
                    System.out.println("response_time : " + (rx - tx) + " ms");
                    return response;
                }
            })
            .build();

}
