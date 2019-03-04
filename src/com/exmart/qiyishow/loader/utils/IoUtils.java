package com.exmart.qiyishow.loader.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 提供 I/O 操作
 * @author henry
 * 
 */
public class IoUtils {
	/** {@value} */ 
	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 KB
	/** {@value} */
	public static final int DEFAULT_IMAGE_TOTAL_SIZE = 500 * 1024; // 500KB;
	/** {@value} */
	public static final int CONTINUE_LOADING_PERCENTAGE = 75; // 百分比
	
	private IoUtils(){
		
	}
	/**
	 * 复制流， 通过监听启动进度事件，可以通过监听打断复制, 使用 buffer size =
	 * {@value #DEFAULT_BUFFER_SIZE} bytes.
	 * @param is		Input stream
	 * @param os		Output stream
	 * @param listener	可以null;监听复制进度、控制打断复制
	 * @return	<b>true</b> - 如果复制成功；<b>false</b> - 如果复制打断通过监听
	 * @throws IOException
	 */
	public static boolean copyStream(InputStream is, OutputStream os, CopyListener listener) throws IOException {
		return copyStream(is, os, listener, DEFAULT_BUFFER_SIZE);
	}
	/**
	 * 复制流， 通过监听启动进度事件，可以通过监听打断复制
	 * @param is		Input stream
	 * @param os		Output stream
	 * @param listener 	可以为null; 监听复制进度、控制打断复制
	 * @param bufferSize 复制缓冲大小， 也相当于每一步填充进度通过监听回调， i.e.
	 * 					 复制<b>bufferSize</b> 字节数之后 进度事件会启动
	 * @return <b>true</b> - 如果复制成功；<b>false</b> - 如果复制打断通过监听
	 * @throws IOException
	 */
	public static boolean copyStream(InputStream is, OutputStream os, CopyListener listener, int bufferSize)
			throws IOException {
		int current = 0;
		int total = is.available();
		if (total <= 0){
			total = DEFAULT_IMAGE_TOTAL_SIZE;
		}
		final byte[] bytes = new byte[bufferSize];
		int count;
		if (shouldStopLoading(listener, current, bufferSize)) {
			return false;
		}
		while ((count = is.read(bytes, 0, bufferSize)) != -1) {
			os.write(bytes, 0, count);
			current += count;
			if (shouldStopLoading(listener, current, total)) {
				return false;
			}
		}
		os.flush();
		return true;
	}
	private static boolean shouldStopLoading(CopyListener listener, int current, int total) {
		if(listener != null) {
			boolean shouldContinue = listener.onBytesCopied(current, total);
			if(!shouldContinue) {
				if(100 * current / total < CONTINUE_LOADING_PERCENTAGE) {
					return true; // 如果已经下载大与 75% 继续下载无论如何
				}
			}
		}
		return false;
	}
	public static void readAndCloseStream(InputStream is) {
		final byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
		try {
			while (is.read(bytes, 0, DEFAULT_BUFFER_SIZE) != -1);
		} catch (IOException e) {
		} finally {
			closeSilently(is);
		}
	}
	
	
	public static void closeSilently(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
			}
		}
	}
	/** 监听控制复制进度*/
	public static interface CopyListener {
		/**
		 * 
		 * @param current 已下载字节
		 * @param total 下载总数
		 * @return <b>true</b> - 如果复制需要继续； <b>false</b> - 如果复制需要打断
		 */
		boolean onBytesCopied(int current, int total);
	}
	public static void readBitmapToFile(File dir, Bitmap bitmap) {
		String fileName = System.currentTimeMillis() + ".png";
		File f = new File(dir, fileName);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream bos = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
