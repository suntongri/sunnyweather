package com.suntongri.sunnyweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by STR on 2017/10/17 18:17.
 * Description:
 */

public class Country extends DataSupport {
    private int id;
    private String countryName;
    private int cityId;
    private String weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
