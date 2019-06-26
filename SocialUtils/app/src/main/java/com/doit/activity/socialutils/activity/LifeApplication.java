package com.doit.activity.socialutils.activity;

import android.app.Application;
import com.alivc.player.AliVcMediaPlayer;
import com.tencent.mm.opensdk.openapi.IWXAPI;

/**
 * Created by lzh on 2018/6/28.
 */

public class LifeApplication extends Application {

    private static LifeApplication mInstance = null;

    public static IWXAPI mWxApi;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AliVcMediaPlayer.init(getApplicationContext());
    }


    public static LifeApplication getInstance() {

        if (null == mInstance) {
            mInstance = new LifeApplication();
        }
        return mInstance;

    }

}
