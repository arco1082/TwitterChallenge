package com.twitter.challenge;

import org.assertj.core.data.Offset;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.within;

public class StdDevCalculatorTests {

    @Test
    public void testStdCalculator() {
        final Offset<Double> precision = within(0.05d);
        float[] firstSet = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        float[] secondSet = { 8, 9, 16.5f, 20,0, 12, 25, 30, 35, 8};
        float[] thirdSet = { 5, 10, 15, 20, 25, 30, 35, 40, 45, 50};
        float[] fourthSet = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        float[] fifthSet = { 15.5f, 16, 16.5f, 17, 17.5f, 18, 18.5f, 19, 19.5f, 20};

        assertThat(StdDevCalculator.calculateSD(firstSet)).isCloseTo(3.02d, precision);
        assertThat(StdDevCalculator.calculateSD(secondSet)).isCloseTo(11.05d, precision);
        assertThat(StdDevCalculator.calculateSD(thirdSet)).isCloseTo(15.13d, precision);
        assertThat(StdDevCalculator.calculateSD(fourthSet)).isCloseTo(30.27d, precision);
        assertThat(StdDevCalculator.calculateSD(fifthSet)).isCloseTo(1.5d, precision);

    }
}
