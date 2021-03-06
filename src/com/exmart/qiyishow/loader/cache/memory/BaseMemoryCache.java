package com.exmart.qiyishow.loader.cache.memory;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import android.graphics.Bitmap;


/**
 * Base 内存缓存. 实现memorycache 通用的方法，提供若引用(
 * {@linkplain Reference not strong} 存储
 * 
 * @author henry
 *
 */
public abstract class BaseMemoryCache implements MemoryCache { 
	/** 存储对象弱引用 */
	private final Map<String, Reference<Bitmap>> softMap = Collections.synchronizedMap(new HashMap<String, Reference<Bitmap>>());
	
	@Override
	public Bitmap get(String key) {
		Bitmap result = null;
		Reference<Bitmap> reference = softMap.get(key);
		if (reference != null) {
			result = reference.get();
		}
		return result;
	}
	@Override
	public boolean put(String key, Bitmap value) {
		softMap.put(key, createReference(value));
		return true;
	}
	@Override
	public Bitmap remove(String key) {
		Reference<Bitmap> bmpRef = softMap.get(key);
		return bmpRef == null ? null : bmpRef.get();
	}
	
	@Override
	public Collection<String> keys() {
		synchronized (softMap) {
			return new HashSet<String>(softMap.keySet());
		}
	}
	
	@Override
	public void clear() {
		softMap.clear();
	}
	
	/** 创建 {@linkplain Reference not strong} 值的引用 */
	protected abstract Reference<Bitmap> createReference(Bitmap value);
}
