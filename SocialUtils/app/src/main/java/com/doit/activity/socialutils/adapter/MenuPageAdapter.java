package com.doit.activity.socialutils.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by lzh on 2018/4/23.
 */

public class MenuPageAdapter extends PagerAdapter {

    private Context mContext;

    public MenuPageAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 0;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}
