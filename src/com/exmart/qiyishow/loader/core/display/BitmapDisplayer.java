package com.exmart.qiyishow.loader.core.display;

import com.exmart.qiyishow.loader.core.assist.LoadedFrom;
import com.exmart.qiyishow.loader.core.imageaware.ImageAware;

import android.graphics.Bitmap;

/**
 * Displays {@link Bitmap} in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware}. Implementations can
 * apply some changes to Bitmap or any animation for displaying Bitmap.<br />
 * Implementations have to be thread-safe.
 *
 * @author 
 * @see imageloader.core.imageaware.ImageAware
 * @see imageloader.core.assist.LoadedFrom
 * @since 1.5.6
 */
public interface BitmapDisplayer {
	/**
	 * Displays bitmap in {@link com.nostra13.universalimageloader.core.imageaware.ImageAware}.
	 * <b>NOTE:</b> This method is called on UI thread so it's strongly recommended not to do any heavy work in it.
	 *
	 * @param bitmap     Source bitmap
	 * @param imageAware {@linkplain com.nostra13.universalimageloader.core.imageaware.ImageAware Image aware view} to
	 *                   display Bitmap
	 * @param loadedFrom Source of loaded image
	 */
	void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom);
}