package audio.test.com.audiodemo.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import audio.test.com.audiodemo.R;


public class ToastUtils {
    /**
     * 提示框，默认维持200毫秒
     *
     * @param context
     * @param charSequence
     */
    public static void showShort(Context context, CharSequence charSequence) {
        showShort(context, charSequence, 2000);
    }

    /**
     * 提示框，自行设定秒数
     *
     * @param context
     * @param charSequence
     * @param millisecond  持续的毫秒数
     */
    public static void showShort(Context context, CharSequence charSequence, int millisecond) {
//        FontConstants.TEXT_SIZE = context.getSharedPreferences(FontConstants.FONTS_SIZE_SP_NAME,
//                Context.MODE_PRIVATE).getInt(FontConstants.FONTS_SIZE_CONSTANT, 0);
//        if (FontConstants.TEXT_SIZE == 0) {
//            context.setTheme(R.style.custom_dialog_font_default);
//        } else {
//            context.setTheme(R.style.custom_dialog_font_larger);
//        }
        try {
            final AlertDialog dlg = new AlertDialog.Builder(context).create();
            dlg.show();
            dlg.setCanceledOnTouchOutside(false);
            Window window = dlg.getWindow();
            window.setContentView(R.layout.ehuilib_toast_dialog_white);
//            ColorDrawable dw = new ColorDrawable(R.color.black);
//            window.setBackgroundDrawable(dw);
            WindowManager.LayoutParams lp = window.getAttributes();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(ContextCompat.getColor(context, R.color.bg_nav));
            }
            lp.alpha = 1f;
//            lp.dimAmount = 0f;
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            lp.screenOrientation = Gravity.CENTER_HORIZONTAL;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

            TextView txtTip = (TextView) window.findViewById(R.id.txtTip);
            String txtString = charSequence.toString();
            if (!txtString.contains("\n")) {
                if (txtString.matches("[\\u4e00-\\u9fa5]+")) {
                    txtString = Utils.ToDBC(txtString);
                }
            }

            txtTip.setText(txtString);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    dlg.dismiss();
                }
            }, millisecond);
        } catch (Exception ex) {
            Toast.makeText(context, charSequence, Toast.LENGTH_LONG).show();
        }
    }

}
