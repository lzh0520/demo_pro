package com.doit.activity.socialutils.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.activity.AliVideoActivity;
import com.doit.activity.socialutils.activity.FoodActivity;
import com.doit.activity.socialutils.activity.VideoPlayActivity;
import com.doit.activity.socialutils.activity.WeatherActivity;
import com.doit.activity.socialutils.view.GlideBlurTransformation;
import com.doit.activity.socialutils.view.WebImageView;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by lzh on 2018/4/19.
 */

public class MineFragement extends Fragment implements  View.OnClickListener {


    private View mView;


    private LinearLayout mShowView;
    private LinearLayout mTitleView;


    private TextView mTvWeather;
    private TextView mTvVideo;
    private TextView mTvFood;
    private TextView mTvAliVideo;

    private ImageView mImgBg;

    private WebImageView mImgHead;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.layout_mine, null);

        initView();

        return mView;
    }

    /**
     * initView
     */

    public void initView() {


        mShowView = mView.findViewById(R.id.linear_sview);
        mTitleView = mView.findViewById(R.id.top_title);


        mTvWeather = mView.findViewById(R.id.text_weather);
        mTvWeather.setOnClickListener(this);

        mTvVideo = mView.findViewById(R.id.text_video);
        mTvVideo.setOnClickListener(this);

        mTvFood = mView.findViewById(R.id.text_food);
        mTvFood.setOnClickListener(this);

        mTvAliVideo = mView.findViewById(R.id.text_ali_video);
        mTvAliVideo.setOnClickListener(this);

        mImgBg = mView.findViewById(R.id.img_tipbg);
        mImgHead = mView.findViewById(R.id.img_head);




        Glide.with(getActivity())
                .load("https://hbimg.huabanimg.com/5d04392d71ca46de2519626dc543957bccccb72217f66-zRkOqO_fw658")
                .apply(bitmapTransform(new CircleCrop()))
                .into(mImgHead);


        Glide.with(getActivity())
                .load("https://hbimg.huabanimg.com/5d04392d71ca46de2519626dc543957bccccb72217f66-zRkOqO_fw658")
                .apply(RequestOptions.bitmapTransform(new GlideBlurTransformation(getActivity())))
                .into(mImgBg);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.text_weather:

                startActivity(new Intent(getActivity(), WeatherActivity.class));

                break;

            case R.id.text_video:

                startActivity(new Intent(getActivity(), VideoPlayActivity.class));

                break;
            case R.id.text_food:

                startActivity(new Intent(getActivity(), FoodActivity.class));

                break;
            case R.id.text_ali_video:

                startActivity(new Intent(getActivity(), AliVideoActivity.class));

                break;


        }

    }
}
