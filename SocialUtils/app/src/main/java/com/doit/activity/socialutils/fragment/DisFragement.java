package com.doit.activity.socialutils.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.activity.ContentActivity;
import com.doit.activity.socialutils.adapter.HomeAdapter;
import com.doit.activity.socialutils.bean.HomeBean;
import com.doit.activity.socialutils.interfaces.OnItemClickListener;
import com.doit.activity.socialutils.util.Utils;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lzh on 2018/4/19.
 */

public class DisFragement extends Fragment {


    private View mView;


    private RecyclerView mReView;
    private HomeAdapter mAdapter;
    private List<HomeBean> mHomeData = new ArrayList<HomeBean>();
    private HomeBean mBean;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String mUrl = "http://qt.qq.com/lua/lol_news/recommend_refresh?";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        mView = inflater.inflate(R.layout.fragment_dis, null);


        init();

        return mView;
    }


    public void init() {


        mReView = (RecyclerView) mView.findViewById(R.id.review_discover);
        mReView.setLayoutManager(new LinearLayoutManager(getActivity()));


        getInfo();

    }


    public void getInfo() {


        Utils.showProgress("加载中,请稍后", getActivity());

        String param = "cid=" + "12" + "&plat=" + "android" + "&version=" + "9774";

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(mUrl + param).build();

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


                    try {

                        JSONObject obj = new JSONObject(param);

                        Log.i("data", "obj---" + obj);

                        JSONArray jsonArray = obj.getJSONArray("update_list");


                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject js = jsonArray.getJSONObject(i);

                                mBean = new HomeBean();
                                mBean.setTitle(js.getString("title"));
                                mBean.setAuthor(js.getString("summary"));
                                mBean.setClassic(js.getString("author"));
                                mBean.setTime(js.getString("publication_date"));
                                mBean.setUrl(js.getString("article_url"));
                                mBean.setItemPic(js.getString("image_url_big"));
                                mHomeData.add(mBean);

                                Log.i("data", "mHo----" + mHomeData);

                            }

                        }


                        if (isAdded()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter = new HomeAdapter(getActivity(), mHomeData);
                                    mReView.setAdapter(mAdapter);
                                    mAdapter.setOnItemClickListener(new OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int postion) {

                                            String url = mHomeData.get(postion).getUrl();


                                            Intent it = new Intent(getActivity(), ContentActivity.class);
                                            it.putExtra("news_url", url);
                                            startActivity(it);

                                          //  startActivity(new Intent(getActivity(), VideoPlayActivity.class));
//                                        ToastUtils.showShort(getActivity(), "点击" + postion);
                                        }
                                    });

                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

}
