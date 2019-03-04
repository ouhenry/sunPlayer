package com.exmart.qiyishow.loader.cache.memory.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;

import com.exmart.qiyishow.loader.cache.memory.MemoryCache;

/**
 * 一个使用强引用缓存, 限制bitmaps的数量. 每一次添加bitmap, 它将被移动到队列头位置, 当一个bitmap添加到一个满的cache,
 * 在队列尾的bitmap就被驱逐可能被垃圾回收.<br />
 * <br />
 * <b>NOTE:</b> 这个缓存只是用强引用缓存图片
 * @author henry
 *
 */

public class LruMemoryCache implements MemoryCache{
	
	private final LinkedHashMap<String, Bitmap> map;
	private final int maxSize;
	/** 缓存的大小 in bytes */
	private int size;
	
	public LruMemoryCache(int maxSize) {
		if(maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		this.maxSize = maxSize;
		this.map = new LinkedHashMap<String, Bitmap>(0, 0.75f, true);
	}
	
	/** The Bitmap 移动到队列头 */
	@Override
	public boolean put(String key, Bitmap value) {
		if (key == null || value == null) {
			throw new NullPointerException("key == null || value == null");
		}
		synchronized (this) {
			size += sizeOf(key, value);
			Bitmap previous = map.put(key, value);
			if (previous != null) {
				size -= sizeOf(key, previous);
			}
		}
		
		trimToSize(maxSize);
		return true;
	}
	
	/**
	 * 删除最大的entries 直到保留entries的总数在要求大小或达到要求大小时;
	 * @param maxSize	the maximum size of the cache before returning. May be -1 to evict even 0-sized elements.
	 */
	private void trimToSize(int maxSize) {
		while (true) {
			String key;
			Bitmap value;
			synchronized (this) {
				if (size < 0 || (map.isEmpty() && size != 0)) {
					throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results");
				}
				
				if (size <= maxSize || map.isEmpty()) {
					break;
				}
				
				Map.Entry<String, Bitmap> toEvict = map.entrySet().iterator().next();
				if (toEvict == null) {
					break;
				}
				key = toEvict.getKey();
				value = toEvict.getValue();
				map.remove(key);
				size -= sizeOf(key, value);
			}
		}
	}
	
	/**
	 * 
	 * @param key
	 * @return bitmap 如果它在内存存在, 如果一个bitmap 返回, 这个bitmap移动到队列头.如果缓存不存在返回null
	 */
	@Override
	public final Bitmap get(String key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}
		
		synchronized (this) {
			return map.get(key);
		}
	}
	/** 删除 the entry for {@code key} 如果存在 */
	@Override
	public Bitmap remove(String key) {
		if (key == null) {
			throw new NullPointerException("key == null");
		}
		
		synchronized (this) {
			Bitmap previous = map.remove(key);
			if (previous != null) {
				size -= sizeOf(key, previous);
			}
			return previous;
		}
	}

	@Override
	public Collection<String> keys() {
		synchronized (this) {
			return new HashSet<String>(map.keySet());
		}
	}

	@Override
	public void clear() {
		trimToSize(-1);// -1 will evict 0-sized elements
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return 大小{@code Bitmap} in bytes
	 * <p/>
	 * entry's 的大小不改变，当bitamp还在缓存
	 */
	private int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
	
	@Override
	public synchronized final String toString() {
		return String.format("LruCache[maxSize=%d]", maxSize);
	}

}
