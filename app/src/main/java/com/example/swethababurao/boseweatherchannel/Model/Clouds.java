package com.example.swethababurao.boseweatherchannel.Model;

import com.google.gson.annotations.SerializedName;

public class Clouds {
    @SerializedName("all")
    String all;

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }
}
