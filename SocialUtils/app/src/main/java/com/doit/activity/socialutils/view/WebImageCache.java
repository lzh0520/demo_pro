package com.doit.activity.socialutils.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.*;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WebImageCache {
	private final static String TAG = WebImageCache.class.getCanonicalName();

	// cache rules
	private static boolean mIsMemoryCachingEnabled = true;
	private static boolean mIsDiskCachingEnabled = true;
	private static int mDefaultDiskCacheTimeoutInSeconds = 7 * 60 * 60 * 24; // 7
																				// day
																				// default

	private Map<String, SoftReference<Bitmap>> mMemCache;

	public WebImageCache() {
		mMemCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public static void setMemoryCachingEnabled(boolean enabled) {
		mIsMemoryCachingEnabled = enabled;
		// Log.e(TAG, "Memory cache " + (enabled ? "enabled" : "disabled") +
		// ".");
	}

	public static void setDiskCachingEnabled(boolean enabled) {
		mIsDiskCachingEnabled = enabled;
		// Log.e(TAG, "Disk cache " + (enabled ? "enabled" : "disabled") + ".");
	}

	public static void setDiskCachingDefaultCacheTimeout(int seconds) {
		mDefaultDiskCacheTimeoutInSeconds = seconds;
		// Log.e(TAG, "Disk cache timeout set to " + seconds + " seconds.");
	}

	public Bitmap getBitmapFromMemCache(String urlString) {
		if (mIsMemoryCachingEnabled) {
			synchronized (mMemCache) {
				SoftReference<Bitmap> bitmapRef = mMemCache.get(urlString);

				if (bitmapRef != null) {
					Bitmap bitmap = bitmapRef.get();

					if (bitmap == null) {
						mMemCache.remove(urlString);
						// Log.e(TAG, "Expiring memory cache for URL " +
						// urlString
						// + ".");
					} else {
						// Log.e(TAG, "Retrieved " + urlString
						// + " from memory cache.");
						return bitmap;
					}
				}
			}
		}

		return null;
	}

	public Bitmap getBitmapFromDiskCache(Context context, String urlString,
                                         int diskCacheTimeoutInSeconds) {
		if (mIsDiskCachingEnabled) {
			Bitmap bitmap = null;
			File path = context.getCacheDir();
			// Log.e("getCacheDir()", path + "");
			InputStream is = null;
			String hashedURLString = hashURLString(urlString);

			// correct timeout
			if (diskCacheTimeoutInSeconds < 0) {
				diskCacheTimeoutInSeconds = mDefaultDiskCacheTimeoutInSeconds;
			}

			File file = new File(path, hashedURLString);

			if (file.exists() && file.canRead()) {
				// check for timeout
				if ((file.lastModified() + (diskCacheTimeoutInSeconds * 1000L)) < new Date()
						.getTime()) {
					file.delete();
				} else {
					try {
						is = new FileInputStream(file);

						bitmap = BitmapFactory.decodeStream(is);
					} catch (Exception ex) {
					} catch (OutOfMemoryError ex) {
					} finally {
						try {
							is.close();
						} catch (Exception ex) {
						}
					}
				}
			}

			return bitmap;
		}

		return null;
	}

	public void addBitmapToMemCache(String urlString, Bitmap bitmap) {
		if (mIsMemoryCachingEnabled) {
			synchronized (mMemCache) {
				mMemCache.put(urlString, new SoftReference<Bitmap>(bitmap));
			}
		}
	}

	public void addBitmapToCache(Context context, String urlString,
                                 Bitmap bitmap) {
		// mem cache
		addBitmapToMemCache(urlString, bitmap);

		// disk cache
		// TODO: manual cache cleanup
		if (mIsDiskCachingEnabled) {
			File path = context.getCacheDir();
			OutputStream os = null;
			String hashedURLString = hashURLString(urlString);

			try {
				// NOWORKY File tmpFile = File.createTempFile("wic.", null);
				File file = new File(path, hashedURLString);
				os = new FileOutputStream(file.getAbsolutePath());

				bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
				os.flush();
				os.close();

				// NOWORKY tmpFile.renameTo(file);
			} catch (Exception ex) {
				// Log.e(TAG, "Could not store " + urlString +
				// " to disk cache: "
				// + ex.toString());
			} finally {
				try {
					os.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	private String hashURLString(String urlString) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest
					.getInstance("MD5");
			digest.update(urlString.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		// fall back to old method
		return urlString.replaceAll("[^A-Za-z0-9]", "#");
	}
}
