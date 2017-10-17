package com.suntongri.sunnyweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by STR on 2017/10/17 18:10.
 * Description:
 */

public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int provindeCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvindeCode() {
        return provindeCode;
    }

    public void setProvindeCode(int provindeCode) {
        this.provindeCode = provindeCode;
    }
}
