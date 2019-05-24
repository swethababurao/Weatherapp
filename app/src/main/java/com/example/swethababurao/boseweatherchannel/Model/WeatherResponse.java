package com.example.swethababurao.boseweatherchannel.Model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class WeatherResponse {

    @SerializedName("coord")
    Coordinate coordiate;
    @SerializedName("weather")
    Weather[] weather;
    @SerializedName("base")
    String base;
    @SerializedName("main")
    Main main;
    @SerializedName("visibility")
    String visibility;
    @SerializedName("wind")
    Wind wind;
    @SerializedName("clouds")
    Clouds clouds;
    @SerializedName("dt")
    String dt;
    @SerializedName("sys")
    System sys;
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String cityName;
    @SerializedName("cod")
    String cod;


    public Coordinate getCoordiate() {
        return coordiate;
    }

    public void setCoordiate(Coordinate coordiate) {
        this.coordiate = coordiate;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public System getSys() {
        return sys;
    }

    public void setSys(System sys) {
        this.sys = sys;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }
}

