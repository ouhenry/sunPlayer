package com.exmart.qiyishow.loader.cache.disc.naming;

/**
 * 命名图片文件 根据图片URI {@linkplain String#hashCode() hashcode}
 * 
 * @author henry
 *
 */
public class HashCodeFileNameGenerator implements FileNameGenerator{

	@Override
	public String generate(String imageUri) {
		return String.valueOf(imageUri.hashCode());
	}

}
