package com.doit.activity.socialutils.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.view.*;
import com.doit.activity.socialutils.base64.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lzh on 2018/4/25.
 */

public class Utils {


    public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels) {
        try {
            if (bitmap == null) {
                return null;
            }
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = roundPixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Bitmap getRoundCornerImage(Bitmap bitmap, int diameter,
                                             boolean isRing) {
        if (isRing) {
            int with = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (with < height) {
                diameter = with;
            } else {
                diameter = height;
            }

            Bitmap output = Bitmap.createBitmap(diameter, diameter,
                    Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, diameter, diameter);
            final float roundPx = diameter / 2;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(roundPx, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        } else {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = diameter;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }

    }


    public static Bitmap getBitmapToBase64(String btStr) {
        try {
            if (null != btStr) {
                byte[] bytes = Base64.decodeBase64(btStr);
                if (bytes != null) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0,
                            bytes.length);
                    return bm;
                }
            }
        } catch (OutOfMemoryError e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }


    public static String getPackageName(Context context) {

        String pkName = null;
        try {

            pkName = context.getPackageName();


        } catch (Exception e) {

        }

        return pkName;

    }


    public static Bitmap decodeFile(File f) {
        Bitmap rt = null;

        if (null == f)
            return rt;
        if (!f.exists()) {
            return rt;
        }

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(f.getAbsolutePath(), o);
        o.inJustDecodeBounds = false;

        // The new size we want to scale to
        final int REQUIRED_SIZE = 200;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;

        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        o.inSampleSize = scale;
        rt = BitmapFactory.decodeFile(f.getAbsolutePath(), o);

        return rt;
    }

    public static String getBase64ToBitmap(Bitmap bitmap) {

        if (bitmap != null) {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, 100, bo);
            byte[] cpsed = bo.toByteArray();
            byte[] ot = Base64.encodeBase64(cpsed);
            return new String(ot);
        }
        return null;
    }

    public static ProgressDialogWhite progressDialog;

    public static void showProgress(String message, Context context) {
        try {


            if (progressDialog == null) {
                progressDialog = new ProgressDialogWhite(context);
            }
            progressDialog.setMessage(message);

            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void dismissProgress() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (Exception e) {

            }
        }
    }

    public static void setTopBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                attributes.flags |= flagTranslucentStatus | flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }

    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getNavigationBarHeight(Context context) {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");//获取NavigationBar的高度
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        } else {
            return 0;
        }
    }

}
