package com.doit.activity.socialutils.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.doit.activity.socialutils.GlideApp;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.bean.FoodItemBean;
import com.doit.activity.socialutils.interfaces.OnItemClickListener;
import com.doit.activity.socialutils.view.CenterCropRoundCornerTransform;

import java.util.List;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * Created by lzh on 2018/12/25.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.BeautyViewHolder> {

    private Context mContext;

    private List<FoodItemBean> data;

    private OnItemClickListener itemClickListener;


    public FoodAdapter(List<FoodItemBean> data, Context context) {
        this.data = data;
        this.mContext = context;

    }

    @Override
    public BeautyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        view.setBackgroundResource(typedValue.resourceId);


        BeautyViewHolder holder = new BeautyViewHolder(view, itemClickListener);


        return holder;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onBindViewHolder(final BeautyViewHolder holder, int position) {
        //将数据设置到item上
        FoodItemBean foodBean = data.get(position);


//        RequestOptions requestOptions = new RequestOptions()
//                .signature(new ObjectKey(System.currentTimeMillis()))
//                .centerCrop();
//        Glide.with(mContext).load(foodBean.getPicUrl())
//                .apply(requestOptions)
//                .into(holder.image);


        RequestOptions options = bitmapTransform(new CenterCropRoundCornerTransform(4));
        GlideApp.with(mContext)
                .load(foodBean.getPicUrl())
                .apply(options)
                .placeholder(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                .error(errorid)
                .into(holder.image);


        Glide.with(mContext).load(foodBean.getAuthorPic()).apply(bitmapTransform(new CircleCrop())).into(holder.imageAuthor);


//        ViewGroup.LayoutParams params = holder.beautyImage.getLayoutParams();
//        if (position%2 ==0) {
//            params.height =450;
//        }
//        holder.beautyImage.setLayoutParams(params);

        Log.i("data", "--=-=" + foodBean.getPicUrl());
        holder.nameTv.setText(foodBean.getTitle());

        String type = foodBean.getType() + " | " + foodBean.getTypeD();
        holder.typeTv.setText(type);


        holder.authorTv.setText(foodBean.getAuthor());
        holder.authorHotTv.setText(foodBean.getAuthorHot());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BeautyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        ImageView imageAuthor;

        TextView nameTv;
        TextView typeTv;
        TextView authorTv;
        TextView authorHotTv;

        public BeautyViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            itemClickListener = listener;
            itemView.setOnClickListener(this);

            image = itemView.findViewById(R.id.img_food);
            imageAuthor = itemView.findViewById(R.id.img_food_author);


            nameTv = itemView.findViewById(R.id.name_item);
            typeTv = itemView.findViewById(R.id.type_item);
            authorTv = itemView.findViewById(R.id.text_author_name);
            authorHotTv = itemView.findViewById(R.id.text_author_hot);
        }

        @Override
        public void onClick(View v) {

            itemClickListener.onItemClick(v, getPosition());

        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

}