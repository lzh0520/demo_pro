package com.doit.activity.socialutils.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.bean.VideoBean;
import com.doit.activity.socialutils.util.Utils;
import com.doit.activity.socialutils.video.JZVideoPlayerStandard;
import com.doit.activity.socialutils.video.MyJZVideoPlayerStandard;

import java.util.List;

/**
 * Created by lzh on 2018/8/3.
 */

public class VideoAdapter1 extends BaseAdapter {

    private Context mContext;
    private List<VideoBean> mData;
    private MyHolder myHolder;
    String headimage;
    Handler handler;

    public VideoAdapter1(Context context, List<VideoBean> data) {
        this.mContext = context;
        this.mData = data;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        myHolder = new MyHolder();
        view = LayoutInflater.from(mContext).inflate(R.layout.video_item, null);


        myHolder.title = view.findViewById(R.id.text_video_title);

        myHolder.jz_video = (MyJZVideoPlayerStandard) view.findViewById(R.id.videoplay_content);


//            author = itemView.findViewById(R.id.text_author);
//            time = itemView.findViewById(R.id.text_time);


        final String videoUrl = mData.get(i).getUrl();
        String title = mData.get(i).getTitle();
        headimage = mData.get(i).getHeadimage();


        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    if (headimage != null) {
                        Bitmap bt = Utils.returnBitmap(headimage);

                        Message msg = handler.obtainMessage();
                        msg.what = 0;
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("pic", bt);
                        msg.setData(bundle);
                        msg.sendToTarget();
                        myHolder.jz_video.setTag(headimage);
                    }

                } catch (Exception e) {
                }
            }
        }).start();


        handler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Bundle b = msg.getData();
                    Bitmap bt = b.getParcelable("pic");
                    Log.i("data", "bt-----" + bt);

                    if (headimage.equals(myHolder.jz_video.getTag())) {
                        myHolder.jz_video.thumbImageView.setImageBitmap(bt);
                    }


                }

            }


        };


//        holder.title.setText(title);
        myHolder.jz_video.setUp(videoUrl,
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, title);


        return view;
    }


    class MyHolder {

        MyJZVideoPlayerStandard jz_video;

        TextView title;
        TextView author;
        TextView time;

    }
}
