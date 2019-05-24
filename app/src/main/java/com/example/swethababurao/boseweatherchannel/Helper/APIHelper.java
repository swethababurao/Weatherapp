package com.example.swethababurao.boseweatherchannel.Helper;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Helper Class which has a method to return BASE_URL
 */

public final class APIHelper {

    public static String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    /**
     * Helper method to get Api url based on ApiMethod call
     *
     * @param iMethod Type of Api
     * @return Url String
     */
    public static URL getUrl(APIMethods iMethod, String city, String apiKey) throws UnsupportedEncodingException, MalformedURLException {

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        switch (iMethod) {

            case GET_WEATHER_URL:
                urlBuilder.append("weather").append("?");
                break;

            case GET_FORECAST_URL:
                urlBuilder.append("forecast").append("?");

        }

        urlBuilder.append("q=").append(URLEncoder.encode(city, "UTF-8"));
        urlBuilder.append("&mode=json");
        urlBuilder.append("&appid=").append(apiKey);

        return new URL(urlBuilder.toString());
    }

    public static URL getUrl(APIMethods iMethod, String lat, String lng, String apiKey) throws UnsupportedEncodingException, MalformedURLException {

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        switch (iMethod) {

            case GET_WEATHER_URL:
                urlBuilder.append("weather").append("?");
                break;

            case GET_FORECAST_URL:
                urlBuilder.append("forecast").append("?");

        }
        urlBuilder.append("lat=").append(lat).append("&lon=").append(lng);
        urlBuilder.append("&mode=json");
        urlBuilder.append("&appid=").append(apiKey);

        return new URL(urlBuilder.toString());
    }

    public static String convertStreamToString(InputStream instream) {
        // TODO Auto-generated method stub
        String result = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    instream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();

        } catch (Exception e) {
            Log.d("log_tag", "Error converting result " + e.toString());
        }
        return result;
    }


}

