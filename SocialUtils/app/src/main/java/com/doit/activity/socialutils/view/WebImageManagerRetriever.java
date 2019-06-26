package com.doit.activity.socialutils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.doit.activity.socialutils.util.Utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class WebImageManagerRetriever extends AsyncTask<Void, Void, Bitmap> {
	private final static String TAG = WebImageManagerRetriever.class
			.getSimpleName();

	// cache
	public static WebImageCache mCache;

	// what we're looking for
	private Context mContext;
	private String mURLString;
	private int mroundPixels = 0;
	private int mDiskCacheTimeoutInSeconds;
	private OnWebImageLoadListener mListener;
	private int imageFromType = 0;
	private boolean isRing = false;

	static {
		mCache = new WebImageCache();
	}

	public WebImageManagerRetriever(Context context, String urlString,
                                    int diskCacheTimeoutInSeconds, int roundPixels,
                                    OnWebImageLoadListener listener) {
		mContext = context;
		mURLString = urlString;
		mDiskCacheTimeoutInSeconds = diskCacheTimeoutInSeconds;
		mroundPixels = roundPixels;
		mListener = listener;
	}

	public WebImageManagerRetriever(Context context, String urlString,
                                    int diskCacheTimeoutInSeconds, int roundPixels, boolean isRing,
                                    OnWebImageLoadListener listener, int imageFromType) {
		mContext = context;
		mURLString = urlString;
		mDiskCacheTimeoutInSeconds = diskCacheTimeoutInSeconds;
		mroundPixels = roundPixels;
		mListener = listener;
		this.imageFromType = imageFromType;
		this.isRing = isRing;
	}

	// �ӻ����м��أ����û����������ȡ
	@Override
	protected Bitmap doInBackground(Void... params) {
		// check mem cache first
		Bitmap bitmap = mCache.getBitmapFromMemCache(mURLString);

		// check disk cache first
		if (bitmap == null) {
			try {
				bitmap = mCache.getBitmapFromDiskCache(mContext, mURLString,
						mDiskCacheTimeoutInSeconds);
			} catch (Exception e) {
				// TODO: handle exception
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
			}

			mCache.addBitmapToMemCache(mURLString, bitmap);
		}

		if (bitmap == null) {
			InputStream is = null;
			FlushedInputStream fis = null;

			try {
				URL url = new URL(mURLString);
				URLConnection conn = url.openConnection();

				is = conn.getInputStream();
				fis = new FlushedInputStream(is);

				try {
					bitmap = BitmapFactory.decodeStream(fis);
					bitmap = Utils.getRoundCornerImage(bitmap, mroundPixels,isRing);
				} catch (OutOfMemoryError e) {
					// TODO: handle exception
				}

				// cache
				if (bitmap != null) {
					mCache.addBitmapToCache(mContext, mURLString, bitmap);
				}

				imageFromType = 1;
			} catch (Exception ex) {
				// Log.e(TAG, "Error loading image from URL " + mURLString +
				// ": "
				// + ex.toString());
			} finally {
				try {
					is.close();
				} catch (Exception ex) {
				}
			}
		}

		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		// complete!
		if (null != mListener) {
			if (null == bitmap) {
				mListener.onWebImageError();
			} else {
				mListener.onWebImageLoad(mContext, mURLString, bitmap,
						mroundPixels,isRing , imageFromType);
			}
		}
	}

	public static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;

			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);

				if (bytesSkipped == 0L) {
					int b = read();

					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}

				totalBytesSkipped += bytesSkipped;
			}

			return totalBytesSkipped;
		}
	}

	public interface OnWebImageLoadListener {
		public void onWebImageLoad(Context context, String url, Bitmap bitmap,
                                   int roundPixels, boolean isRing, int type);

		public void onWebImageError();
	}
}
