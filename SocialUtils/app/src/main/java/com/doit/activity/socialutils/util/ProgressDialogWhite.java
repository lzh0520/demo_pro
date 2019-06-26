package com.doit.activity.socialutils.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import com.doit.activity.socialutils.R;


/**
 * Created by lzh on 2017/9/14.
 */

public class ProgressDialogWhite extends ProgressDialog {
    private Context mContext;
    /**
     * 定义提示文字
     */
    private String strMessage = "";

    public ProgressDialogWhite(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public ProgressDialogWhite(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FontConstants.TEXT_SIZE = mContext.getSharedPreferences(FontConstants.FONTS_SIZE_SP_NAME,
//                Context.MODE_PRIVATE).getInt(FontConstants.FONTS_SIZE_CONSTANT, 0);
//        if (FontConstants.TEXT_SIZE == 0) {
//            mContext.setTheme(R.style.custom_dialog_font_default);
//        } else {
//            mContext.setTheme(R.style.custom_dialog_font_larger);
//        }
        setContentView(R.layout.ehuilib_progress_dialog_white);
//        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                TextView txtTip = (TextView) ProgressDialogWhite.this.findViewById(R.id.text_tips);
                txtTip.setText(strMessage);
            }
        });
    }

    /**
     * 提示文字
     *
     * @see ProgressDialog#setMessage(CharSequence)
     */
    @Override
    public void setMessage(CharSequence message) {

        this.strMessage = message.toString();
        super.setMessage(message);
    }
}

