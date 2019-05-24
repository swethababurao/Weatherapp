package com.example.swethababurao.boseweatherchannel.Model;

import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("3h")
    String rainData;

    public String getRainData() {
        return rainData;
    }

    public void setRainData(String rainData) {
        this.rainData = rainData;
    }
}
