package com.doit.activity.socialutils.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import com.doit.activity.socialutils.activity.LifeApplication;
import com.doit.activity.socialutils.util.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by lzh on 2018/6/28.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LifeApplication.mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.i("data", resp.errStr);
        Log.i("data", "错误码 : " + resp.errCode + "");
        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) ToastUtils.showShort(this, "分享失败");
                else ToastUtils.showShort(this, "登录失败");
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        String code = ((SendAuth.Resp) resp).code;
                        Log.i("data", "code = " + code);


                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        ToastUtils.showShort(this, "微信分享成功");
                        finish();
                        break;
                }
                break;
        }
    }
}
