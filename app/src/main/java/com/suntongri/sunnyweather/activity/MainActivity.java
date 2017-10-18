package com.suntongri.sunnyweather.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.suntongri.sunnyweather.R;
import com.suntongri.sunnyweather.fragment.ChooseAreaFragment;

public class MainActivity extends AppCompatActivity {

    private ChooseAreaFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = new ChooseAreaFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        fragment.backPressed();
    }
}
