package com.example.swethababurao.boseweatherchannel;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.swethababurao.boseweatherchannel.Adapter.ViewPagerAdapter;
import com.example.swethababurao.boseweatherchannel.Adapter.WeatherAdapter;
import com.example.swethababurao.boseweatherchannel.Fragment.RecyclerViewFragment;
import com.example.swethababurao.boseweatherchannel.Helper.APIHelper;
import com.example.swethababurao.boseweatherchannel.Helper.APIMethods;
import com.example.swethababurao.boseweatherchannel.Helper.ApplicationController;
import com.example.swethababurao.boseweatherchannel.Helper.AsyncCallback;
import com.example.swethababurao.boseweatherchannel.Helper.VolleyRequest;
import com.example.swethababurao.boseweatherchannel.Model.Weather;
import com.example.swethababurao.boseweatherchannel.Model.WeatherForecast;
import com.example.swethababurao.boseweatherchannel.Model.WeatherForecastResponse;
import com.example.swethababurao.boseweatherchannel.Model.WeatherResponse;
import com.example.swethababurao.boseweatherchannel.Receiver.AlarmReceiver;
import com.example.swethababurao.boseweatherchannel.Receiver.NotificationEventReceiver;
import com.example.swethababurao.boseweatherchannel.Utils.UnitConversion;


public class MainActivity extends AppCompatActivity implements LocationListener, AsyncCallback {
    protected static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;

    public static final String TAG = "MAIN_ACTIVITY";
    public static final String NOTIFICATION_CHANNEL_ID = "WEATHER_CHANNEL";
    public static final String API_KEY = BuildConfig.ApiKey;
    public static final String WEATHER = "GET_WEATHER_URL";
    public static final String FORECAST = "GET_FORECAST_URL";
    private static final int REFRESH_EVERY = 300000;

    private static Map<String, Integer> windSpeedUnits = new HashMap<>();
    private static Map<String, Integer> pressureUnits = new HashMap<>();
    private static boolean mappingsInitialised = false;

    private WeatherResponse todayWeather = new WeatherResponse();
    private WeatherForecastResponse weatherForecast = new WeatherForecastResponse();
    private List<WeatherForecast> fiveDayForecast = new ArrayList<>();
    private List<WeatherForecast> todayThreeHourlyForecast = new ArrayList<>();
    private List<WeatherForecast> tomorrowForecast = new ArrayList<>();

    private Typeface weatherFont;

    private TextView todayTemperature;
    private TextView todayDescription;
    private TextView todayWind;
    private TextView todayPressure;
    private TextView todayHumidity;
    private TextView todaySunrise;
    private TextView todaySunset;
    private TextView lastUpdated;
    private TextView todayIcon;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View appView;

    private boolean darkTheme;
    private boolean blackTheme;
    private int theme;

    LocationManager locationManager;
    ProgressDialog progressDialog;


    boolean destroyed = false;
    private static SharedPreferences prefs;

    public String recentCity = "";

    private static AsyncCallback asyncCallback;
    private static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize the associated SharedPreferences file with default values
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        setActivityTheme();

        mContext = MainActivity.this;
        asyncCallback = MainActivity.this;

        //Main activity view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationEventReceiver.setupAlarm(getApplicationContext());


        /*Intent alarm = new Intent(mContext, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(mContext, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
        }*/
        // progressDialog = new ProgressDialog(MainActivity.this);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (darkTheme) {
            toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay_Dark);
        } else if (blackTheme) {
            toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay_Black);
        }

        //child views of main activity
        appView = findViewById(R.id.viewApp);
        todayTemperature = (TextView) findViewById(R.id.todayTemperature);
        todayDescription = (TextView) findViewById(R.id.todayDescription);
        todayWind = (TextView) findViewById(R.id.todayWind);
        todayPressure = (TextView) findViewById(R.id.todayPressure);
        todayHumidity = (TextView) findViewById(R.id.todayHumidity);
        todaySunrise = (TextView) findViewById(R.id.todaySunrise);
        todaySunset = (TextView) findViewById(R.id.todaySunset);
        lastUpdated = (TextView) findViewById(R.id.lastUpdated);
        todayIcon = (TextView) findViewById(R.id.todayIcon);
        weatherFont = Typeface.createFromAsset(this.getAssets(), "fonts/weather.ttf");
        todayIcon.setTypeface(weatherFont);

        //viewPager for today, tomorrow and 5 day weather forecast
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        destroyed = false;

        initMappings();
        preloadWeatherData(); //need to load it from cache if internet not present, handled in VolleyRequest
        AlarmReceiver.setRecurringAlarm(MainActivity.this);
    }


    @Override
    public void onStart() {
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
       /* when application is not killed and started again, onStart() is called without onCreate being called
        Hence we need to update today weather UI and forecast weather UI*/
        // updateTodayWeatherUI();
        //updateLongTermWeatherUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getTheme(prefs.getString("theme", "fresh")) != theme) {
            // Restart activity to apply theme
            overridePendingTransition(0, 0);
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
        } else if (shouldUpdate()) {
            updateTodaysWeather();
            updateFutureForecast();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        ApplicationController.getInstance().cancelPendingRequests(TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
        ApplicationController.getInstance().cancelPendingRequests(TAG);

        if (locationManager != null) {
            try {
                locationManager.removeUpdates(MainActivity.this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void setActivityTheme() {
        setTheme(theme = getTheme(prefs.getString("theme", "classic")));
        darkTheme = theme == R.style.AppTheme_NoActionBar_Dark ||
                theme == R.style.AppTheme_NoActionBar_Classic_Dark;
        blackTheme = theme == R.style.AppTheme_NoActionBar_Black ||
                theme == R.style.AppTheme_NoActionBar_Classic_Black;
    }


    private void preloadWeatherData() {

        updateTodaysWeather();

        updateFutureForecast();
    }

    public static void updateTodaysWeather() {

        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String city = prefs.getString("city", Constants.DEFAULT_CITY);
        String requestUrl = null;
        try {
            requestUrl = APIHelper.getUrl(APIMethods.GET_WEATHER_URL, city, API_KEY).toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        VolleyRequest volleyRequest = new VolleyRequest(mContext, requestUrl, asyncCallback);
        volleyRequest.executeRequest(WEATHER);

    }

    public static void updateFutureForecast() {

        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String city = prefs.getString("city", Constants.DEFAULT_CITY);
        String requestUrl = null;

        try {
            requestUrl = APIHelper.getUrl(APIMethods.GET_FORECAST_URL, city, API_KEY).toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        VolleyRequest volleyRequest = new VolleyRequest(mContext, requestUrl, asyncCallback);
        //** try to get cached data before executeRequest **//
       /* Cache cache = (Cache) ApplicationController.getInstance().getRequestQueue().getCache().get(requestUrl);
        if (ApplicationController.getInstance().getRequestQueue().getCache().get(requestUrl) != null) {
            //response exists
            String cachedResponse = new String(ApplicationController.getInstance().getRequestQueue().getCache().get(requestUrl).data);
            VolleyRequest.handleResponse(cachedResponse, FORECAST);
        } else {
            //no response

            volleyRequest.executeRequest(WEATHER);
        }*/

        volleyRequest.executeRequest(FORECAST);
    }


    @Override
    public void processFinishWeather(WeatherResponse weatherResponse, String url) {
        Log.d("response city name", weatherResponse.getCityName());
        Log.d("response city temp", weatherResponse.getMain().getTemp());
        todayWeather = weatherResponse;
        /*if (todayWeather.getCod() == "404") {
            Toast.makeText(MainActivity.this, "City not Found. Enter a valid city name", Toast.LENGTH_LONG);
            return;
        }*/

        updateTodayWeatherUI();
        updateLastUpdateTime();
    }

    @Override
    public void processFinishForecast(WeatherForecastResponse weatherForecastResponse, String url) {
        Log.d("response city name", weatherForecastResponse.getCity().getCityName());
        weatherForecast = weatherForecastResponse;
        WeatherForecast[] weatherForecasts = weatherForecast.getWeatherForecasts();
        for (int i = 0; i < weatherForecasts.length; i++) {
            Weather[] weathers = weatherForecasts[i].getWeather();
            weathers[0].setIcon(setWeatherIcon(Integer.parseInt(weathers[0].getId()), Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
            Calendar forecastDate = Calendar.getInstance();
            String dateStringFromResponse = weatherForecasts[i].getDateTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(dateStringFromResponse);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            forecastDate.setTime(convertedDate);

            Calendar today = Calendar.getInstance();
            if (forecastDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                todayThreeHourlyForecast.add(weatherForecasts[i]);
            } else if (forecastDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) + 1) {
                tomorrowForecast.add(weatherForecasts[i]);
            } else {
                fiveDayForecast.add(weatherForecasts[i]);
            }

        }

        updateLongTermWeatherUI();
    }

    @Override
    public void processErrorHandle(String errorMsg) {
        Log.d("Volley Error", errorMsg);
        //handle error by parsing error message
    }


    private void searchCities() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(this.getString(R.string.search_title));

        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(64, 0, 64, 0);//l, t , r , b
        // Set editText layout parameters to the editText field
        final TextInputEditText textInputEditText = new TextInputEditText(this);
        textInputEditText.setId(View.generateViewId());
        textInputEditText.setHint(MainActivity.this.getString(R.string.enter_city_name));
        textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        textInputEditText.setMaxLines(1);
        textInputEditText.setSingleLine(true);
        textInputEditText.setHintTextColor(MainActivity.this.getResources().getColor(R.color.text_hint));
        textInputEditText.setTextColor(MainActivity.this.getResources().getColor(R.color.text_dark));
        textInputEditText.setLayoutParams(editTextParams);


        LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textInputLayoutParams.setMargins(64, 64, 64, 0);//l, t , r , b
        final TextInputLayout textInputLayout = new TextInputLayout(this);
        textInputLayout.setId(View.generateViewId());
        textInputLayout.setLayoutParams(textInputLayoutParams);
        textInputLayout.addView(textInputEditText);

        alert.setView(textInputLayout);
        alert.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String result = textInputEditText.getText().toString();
                if (!result.isEmpty()) {
                    saveCity(result);
                }
            }
        });
        alert.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled
            }
        });
        alert.show();
    }

    private void saveCity(String result) {
        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        recentCity = prefs.getString("city", Constants.DEFAULT_CITY);


        if (!recentCity.equals(result)) {
            //New location, update shared preferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("city", result);
            editor.putBoolean("cityChanged", true);
            editor.commit();
            // New location, update weather
            updateTodaysWeather();
            updateFutureForecast();
        }
    }


    private String setWeatherIcon(int actualId, int hourOfDay) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            if (hourOfDay >= 7 && hourOfDay < 20) { // consider actual sunrise and sunset
                icon = this.getString(R.string.weather_sunny);
            } else {
                icon = this.getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2:
                    icon = this.getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = this.getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = this.getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = this.getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = this.getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = this.getString(R.string.weather_rainy);
                    break;
            }
        }
        return icon;
    }

    public WeatherAdapter getAdapter(int id) {
        WeatherAdapter weatherAdapter;
        if (id == 0 && !(todayThreeHourlyForecast.isEmpty())) {
            weatherAdapter = new WeatherAdapter(this, todayThreeHourlyForecast);
        } else if (id == 1) {
            weatherAdapter = new WeatherAdapter(this, tomorrowForecast);
        } else {
            weatherAdapter = new WeatherAdapter(this, fiveDayForecast);
        }
        return weatherAdapter;
    }


    private void updateTodayWeatherUI() {
        try {
            if (todayWeather.getSys().getCountry().isEmpty()) {
                preloadWeatherData();
                return;
            }
        } catch (Exception e) {

        }
        String city = todayWeather.getCityName();
        String country = todayWeather.getSys().getCountry();
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());
        getSupportActionBar().setTitle(city + (country.isEmpty() ? "" : ", " + country));


        Float initialTemperatureRepresentation = Float.parseFloat(todayWeather.getMain().getTemp());
        float temperatureAfterConverion;
        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (prefs.getString("unit", "°C").equals("°C")) {
            temperatureAfterConverion = UnitConversion.kelvinToCelsius(initialTemperatureRepresentation);
        } else if (prefs.getString("unit", "°C").equals("°F")) {
            temperatureAfterConverion = UnitConversion.kelvinToFahrenheit(initialTemperatureRepresentation);
        } else {
            temperatureAfterConverion = initialTemperatureRepresentation;
        }

        // Temperature

        /*float temperature = UnitConversion.convertTemperature(Float.parseFloat(todayWeather.getMain().getTemp()), sp);
        if (sp.getBoolean("temperatureInteger", false)) {
            temperature = Math.round(temperature);
        }*/


        // Wind
        double wind;
        try {
            wind = Double.parseDouble(todayWeather.getWind().getSpeed());
        } catch (Exception e) {
            e.printStackTrace();
            wind = 0;
        }
        String windSpeed = prefs.getString("speedUnit", "mph");
        wind = UnitConversion.convertWind(wind, windSpeed);
        //wind = UnitConversion.convertWind(wind, sp);

        // Pressure

        String pressureUnit = prefs.getString("pressureUnit", "hPa");
        double pressure = UnitConversion.convertPressure((float) Double.parseDouble(todayWeather.getMain().getPressure()), pressureUnit);
        //double pressure = UnitConversion.convertPressure((float) Double.parseDouble(todayWeather.getMain().getPressure()), sp);
        todayTemperature.setText(new DecimalFormat("0.#").format(temperatureAfterConverion) + " " + prefs.getString("unit", "°C"));

        Weather[] weathers = todayWeather.getWeather();
        todayDescription.setText(weathers[0].getDescription().substring(0, 1).toUpperCase() +
                weathers[0].getDescription().substring(1));
        if (prefs.getString("speedUnit", "mph").equals("bft")) {
            todayWind.setText(getString(R.string.wind) + ": " + UnitConversion.getBeaufortName((int) wind));
        } else {
            todayWind.setText(getString(R.string.wind) + ": " + new DecimalFormat("0.0").format(wind) + " " + localize(prefs, "speedUnit", "mph"));
        }
        todayPressure.setText(getString(R.string.pressure) + ": " + new DecimalFormat("0.0").format(pressure) + " " +
                localize(prefs, "pressureUnit", "hPa"));
        todayHumidity.setText(getString(R.string.humidity) + ": " + todayWeather.getMain().getHumidity() + " %");

        String sunriseString = todayWeather.getSys().getSunrise();
        Date sunriseTime = new Date(Long.parseLong(sunriseString) * 1000);
        todaySunrise.setText(getString(R.string.sunrise) + ": " + timeFormat.format(sunriseTime));

        String sunsetString = todayWeather.getSys().getSunset();
        Date sunsetTime = new Date(Long.parseLong(sunsetString) * 1000);
        todaySunset.setText(getString(R.string.sunset) + ": " + timeFormat.format(sunsetTime));
        todayIcon.setText(setWeatherIcon(Integer.parseInt(weathers[0].getId()), Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
    }


    private void updateLongTermWeatherUI() {
        if (destroyed) {
            return;
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundleToday = new Bundle();
        bundleToday.putInt("day", 0);
        RecyclerViewFragment listViewFragmentToday = new RecyclerViewFragment();
        listViewFragmentToday.setArguments(bundleToday);
        viewPagerAdapter.addFragment(listViewFragmentToday, getString(R.string.today));

        Bundle bundleTomorrow = new Bundle();
        bundleTomorrow.putInt("day", 1);
        RecyclerViewFragment ListViewFragmentTomorrow = new RecyclerViewFragment();
        ListViewFragmentTomorrow.setArguments(bundleTomorrow);
        viewPagerAdapter.addFragment(ListViewFragmentTomorrow, getString(R.string.tomorrow));

        Bundle bundle = new Bundle();
        bundle.putInt("day", 2);
        RecyclerViewFragment listViewFramgmentFurthur = new RecyclerViewFragment();
        listViewFramgmentFurthur.setArguments(bundle);
        viewPagerAdapter.addFragment(listViewFramgmentFurthur, getString(R.string.fivedays));

        int currentPage = viewPager.getCurrentItem();

        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (currentPage == 0 && todayThreeHourlyForecast.isEmpty()) {
            currentPage = 1;
        }
        viewPager.setCurrentItem(currentPage, false);
    }


    private boolean shouldUpdate() {
        long lastUpdatedTime = PreferenceManager.getDefaultSharedPreferences(this).getLong("lastUpdate", -1);
        boolean cityChanged = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("cityChanged", false);
        // Update if city has changed or if the lastUpdatedTime is greater than the refresh interval
        return cityChanged || lastUpdatedTime < 0 || (Calendar.getInstance().getTimeInMillis() - lastUpdatedTime) > REFRESH_EVERY;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateTodaysWeather();
            updateFutureForecast();
            return true;
        }
        if (id == R.id.action_search) {
            searchCities();
            return true;
        }
        if (id == R.id.action_location) {
            getCityByLocation();
            return true;
        }
        /*if (id == R.id.action_map) {
            handle location activity
        }*/

       /* if (id == R.id.action_settings) {
            handle settings activity
        }*/

        return super.onOptionsItemSelected(item);
    }

    public static void initMappings() {
        if (mappingsInitialised) {
            return;
        }
        mappingsInitialised = true;
        windSpeedUnits.put("m/s", R.string.speed_unit_mps);
        windSpeedUnits.put("kph", R.string.speed_unit_kph);
        windSpeedUnits.put("mph", R.string.speed_unit_mph);
        windSpeedUnits.put("kn", R.string.speed_unit_kn);

        pressureUnits.put("hPa", R.string.pressure_unit_hpa);
        pressureUnits.put("kPa", R.string.pressure_unit_kpa);
        pressureUnits.put("mm Hg", R.string.pressure_unit_mmhg);
    }

    private String localize(SharedPreferences sp, String preferenceKey, String defaultValueKey) {
        return localize(sp, this, preferenceKey, defaultValueKey);
    }

    public static String localize(SharedPreferences sp, Context context, String preferenceKey, String defaultValueKey) {
        String preferenceValue = sp.getString(preferenceKey, defaultValueKey);
        String result = preferenceValue;
        if ("speedUnit".equals(preferenceKey)) {
            if (windSpeedUnits.containsKey(preferenceValue)) {
                result = context.getString(windSpeedUnits.get(preferenceValue));
            }
        } else if ("pressureUnit".equals(preferenceKey)) {
            if (pressureUnits.containsKey(preferenceValue)) {
                result = context.getString(pressureUnits.get(preferenceValue));
            }
        }
        return result;
    }


    void getCityByLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);
            }

        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.getting_location));
            progressDialog.setCancelable(false);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        locationManager.removeUpdates(MainActivity.this);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            });
            progressDialog.show();
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        } else {
            showLocationSettingsDialog();
        }
    }

    private void showLocationSettingsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.location_settings);
        alertDialog.setMessage(R.string.location_settings_message);
        alertDialog.setPositiveButton(R.string.location_settings_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCityByLocation();
                }
                return;
            }
        }
    }

    //callback to receive notification from LocationManager when location has changed
    @Override
    public void onLocationChanged(Location location) {
        progressDialog.hide();
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            Log.e("LocationManager", "Error while trying to stop listening for location updates. This is probably a permissions issue", e);
        }
        Log.i("LOCATION (" + location.getProvider().toUpperCase() + ")", location.getLatitude() + ", " + location.getLongitude());
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        try {
            VolleyRequest volleyRequest = new VolleyRequest(MainActivity.this, APIHelper.getUrl(APIMethods.GET_WEATHER_URL, Double.toString(latitude), Double.toString(longitude), API_KEY).toString(), asyncCallback);
            volleyRequest.executeRequest(WEATHER);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void updateCityName() {


        try {
            VolleyRequest volleyRequest = new VolleyRequest(MainActivity.this, APIHelper.getUrl(APIMethods.GET_WEATHER_URL, recentCity, API_KEY).toString(), asyncCallback);
            volleyRequest.executeRequest(WEATHER);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }


    public static long saveLastUpdateTime(SharedPreferences sp) {
        Calendar now = Calendar.getInstance();
        sp.edit().putLong("lastUpdate", now.getTimeInMillis()).apply();
        return now.getTimeInMillis();
    }

    private void updateLastUpdateTime() {
        updateLastUpdateTime(
                PreferenceManager.getDefaultSharedPreferences(this).getLong("lastUpdate", -1)
        );
    }

    private void updateLastUpdateTime(long timeInMillis) {
        if (timeInMillis < 0) {
            // No time
            lastUpdated.setText("");
        } else {
            lastUpdated.setText(getString(R.string.last_update, formatTimeWithDayIfNotToday(this, timeInMillis)));
        }
    }

    public static String formatTimeWithDayIfNotToday(Context context, long timeInMillis) {
        Calendar now = Calendar.getInstance();
        Calendar lastCheckedCal = new GregorianCalendar();
        lastCheckedCal.setTimeInMillis(timeInMillis);
        Date lastCheckedDate = new Date(timeInMillis);
        String timeFormat = android.text.format.DateFormat.getTimeFormat(context).format(lastCheckedDate);
        if (now.get(Calendar.YEAR) == lastCheckedCal.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == lastCheckedCal.get(Calendar.DAY_OF_YEAR)) {

            return timeFormat;
        } else {
            return android.text.format.DateFormat.getDateFormat(context).format(lastCheckedDate) + " " + timeFormat;
        }
    }

    private int getTheme(String themePref) {
        switch (themePref) {
            case "dark":
                return R.style.AppTheme_NoActionBar_Dark;
            case "black":
                return R.style.AppTheme_NoActionBar_Black;
            case "classic":
                return R.style.AppTheme_NoActionBar_Classic;
            case "classicdark":
                return R.style.AppTheme_NoActionBar_Classic_Dark;
            case "classicblack":
                return R.style.AppTheme_NoActionBar_Classic_Black;
            default:
                return R.style.AppTheme_NoActionBar;
        }
    }

    public void sendNotification(View view) {

        // Gets an instance of the NotificationManager service//
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(Integer.parseInt(setWeatherIcon(800, Calendar.getInstance().get(Calendar.HOUR_OF_DAY))))
                        .setContentTitle("Weather Update")
                        .setContentText("Last Updated");


        //action needed to be done on click of the notification
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mNotificationManager.notify(001, mBuilder.build());
    }

}
