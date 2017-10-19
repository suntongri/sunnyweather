package com.suntongri.sunnyweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by STR on 2017/10/18 16:06.
 * Description:
 */

public class basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
