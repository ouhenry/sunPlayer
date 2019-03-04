package com.exmart.qiyishow.loader.cache.disc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;

import com.exmart.qiyishow.loader.utils.IoUtils;

/**
 * 本地缓存接口
 * @author henry
 *
 */
public interface DiskCache {
	/**
	 * 返回本地缓存根目录
	 * @return 本地缓存根目录
	 */
	File getDirectory();
	/**
	 * 返回缓存图片文件
	 * @param imageUri 原图片url
	 * @return 缓存图片文件 或者  如果图片不在缓存返回null
	 */
	File get(String imageUri);
	/**
	 * 本地保存图片流.
	 * 传入的图片流不能关闭在此方法.
	 * @param imageUri 		原图片URL
	 * @param imageStream 	图片输入流(不能关闭在此方法)
	 * @param listener		保存进度监听器，如果不使用不能忽略
	 * 						{@linkplain com.exmart.qiyishow.loader.core.listener.ImageLoadingProgressListener
	 * 						progress listener} in ImageLoader calls
	 * @return <b>true</b> - 如果图片保存成功; <b>false</b> - 如果图片没有保存在本地缓存
	 * @throws IOException
	 */
	boolean save(String imageUri, InputStream imageStream, IoUtils.CopyListener listener) throws IOException;
	
	/**
	 * 保存图片bitmap在本地缓存
	 * 
	 * @param imageUri	原图片URI
	 * @param bitmap	图片bitmap
	 * @return <b>true</b> - 如果图片保存成功; <b>false</b> - 如果图片没有保存在本地缓存
	 * @throws IOException
	 */
	boolean save(String imageUri, Bitmap bitmap) throws IOException;
	
	/**
	 * 删除图片文件通过传入的URI
	 * 
	 * @param imageUri Image URI
	 * @return <b>true</b> - 如果图片文件删除成功; <b>false</b> - 如果通过传入的URI图片文件不存在或者图片文件不能删除.
	 */
	boolean remove(String imageUri);
	
	/** 关闭本地缓存, 释放资源. */
	void close();
	
	/** 清空本地缓存. */
	void clear();
}
