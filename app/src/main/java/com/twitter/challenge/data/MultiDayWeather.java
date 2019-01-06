package com.twitter.challenge.data;

import android.util.Log;

import com.twitter.challenge.StdDevCalculator;

import java.io.Serializable;
import java.util.HashMap;

public class MultiDayWeather implements Serializable {

    private HashMap<Integer, WeatherWrapper> mWeatherDays = new HashMap<>();
    private int mDays;

    public MultiDayWeather(int days) {
        mDays = days;
    }

    public void addWeather(int day, WeatherWrapper w) {
        mWeatherDays.put(day, w);
    }

    public boolean isComplete() {

        for (int i = 1 ; i <= mDays; i ++) {
            if (!mWeatherDays.containsKey(i)) {
                return false;
            }
        }

        return true;
    }

    public HashMap<Integer, WeatherWrapper> getData() {
        return mWeatherDays;
    }

    public double calculateStdDev() {
        float[] tempVals = new float[mWeatherDays.size()];
        int i = 0;

        for (WeatherWrapper entry : mWeatherDays.values()) {
            tempVals[i] = entry.weather.temp;
            i++;
        }

        double stdDev = StdDevCalculator.calculateSD(tempVals);

        return stdDev;
    }
}
