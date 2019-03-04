package com.exmart.qiyishow.loader.cache.disc.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

import com.exmart.qiyishow.loader.cache.disc.naming.FileNameGenerator;
import com.exmart.qiyishow.loader.core.DefaultConfigurationFactory;
import com.exmart.qiyishow.loader.utils.IoUtils.CopyListener;

/**
 * 删除超过规定时间的缓存文件.缓存大小不受限.
 * @author henry
 *
 */
public class LimitedAgeDiskCache extends BaseDiskCache{
	
	private final long maxFileAge;
	
	private final Map<File, Long> loadingDates = Collections.synchronizedMap(new HashMap<File, Long>());
	
	/**
	 * 
	 * @param cacheDir	文件缓存目录
	 * @param maxAge	文件最大存储时长. 如果文件超过这个值，删除文件
	 */
	public LimitedAgeDiskCache(File cacheDir, long maxAge) {
		this(cacheDir, null, DefaultConfigurationFactory.createFileNameGenerator(), maxAge);
	}
	/**
	 * @param cacheDir			文件缓存目录
	 * @param reserveCacheDir	备用目录
	 * @param maxAge			文件最大存储时长. 如果文件超过这个值，删除文件
	 */
	public LimitedAgeDiskCache(File cacheDir, File reserveCacheDir,
			long maxAge) {
		this(cacheDir, reserveCacheDir, DefaultConfigurationFactory.createFileNameGenerator(), maxAge);
	}
	/**
	 * @param cacheDir			文件缓存目录
	 * @param reserveCacheDir	备用目录
	 * @param fileNameGenerator	文件生成名
	 * @param maxAge			文件最大存储时长. 如果文件超过这个值，删除文件
	 */
	public LimitedAgeDiskCache(File cacheDir, File reserveCacheDir, FileNameGenerator fileNameGenerator, long maxAge) {
		super(cacheDir, reserveCacheDir, fileNameGenerator);
		this.maxFileAge = maxAge * 1000; // to milliseconds
	}
	
	@Override
	public File get(String imageUri) {
		File file = super.get(imageUri);
		if (file != null && file.exists()) {
			boolean cached;
			Long loadingDate = loadingDates.get(file);
			if (loadingDate == null) {
				cached = false;
				loadingDate = file.lastModified();
			} else {
				cached = true;
			}
			
			if(System.currentTimeMillis() - loadingDate > maxFileAge) {
				file.delete();
				loadingDates.remove(file);
			} else if (!cached) {
				loadingDates.put(file, loadingDate);
			}
		}
		return file;
	}
	
	@Override
	public boolean save(String imageUri, InputStream imageStream,
			CopyListener listener) throws IOException {
		boolean saved = super.save(imageUri, imageStream, listener);
		rememberUsage(imageUri);
		return saved;
	}
	@Override
	public boolean save(String imageUri, Bitmap bitmap) throws IOException {
		boolean saved = super.save(imageUri, bitmap);
		rememberUsage(imageUri);
		return saved;
	}
	@Override
	public boolean remove(String imageUri) {
		loadingDates.remove(getFile(imageUri));
		return super.remove(imageUri);
	}
	@Override
	public void clear() {
		super.clear();
		loadingDates.clear();
	}
	
	private void rememberUsage(String imageUri) {
		File file = getFile(imageUri);
		long currentTime = System.currentTimeMillis();
		file.setLastModified(currentTime);
		loadingDates.put(file, currentTime);
	}
}
