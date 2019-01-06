package com.twitter.challenge;

import com.twitter.challenge.data.WeatherCloud;

import org.assertj.core.data.Offset;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.within;

public class CloudinessTests {
    @Test
    public void testCloudiness() {
        WeatherCloud cloudinessFirst = new WeatherCloud();
        cloudinessFirst.cloudiness = 49;

        WeatherCloud cloudinessSecond = new WeatherCloud();
        cloudinessSecond.cloudiness = 51;

        WeatherCloud cloudinessThird = new WeatherCloud();
        cloudinessThird.cloudiness = 0;

        WeatherCloud cloudinessFourth = new WeatherCloud();
        cloudinessFourth.cloudiness = 100;

        WeatherCloud cloudinessFifth = new WeatherCloud();
        cloudinessFifth.cloudiness = 60;

        assertThat(cloudinessFirst.isCloudy()).isFalse();
        assertThat(cloudinessSecond.isCloudy()).isTrue();
        assertThat(cloudinessThird.isCloudy()).isFalse();
        assertThat(cloudinessFourth.isCloudy()).isTrue();
        assertThat(cloudinessFifth.isCloudy()).isTrue();

    }
}
