package com.doit.activity.socialutils.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.doit.activity.socialutils.R;
import com.doit.activity.socialutils.util.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class WebImageManager implements WebImageManagerRetriever.OnWebImageLoadListener {
	private static WebImageManager mInstance = null;

	// TODO: pool retrievers

	// views waiting for an image to load in
	private Map<String, WebImageManagerRetriever> mRetrievers;
	private Map<WebImageManagerRetriever, Set<WebImageView>> mRetrieverWaiters;
	private Set<WebImageView> mWaiters;
	private Bitmap srcBitp = null;

	public static WebImageManager getInstance() {
		if (mInstance == null) {
			mInstance = new WebImageManager();
		}

		return mInstance;
	}

	private WebImageManager() {
		mRetrievers = new HashMap<String, WebImageManagerRetriever>();
		mRetrieverWaiters = new HashMap<WebImageManagerRetriever, Set<WebImageView>>();
		mWaiters = new HashSet<WebImageView>();
	}

	public void downloadURL(Context context, String urlString,
                            final WebImageView view, int pixels, boolean isRing , int diskCacheTimeoutInSeconds) {
		WebImageManagerRetriever retriever = mRetrievers.get(urlString);

		view.setTag(urlString);
		if (retriever == null) {
			retriever = new WebImageManagerRetriever(context, urlString,
					diskCacheTimeoutInSeconds, pixels,isRing, this, 0);
			mRetrievers.put(urlString, retriever);
			mWaiters.add(view);

			Set<WebImageView> views = new HashSet<WebImageView>();
			views.add(view);
			mRetrieverWaiters.put(retriever, views);

			// start!
			retriever.execute();
		} else {
			mRetrieverWaiters.get(retriever).add(view);
			mWaiters.add(view);
		}
	}

	public void downloadURL(Context context, String urlString,
                            final WebImageView view, int pixels, boolean isRing, int diskCacheTimeoutInSeconds,
                            Bitmap bitmap) {
		this.srcBitp = bitmap;
		WebImageManagerRetriever retriever = mRetrievers.get(urlString);

		view.setTag(urlString);
		if (retriever == null) {
			retriever = new WebImageManagerRetriever(context, urlString,
					diskCacheTimeoutInSeconds, pixels,isRing, this, 0);
			mRetrievers.put(urlString, retriever);
			mWaiters.add(view);

			Set<WebImageView> views = new HashSet<WebImageView>();
			views.add(view);
			mRetrieverWaiters.put(retriever, views);

			// start!
			retriever.execute();
		} else {
			mRetrieverWaiters.get(retriever).add(view);
			mWaiters.add(view);
		}
	}

	// ��WebView���bitmap
	public void reportImageLoad(Context context, String urlString,
                                Bitmap bitmap, int roundPixels, boolean isRing, int type) {
		this.srcBitp = bitmap;
		WebImageManagerRetriever retriever = mRetrievers.get(urlString);

		for (WebImageView iWebImageView : mRetrieverWaiters.get(retriever)) {
			if (mWaiters.contains(iWebImageView)) {

				if (((String) iWebImageView.getTag()).equals(urlString)) {

					if (roundPixels == 0) {
						iWebImageView.setImageBitmap(bitmap);
					} else {
						try {
							iWebImageView.setImageBitmap(Utils
									.getRoundCornerImage(bitmap, roundPixels,isRing));
						} catch (OutOfMemoryError e) {
							// TODO: handle exception
						}
					}
					if (type == 1) {
						Animation scaleAnimation = AnimationUtils
								.loadAnimation(context,
										R.anim.tob0_pic_animation);
						iWebImageView.startAnimation(scaleAnimation);
					}
				}

				mWaiters.remove(iWebImageView);
			}

		}

		mRetrievers.remove(urlString);
		mRetrieverWaiters.remove(retriever);
	}

	public void cancelForWebImageView(WebImageView view) {
		// TODO: cancel connection in progress, too
		mWaiters.remove(view);
	}

	@Override
	public void onWebImageLoad(Context context, String url, Bitmap bitmap,
                               int roundPixels, boolean isRing, int type) {
		reportImageLoad(context, url, bitmap, roundPixels,isRing, type);
	}

	@Override
	public void onWebImageError() {
	}
}
