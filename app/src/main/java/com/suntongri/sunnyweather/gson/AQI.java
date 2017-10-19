package com.suntongri.sunnyweather.gson;

/**
 * Created by STR on 2017/10/18 16:12.
 * Description:
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }

}
