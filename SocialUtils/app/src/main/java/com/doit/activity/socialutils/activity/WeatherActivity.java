package com.doit.activity.socialutils.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.adapter.WeatherAdapter;
import com.doit.activity.socialutils.bean.HttpContants;
import com.doit.activity.socialutils.bean.JsonBean;
import com.doit.activity.socialutils.bean.WeatherBean;
import com.doit.activity.socialutils.pickerview.GetJsonDataUtil;
import com.doit.activity.socialutils.pickerview.OptionsPickerView;
import com.doit.activity.socialutils.util.Utils;
import com.doit.activity.socialutils.view.LineCircle;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzh on 2018/7/20.
 */

public class WeatherActivity extends Activity implements View.OnClickListener {


    private ImageView mImgSun;

    private AnimationDrawable animationDrawable;

    private RelativeLayout mRWeatherBg;

    private LineCircle mLcView;

    private WeatherAdapter mAdapter;
    private List<WeatherBean> mWeatherData = new ArrayList<>();
    private WeatherBean mBean;

    private LinearLayout mLWeather;
    private ImageView mImgToday;
    private TextView mTvWeek;
    private String mWeek;
    private TextView mTvWInfo;
    private String mWeather;

    private TextView mTvCity;
    private String mCity = "北京";

    private int mTemplow;
    private int mTemphigh;
    private int mTem;
    private int mType;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private Thread thread;

    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Utils.setTopBar(this);
        init();
    }

    /**
     * init
     */
    public void init() {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

        mImgSun = findViewById(R.id.img_sun);
        mRWeatherBg = findViewById(R.id.re_weather_bg);
        mLcView = findViewById(R.id.lc_view);


        mLWeather = findViewById(R.id.linear_weather);

        mImgToday = findViewById(R.id.img_todayw);
        mTvWeek = findViewById(R.id.text_week);
        mTvWInfo = findViewById(R.id.text_wea_info);
        mTvCity = findViewById(R.id.text_city);
        mTvCity.setOnClickListener(this);
        getWeatherInfo();

    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        // Toast.makeText(ClueAddActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:

                    Log.i("data", "解析成功");
                    break;

                case MSG_LOAD_FAILED:
                    break;

            }
        }
    };


    public void setData(int type) {

        try {


            mLcView.setCurrentTem(mTem);
            mLcView.setStartTem(mTemplow);
            mLcView.setStopTem(mTemphigh);
            mTvWeek.setText(mWeek);
            mTvWInfo.setText(mWeather);


            for (int i = 0; i < mWeatherData.size(); i++) {
                View wView = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.weather_item, null);

                int imgType = mWeatherData.get(i).getWeather_icon();
                String wea = mWeatherData.get(i).getWeather();
                String tem = mWeatherData.get(i).getTemperature();
                String week = mWeatherData.get(i).getWeek();

                TextView weather = wView.findViewById(R.id.text_weather);
                weather.setText(wea);

                TextView temp = wView.findViewById(R.id.text_tem);
                temp.setText(tem);

                TextView weekd = wView.findViewById(R.id.text_date);
                weekd.setText(week);


                ImageView img = wView.findViewById(R.id.img_weather);

                switch (imgType) {
                    case 0:
                        img.setBackgroundResource(R.mipmap.sun_0);
                        break;
                    case 1:
                        img.setBackgroundResource(R.mipmap.cloudy_1);
                        break;
                    case 2:
                        img.setBackgroundResource(R.mipmap.yin_2);
                        break;
                    case 3:
                        img.setBackgroundResource(R.mipmap.zy_3);
                        break;
                    case 4:
                        img.setBackgroundResource(R.mipmap.lzy_4);
                        break;
                    case 5:
                        img.setBackgroundResource(R.mipmap.lzy_bb_5);
                        break;
                    case 6:
                        img.setBackgroundResource(R.mipmap.yjx_6);
                        break;
                    case 7:
                        img.setBackgroundResource(R.mipmap.rain_7);
                        break;
                    case 8:
                        img.setBackgroundResource(R.mipmap.mrain_8);
                        break;
                    case 9:
                        img.setBackgroundResource(R.mipmap.lrain_9);
                        break;
                    case 10:
                        img.setBackgroundResource(R.mipmap.brain_10);
                        break;
                    case 11:
                        img.setBackgroundResource(R.mipmap.srain_11);
                        break;
                    case 12:
                        img.setBackgroundResource(R.mipmap.tdrain_12);
                        break;
                    case 13:
                        img.setBackgroundResource(R.mipmap.snow_13);
                        break;
                    case 14:
                        img.setBackgroundResource(R.mipmap.ssnow14);
                        break;
                    case 15:
                        img.setBackgroundResource(R.mipmap.msnow_15);
                        break;
                    case 16:
                        img.setBackgroundResource(R.mipmap.lsnow_16);
                        break;
                    case 17:
                        img.setBackgroundResource(R.mipmap.bsnow_17);
                        break;
                    case 18:
                        img.setBackgroundResource(R.mipmap.wu_18);
                        break;
                    case 53:
                        img.setBackgroundResource(R.mipmap.m_53);
                        break;
                    case 301:
                        img.setBackgroundResource(R.mipmap.rain_301);
                        break;
                    case 302:
                        img.setBackgroundResource(R.mipmap.snow_302);
                        break;

                    default:

                        if (wea.contains(getString(R.string.text_rain))) {
                            img.setBackgroundResource(R.mipmap.rain_301);
                        } else if (wea.contains(getString(R.string.text_snow))) {
                            img.setBackgroundResource(R.mipmap.snow_302);
                        } else if (wea.contains(getString(R.string.text_cloudy))) {
                            img.setBackgroundResource(R.mipmap.cloudy_1);
                        } else if (wea.contains(getString(R.string.text_yin))) {
                            img.setBackgroundResource(R.mipmap.yin_2);
                        } else if (wea.contains(getString(R.string.text_mai))) {
                            img.setBackgroundResource(R.mipmap.m_53);
                        } else {
                            img.setBackgroundResource(R.mipmap.divider);
                        }


                        break;
                }


                mLWeather.addView(wView);
            }


            switch (type) {
                case 0:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_sunny);
                    mImgToday.setBackgroundResource(R.mipmap.sun_0);

                    break;
                case 1:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_cloudy);
                    mImgToday.setBackgroundResource(R.mipmap.cloudy_1);
                    break;
                case 2:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_cloudy_vague);
                    mImgToday.setBackgroundResource(R.mipmap.yin_2);
                    break;
                case 3:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_rain_day);
                    mImgToday.setBackgroundResource(R.mipmap.zy_3);
                    break;
                case 7:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_rain_day);
                    mImgToday.setBackgroundResource(R.mipmap.rain_7);
                    break;
                case 8:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_rain_day);
                    mImgToday.setBackgroundResource(R.mipmap.mrain_8);
                    break;
                case 9:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_rain_day);
                    mImgToday.setBackgroundResource(R.mipmap.lrain_9);
                    break;
                case 14:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_snow);
                    mImgToday.setBackgroundResource(R.mipmap.ssnow14);
                    break;
                case 15:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_snow);
                    mImgToday.setBackgroundResource(R.mipmap.msnow_15);
                    break;
                case 16:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_snow);
                    mImgToday.setBackgroundResource(R.mipmap.lsnow_16);
                    break;
                case 18:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_cloudy);
                    mImgToday.setBackgroundResource(R.mipmap.wu_18);
                    break;
                case 53:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_cloudy);
                    mImgToday.setBackgroundResource(R.mipmap.m_53);
                    break;
                case 301:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_rain_day);
                    mImgToday.setBackgroundResource(R.mipmap.rain_301);
                    break;
                case 302:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_snow);
                    mImgToday.setBackgroundResource(R.mipmap.snow_302);
                    break;

                default:
                    mRWeatherBg.setBackgroundResource(R.mipmap.weather_surface_bg_smog);
                    mImgToday.setBackgroundResource(R.mipmap.divider);
                    break;
            }


//        animationDrawable = (AnimationDrawable) mImgSun.getBackground();
//        if (animationDrawable != null && !animationDrawable.isRunning()) {
//            animationDrawable.start();
//        }
        } catch (Exception e) {

        }
    }


    public void getWeatherInfo() {

        String url = HttpContants.weatherInfo;

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("city", mCity)
                .add("appkey", "46f640bae54e8ea7")
                .build();

//        String url = HttpContants.weatherInfo + "cityname=" + "北京" + "&key=76e362dda5dd29de4fe81db4207c999f" + "&dtype=" + "format=";

        final Request request = new Request.Builder()
                .url(url)
                .post(body).build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("data", "错误信息：" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {

                    String param = response.body().string();

//                    Log.i("data", param);

                    try {
                        mWeatherData.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mLWeather != null) {
                                    mLWeather.removeAllViews();
                                }

                            }
                        });
                        JSONObject obj = new JSONObject(param);
                        JSONObject js = obj.getJSONObject("result");

                        mWeek = js.getString("week");
                        mWeather = js.getString("weather");
                        mTemplow = js.getInt("templow");
                        mTemphigh = js.getInt("temphigh");
                        mTem = js.getInt("temp");
                        mType = js.getInt("img");


                        JSONArray wArray = js.getJSONArray("daily");
                        if (wArray.length() > 0) {
                            for (int i = 1; i < 5; i++) {//只取一周
                                mBean = new WeatherBean();

                                JSONObject jsw = wArray.getJSONObject(i);

                                //白天温度
                                JSONObject dJs = jsw.getJSONObject("day");
                                String dTem = dJs.getString("temphigh");

                                //夜间温度
                                JSONObject nJs = jsw.getJSONObject("night");
                                String nTem = nJs.getString("templow");

                                mBean.setTemperature(dTem + "°/" + nTem + "°");
                                mBean.setWeather(dJs.getString("weather"));
                                mBean.setWeather_icon(dJs.getInt("img"));
                                mBean.setWeek(jsw.getString("week"));

                                mWeatherData.add(mBean);


                            }
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData(mType);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }


    /**
     * 城市选择
     */
    private void ShowPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
//                String tx = options1Items.get(options1).getPickerViewText() +
//                        options2Items.get(options1).get(options2) +
//                        options3Items.get(options1).get(options2).get(options3);

                String c1 = options1Items.get(options1).getPickerViewText();
                String c2 = options2Items.get(options1).get(options2);
                String c3 = options3Items.get(options1).get(options2).get(options3);


                if (!TextUtils.isEmpty(c2)) {
                    mCity = c2;
                } else {
                    mCity = c1;
                }


//                Toast.makeText(ClueAddActivity.this, tx, Toast.LENGTH_SHORT).show();

                mTvCity.setText(c1+"-"+c2);

                getWeatherInfo();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

//        pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.text_city:

                ShowPickerView();

                break;


        }
    }
}
