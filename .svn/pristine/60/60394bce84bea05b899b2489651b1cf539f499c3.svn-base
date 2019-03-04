package com.exmart.qiyishow.loader.cache.memory;

import java.lang.ref.Reference;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.exmart.qiyishow.loader.utils.L;

import android.graphics.Bitmap;

/**
 * 缓存限制. 提供对象存储. 存储的bitmaps 不能超过大小限制 (
 * {@link #getSizeLimit()}).<br />
 * <br />
 * <b>NOTE:<b/> 缓存使用强引用和弱引用存储bitmaps. 强引用 － 限制bitmaps数量(根据缓存大小), 弱引用 － 缓存bitmaps
 * 
 * @author henry
 *
 */
public abstract class LimitedMemoryCache extends BaseMemoryCache{
	
	private static final int MAX_NORMAL_CACHE_SIZE_IN_MB = 16;
	private static final int MAX_NORMAL_CACHE_SIZE = MAX_NORMAL_CACHE_SIZE_IN_MB * 1024 * 1024;
	
	private final int sizeLimit;
	private final AtomicInteger cacheSize;
	/**
	 * 包含强引用存储对象. 每一次对象添加在最后. 如果 hardcache 大小将要超过限制则删除第一个对象(但是这个对象仍然存在在{@link #softMap}
	 * 并且会随时被垃圾回收掉)
	 */
	private final List<Bitmap> hardCache = Collections.synchronizedList(new LinkedList<Bitmap>());
	
	public LimitedMemoryCache(int sizeLimit) {
		this.sizeLimit = sizeLimit;
		cacheSize = new AtomicInteger();
		if (sizeLimit > MAX_NORMAL_CACHE_SIZE) {
			L.w("你设置了太大的内存存储大小(超过 %1$d Mb)", MAX_NORMAL_CACHE_SIZE_IN_MB);
		}
	}
	
	@Override
	public boolean put(String key, Bitmap value) {
		boolean putSuccessfully = false;
		//尝试添加值到hardcache
		int valueSize = getSize(value);
		int sizeLimit = getSizeLimit();
		int curCacheSize = cacheSize.get();
		if (valueSize < sizeLimit) {
			while (curCacheSize + valueSize > sizeLimit) {
				Bitmap removeValue = removeNext();
				if (hardCache.remove(removeValue)) {
					curCacheSize = cacheSize.addAndGet(-getSize(removeValue));
				}
			}
			hardCache.add(value);
			cacheSize.addAndGet(valueSize);
			
			putSuccessfully = true;
		}
		// 添加值到softcache
		super.put(key, value);
		return putSuccessfully;
	}
	
	@Override
	public Bitmap remove(String key) {
		Bitmap value = super.get(key);
		if (value != null) {
			if (hardCache.remove(value)) {
				cacheSize.addAndGet(-getSize(value));
			}
		}
		return super.remove(key);
	}
	
	@Override
	public void clear() {
		hardCache.clear();
		cacheSize.set(0);
		super.clear();
	}
	protected int getSizeLimit() {
		return sizeLimit;
	}
	
	protected abstract int getSize(Bitmap value);
	
	protected abstract Bitmap removeNext();
}
