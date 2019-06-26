package com.doit.activity.socialutils.fragment;

import android.content.Context;
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
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.activity.ContentActivity;
import com.doit.activity.socialutils.adapter.HomeAdapter;
import com.doit.activity.socialutils.bean.HomeBean;
import com.doit.activity.socialutils.interfaces.OnItemClickListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
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

public class HomepageFragement extends Fragment implements OnBannerListener {


    private View mView;


    private RecyclerView mReView;
    private HomeAdapter mAdapter;
    private List<HomeBean> mHomeData = new ArrayList<HomeBean>();
    private HomeBean mBean;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String mUrl = "http://v.juhe.cn/toutiao/index?";


    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_home, null);

        init();

        return mView;
    }


    public void init() {

        mReView = (RecyclerView) mView.findViewById(R.id.review_home);
        mReView.setLayoutManager(new LinearLayoutManager(getActivity()));
        banner = (Banner) mView.findViewById(R.id.banner_top);


        getInfo();
        initView();

    }


    private void initView() {

        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1529401531455&di=260719a1e43d9431fc8575537219e118&imgtype=0&src=http%3A%2F%2Fpic2.16pic.com%2F00%2F52%2F03%2F16pic_5203018_b.jpg");
        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1529401580629&di=d81805b68e32e19adb1dba62c0bd1fc5&imgtype=0&src=http%3A%2F%2Feasyread.ph.126.net%2Fk1x6UWVjsxA51smNasBN5w%3D%3D%2F7917109342107532255.jpg");
        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1529401725878&di=74ff9fa0d40b97557bf89c013815cc78&imgtype=0&src=http%3A%2F%2Fi2.265g.com%2Fimages%2F201411%2F201411191224194138.jpg");
        list_path.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1529402130006&di=95396b44a3012f0158a3b25cbd2b8dde&imgtype=0&src=http%3A%2F%2Fp2.qhimgs4.com%2Ft01c80d85281a0e899a.jpg");
        list_title.add("海贼王");
        list_title.add("火影忍者");
        list_title.add("灌篮高手");
        list_title.add("龙珠超");


        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();


    }

    @Override
    public void OnBannerClick(int position) {

    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }


    public void getInfo() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("type", "top")
                .add("key", "2adeafa2f054d53c61b4f54d7592b4c1")
                .build();

        final Request request = new Request.Builder()
                .url(mUrl)
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

                    Log.i("data", param);

                    try {

                        JSONObject obj = new JSONObject(param);

                        JSONObject object = obj.getJSONObject("result");

                        JSONArray jsonArray = object.getJSONArray("data");


                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject js = jsonArray.getJSONObject(i);

                                mBean = new HomeBean();
                                mBean.setTitle(js.getString("title"));
                                mBean.setAuthor(js.getString("author_name"));
                                mBean.setClassic(js.getString("category"));
                                mBean.setTime(js.getString("date"));
                                mBean.setUrl(js.getString("url"));
                                mBean.setItemPic(js.getString("thumbnail_pic_s"));
                                mHomeData.add(mBean);


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
                                        public void onItemClick(View view, int position) {

                                            String url = mHomeData.get(position).getUrl();


                                            Intent it = new Intent(getActivity(), ContentActivity.class);
                                            it.putExtra("news_url", url);
                                            startActivity(it);

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
