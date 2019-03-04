package com.exmart.qiyishow.loader.cache.memory.impl;

import java.util.Collection;
import java.util.Comparator;

import android.graphics.Bitmap;

import com.exmart.qiyishow.loader.cache.memory.MemoryCache;

/**
 * 装饰 {@link MemoryCache}. 为缓存提供特别需求: 一些不同的keys 去考虑 as
 * equals (using {@link Comparator comparator}). 当你试图通过key存放数据到缓存，entris with "equals" keys 
 * 可能之前从缓存中被删除.<br />
 * <b>NOTE:</b> 在内部使用.通常不需要使用这个类
 * 
 * @author henry
 *
 */
public class FuzzyKeyMemoryCache implements MemoryCache{
	
	private final MemoryCache cache;
	private final Comparator<String> keyComarator;
	
	public FuzzyKeyMemoryCache(MemoryCache cache, Comparator<String> keyComparator) {
		this.cache = cache;
		this.keyComarator = keyComparator;
	}
	

	@Override
	public boolean put(String key, Bitmap value) {
		//搜索 equal key 并删除这个entry
		synchronized (cache) {
			String keyToRmeove = null;
			for (String cacheKey : cache.keys()) {
				if (keyComarator.compare(key, cacheKey) == 0) {
					keyToRmeove = cacheKey;
					break;
				}
			}
			if (keyToRmeove != null) {
				cache.remove(keyToRmeove);
			}
		}
		return cache.put(key, value);
	}

	@Override
	public Bitmap get(String key) {
		return cache.get(key);
	}

	@Override
	public Bitmap remove(String key) {
		return cache.remove(key);
	}

	@Override
	public Collection<String> keys() {
		return cache.keys();
	}

	@Override
	public void clear() {
		cache.clear();
		
	}

}
