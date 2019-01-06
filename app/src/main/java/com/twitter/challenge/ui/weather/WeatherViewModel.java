package com.twitter.challenge.ui.weather;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.twitter.challenge.data.MultiDayWeather;
import com.twitter.challenge.data.WeatherWrapper;
import com.twitter.challenge.repository.WeatherRepository;

public class WeatherViewModel extends ViewModel {
    private LiveData<WeatherWrapper> weather;
    private LiveData<MultiDayWeather> multidayWeather;
    private WeatherRepository weatherRepository;

    public WeatherViewModel() {

    }

    public void init(WeatherRepository repo) {
        this.weatherRepository = repo;

        if (!weatherRepository.hasErrored() && weather != null) {
            return;
        }

        weather = weatherRepository.getCurrentWeather();
    }

    public LiveData<WeatherWrapper> getCurrentWeather() {
        return this.weather;
    }

    public LiveData<MultiDayWeather> getMultidayWeather() {
        return this.multidayWeather;
    }

    public void resetWeather() {
        multidayWeather = null;
    }

    public LiveData<MultiDayWeather> reloadMultiDayWeather(int days) {

        if (!weatherRepository.hasErrored() && multidayWeather != null) {
            return multidayWeather;
        }

        multidayWeather = weatherRepository.getMultiDayTempStdDev(days);
        return multidayWeather;
    }

}
