package com.twitter.challenge.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherWrapper implements Serializable {

    public String name;
    public WeatherCoordinate coord;
    public Weather weather;
    public WeatherWind wind;
    public WeatherRain rain;
    public WeatherCloud clouds;

    public WeatherWrapper() {
    }

}