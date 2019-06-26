package com.doit.activity.socialutils.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.bean.VideoBean;
import com.doit.activity.socialutils.interfaces.OnItemClickListener;
import com.doit.activity.socialutils.util.Utils;
import com.doit.activity.socialutils.video.JZVideoPlayerStandard;
import com.doit.activity.socialutils.video.MyJZVideoPlayerStandard;

import java.util.List;

/**
 * Created by lzh on 2018/4/23.
 * <p>
 * 例：https://blog.csdn.net/htwhtw123/article/details/77917403
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyHolder> {

    private Context mContext;
    private List<VideoBean> mList;
    private OnItemClickListener itemClickListener;
    private Activity activity;

    private RequestManager glideRequest;
    Handler handler;
    String headimage;

    public VideoAdapter(Context context, List<VideoBean> list) {
        this.mContext = context;
        this.mList = list;
        activity = (Activity) context;


    }


    @SuppressLint("HandlerLeak")
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        view.setBackgroundResource(typedValue.resourceId);


        MyHolder holder = new MyHolder(view, itemClickListener);


        return holder;
    }


    @SuppressLint("HandlerLeak")
    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {


        final String videoUrl = mList.get(position).getUrl();
        String title = mList.get(position).getTitle();
        headimage = mList.get(position).getHeadimage();


//        holder.title.setText(title);
        holder.jz_video.setUp(videoUrl,
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, title);

        handler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Bundle b = msg.getData();
                    Bitmap bt = b.getParcelable("pic");
                    Log.i("data", "bt-----" + bt);

//                    if (headimage.equals(holder.jz_video.getTag())) {
                        holder.jz_video.thumbImageView.setImageBitmap(bt);
//                    }


                }

            }


        };

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
                        holder.jz_video.setTag(headimage);
                    }

                } catch (Exception e) {
                }
            }
        }).start();


//https://blog.csdn.net/bskfnvjtlyzmv867/article/details/71106123

//        Glide.with(mContext)
//                .load(mList.get(position).getItemPic())
////                .override(360, 315)
//                .into(holder.pic);


        //圆形图片
//        glideRequest = Glide.with(mContext);
//        glideRequest.load(mList.get(position).getItemPic())
//                .transform(new GlideCircleTransform(mContext)).into(holder.pic);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MyJZVideoPlayerStandard jz_video;

        TextView title;
        TextView author;
        TextView time;

//        ImageView pic;


        @SuppressLint("WrongViewCast")
        public MyHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            itemClickListener = listener;
            // 为ItemView添加点击事件
            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.text_video_title);

            jz_video = (MyJZVideoPlayerStandard) itemView.findViewById(R.id.videoplay_content);


//            author = itemView.findViewById(R.id.text_author);
//            time = itemView.findViewById(R.id.text_time);

//            pic = itemView.findViewById(R.id.img_itempic);
        }


        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getPosition());
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }


}
