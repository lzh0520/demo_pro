package com.doit.activity.socialutils.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.bean.CookStepBean;
import com.doit.activity.socialutils.bean.FoodItemBean;
import com.doit.activity.socialutils.bean.HttpContants;
import com.doit.activity.socialutils.bean.RadarMapData;
import com.doit.activity.socialutils.util.ToastUtils;
import com.doit.activity.socialutils.util.Utils;
import com.doit.activity.socialutils.view.MyProgressBar;
import com.doit.activity.socialutils.view.RadarView;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FoodDetailActivity extends Activity implements View.OnClickListener {

    private String mFoodId;

    private ImageView mImgHead;
    private String mHeadImg;

    private TextView mTvFoodName;
    private String mFoodName;

    private TextView mTvFoodAuthor;
    private String mFoodAuthor;

    private TextView mTvColCount;
    private String mColCount;

    private TextView mTvBroCount;
    private String mBroCount;

    private TextView mTvIngreMore;

    private RadarView mRView;

    private LinearLayout mLiDes;

    private TextView mTvRate;
    private String mRate;

    private TextView mTvType;
    private String mType;

    private TextView mTvTaste;
    private String mTaste;

    private TextView mTvTime;
    private String mTime;

    private TextView mTvDes;
    private String mDescription;

    private TextView mTvMore;

    private TextView mTvFits;
    private String mFits;

    private TextView mTvMain;
    private String mMainIngredient;


    private RadarView mRvFood;
    private RadarMapData radarMapData;

    private String[] mScTitle;
    private Double[] mScore;

    private LinearLayout mLiLabel;
    private LinearLayout mLiA;

    private LinearLayout mLiStep;


    private LinearLayout mLiMain;
    private LinearLayout mLiSecode;


    List titleList = new ArrayList();
    List scoreList = new ArrayList();
    List labelList = new ArrayList();


    List<FoodItemBean> mFoodData = new ArrayList();
    FoodItemBean mBean;


    private MyProgressBar mPbK;
    private MyProgressBar mPbD;

    private String mDesType;
    private String mDesValue;


    private List<Map<String, String>> mMainData = new ArrayList<>();
    private Map<String, String> mMainMap;
    private int mMainSize;

    private List<Map<String, String>> mSecondData = new ArrayList<>();
    private Map<String, String> mSecondMap;
    private int mSecondSize;


    private List<CookStepBean> mStepData = new ArrayList<>();
    private CookStepBean mStepBean;


    private ImageView mImgSmall;
    private LinearLayout mLiBig;

    private List<String> mPicList = new ArrayList<>();

    public FoodDetailActivity() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_food_detail);

        Utils.setTopBar(this);

        init();

    }

    public void init() {


        mFoodId = getIntent().getStringExtra("foodId");

        Log.i("data", "mFoodId==" + mFoodId);

        mImgHead = findViewById(R.id.img_food_detail_head);
        mTvFoodName = findViewById(R.id.text_food_detail_title);
        mTvFoodAuthor = findViewById(R.id.text_food_detail_author);
        mTvColCount = findViewById(R.id.text_food_detail_col_count);
        mTvBroCount = findViewById(R.id.text_food_detail_bro_count);

        mLiDes = findViewById(R.id.linear_food_des);
        mTvRate = findViewById(R.id.text_food_rate);
        mTvType = findViewById(R.id.text_food_type);
        mTvTaste = findViewById(R.id.text_food_taste);
        mTvTime = findViewById(R.id.text_food_cooking_time);
        mTvDes = findViewById(R.id.text_food_summary);
        mTvFits = findViewById(R.id.text_fits);
        mTvMain = findViewById(R.id.text_main);
        mTvIngreMore = findViewById(R.id.text_ingredient_more);
        mTvIngreMore.setOnClickListener(this);


        mImgSmall = findViewById(R.id.img_small_pic);
        mLiBig = findViewById(R.id.hs_big_pic);


        mTvMore = findViewById(R.id.text_food_more);
        mTvMore.setOnClickListener(this);

        mRvFood = findViewById(R.id.rv_food);

        mLiLabel = findViewById(R.id.linear_label);
        mLiA = findViewById(R.id.li_food_label);
        mLiMain = findViewById(R.id.linear_main);
        mLiSecode = findViewById(R.id.linear_second);
        mLiStep = findViewById(R.id.linear_step);

        mPbK = findViewById(R.id.pb_kll);
        mPbK.setFoodColor(getColor(R.color.food_red), getColor(R.color.black));

        mPbD = findViewById(R.id.pb_dbz);
        mPbD.setFoodColor(getColor(R.color.food_blue), getColor(R.color.black));


        foodDetail();


    }


    public void foodDetail() {

        Random random = new Random();
        int page = random.nextInt(100);

        Utils.showProgress("loading...", this);

        String url = HttpContants.foodDetail + mFoodId + "&page=" + page;

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .get().build();

        Log.i("data","美食详情===" +request.toString());


        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.dismissProgress();
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    int resultCode = -1;

                    String param = response.body().string();


                    try {
//                        mLiMain.removeAllViews();
//                        mLiSecode.removeAllViews();
                        mMainData.clear();
                        mSecondData.clear();


                        JSONObject obj = new JSONObject(param);

                        resultCode = obj.getInt("code");

                        if (resultCode == 1) {

                            JSONObject tJs = obj.getJSONObject("data");

                            mFoodName = tJs.getString("title");
                            mColCount = tJs.getString("favor_amount_new");
                            mBroCount = tJs.getString("viewed_amount");
                            mRate = tJs.getString("rate");
                            mType = tJs.getString("technology");
                            mTaste = tJs.getString("taste");
                            mTime = tJs.getString("cooking_time");
                            mDescription = tJs.getString("story_content");
                            mMainIngredient = tJs.getString("amount");

                            //食物预览图
                            JSONObject pJs = tJs.getJSONObject("cover_img");
                            mHeadImg = pJs.getString("big");

                            //制作者
                            JSONObject aJs = tJs.getJSONObject("author");
                            mFoodAuthor = aJs.getString("nickname");

                            //主料
                            if (tJs.has("main_ingredient")) {
                                JSONArray iJsA = tJs.getJSONArray("main_ingredient");
                                if (iJsA.length() > 0) {
                                    for (int i = 0; i < iJsA.length(); i++) {
                                        JSONObject iJs = iJsA.getJSONObject(i);
                                        mMainMap = new HashMap<>();
                                        mMainMap.put("name", iJs.getString("title"));
                                        mMainMap.put("count", iJs.getString("amount"));
                                        mMainData.add(mMainMap);
                                    }
                                }

                            }
                            //辅料
                            if (tJs.has("secondary_ingredient")) {
                                JSONArray sJsA = tJs.getJSONArray("secondary_ingredient");
                                if (sJsA.length() > 0) {
                                    for (int i = 0; i < sJsA.length(); i++) {
                                        JSONObject sJs = sJsA.getJSONObject(i);
                                        mSecondMap = new HashMap<>();
                                        mSecondMap.put("name", sJs.getString("title"));
                                        mSecondMap.put("count", sJs.getString("amount"));
                                        mSecondData.add(mSecondMap);
                                    }
                                }

                            }


                            //步骤
                            if (tJs.has("cook_steps")) {

                                JSONArray stepJsA = tJs.getJSONArray("cook_steps");
                                if (stepJsA.length() > 0) {

                                    for (int i = 0; i < stepJsA.length(); i++) {


                                        mStepBean = new CookStepBean();

                                        JSONObject sJs = stepJsA.getJSONObject(i);

                                        JSONArray pJsA = sJs.getJSONArray("pic_urls");

                                        if (pJsA.length() > 0)
                                            for (int j = 0; j < pJsA.length(); j++) {
                                                JSONObject picJs = pJsA.getJSONObject(j);
                                                if (TextUtils.isEmpty(sJs.getString("id"))) {
                                                    mPicList.add(picJs.getString("big"));
                                                }
                                                mStepBean.setPic(picJs.getString("big"));
                                            }


                                        mStepBean.setTitle(sJs.getString("title"));

                                        if (sJs.has("content")) {
                                            mStepBean.setContent(sJs.getString("content"));
                                        }

                                        if (!TextUtils.isEmpty(sJs.getString("id"))) {
                                            mStepData.add(mStepBean);
                                        }


                                    }

                                }
                            }


                        } else {
                            ToastUtils.showLong(FoodDetailActivity.this, "加载失败,请稍后重试");
                        }


                        Utils.dismissProgress();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                setFoodInfo();
                                foodScore();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }


    public void foodScore() {

        String url = HttpContants.foodScore + mFoodId;

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(url).get().build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {

            int resultCode = -1;


            @Override
            public void onFailure(Call call, IOException e) {
                Utils.dismissProgress();
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (response.isSuccessful()) {

                    String param = response.body().string();

                    try {


                        JSONObject obj = new JSONObject(param);

                        resultCode = obj.getInt("code");

                        JSONObject dJs = obj.getJSONObject("data");


                        mFits = dJs.getString("fits");

                        if (dJs.has("label")) {
                            JSONArray aJs = dJs.getJSONArray("label");
                            if (aJs.length() > 0) {
                                for (int i = 0; i < aJs.length(); i++) {
                                    labelList.add(aJs.get(i));
                                }
                            }
                        }


                        if (dJs.has("special")) {
                            JSONArray sjs = dJs.getJSONArray("special");
                            if (sjs.length() > 0) {
                                for (int i = 0; i < sjs.length(); i++) {

                                    JSONObject js = sjs.getJSONObject(i);
                                    mBean = new FoodItemBean();
                                    mBean.setTitle(js.getString("desc"));
                                    mBean.setType(js.getString("value"));
                                    mBean.setScore(js.getDouble("value100NRV"));
                                    mFoodData.add(mBean);

                                }
                            }

                        }


                        if (dJs.has("chart")) {
                            JSONArray cJs = dJs.getJSONArray("chart");
                            if (cJs.length() > 0) {
                                for (int i = 0; i < cJs.length(); i++) {

                                    JSONObject js = cJs.getJSONObject(i);

                                    titleList.add(js.getString("desc"));
                                    scoreList.add(js.getDouble("value100NRV") + 0.0);

                                }

                            }
                        }

                        Utils.dismissProgress();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (resultCode == 1) {
                                    mLiA.setVisibility(View.VISIBLE);

                                    setOtherInfo();
                                } else {
                                    mLiA.setVisibility(View.GONE);
                                }


                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }


    @SuppressLint("SetTextI18n")
    public void setFoodInfo() {

        if (!TextUtils.isEmpty(mHeadImg)) {
            Glide.with(FoodDetailActivity.this)
                    .load(mHeadImg)
                    .into(mImgHead);
        }
        if (!TextUtils.isEmpty(mFoodName)) {
            mTvFoodName.setText(mFoodName);
        }

        if (!TextUtils.isEmpty(mFoodAuthor)) {
            mTvFoodAuthor.setText(getString(R.string.food_author) + mFoodAuthor);
        }

        if (!TextUtils.isEmpty(mRate)) {
            mLiDes.setVisibility(View.VISIBLE);
            mTvRate.setText(mRate + "星");
        }
        if (!TextUtils.isEmpty(mType)) {
            mTvType.setText(mType);
        }
        if (!TextUtils.isEmpty(mTaste)) {
            mTvTaste.setText(mTaste);
        }
        if (!TextUtils.isEmpty(mTime)) {
            mTvTime.setText(mTime);
        }
        if (!TextUtils.isEmpty(mDescription)) {
            mTvDes.setText(mDescription);
        }

//        int line = mTvDes.getLayout().getLineCount();
//        if (line > 3) {
//            mTvMore.setVisibility(View.VISIBLE);
//        } else {
//            mTvMore.setVisibility(View.GONE);
//        }

        mTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mTvDes.getLineCount() > 3) {
                    mTvDes.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                    mTvDes.setMaxLines(3);
                    mTvDes.setEllipsize(TextUtils.TruncateAt.END);

                    mTvMore.setVisibility(View.VISIBLE);
                } else {
                    mTvMore.setVisibility(View.GONE);
                }

            }
        });


        mTvColCount.setText(getString(R.string.food_col) + mColCount);
        mTvBroCount.setText(getString(R.string.food_bro) + mBroCount);


        mainIngreInfo(0);
        secondIngreInfo(0);

        if (mStepData != null && mStepData.size() > 0) {

            for (int i = 0; i < mStepData.size(); i++) {
                View stepView = LayoutInflater.from(FoodDetailActivity.this)
                        .inflate(R.layout.food_step_item, null);

                TextView title = stepView.findViewById(R.id.text_food_step);
                TextView content = stepView.findViewById(R.id.text_food_step_des);

                ImageView pic = stepView.findViewById(R.id.img_food_step_pic);

                if (!TextUtils.isEmpty(mStepData.get(i).getTitle())) {
                    title.setText(mStepData.get(i).getTitle());
                } else {
                    title.setVisibility(View.GONE);
                }

                title.bringToFront();

                content.setText(mStepData.get(i).getContent());

                Glide.with(FoodDetailActivity.this)
                        .load(mStepData.get(i).getPic())
                        .into(pic);

                mLiStep.addView(stepView);

            }

        }
        getInfo();

    }


    public void mainIngreInfo(int type) {

        if (mMainData != null) {


            if (mMainData.size() > 1 && type == 0) {
                mMainSize = 1;
            } else {
                mMainSize = mMainData.size();
            }

            for (int i = 0; i < mMainSize; i++) {

                View mainView = LayoutInflater.from(FoodDetailActivity.this)
                        .inflate(R.layout.food_ingre_item, null);

                TextView title = mainView.findViewById(R.id.text_ingredient_name);
                TextView count = mainView.findViewById(R.id.text_ingredient_count);

                title.setText(mMainData.get(i).get("name"));
                count.setText(mMainData.get(i).get("count"));

                mLiMain.addView(mainView);
            }


        }
    }

    public void secondIngreInfo(int type) {


        if (mSecondData != null) {

            if (mSecondData.size() > 2 && type == 0) {
                mSecondSize = 2;
            } else {
                mSecondSize = mSecondData.size();
            }

            for (int i = 0; i < mSecondSize; i++) {

                View secondView = LayoutInflater.from(FoodDetailActivity.this)
                        .inflate(R.layout.food_ingre_item, null);

                TextView title = secondView.findViewById(R.id.text_ingredient_name);
                TextView count = secondView.findViewById(R.id.text_ingredient_count);

                title.setText(mSecondData.get(i).get("name"));
                count.setText(mSecondData.get(i).get("count"));

                mLiSecode.addView(secondView);
            }


        }


    }

    public void setOtherInfo() {
        radarMapData = new RadarMapData();
        radarMapData.setCount(6);
        radarMapData.setMainPaintColor(getResources().getColor(R.color.food_green));


        mScTitle = (String[]) titleList.toArray(new String[titleList.size()]);
        radarMapData.setTitles(mScTitle);


        mScore = (Double[]) scoreList.toArray(new Double[scoreList.size()]);
        radarMapData.setValuse(mScore);

        radarMapData.setTextSize(40);
        mRvFood.setData(radarMapData);

        if (labelList != null && labelList.size() > 0) {


            for (int i = 0; i < labelList.size(); i++) {

                View labelView = LayoutInflater.from(FoodDetailActivity.this).inflate(R.layout.food_fits, null);

                TextView tvLabel = labelView.findViewById(R.id.text_food_fits);

                tvLabel.setText(labelList.get(i).toString());

                mLiLabel.addView(labelView);
            }
        }

        if (mFoodData != null && mFoodData.size() > 0) {
//            for (int i = 0; i < mFoodData.size(); i++) {

            mPbK.setFoodText(mFoodData.get(0).getTitle(), mFoodData.get(0).getType());
            mPbD.setFoodText(mFoodData.get(1).getTitle(), mFoodData.get(1).getType());

            mPbK.setProgress(mFoodData.get(0).getScore());
            mPbD.setProgress(mFoodData.get(1).getScore());

//            }
        }


        if (!TextUtils.isEmpty(mFits)) {
            mTvFits.setText(mFits);
        }
        if (!TextUtils.isEmpty(mMainIngredient)) {
            mTvMain.setText(mMainIngredient);
        }


    }

    public void getInfo() {

        try {


            Glide.with(FoodDetailActivity.this)
                    .load(mPicList.get(0))
                    .into(mImgSmall);

            if (mPicList.size() > 1) {

                for (int i = 0; i < mPicList.size(); i++) {


                    final ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(350, 320));
                    imageView.setPadding(30, 0, 30, 0);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setId(i);


                    if (i == 0) {
                        imageView.getBackground().setAlpha(65);

                    }

                    Glide.with(FoodDetailActivity.this)
                            .load(mPicList.get(i))
                            .into(imageView);


                    final int finalI = i;
                    mLiBig.addView(imageView);


                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Glide.with(FoodDetailActivity.this)
                                    .load(mPicList.get(finalI))
                                    .into(mImgSmall);

                            for (int j = 0; j < mLiBig.getChildCount(); j++) {

                                if (finalI == j) {
                                    mLiBig.getChildAt(j).setAlpha(0.3f);
                                } else {
                                    mLiBig.getChildAt(j).setAlpha(1.0f);
                                }
                            }
                        }
                    });

                }
            }


        } catch (Exception e) {
        }


    }


    public void backOnClick(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.text_food_more:
                mTvDes.setMaxLines(100000000);
                mTvMore.setVisibility(View.GONE);
                break;

            case R.id.text_ingredient_more:


                mLiMain.removeAllViews();
                mainIngreInfo(1);

                mLiSecode.removeAllViews();
                secondIngreInfo(1);

                mTvIngreMore.setVisibility(View.GONE);

                break;

        }

    }
}
