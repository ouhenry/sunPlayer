package com.exmart.qiyishow.loader.core.listener;

import com.exmart.qiyishow.loader.core.assist.FailReason;

import android.graphics.Bitmap;
import android.view.View;

/**
 * A convenient class to extend when you only want to listen for a subset of all the image loading events. This
 * implements all methods in the {@link com.nostra13.universalimageloader.core.listener.ImageLoadingListener} but does
 * nothing.
 *
 * @author 
 * @since 1.4.0
 */
public class SimpleImageLoadingListener implements ImageLoadingListener {
	@Override
	public void onLoadingStarted(String imageUri, View view) {
		// Empty implementation
	}

	@Override
	public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
		// Empty implementation
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		// Empty implementation
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		// Empty implementation
	}
}
