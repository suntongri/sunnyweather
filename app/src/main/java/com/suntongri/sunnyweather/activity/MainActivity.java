package com.suntongri.sunnyweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.suntongri.sunnyweather.R;
import com.suntongri.sunnyweather.fragment.ChooseAreaFragment;
import com.suntongri.sunnyweather.gson.Weather;
import com.suntongri.sunnyweather.util.HttpUtil;
import com.suntongri.sunnyweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ChooseAreaFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = new ChooseAreaFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, fragment).commit();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherId = prefs.getString("weather",null);
        if(weatherId!=null){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }

    }



    @Override
    public void onBackPressed() {
        fragment.backPressed();
    }
}
