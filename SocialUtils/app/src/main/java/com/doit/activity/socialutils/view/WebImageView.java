package com.doit.activity.socialutils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


@SuppressLint("AppCompatCustomView")
public class WebImageView extends ImageView {

    public WebImageView(Context context) {
        super(context);
    }

    public WebImageView(Context context, AttributeSet attSet) {
        super(context, attSet);
    }

    public static void setMemoryCachingEnabled(boolean enabled) {
        WebImageCache.setMemoryCachingEnabled(enabled);
    }

    public static void setDiskCachingEnabled(boolean enabled) {
        WebImageCache.setDiskCachingEnabled(enabled);
    }

    public static void setDiskCachingDefaultCacheTimeout(int seconds) {
        WebImageCache.setDiskCachingDefaultCacheTimeout(seconds);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDetachedFromWindow() {
        // cancel loading if view is removed
        super.onDetachedFromWindow();
        cancelCurrentLoad();
    }

    public void setImageWithURL(Context context, String urlString,
                                Drawable placeholderDrawable, int diskCacheTimeoutInSeconds) {
        final WebImageManager mgr = WebImageManager.getInstance();

        cancelCurrentLoad();
        setImageDrawable(placeholderDrawable);

        if (urlString != null) {
//			mgr.downloadURL(context, urlString, WebImageView.this,
//					diskCacheTimeoutInSeconds, 0);
            mgr.downloadURL(context, urlString, WebImageView.this, 0, false, diskCacheTimeoutInSeconds);
        }
    }

    public void setImageWithURL(Context context, String urlString, int resId,
                                int diskCacheTimeoutInSeconds) {
        final WebImageManager mgr = WebImageManager.getInstance();

        cancelCurrentLoad();
        setImageResource(resId);

        if (urlString != null) {

            mgr.downloadURL(context, urlString, WebImageView.this, 0, false,
                    diskCacheTimeoutInSeconds);
        }
    }

    public void setRoundImageWithURL(Context context, String urlString,
                                     int resId, int pixels, boolean isRing) {
        final WebImageManager mgr = WebImageManager.getInstance();

        // Animation scaleAnimation = AnimationUtils.loadAnimation(context,
        // R.anim.tob0_pic_animation);

        cancelCurrentLoad();
        setImageResource(resId);
        // startAnimation(scaleAnimation);

        if (urlString != null) {

            mgr.downloadURL(context, urlString, WebImageView.this, pixels, isRing,
                    3600 * 24 * 3);
        }
    }

    public void setRoundImageWithURL(Context context, String urlString,
                                     int resId, int pixels, boolean isRing, Bitmap bitmap) {
        final WebImageManager mgr = WebImageManager.getInstance();

        // Animation scaleAnimation = AnimationUtils.loadAnimation(context,
        // R.anim.tob0_pic_animation);

        cancelCurrentLoad();
        setImageResource(resId);
        // startAnimation(scaleAnimation);

        if (urlString != null) {

            mgr.downloadURL(context, urlString, WebImageView.this, pixels, isRing,
                    3600 * 24 * 3, bitmap);
        }
    }

    public void setImageWithURL(Context context, String urlString, int resId) {
        setImageWithURL(context, urlString, resId, 3600 * 24 * 3);
    }

    // public void setImageWithURL(Context context, String urlString,
    // Drawable placeholderDrawable) {
    // setImageWithURL(context, urlString, placeholderDrawable, -1);
    // }

    // public void setImageWithURL2(final Context context, final String
    // urlString,
    // int diskCacheTimeoutInSeconds) {
    // setImageWithURL(context, urlString, null, diskCacheTimeoutInSeconds);
    // }

    public void setImageWithURL(final Context context, final String urlString) {
        setImageWithURL(context, urlString, null, -1);
    }

    public void cancelCurrentLoad() {
        WebImageManager mgr = WebImageManager.getInstance();

        // cancel any existing request
        mgr.cancelForWebImageView(this);
    }

}
