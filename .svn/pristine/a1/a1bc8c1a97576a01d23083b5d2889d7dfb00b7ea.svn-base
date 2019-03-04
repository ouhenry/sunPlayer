package com.exmart.qiyishow.loader.core.assist;

import java.io.IOException;
import java.io.InputStream;

/**
 * 装饰 {@link java.io.InputStream InputStream}. 提供stream length  根据{@link #available()} method
 * 
 * @author henry
 *
 */
public class ContentLengthInputStream extends InputStream {
	
	private final InputStream stream;
	private final int length;
	
	public ContentLengthInputStream(InputStream stream, int length) {
		this.stream = stream;
		this.length = length;
	}
	@Override
	public int available() throws IOException {
		return length;
	}
	
	@Override
	public void close() throws IOException {
		stream.close();
	}
	
	@Override
	public void mark(int readlimit) {
		stream.mark(readlimit);
	}
	
	@Override
	public int read() throws IOException {
		return stream.read();
	}
	
	@Override
	public int read(byte[] buffer) throws IOException {
		return stream.read(buffer);
	}
	
	@Override
	public int read(byte[] buffer, int byteOffset, int byteCount)
			throws IOException {
		return stream.read(buffer, byteOffset, byteCount);
	}
	
	@Override
	public synchronized void reset() throws IOException {
		stream.reset();
	}
	
	@Override
	public long skip(long byteCount) throws IOException {
		return stream.skip(byteCount);
	}
	
	@Override
	public boolean markSupported() {
		return stream.markSupported();
	}

}
