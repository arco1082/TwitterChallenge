package com.twitter.challenge.api;

import com.twitter.challenge.data.WeatherWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface WeatherInterface {

    @GET("current.json")
    @Headers("Content-type: application/json")
    Call<WeatherWrapper> getCurrentWeather();

    @GET("future_{day}.json")
    @Headers("Content-type: application/json")
    Call<WeatherWrapper> getFutureWeather(@Path("day") String day);

}