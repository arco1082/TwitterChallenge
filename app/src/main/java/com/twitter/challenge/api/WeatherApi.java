package com.twitter.challenge.api;

import com.twitter.challenge.api.WeatherInterface;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApi {
    private static WeatherApi sInstance;
    private WeatherInterface mInterface;

    private WeatherApi(String endpoint) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        OkHttpClient again = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(again)
                .build();
        mInterface =  retrofit.create(WeatherInterface.class);
    }

    public static WeatherApi getInstance(String endpoint) {
        if (sInstance == null) {
            synchronized (WeatherApi.class) {
                if (sInstance == null) {
                    sInstance = new WeatherApi(endpoint);
                }
            }
        }
        return sInstance;
    }

    public WeatherInterface getWeatherInterface() {
        return mInterface;
    }
}
