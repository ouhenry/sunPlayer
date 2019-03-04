package com.exmart.qiyishow.loader.cache.memory.impl;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;

import com.exmart.qiyishow.loader.cache.memory.BaseMemoryCache;

/**
 * 使用{@linkplain WeakReference weak references} 缓存bitmap <br />
 * <br />
 * <b>NOTE:</b> 这个缓存仅使用弱引用存储Bitmaps
 * @author henry
 *
 */

public class WeakMemoryCache extends BaseMemoryCache{

	@Override
	protected Reference<Bitmap> createReference(Bitmap value) {
		return new WeakReference<Bitmap>(value);
	}

}
