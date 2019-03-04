package com.exmart.qiyishow.tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

public class VideoLoadAsync extends MediaAsync<String, String, String>{
	private Activity mActivity;
	private ImageView mImageView;
	private static ListCache mCache;
	private boolean mIsScrolling;
	private int mWidth;
	
	public VideoLoadAsync(Activity activity, ImageView iamgeView, boolean isScrolling, int width){
		mImageView = iamgeView;
		this.mActivity = activity;
		mWidth = width;
		mIsScrolling = isScrolling;
		final int memClass = ((ActivityManager)mActivity.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		final int size = 1024 * 1024 * memClass / 8;
		
		ListRetainCache c = ListRetainCache.getOrCreateRetainableCache();
		mCache = c.mRetainCache;
		if(mCache == null){
			mCache = new ListCache(size, mWidth, mWidth);
			c.mRetainCache = mCache;
		}
		
	}
	@Override
	protected String doInBackground(String... params) {
		String url = params[0].toString();
		return url;
	}
	@Override
	protected void onPostExecute(String result) {
		mCache.loadBitmap(mActivity, result, mImageView, mIsScrolling);
	}

}
