package com.twitter.challenge.data;

import java.io.Serializable;

public class WeatherCloud implements Serializable {

    public int cloudiness;

    public WeatherCloud() {
    }

    public boolean isCloudy() {
        return cloudiness > 50;
    }

}
