package com.example.swethababurao.boseweatherchannel.Helper;

import com.example.swethababurao.boseweatherchannel.Model.WeatherForecastResponse;
import com.example.swethababurao.boseweatherchannel.Model.WeatherResponse;

public interface AsyncCallback {
    void processFinishWeather(WeatherResponse weatherResponse, String url);
    void processFinishForecast(WeatherForecastResponse weatherForecastResponse, String url);
    void processErrorHandle(String errorMsg);
}
