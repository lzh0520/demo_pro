package com.doit.activity.socialutils.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.adapter.VideoAdapter;
import com.doit.activity.socialutils.bean.HttpContants;
import com.doit.activity.socialutils.bean.VideoBean;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzh on 2018/7/4.
 */

public class VideoPlayActivity extends Activity implements View.OnClickListener {


    private TextView mTvBack;
    private TextView mTvTitle;

    private RecyclerView mReView;
    private VideoAdapter mAdapter;
    //    private ListView mVideoList;
//    private VideoAdapter1 mAdapter;
    private List<VideoBean> mData = new ArrayList();
    private VideoBean mBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();
    }


    public void init() {

        mTvBack = findViewById(R.id.text_left);
        mTvBack.setVisibility(View.VISIBLE);
        mTvBack.setOnClickListener(this);

        mTvTitle = findViewById(R.id.text_center);
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setText(getString(R.string.text_video));

        mReView = findViewById(R.id.review_video);
        mReView.setLayoutManager(new LinearLayoutManager(this));

//        mVideoList = findViewById(R.id.video_listview);

        videoInfo();


    }


    public void videoInfo() {


        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        String url = HttpContants.videoInfo + "?city=北京&endDate=2018-08-03&beginDate=2018-08-02&pageToken=1&catid=10058&apikey=AKIIaapD2jKBfzxcYodK8WWbt9QzX8D73SDB8cTSqmCQVvpzfLUFXw6UKnWd9jqs";

//        RequestBody body = new FormBody.Builder()
//                .add("city", "北京")
//                .add("endDate", "2018-08-03")
//                .add("beginDate", "2018-08-02")
//                .add("pageToken", "1")
//                .add("catid", "10058")
//                .add("apikey", "AKIIaapD2jKBfzxcYodK8WWbt9QzX8D73SDB8cTSqmCQVvpzfLUFXw6UKnWd9jqs")
//                .build();

        final Request request = new Request.Builder()
                .url(url).get().build();

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


                    try {

                        mData.clear();

                        JSONObject obj = new JSONObject(param);

                        JSONArray jsonArray = obj.getJSONArray("data");
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject js = jsonArray.getJSONObject(i);

                                JSONArray vArray = js.getJSONArray("videoUrls");
                                JSONArray imgArray = js.getJSONArray("imageUrls");

//
//                                Log.i("data", "vJS---" + vArray.getString(0));

                                mBean = new VideoBean();
                                mBean.setTitle(js.getString("title"));
                                mBean.setUrl(vArray.getString(0));
                                mBean.setHeadimage(imgArray.getString(0));
                                mData.add(mBean);


                            }

                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mAdapter = new VideoAdapter(VideoPlayActivity.this, mData);
//                                mAdapter.setHasStableIds(true);
                                mReView.setAdapter(mAdapter);


// mAdapter = new VideoAdapter1(VideoPlayActivity.this, mData);
//                                mVideoList.setAdapter(mAdapter);


                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
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
