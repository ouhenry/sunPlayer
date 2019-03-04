package com.exmart.qiyishow.loader.core.assist;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 很多streams 存在 非常缓慢连接
 * 
 * @author henry
 *
 */
public class FlushedInputStream extends FilterInputStream {

	public FlushedInputStream(InputStream in) {
		super(in);
	}
	
	@Override
	public long skip(long byteCount) throws IOException {
		long totalBytesSkipped = 0l;
		while (totalBytesSkipped < byteCount) {
			long bytesSkipped = in.skip(byteCount - totalBytesSkipped);
			if (bytesSkipped == 0L) {
				int by_te = read();
				if (by_te < 0) {
					break;// 到达末尾
				} else {
					bytesSkipped = 1; //读取一个字节
				}
			}
			totalBytesSkipped += bytesSkipped;
		}
		return totalBytesSkipped;
	}
	

}
