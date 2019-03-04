package com.exmart.qiyishow.tools;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

/**
 * 继承 提供数据加载进度
 * 
 * @author henry
 * 
 */
public class CustomMultipartEntity extends MultipartEntity {
	private final ProgressListener listener;

	// private final

	@Override
	public void consumeContent() throws IOException,
			UnsupportedOperationException {
		// TODO Auto-generated method stub
		super.consumeContent();
	}

	public CustomMultipartEntity(final ProgressListener listener) {
		super();
		this.listener = listener;
	}

	public CustomMultipartEntity(final HttpMultipartMode mode,
			final ProgressListener listener) {
		super(mode);
		this.listener = listener;
	}

	public CustomMultipartEntity(HttpMultipartMode mode, final String boundary,
			final Charset charset, final ProgressListener listener) {
		super(mode, boundary, charset);
		this.listener = listener;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}

	public static interface ProgressListener {
		void transferred(long num);
	}

	public static class CountingOutputStream extends FilterOutputStream {
		private final ProgressListener listener;
		private long transferred;

		public CountingOutputStream(final OutputStream out,
				ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		@Override
		public void write(byte[] buffer, int offset, int length)
				throws IOException {
			int BUFFER_SIZE = 20480;
			int chunkSize;
			int currentOffset = 0;

			while (length > currentOffset) {
				chunkSize = length - currentOffset;
				if (chunkSize > BUFFER_SIZE) {
					chunkSize = BUFFER_SIZE;
				}
				out.write(buffer, currentOffset, chunkSize);
				currentOffset += chunkSize;
				this.transferred += chunkSize;
				this.listener.transferred(this.transferred);
			}
		}

		@Override
		public void write(int oneByte) throws IOException {
			out.write(oneByte);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}

}
