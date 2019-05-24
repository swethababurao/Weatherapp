package com.example.swethababurao.boseweatherchannel.Model;

import com.google.gson.annotations.SerializedName;

public class WeatherForecastResponse {
    @SerializedName("cod")
    String cod;
    @SerializedName("message")
    String message;
    @SerializedName("cnt")
    String cnt;
    @SerializedName("list")
    WeatherForecast[] weatherForecasts;
    @SerializedName("city")
    CityForecast city;

    public WeatherForecast[] getWeatherForecasts() {
        return weatherForecasts;
    }

    public void setWeatherForecasts(WeatherForecast[] weatherForecasts) {
        this.weatherForecasts = weatherForecasts;
    }

    public CityForecast getCity() {
        return city;
    }

    public void setCity(CityForecast city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }
}
