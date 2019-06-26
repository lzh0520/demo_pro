package com.doit.activity.socialutils.interfaces;

import android.graphics.Bitmap;

public interface OnPictureIntentResultListener {
	
	public void onPictureIntentResult(Bitmap bitmap);
	
	public void OnException(Exception ex);
}
