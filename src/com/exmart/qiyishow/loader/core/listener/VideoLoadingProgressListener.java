package com.exmart.qiyishow.loader.core.listener;

/**
 * 监听 视频现在进度
 * 
 * @author henry
 *
 */
public interface VideoLoadingProgressListener {
	
	void onProgressUpdate(String videoUri, int current, int total);

}
