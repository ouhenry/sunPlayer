package com.exmart.qiyishow.loader.cache.memory.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import android.graphics.Bitmap;

import com.exmart.qiyishow.loader.cache.memory.LimitedMemoryCache;

/**
 * 限制缓存. 提供bitmaps存储. 存储bitmaps 不能超过限制大小.  当缓存达到限制大小 删除最近最少使用的bitmap.<br />
 * <br />
 * <b>NOTE:<b/> 这个缓存使用强和弱引用存储bitmaps. 强引用 － 限制bitmaps的数量, 弱引用 － 其他所有的bitmaps 缓存.
 * 
 * @author henry
 *
 */

public class UsingFreqMemoryCache extends LimitedMemoryCache{
	
	private final Map<Bitmap, Integer> usingCounts = Collections.synchronizedMap(new HashMap<Bitmap, Integer>());
	
	public UsingFreqMemoryCache(int sizeLimit) {
		super(sizeLimit);
	}
	
	@Override
	public boolean put(String key, Bitmap value) {
		if (super.put(key, value)) {
			usingCounts.put(value, 0);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Bitmap get(String key) {
		Bitmap value = super.get(key);
		//创建使用次数 如果value 在hardCache存在
		if (value != null) {
			Integer usageCount = usingCounts.get(value);
			if (usageCount != null) {
				usingCounts.put(value, usageCount + 1); 
			}
		}
		return value;
	}
	
	@Override
	public Bitmap remove(String key) {
		Bitmap value = super.get(key);
		if (value != null) {
			usingCounts.remove(value);
		}
		return super.remove(key);
	}
	
	@Override
	public void clear() {
		usingCounts.clear();
		super.clear();
	}

	@Override
	protected int getSize(Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	protected Bitmap removeNext() {
		Integer minUsageCount = null;
		Bitmap leastUsedValue = null;
		Set<Entry<Bitmap, Integer>> entries = usingCounts.entrySet();
		synchronized (usingCounts) {
			for (Entry<Bitmap, Integer> entry : entries) {
				if (leastUsedValue == null) {
					leastUsedValue = entry.getKey();
					minUsageCount = entry.getValue();
				} else {
					Integer lastValueUsage = entry.getValue();
					if (lastValueUsage < minUsageCount) {
						minUsageCount = lastValueUsage;
						leastUsedValue = entry.getKey();
					}
				}
			}
		}
		usingCounts.remove(leastUsedValue);
		return leastUsedValue;
	}

	@Override
	protected Reference<Bitmap> createReference(Bitmap value) {
		return new WeakReference<Bitmap>(value);
	}

}
