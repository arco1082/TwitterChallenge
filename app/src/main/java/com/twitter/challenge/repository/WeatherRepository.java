package com.twitter.challenge.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.twitter.challenge.api.WeatherInterface;
import com.twitter.challenge.data.MultiDayWeather;
import com.twitter.challenge.data.WeatherWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private WeatherInterface webService;

    private boolean mHasErrored = false;

    public WeatherRepository(WeatherInterface webservice) {
        this.webService = webservice;
    }

    public LiveData<WeatherWrapper> getCurrentWeather() {

        final MutableLiveData<WeatherWrapper> data = new MutableLiveData<>();
        webService.getCurrentWeather().enqueue(new Callback<WeatherWrapper>() {
            @Override
            public void onResponse(Call<WeatherWrapper> call, Response<WeatherWrapper> response) {
                mHasErrored = false;
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<WeatherWrapper> call, Throwable t) {
                mHasErrored = true;
                data.setValue(null);
            }

        });

        return data;
    }

    public LiveData<MultiDayWeather> getMultiDayTempStdDev(int days) {

        final MutableLiveData<MultiDayWeather> data = new MutableLiveData<>();

        final MultiDayWeather multiDayWeather = new MultiDayWeather(days);

        for (int i = 1; i <= days; i ++) {

            final int day = i ;

            webService.getFutureWeather(String.valueOf(day)).enqueue(new Callback<WeatherWrapper>() {
                @Override
                public void onResponse(Call<WeatherWrapper> call, Response<WeatherWrapper> response) {
                    mHasErrored = false;
                    multiDayWeather.addWeather(day, response.body());

                    if (multiDayWeather.isComplete()) {
                        data.setValue(multiDayWeather);
                    }
                }

                @Override
                public void onFailure(Call<WeatherWrapper> call, Throwable t) {
                    mHasErrored = true;
                    data.setValue(null);
                }

            });

        }

        return data;
    }

    public boolean hasErrored() {
        return mHasErrored;
    }
}
