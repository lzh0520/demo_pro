package com.doit.activity.socialutils.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lzh on 2018/5/3.
 */

public class ToastUtils extends Toast {


    public ToastUtils(Context context) {
        super(context);
    }

    private static final boolean isShow = true;

    public static void toast(Context context, String msg) {
        if (isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }


}
