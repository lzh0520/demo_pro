package com.doit.activity.socialutils.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.bean.WeatherBean;

import java.util.List;

/**
 * Created by lzh on 2018/4/23.
 * <p>
 * 例：https://blog.csdn.net/htwhtw123/article/details/77917403
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyHolder> {

    private Context mContext;
    private List<WeatherBean> mList;
    private Activity activity;

    public WeatherAdapter(Context context, List<WeatherBean> list) {
        this.mContext = context;
        this.mList = list;
        activity = (Activity) context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_item, parent, false);

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        view.setBackgroundResource(typedValue.resourceId);


        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {


        Log.i("data", "mList.get(position).getWeather()---" + mList.get(position).getWeather());
        holder.weather.setText(mList.get(position).getWeather());
        holder.tem.setText(mList.get(position).getTemperature());
        holder.date.setText(mList.get(position).getDate_y());


        int imgType = mList.get(position).getWeather_icon();

        switch (imgType) {
            case 0:
                holder.imgWeather.setBackgroundResource(R.mipmap.sun_0);
                break;
            case 1:
                holder.imgWeather.setBackgroundResource(R.mipmap.cloudy_1);
                break;
            case 2:
                holder.imgWeather.setBackgroundResource(R.mipmap.yin_2);
                break;
            case 3:
                holder.imgWeather.setBackgroundResource(R.mipmap.zy_3);
                break;
            case 7:
                holder.imgWeather.setBackgroundResource(R.mipmap.rain_7);
                break;
            case 8:
                holder.imgWeather.setBackgroundResource(R.mipmap.mrain_8);
                break;
            case 9:
                holder.imgWeather.setBackgroundResource(R.mipmap.lrain_9);
                break;
            case 14:
                holder.imgWeather.setBackgroundResource(R.mipmap.ssnow14);
                break;
            case 15:
                holder.imgWeather.setBackgroundResource(R.mipmap.msnow_15);
                break;
            case 16:
                holder.imgWeather.setBackgroundResource(R.mipmap.lsnow_16);
                break;
            case 18:
                holder.imgWeather.setBackgroundResource(R.mipmap.wu_18);
                break;
            case 53:
                holder.imgWeather.setBackgroundResource(R.mipmap.m_53);
                break;
            case 301:
                holder.imgWeather.setBackgroundResource(R.mipmap.rain_301);
                break;
            case 302:
                holder.imgWeather.setBackgroundResource(R.mipmap.snow_302);
                break;

            default:
                holder.imgWeather.setBackgroundResource(R.mipmap.yzm_56);
                break;
        }


//        Glide.with(mContext)
//                .load(mList.get(position).getItemPic())
////                .override(360, 315)
//                .into(holder.imgWeather);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView weather;
        TextView tem;
        TextView date;

        ImageView imgWeather;


        @SuppressLint("WrongViewCast")
        public MyHolder(View itemView) {
            super(itemView);


            weather = itemView.findViewById(R.id.text_weather);
            tem = itemView.findViewById(R.id.text_tem);
            date = itemView.findViewById(R.id.text_date);

            imgWeather = itemView.findViewById(R.id.img_weather);
        }


    }


}
