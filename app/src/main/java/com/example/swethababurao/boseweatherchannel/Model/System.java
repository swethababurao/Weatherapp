package com.example.swethababurao.boseweatherchannel.Model;

import com.google.gson.annotations.SerializedName;

public class System {
    @SerializedName("type")
    String type;
    @SerializedName("id")
    String id;
    @SerializedName("message")
    String msg;
    @SerializedName("country")
    String country;
    @SerializedName("sunrise")
    String sunrise;
    @SerializedName("sunset")
    String sunset;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
