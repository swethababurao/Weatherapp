package com.example.swethababurao.boseweatherchannel;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class LocationActivity extends AppCompatActivity {
    private static final String GOOGLE_MAPS_API_KEY = BuildConfig.GoogleMapsApiKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        String apiKey = MainActivity.API_KEY;

        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/map.html?lat=" + prefs.getFloat("latitude", 0) + "&lon=" + prefs.getFloat("longitude", 0) + "&appid=" + apiKey);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

