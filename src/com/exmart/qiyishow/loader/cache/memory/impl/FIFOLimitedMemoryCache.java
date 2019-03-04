package com.exmart.qiyishow.loader.cache.memory.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;

import com.exmart.qiyishow.loader.cache.memory.LimitedMemoryCache;

/**
 * 限制{@link Bitmap bitmap} 缓存. 提供{@link Bitmap bitmaps} 存储. 存储bitmaps的大小不能超过限制.
 * 当缓存达到缓存限制大小时清除处理根据FIFO 原则.<br />
 * <br />
 * <b>NOTE:</b> 这个缓存使用强和弱引用去存储bitmaps. 强引用 － 限制Bitmaps的数量(根据缓存大小), 弱引用 － 其他所有Bitmaps缓存
 * 
 * @author henry
 *
 */

public class FIFOLimitedMemoryCache extends LimitedMemoryCache{
	
	private final List<Bitmap> queue = Collections.synchronizedList(new LinkedList<Bitmap>());
	

	public FIFOLimitedMemoryCache(int sizeLimit) {
		super(sizeLimit);
	}

	@Override
	protected int getSize(Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
	
	@Override
	public boolean put(String key, Bitmap value) {
		if (super.put(key, value)) {
			queue.add(value);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Bitmap remove(String key) {
		Bitmap value = super.get(key);
		if (value != null) {
			queue.remove(value);
		}
		return super.remove(key);
	}
	
	@Override
	public void clear() {
		queue.clear();
		super.clear();
	}
	

	@Override
	protected Bitmap removeNext() {
		return queue.remove(0);
	}

	@Override
	protected Reference<Bitmap> createReference(Bitmap value) {
		return new WeakReference<Bitmap>(value);
	}

}
