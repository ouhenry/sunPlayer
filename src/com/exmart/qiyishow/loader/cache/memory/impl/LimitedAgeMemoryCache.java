package com.exmart.qiyishow.loader.cache.memory.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

import com.exmart.qiyishow.loader.cache.memory.MemoryCache;

/**
 * 缓存限制, 如果bitmap 超过时间就从缓存中删除
 * 
 * @author zheyi
 *
 */
public class LimitedAgeMemoryCache implements MemoryCache{
	
	private final MemoryCache cache;
	
	private final long maxAge;
	private final Map<String, Long> loadingDates = Collections.synchronizedMap(new HashMap<String, Long>());
	
	public LimitedAgeMemoryCache(MemoryCache cache, long maxAge) {
		this.cache = cache;
		this.maxAge = maxAge * 1000;//转化为毫秒
	}

	@Override
	public boolean put(String key, Bitmap value) {
		boolean putSuccesfully = cache.put(key, value);
		if (putSuccesfully) {
			loadingDates.put(key, System.currentTimeMillis());
		}
		return putSuccesfully;
	}

	@Override
	public Bitmap get(String key) {
		Long loadingDate = loadingDates.get(key);
		if (loadingDate != null && System.currentTimeMillis() - loadingDate > maxAge) {
			cache.remove(key);
			loadingDates.remove(key);
		}
		return cache.get(key);
	}

	@Override
	public Bitmap remove(String key) {
		loadingDates.remove(key);
		return cache.remove(key);
	}

	@Override
	public Collection<String> keys() {
		return cache.keys();
	}

	@Override
	public void clear() {
		cache.clear();
		loadingDates.clear();
	}
}
