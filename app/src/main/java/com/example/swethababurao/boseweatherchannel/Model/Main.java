package com.example.swethababurao.boseweatherchannel.Model;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    String temp;
    @SerializedName("temp_min")
    String temp_min;
    @SerializedName("temp_max")
    String temp_max;
    @SerializedName("pressure")
    String pressure;
    @SerializedName("humidity")
    String humidity;

    public String getTemp() {
        return temp;
    }

    public String getMinTemp() {
        return temp_min;
    }

    public String getMaxTemp() {
        return temp_max;
    }

    public String getPressure() {
        return pressure;
    }


    public String getHumidity() {
        return humidity;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setMinTemp(String temp_min) {
        this.temp_min = temp_min;
    }

    public void setMaxTemp(String temp_max) {
        this.temp_max = temp_max;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
