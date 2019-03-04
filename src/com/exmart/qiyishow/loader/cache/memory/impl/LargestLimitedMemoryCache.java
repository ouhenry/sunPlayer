package com.exmart.qiyishow.loader.cache.memory.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.graphics.Bitmap;

import com.exmart.qiyishow.loader.cache.memory.LimitedMemoryCache;

/**
 * 限制缓存. 提供bitmaps存储. 存储的bitmaps的大小不能超过限制. 当缓存达到限制大小时删除缓存中最大的bitmap.<br />
 * <br />
 * <b>NOTE:</b> 这个缓存使用强和弱引用存储bitmaps. 强引用 － 限制bitmaps的数量, 弱引用 － 其他所有的bitmaps 缓存.
 * 
 * @author henry
 *
 */
public class LargestLimitedMemoryCache extends LimitedMemoryCache{
	
	/**
	 * 包含强引用 存储对象和objects的大小. 如果 hard cache size 将要超过限制，删除最大的对象(但是它继续存在在
	 * {@link #softMap} 并 能随时被回收)
	 */
	private final Map<Bitmap, Integer> valueSizes = Collections.synchronizedMap(new HashMap<Bitmap, Integer>());
	

	public LargestLimitedMemoryCache(int sizeLimit) {
		super(sizeLimit);
	}
	
	@Override
	public boolean put(String key, Bitmap value) {
		if (super.put(key, value)) {
			valueSizes.put(value, getSize(value));
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Bitmap remove(String key) {
		Bitmap value = super.get(key);
		if (value != null) {
			valueSizes.remove(value);
		}
		return super.remove(key);
	}
	
	@Override
	public void clear() {
		valueSizes.clear();
		super.clear();
	}

	@Override
	protected int getSize(Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	protected Bitmap removeNext() {
		Integer maxSize = null;
		Bitmap largestValue = null;
		Set<Entry<Bitmap, Integer>> entries = valueSizes.entrySet();
		synchronized (valueSizes) {
			for (Entry<Bitmap, Integer> entry : entries) {
				if (largestValue == null) {
					largestValue = entry.getKey();
					maxSize = entry.getValue();
				} else {
					Integer size = entry.getValue();
					if (size > maxSize) {
						maxSize = size;
						largestValue = entry.getKey();
					}
				}
			}
		}
		valueSizes.remove(largestValue);
		return largestValue;
	}

	@Override
	protected Reference<Bitmap> createReference(Bitmap value) {
		return new WeakReference<Bitmap>(value);
	}
	
}
