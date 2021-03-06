package com.suntongri.sunnyweather.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.suntongri.sunnyweather.R;
import com.suntongri.sunnyweather.activity.MainActivity;
import com.suntongri.sunnyweather.activity.WeatherActivity;
import com.suntongri.sunnyweather.db.City;
import com.suntongri.sunnyweather.db.Country;
import com.suntongri.sunnyweather.db.Province;
import com.suntongri.sunnyweather.util.HttpUtil;
import com.suntongri.sunnyweather.util.Utility;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.media.CamcorderProfile.get;
import static com.suntongri.sunnyweather.util.Utility.handleProvinceResponse;
import static org.litepal.crud.DataSupport.findAll;
import static org.litepal.crud.DataSupport.where;

/**
 * Created by STR on 2017/10/17 20:19.
 * Description:
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;
    private Button backButton;
    private TextView titleText;
    private ListView listView;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<Country> countryList;

    private int currentLevel;
    private Province selectProvince;
    private City selectCity;
    private ArrayAdapter<String> adapter;

    private ProgressDialog dialog;
    private String weatherId;
    private SharedPreferences preferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        View view  = View.inflate(getActivity(), R.layout.choose_area,null);
        View view = inflater.inflate(R.layout.choose_area,container,false);
        backButton = view.findViewById(R.id.back_button);
        titleText = view.findViewById(R.id.title_text);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectCity = cityList.get(position);
                    queryCountries();
                }else if(currentLevel == LEVEL_COUNTRY){
                    if(getActivity() instanceof MainActivity){
                        weatherId = countryList.get(position).getWeatherId();
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.refreshLayout.setRefreshing(true);
                        weatherId = countryList.get(position).getWeatherId();
                        preferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
                        preferences.edit().putString("weather_id",weatherId).commit();
                        activity.requestWeather(weatherId);
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_COUNTRY){
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://www.guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryCities() {
        titleText.setText(selectProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid=?",String.valueOf(selectProvince.getId())).find(City.class);
        if(cityList.size()>0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            int provinceCode = selectProvince.getProvindeCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }

    }

    private void queryCountries() {
        titleText.setText(selectCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countryList = DataSupport.where("cityId=?",String.valueOf(selectCity.getId())).find(Country.class);
        if (countryList.size()>0){
            dataList.clear();
            for (Country country : countryList){
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            currentLevel = LEVEL_COUNTRY;
        }else{
            int provinceCode = selectProvince.getProvindeCode();
            int cityCode = selectCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+ "/"+ cityCode;
            queryFromServer(address,"country");
        }
    }

    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressBar();
                        Toast.makeText(getActivity(),"加载失败！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();//
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    int id  = selectProvince.getId();
                    result = Utility.handleCityResponse(responseText,id);
                }else if("country".equals(type)){
                    int id = selectCity.getId();
                    result = Utility.handleCountryResponse(responseText,id);
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressBar();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("country".equals(type)){
                                queryCountries();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog() {
        if(dialog == null){
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("正在加载");
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
    }

    private void closeProgressBar() {
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    public void backPressed(){
        if(currentLevel == LEVEL_COUNTRY){
            queryCities();
        }else if(currentLevel == LEVEL_CITY){
            queryProvinces();
        }
    }
}
