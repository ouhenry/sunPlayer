package com.exmart.qiyishow.loader.cache.memory;

import java.util.Collection;

import android.graphics.Bitmap;


/**
 * 内存缓存接口
 * @author henry
 *
 */
public interface MemoryCache {
	
	/**
	 * 存储键值对
	 * 
	 * @param key
	 * @param value
	 * @return <b>true></b> - 如果存放值成功， <b>false</> - 值没有存储
	 */
	boolean put(String key, Bitmap value);
	
	/** 通过key 返回值， 如果没有没有值，则返回null； */
	Bitmap get(String key);
	
	Bitmap remove(String key);
	
	/** 返回缓存中所有的key */
	Collection<String> keys();
	/** 删除缓存中所有的项 */
	void clear();
}
