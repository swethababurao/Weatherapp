package com.example.swethababurao.boseweatherchannel.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.swethababurao.boseweatherchannel.MainActivity;
import com.example.swethababurao.boseweatherchannel.Model.Weather;
import com.example.swethababurao.boseweatherchannel.Model.WeatherForecast;
import com.example.swethababurao.boseweatherchannel.Model.WeatherResponse;
import com.example.swethababurao.boseweatherchannel.R;
import com.example.swethababurao.boseweatherchannel.Utils.UnitConversion;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<WeatherForecast> weatherForecasts;
    private Context context;

    public WeatherAdapter(Context context, List<WeatherForecast> itemList) {
        this.weatherForecasts = itemList;
        this.context = context;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.three_hourly_list_item, null);

        WeatherViewHolder viewHolder = new WeatherViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder customViewHolder, int i) {
        WeatherForecast weatherForecastItem = weatherForecasts.get(i);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        // Temperature
        Float initialTemperatureRepresentation = Float.parseFloat(weatherForecastItem.getMain().getTemp());
        float temperatureAfterConverion;
        if (sp.getString("unit", "°C").equals("°C")) {
            temperatureAfterConverion = UnitConversion.kelvinToCelsius(initialTemperatureRepresentation);
        } else if (sp.getString("unit", "°C").equals("°F")) {
            temperatureAfterConverion = UnitConversion.kelvinToFahrenheit(initialTemperatureRepresentation);
        } else {
            temperatureAfterConverion = initialTemperatureRepresentation;
        }
        /*float temperature = UnitConversion.convertTemperature(Float.parseFloat(weatherForecastItem.getMain().getTemp()), sp);
        if (sp.getBoolean("temperatureInteger", false)) {
            temperature = Math.round(temperature);
        }*/

        // Wind
        double wind;
        try {
            wind = Double.parseDouble(weatherForecastItem.getWind().getSpeed());
        } catch (Exception e) {
            e.printStackTrace();
            wind = 0;
        }
        String windSpeed = sp.getString("speedUnit", "m/s");
        wind = UnitConversion.convertWind(wind, windSpeed);
        //wind = UnitConversion.convertWind(wind, sp);

        // Pressure
        String pressureUnit = sp.getString("pressureUnit", "hPa");
        double pressure = UnitConversion.convertPressure((float) Double.parseDouble(weatherForecastItem.getMain().getPressure()), pressureUnit);
        //double pressure = UnitConversion.convertPressure((float) Double.parseDouble(weatherForecastItem.getMain().getPressure()), sp);
        String dateString;
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date convertedDate = new Date();
        try {
            convertedDate = dtFormat.parse(weatherForecastItem.getDateTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TimeZone tz = TimeZone.getDefault();
        String defaultDateFormat = context.getResources().getStringArray(R.array.dateFormatsValues)[0];
        String dateFormat = sp.getString("dateFormat", defaultDateFormat);
        if ("custom".equals(dateFormat)) {
            dateFormat = sp.getString("dateFormatCustom", defaultDateFormat);
        }

        try {
            SimpleDateFormat resultFormat = new SimpleDateFormat(dateFormat);
            resultFormat.setTimeZone(tz);

            dateString = resultFormat.format(convertedDate);
        } catch (IllegalArgumentException e) {
            dateString = context.getResources().getString(R.string.error_dateFormat);
        }

        if (sp.getBoolean("differentiateDaysByTint", false)) {
            Date now = new Date();

            int color;
            if (weatherForecastItem.getNumDaysFrom(now) > 1) {
                TypedArray ta = context.obtainStyledAttributes(new int[]{R.attr.colorTintedBackground, R.attr.colorBackground});
                if (weatherForecastItem.getNumDaysFrom(now) % 2 == 1) {
                    color = ta.getColor(0, context.getResources().getColor(R.color.colorTintedBackground));
                } else {

                    color = ta.getColor(1, context.getResources().getColor(R.color.colorBackground));
                }
                ta.recycle();
                customViewHolder.itemView.setBackgroundColor(color);
            }
        }

        customViewHolder.itemDate.setText(dateString);
        if (sp.getBoolean("displayDecimalZeroes", false)) {
            customViewHolder.itemTemperature.setText(new DecimalFormat("0.0").format(temperatureAfterConverion) + " " + sp.getString("unit", "°C"));
        } else {
            customViewHolder.itemTemperature.setText(new DecimalFormat("#.#").format(temperatureAfterConverion) + " " + sp.getString("unit", "°C"));
        }

        Weather[] weathers = weatherForecastItem.getWeather();
        customViewHolder.itemDescription.setText(weathers[0].getDescription().substring(0, 1).toUpperCase() +
                weathers[0].getDescription().substring(1));
        Typeface weatherFont = Typeface.createFromAsset(context.getAssets(), "fonts/weather.ttf");
        customViewHolder.itemIcon.setTypeface(weatherFont);
        customViewHolder.itemIcon.setText(weathers[0].getIcon());
        if (sp.getString("speedUnit", "m/s").equals("bft")) {
            customViewHolder.itemyWind.setText(context.getString(R.string.wind) + ": " + UnitConversion.getBeaufortName((int) wind));
        } else {
            customViewHolder.itemyWind.setText(context.getString(R.string.wind) + ": " + new DecimalFormat("0.0").format(wind) + " " +
                    MainActivity.localize(sp, context, "speedUnit", "m/s"));
        }
        customViewHolder.itemPressure.setText(context.getString(R.string.pressure) + ": " + new DecimalFormat("0.0").format(pressure) + " " +
                MainActivity.localize(sp, context, "pressureUnit", "hPa"));
        customViewHolder.itemHumidity.setText(context.getString(R.string.humidity) + ": " + weatherForecastItem.getMain().getHumidity() + " %");
    }

    @Override
    public int getItemCount() {
        return (null != weatherForecasts ? weatherForecasts.size() : 0);
    }

    protected class WeatherViewHolder extends RecyclerView.ViewHolder {
        public TextView itemDate;
        public TextView itemTemperature;
        public TextView itemDescription;
        public TextView itemyWind;
        public TextView itemPressure;
        public TextView itemHumidity;
        public TextView itemIcon;
        public View lineView;

        public WeatherViewHolder(View view) {
            super(view);
            this.itemDate = (TextView) view.findViewById(R.id.itemDate);
            this.itemTemperature = (TextView) view.findViewById(R.id.itemTemperature);
            this.itemDescription = (TextView) view.findViewById(R.id.itemDescription);
            this.itemyWind = (TextView) view.findViewById(R.id.itemWind);
            this.itemPressure = (TextView) view.findViewById(R.id.itemPressure);
            this.itemHumidity = (TextView) view.findViewById(R.id.itemHumidity);
            this.itemIcon = (TextView) view.findViewById(R.id.itemIcon);
            this.lineView = view.findViewById(R.id.lineView);
        }
    }

}


