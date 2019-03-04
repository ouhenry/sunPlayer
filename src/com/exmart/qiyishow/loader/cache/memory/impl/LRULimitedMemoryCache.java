package com.exmart.qiyishow.loader.cache.memory.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;

import com.exmart.qiyishow.loader.cache.memory.LimitedMemoryCache;

/**
 * 限制缓存. 提供 Bitmap 存储. 存储bitmaps 的大小不能超过限制大小. 当缓存达到限制大小时从缓存中删除最不常用的bitmap. <br />
 * <br />
 * <b>NOTE:</b> 这个缓存使用强和弱引用存储bitmaps. 强引用 － 限制bitmaps的数量, 弱引用 － 其他所有的bitmaps 缓存.
 * 
 * @author henry
 *
 */

public class LRULimitedMemoryCache extends LimitedMemoryCache {
	
	private static final int INITIAL_CAPACITY = 10;
	private static final float LOAD_FACTOR = 1.1F;
	
	 /** 缓存提供最近使用逻辑 */
	private final Map<String, Bitmap> lruCache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(INITIAL_CAPACITY, LOAD_FACTOR, true));
	
	/**
	 * 
	 * @param sizeLimit Maximum sum of the sizes of the Bitmaps in this cache
	 */
	public LRULimitedMemoryCache(int sizeLimit) {
		super(sizeLimit);
	}
	
	@Override
	public boolean put(String key, Bitmap value) {
		if (super.put(key, value)) {
			lruCache.put(key, value);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Bitmap get(String key) {
		lruCache.get(key);
		return super.get(key);
	}
	@Override
	public Bitmap remove(String key) {
		lruCache.remove(key);
		return super.remove(key);
	}
	
	@Override
	public void clear() {
		lruCache.clear();
		super.clear();
	}

	@Override
	protected int getSize(Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	protected Bitmap removeNext() {
		Bitmap mostLongUsedValue = null;
		synchronized (lruCache) {
			Iterator<Entry<String, Bitmap>> it = lruCache.entrySet().iterator();
			if (it.hasNext()) {
				Entry<String, Bitmap> entry = it.next();
				mostLongUsedValue = entry.getValue();
				it.remove();
			}
		}
		return mostLongUsedValue;
	}

	@Override
	protected Reference<Bitmap> createReference(Bitmap value) {
		return new WeakReference<Bitmap>(value);
	}

}
