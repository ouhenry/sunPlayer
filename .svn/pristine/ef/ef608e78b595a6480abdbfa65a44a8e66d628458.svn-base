package com.exmart.qiyishow.loader.cache.disc.naming;

import java.math.BigInteger;
import java.security.MessageDigest;

import com.exmart.qiyishow.loader.utils.L;

/**
 * 命名图片文件 根据图片URI MD5
 * 
 * @author henry
 *
 */
public class Md5FileNameGenerator implements FileNameGenerator{
	
	private static final String HASH_ALGORITHM = "MD5";
	private static final int RADIX = 10 + 26; // 10 digits + 26 letters

	@Override
	public String generate(String imageUri) {
		byte[] md5 = getMD5(imageUri.getBytes());
		BigInteger bi = new BigInteger(md5).abs();
		return bi.toString(RADIX);
	}
	
	private byte[] getMD5(byte[] data) {
		byte[] hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(data);
			hash = digest.digest();
		} catch (Exception e) {
			L.e(e);
		}
		return hash;
	}
	

}
