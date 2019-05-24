package com.example.swethababurao.boseweatherchannel.Model;

import com.google.gson.annotations.SerializedName;

public class CityForecast {

    @SerializedName("id")
    String id;
    @SerializedName("name")
    String cityName;
    @SerializedName("coord")
    Coordinate coordiate;
    @SerializedName("country")
    String Country;
    @SerializedName("population")
    String population;

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

    public Coordinate getCoordiate() {
        return coordiate;
    }

    public void setCoordiate(Coordinate coordiate) {
        this.coordiate = coordiate;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }
}
