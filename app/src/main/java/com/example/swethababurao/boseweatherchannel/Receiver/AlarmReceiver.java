package com.example.swethababurao.boseweatherchannel.Receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.swethababurao.boseweatherchannel.Constants;
import com.example.swethababurao.boseweatherchannel.Helper.APIHelper;
import com.example.swethababurao.boseweatherchannel.Helper.APIMethods;
import com.example.swethababurao.boseweatherchannel.Helper.AsyncCallback;
import com.example.swethababurao.boseweatherchannel.Helper.VolleyRequest;
import com.example.swethababurao.boseweatherchannel.MainActivity;
import com.example.swethababurao.boseweatherchannel.Model.WeatherForecastResponse;
import com.example.swethababurao.boseweatherchannel.Model.WeatherResponse;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class AlarmReceiver extends BroadcastReceiver implements AsyncCallback {
    private Context mContext;
    private static AsyncCallback asyncCallback;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;
        asyncCallback = AlarmReceiver.this;
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            String interval = sp.getString("refreshInterval", "1");
            if (!interval.equals("0")) {
                setRecurringAlarm(context);
                getWeather();
            }
        } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            // Get weather if last attempt failed or if 'update location in background' is activated
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            String interval = sp.getString("refreshInterval", "1");
            if (!interval.equals("0") &&
                    (sp.getBoolean("backgroundRefreshFailed", false) || isUpdateLocation())) {
                getWeather();
            }
        } else {
            getWeather();
        }

    }

    private static long intervalMillisForRecurringAlarm(String intervalPref) {
        int interval = Integer.parseInt(intervalPref);
        switch (interval) {
            case 0:
                return 0; // special case for cancel
            case 15:
                return AlarmManager.INTERVAL_FIFTEEN_MINUTES;
            case 30:
                return AlarmManager.INTERVAL_HALF_HOUR;
            case 1:
                return AlarmManager.INTERVAL_HOUR;
            case 12:
                return AlarmManager.INTERVAL_HALF_DAY;
            case 24:
                return AlarmManager.INTERVAL_DAY;
            default: // cases 2 and 6 (or any number of hours)
                return interval * 3600000;
        }
    }

    public static void setRecurringAlarm(Context context) {
        String intervalPref = PreferenceManager.getDefaultSharedPreferences(context)
                .getString("refreshInterval", "1");
        Intent refreshIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent recurringRefresh = PendingIntent.getBroadcast(context,
                0, refreshIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(
                Context.ALARM_SERVICE);
        long intervalMillis = intervalMillisForRecurringAlarm(intervalPref);
        if (intervalMillis == 0) {
            // Cancel previous alarm
            alarmManager.cancel(recurringRefresh);
        } else {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + intervalMillis,
                    intervalMillis,
                    recurringRefresh);
        }
    }

    private void getWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String city = prefs.getString("city", Constants.DEFAULT_CITY);
        String requestUrl = null;
        try {
            requestUrl = APIHelper.getUrl(APIMethods.GET_WEATHER_URL, city, MainActivity.API_KEY).toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        VolleyRequest volleyRequest = new VolleyRequest(mContext, requestUrl, asyncCallback);
        volleyRequest.executeRequest(MainActivity.WEATHER);

    }

    private boolean isUpdateLocation() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getBoolean("updateLocationAutomatically", false);
    }

    @Override
    public void processFinishWeather(WeatherResponse weatherResponse, String url) {
        //notification to user

    }

    @Override
    public void processFinishForecast(WeatherForecastResponse weatherForecastResponse, String url) {

    }

    @Override
    public void processErrorHandle(String errorMsg) {

    }
}
