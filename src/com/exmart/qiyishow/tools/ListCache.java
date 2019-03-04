package com.exmart.qiyishow.tools;

import java.util.ArrayList;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.LocalVideoListAdapter;
import com.exmart.qiyishow.ui.home.LocalVideoList;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class ListCache {
	private LruCache<String, Bitmap> mBitmapCache;
	private ArrayList<String> mCurrentTasks;
	private int mMaxWidth;
	private int mMaxHeight;
	
	public ListCache(int size, int maxWidth, int maxHeight){
		mMaxWidth = maxWidth;
		mMaxHeight = maxHeight;
		mBitmapCache = new LruCache<String, Bitmap>(size){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getHeight() * value.getWidth() * 4;
			}
		};
		mCurrentTasks = new ArrayList<String>();
	}
	private void addBitmapToCache(String key, Bitmap bitmap){
		if(getBitmapFromCache(key) == null){
			mBitmapCache.put(key, bitmap);
		}
	}
	private Bitmap getBitmapFromCache(String key){
		return mBitmapCache.get(key);
	}
	/**
	 * 从缓存中获取bitmap
	 */
	public void loadBitmap(Context context, String imageKey, ImageView imageView, boolean isScrolling){
		final Bitmap bitmap = getBitmapFromCache(imageKey);
		if(bitmap != null){
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.color.white);
			if(!isScrolling && !mCurrentTasks.contains(imageKey)){
				if(context instanceof LocalVideoList){
					BitmapLoaderTask task = new BitmapLoaderTask(imageKey, ((LocalVideoList)context).getAdapter());
					task.execute();
				}
				
			}
		}
			
	}
	private class BitmapLoaderTask extends AsyncTask<Void, Void, Bitmap>{
		private LocalVideoListAdapter mAdapter;
		private String mImageKey;
		
		public BitmapLoaderTask(String imageKey, LocalVideoListAdapter adapter){
			mAdapter = adapter;
			mImageKey = imageKey;
		}
		@Override
		protected void onPreExecute() {
			mCurrentTasks.add(mImageKey);
		}
		

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap bitmap = null;
			try {
				bitmap = ThumbnailUtils.createVideoThumbnail(mImageKey, Thumbnails.FULL_SCREEN_KIND);
				if(bitmap != null){
					bitmap = Bitmap.createScaledBitmap(bitmap, mMaxWidth, mMaxHeight, false);
					addBitmapToCache(mImageKey, bitmap);
					return bitmap;
				}
			} catch (Exception e) {
				if(e != null){
					e.printStackTrace();
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			mCurrentTasks.remove(mImageKey);
			if(result != null){
				if(mAdapter != null){
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}
	public void clear(){
		mBitmapCache.evictAll();
	}

}
