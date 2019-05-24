package com.example.swethababurao.boseweatherchannel.Model;

import com.google.gson.annotations.SerializedName;

public class MainForecast {

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
    @SerializedName("sea_level")
    String sea_level;
    @SerializedName("grnd_level")
    String grnd_level;
    @SerializedName("temp_kf")
    String temp_kf;


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

    public String getSeaLevel() {
        return sea_level;
    }

    public void setSeaLevel(String sea_level) {
        this.sea_level = sea_level;
    }

    public String getGrndLevel() {
        return grnd_level;
    }

    public void setGrndLevel(String grnd_level) {
        this.grnd_level = grnd_level;
    }

    public String getTempKf() {
        return temp_kf;
    }

    public void setTempKf(String temp_kf) {
        this.temp_kf = temp_kf;
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
