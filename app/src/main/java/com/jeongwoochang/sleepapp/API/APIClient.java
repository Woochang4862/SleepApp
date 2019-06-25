package com.jeongwoochang.sleepapp.API;

import android.content.Context;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    public static String baseUrl = "http://54.180.57.73:3000";

    public static Retrofit getClient(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        AddCookiesInterceptor in1 = new AddCookiesInterceptor(context);
        ReceivedCookiesInterceptor in2 = new ReceivedCookiesInterceptor(context);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).addNetworkInterceptor(in2).addInterceptor(in1).addNetworkInterceptor(new StethoInterceptor()).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}