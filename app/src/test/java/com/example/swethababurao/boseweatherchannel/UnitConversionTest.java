package com.example.swethababurao.boseweatherchannel;

import com.example.swethababurao.boseweatherchannel.Utils.UnitConversion;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnitConversionTest {

    @Test
    public void testConvertkelvinToCelsius() {
        float actual = UnitConversion.kelvinToCelsius(300f);
        // expected value is 26.85
        float expected = 26.85f;

        assertEquals("Conversion from kelvin to celsius failed", expected, actual, 0.001);
    }

    @Test
    public void testConvertkelvinToFahrenheit() {
        float actual = UnitConversion.kelvinToFahrenheit(298f);
        // expected value is 76.73
        float expected = 76.73f;

        assertEquals("Conversion from kelvin to fahrenheit failed", expected, actual, 0.001);
    }

    @Test
    public void testConvertPressure() {
        float actual = UnitConversion.convertPressure(1019.15f, "in Hg");
        // expected value is 30.095
        float expected = 30.095f;

        assertEquals("Conversion of pressure failed", expected, actual, 0.001);
    }

    @Test
    public void testConvertWind() {
        double actual = UnitConversion.convertWind(1.81, "mph");
        // expected value is 4.04
        double expected = 4.04885468861;

        assertEquals("Conversion of wind failed", expected, actual, 0.001);
    }
}
