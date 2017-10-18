package com.suntongri.sunnyweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by STR on 2017/10/17 18:54.
 * Description:litepal的工具类
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request  = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
