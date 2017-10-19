package com.suntongri.sunnyweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by STR on 2017/10/18 16:24.
 * Description:
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;
    }

}
