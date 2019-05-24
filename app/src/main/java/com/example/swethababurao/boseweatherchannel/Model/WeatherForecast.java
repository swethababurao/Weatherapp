package com.example.swethababurao.boseweatherchannel.Model;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeatherForecast {

    @SerializedName("dt")
    String dt;
    @SerializedName("main")
    MainForecast main;
    @SerializedName("weather")
    Weather[] weathers;
    @SerializedName("clouds")
    Clouds clouds;
    @SerializedName("wind")
    Wind wind;
    @SerializedName("rain")
    Rain rain;
    @SerializedName("dt_txt")
    String dateTime;

    public String getDt() {
        return dt;
    }


    public void setDt(String dt) {
        this.dt = dt;
    }

    public MainForecast getMain() {
        return main;
    }

    public void setMain(MainForecast main) {
        this.main = main;
    }

    public Weather[] getWeather() {
        return weathers;
    }

    public void setWeather(Weather[] weathers) {
        this.weathers = weathers;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

   /* public void setDateTime(String dateString) {
        try {
            setDateTime(new Date(Long.parseLong(dateString) * 1000));
        } catch (Exception e) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            try {
                setDateTime(inputFormat.parse(dateString));
            } catch (ParseException e2) {
                setDateTime(new Date()); // make the error somewhat obvious
                e2.printStackTrace();
            }
        }
    }*/


    public long getNumDaysFrom(Date initialDate) {
        Calendar initial = Calendar.getInstance();
        initial.setTime(initialDate);
        initial.set(Calendar.MILLISECOND, 0);
        initial.set(Calendar.SECOND, 0);
        initial.set(Calendar.MINUTE, 0);
        initial.set(Calendar.HOUR_OF_DAY, 0);

        Calendar me = Calendar.getInstance();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date convertedDate = new Date();
        try {
            convertedDate = inputFormat.parse(dateTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        me.setTime(convertedDate);
        me.set(Calendar.MILLISECOND, 0);
        me.set(Calendar.SECOND, 0);
        me.set(Calendar.MINUTE, 0);
        me.set(Calendar.HOUR_OF_DAY, 0);

        return Math.round((me.getTimeInMillis() - initial.getTimeInMillis()) / 86400000.0);
    }


}
