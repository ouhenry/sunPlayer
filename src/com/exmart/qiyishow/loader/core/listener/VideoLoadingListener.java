package com.exmart.qiyishow.loader.core.listener;

import com.exmart.qiyishow.loader.core.assist.FailReason;

/**
 * 视频下载进度监听
 * @author henry
 *
 */
public interface VideoLoadingListener {
	
	
	/**
	 * 当视频下载任务开始时回调
	 * 
	 * @param imageUri
	 */
	void onLoaingStarted(String videoUri);
	
	void onLoadingFailed(String videoUri, FailReason failReason);
	
	void onLoadingComplete(String vodeoUri);
	
	void onLoadingCancelled(String videoUri);

}
