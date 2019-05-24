package com.example.swethababurao.boseweatherchannel.Model;

import com.google.gson.annotations.SerializedName;


    public class Wind {
        @SerializedName("speed")
        String speed;
        @SerializedName("deg")
        String deg;
        /*@SerializedName("gust")
        String gust;*/

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getDeg() {
            return deg;
        }

        public void setDeg(String deg) {
            this.deg = deg;
        }

        /*public String getGust() {
            return gust;
        }

        public void setGust(String gust) {
            this.gust = gust;
        }*/
    }

