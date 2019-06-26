package com.doit.activity.socialutils.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.adapter.FoodAdapter;
import com.doit.activity.socialutils.bean.FoodBean;
import com.doit.activity.socialutils.bean.FoodItemBean;
import com.doit.activity.socialutils.bean.HttpContants;
import com.doit.activity.socialutils.bean.nav_items;
import com.doit.activity.socialutils.interfaces.OnItemClickListener;
import com.doit.activity.socialutils.util.Utils;
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
 * Created by lzh on 2018/9/4.
 */

public class FoodActivity extends Activity implements View.OnClickListener {

    private Gson mGson;

    private TextView mTvTitle;
    private TextView mTvBack;

    private RecyclerView mFoodView;
    private FoodAdapter mAdapter;
    private List<FoodItemBean> data = new ArrayList<>();
    private FoodItemBean mFoodBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_food);

//        Utils.setTopBar(this);
//
//        getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0, Utils.getNavigationBarHeight(this));

        init();

//        getFoodInfo();
    }


    public void init() {


        mFoodView = findViewById(R.id.rl_food);

        mTvTitle = findViewById(R.id.text_center);
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText(getString(R.string.text_food));

        mTvBack = findViewById(R.id.text_left);
        mTvBack.setText("");
        mTvBack.setVisibility(View.VISIBLE);
        mTvBack.setOnClickListener(this);
        mTvBack.setBackgroundResource(R.mipmap.jz_back_normal);

        mGson = new Gson();

        mFoodView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mFoodView.setItemAnimator(new DefaultItemAnimator());

        foodInfo();


    }


    public void foodInfo() {
        Utils.showProgress("loading...", this);

        String url = HttpContants.foodinfo;

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("source", "android")
                .add("fc", "msjandroid")
                .add("format", "json")
                .add("lat", "40.0488700000")
                .add("lon", "116.2883300000")
                .add("cityCode", "131")
                .add("token", "9fd272ace51bdb5378e38dca688df194")
                .build();


        final Request request = new Request.Builder()
                .url(url)
                .post(body).build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.dismissProgress();
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {

                    String param = response.body().string();

//                    Log.i("data", "neirong"+param);

                    try {


                        JSONObject obj = new JSONObject(param);

                        JSONObject jsonObject = obj.getJSONObject("data");

                        JSONArray jsonArray = jsonObject.getJSONArray("items");


                        if (jsonArray.length() > 0) {
//                            mFoodBean = new FoodItemBean();
//                            mFoodBean.setPicUrl("https://s1.st.meishij.net/r/141/154/4351141/s4351141_147864003033398.jpg");
//                            mFoodBean.setType("三得利");
//                            mFoodBean.setTypeD("来一只蟹叫醒米饭！起奏陛下，您的御膳到了！恳请打赏我五星好评哦！");
//                            mFoodBean.setAuthorHot("6k+");
//                            mFoodBean.setAuthor("一道家味");
//                            mFoodBean.setAuthorPic("https://s1.c.meishij.net/images/default/tx2_8.png");
//                            mFoodBean.setTitle("鱼香肉丝");
//                            data.add(mFoodBean);


//                            Log.i("data", "jsonArray.length()==" + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject sJs = jsonArray.getJSONObject(i);

                                try {
                                    JSONObject js;

                                    String tag = sJs.getString("tag");

                                    if (TextUtils.equals("视频菜谱", tag)) {
                                        js = sJs.getJSONObject("video_recipe");
                                    } else {
                                        js = sJs.getJSONObject("recipe");
                                    }

                                    JSONArray itemArray = js.getJSONArray("label");

                                    JSONObject itemObj = itemArray.getJSONObject(0);


                                    JSONObject authorObj = js.getJSONObject("author");


//                                    Log.i("data", "加==" + js);

                                    mFoodBean = new FoodItemBean();
                                    mFoodBean.setId(js.getString("id"));
                                    mFoodBean.setPicUrl(js.getString("img"));
                                    mFoodBean.setType(itemObj.getString("name"));
                                    mFoodBean.setTypeD(itemObj.getString("desc"));
                                    mFoodBean.setAuthorHot(js.getString("viewed_amount"));
                                    mFoodBean.setAuthor(authorObj.getString("nickname"));
                                    mFoodBean.setAuthorPic(authorObj.getString("avatar_url"));
                                    mFoodBean.setTitle(js.getString("title"));
                                    data.add(mFoodBean);

                                } catch (Exception e) {

                                }

                                //Log.i("data", "得他====" + data.get(i).getTitle());

                            }
                        }
                        Utils.dismissProgress();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                mAdapter = new FoodAdapter(data, FoodActivity.this);
                                //设置adapter
                                mFoodView.setAdapter(mAdapter);


                                mAdapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        String foodId = data.get(position).getId();

                                       // ToastUtils.showShort(FoodActivity.this, "点击==" + position);

                                        Intent it = new Intent(FoodActivity.this, FoodDetailActivity.class);
                                        it.putExtra("foodId", foodId);
                                        startActivity(it);

                                    }
                                });


                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("data", "ec===" + e.getMessage());
                    }
                }

            }
        });


    }


    public void getFoodInfo() {


        Utils.showProgress("加载中,请稍后", this);

        String param = "format=json&source=android&fc=msjandroid";

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(HttpContants.home_recommend_new + param).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.dismissProgress();
                //失败
                Log.e("data", Thread.currentThread().getName() + "结果  " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.i("data", response.body().string());
                Utils.dismissProgress();
                String param = response.body().string();


                if (response.isSuccessful()) {

                    FoodBean food = mGson.fromJson(param, FoodBean.class);


                    Log.i("data", food.toString() + food.getData().toString());

                    List<nav_items> nav_items = food.getData().getNav_items();
                    if (nav_items.size() > 0) {

                        for (int i = 0; i < nav_items.size(); i++) {


                            Log.i("data", "title==" + nav_items.get(i).getTitle());


                        }
                    }


                }

            }
        });

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.text_left:
                finish();


                break;

        }

    }


}
