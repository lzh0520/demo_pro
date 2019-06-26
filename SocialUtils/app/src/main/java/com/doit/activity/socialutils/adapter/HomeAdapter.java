package com.doit.activity.socialutils.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.bean.HomeBean;
import com.doit.activity.socialutils.interfaces.OnItemClickListener;

import java.util.List;

/**
 * Created by lzh on 2018/4/23.
 * <p>
 * 例：https://blog.csdn.net/htwhtw123/article/details/77917403
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyHolder> {

    private Context mContext;
    private List<HomeBean> mList;
    private OnItemClickListener itemClickListener;
    private Activity activity;

    private RequestManager glideRequest;

    public HomeAdapter(Context context, List<HomeBean> list) {
        this.mContext = context;
        this.mList = list;
        activity = (Activity) context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.home_item, parent, false);

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        view.setBackgroundResource(typedValue.resourceId);


        MyHolder holder = new MyHolder(view, itemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        holder.title.setText(mList.get(position).getTitle());
        holder.author.setText(mList.get(position).getAuthor());

        String cl = mList.get(position).getClassic();
        if (cl.contains("@")) {
            holder.classic.setText(cl.substring(0,cl.indexOf("@")));
        } else {
            holder.classic.setText(cl);
        }

        holder.time.setText(mList.get(position).getTime());

//https://blog.csdn.net/bskfnvjtlyzmv867/article/details/71106123


        Glide.with(mContext)
                .load(mList.get(position).getItemPic())
//                .override(360, 315)
                .into(holder.pic);


        //圆形图片
//        glideRequest = Glide.with(mContext);
//        glideRequest.load(mList.get(position).getItemPic())
//                .transform(new GlideCircleTransform(mContext)).into(holder.pic);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView author;
        TextView classic;
        TextView time;

        ImageView pic;


        @SuppressLint("WrongViewCast")
        public MyHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            itemClickListener = listener;
            // 为ItemView添加点击事件
            itemView.setOnClickListener(this);

            title = itemView.findViewById(R.id.text_title);
            author = itemView.findViewById(R.id.text_author);
            classic = itemView.findViewById(R.id.text_classic);
            time = itemView.findViewById(R.id.text_time);

            pic = itemView.findViewById(R.id.img_itempic);
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
