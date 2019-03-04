package com.exmart.qiyishow.loader.cache.disc.impl;

import java.io.File;

import com.exmart.qiyishow.loader.cache.disc.naming.FileNameGenerator;

/**
 * 缓存大小不限制
 * @author henry
 *
 */
public class UnlimitedDiskCache extends BaseDiskCache {
	
	public UnlimitedDiskCache(File cacheDir){
		super(cacheDir);
	}
	public UnlimitedDiskCache(File cacheDir, File reserveCacheDir){
		super(cacheDir, reserveCacheDir);
	}

	public UnlimitedDiskCache(File cacheDir, File reserveCacheDir,
			FileNameGenerator fileNameGenerator) {
		super(cacheDir, reserveCacheDir, fileNameGenerator);
	}
}
