package com.example.swethababurao.boseweatherchannel.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.swethababurao.boseweatherchannel.MainActivity;
import com.example.swethababurao.boseweatherchannel.Model.WeatherForecastResponse;
import com.example.swethababurao.boseweatherchannel.Model.WeatherResponse;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;

import javax.xml.datatype.Duration;

public class VolleyRequest {

    public static String requestUrl;
    HashMap<String, String> postParams;
    public static AsyncCallback asyncResponseCallBack;
    static Context context;

    public VolleyRequest(Context context, String url, AsyncCallback asyncResCallBack) {
        this.context = context;
        this.requestUrl = url;
        this.asyncResponseCallBack = asyncResCallBack;
    }

    public static void executeRequest(final String requestType) {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("result", response);
                handleResponse(response, requestType, pDialog);

               /* Gson gson = new Gson(); //instantiating the GSON class
                if (requestType != null) {
                    if (requestType.equals("GET_WEATHER_URL")) {
                        WeatherResponse weatherResponse = gson.fromJson(response, WeatherResponse.class);
                        asyncResponseCallBack.processFinishWeather(weatherResponse, requestUrl);
                    } else if (requestType.equals("GET_FORECAST_URL")) {
                        WeatherForecastResponse weatherForecastResponse = gson.fromJson(response, WeatherForecastResponse.class);
                        asyncResponseCallBack.processFinishForecast(weatherForecastResponse, requestUrl);
                    }
                    MainActivity.saveLastUpdateTime(PreferenceManager.getDefaultSharedPreferences(context));
                }*/

                MainActivity.saveLastUpdateTime(PreferenceManager.getDefaultSharedPreferences(context));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                asyncResponseCallBack.processErrorHandle(volleyError.toString());
            }
        }) {
            @Override
            public void deliverError(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Cache.Entry entry = this.getCacheEntry();
                    if (entry != null) {
                        Log.d("cached data not null", entry.data.toString());
                        Response<String> response = parseNetworkResponse(new NetworkResponse(entry.data));
                        deliverResponse(response.result);
                        return;
                    }
                }
                super.deliverError(error);
            }
        };

        if (ApplicationController.getInstance().getRequestQueue().getCache().get(requestUrl) != null) {
            Calendar calendar = Calendar.getInstance();
            long serverDate = ApplicationController.getInstance().getRequestQueue().getCache().get(requestUrl).serverDate;
            if (getMinutesDifference(serverDate, calendar.getTimeInMillis()) >= 5) {
                ApplicationController.getInstance().getRequestQueue().getCache().invalidate(requestUrl, true);
            }

        }


        if (ApplicationController.getInstance().getRequestQueue().getCache().get(requestUrl) != null && !(ApplicationController.getInstance().getRequestQueue().getCache().get(requestUrl).isExpired())) {
            //response exists
            String cachedResponse = new String(ApplicationController.getInstance().getRequestQueue().getCache().get(requestUrl).data);
            handleResponse(cachedResponse, requestType, pDialog);
        } else {
            //no response
            ApplicationController.getInstance().addToRequestQueue(stringRequest);
        }

        // ApplicationController.getInstance().addToRequestQueue(stringRequest);


    }

    public static void handleResponse(String response, String requestType, ProgressDialog pDialog) {
        pDialog.dismiss();
        Gson gson = new Gson(); //instantiating the GSON class
        if (requestType != null) {
            if (requestType.equals("GET_WEATHER_URL")) {
                WeatherResponse weatherResponse = gson.fromJson(response, WeatherResponse.class);
                asyncResponseCallBack.processFinishWeather(weatherResponse, requestUrl);
            } else if (requestType.equals("GET_FORECAST_URL")) {
                WeatherForecastResponse weatherForecastResponse = gson.fromJson(response, WeatherForecastResponse.class);
                asyncResponseCallBack.processFinishForecast(weatherForecastResponse, requestUrl);
            }
        }
    }


    public static long getMinutesDifference(long timeStart, long timeStop) {
        long diff = timeStop - timeStart;
        long diffMinutes = diff / (60 * 1000);

        return diffMinutes;
    }


}

