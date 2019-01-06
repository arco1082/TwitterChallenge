package com.twitter.challenge;

public class StdDevCalculator {

    public static double calculateSD(float temperatures[])
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = temperatures.length;

        for(double num : temperatures) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: temperatures) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/ (length - 1));
    }
}
