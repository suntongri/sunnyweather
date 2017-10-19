package com.suntongri.sunnyweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by STR on 2017/10/18 16:53.
 * Description:
 */

public class Weather {
    public String status;
    public basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
