package com.exmart.qiyishow.loader.core;

import android.graphics.Bitmap;
import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import com.exmart.qiyishow.loader.core.assist.FailReason;
import com.exmart.qiyishow.loader.core.assist.FailReason.FailType;
import com.exmart.qiyishow.loader.core.assist.ImageScaleType;
import com.exmart.qiyishow.loader.core.assist.ImageSize;
import com.exmart.qiyishow.loader.core.assist.LoadedFrom;
import com.exmart.qiyishow.loader.core.assist.ViewScaleType;
import com.exmart.qiyishow.loader.core.decode.ImageDecoder;
import com.exmart.qiyishow.loader.core.decode.ImageDecodingInfo;
import com.exmart.qiyishow.loader.core.download.ImageDownloader;
import com.exmart.qiyishow.loader.core.download.ImageDownloader.Scheme;
import com.exmart.qiyishow.loader.core.imageaware.ImageAware;
import com.exmart.qiyishow.loader.core.listener.ImageLoadingListener;
import com.exmart.qiyishow.loader.core.listener.ImageLoadingProgressListener;
import com.exmart.qiyishow.loader.core.listener.VideoLoadingListener;
import com.exmart.qiyishow.loader.core.listener.VideoLoadingProgressListener;
import com.exmart.qiyishow.loader.utils.IoUtils;
import com.exmart.qiyishow.loader.utils.L;

/**
 * Presents load'n'display image task. Used to load image from Internet or file
 * system, decode it to {@link Bitmap}, and display it in
 * {@link com.nostra13.universalimageloader.core.imageaware.ImageAware} using
 * {@link DisplayBitmapTask}.
 * 
 * @author
 * @see ImageLoaderConfiguration
 * @see ImageLoadingInfo
 * @since 1.3.1
 */
public class LoadVideoTask implements Runnable, IoUtils.CopyListener {

	private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
	private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
	private static final String LOG_DELAY_BEFORE_LOADING = "Delay %d ms before loading...  [%s]";
	private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
	private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
	private static final String LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING = "...Get cached bitmap from memory after waiting. [%s]";
	private static final String LOG_LOAD_IMAGE_FROM_NETWORK = "Load image from network [%s]";
	private static final String LOG_LOAD_IMAGE_FROM_DISK_CACHE = "Load image from disk cache [%s]";
	private static final String LOG_RESIZE_CACHED_IMAGE_FILE = "Resize image in disk cache [%s]";
	private static final String LOG_PREPROCESS_IMAGE = "PreProcess image before caching in memory [%s]";
	private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
	private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
	private static final String LOG_CACHE_IMAGE_ON_DISK = "Cache image on disk [%s]";
	private static final String LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISK = "Process image before cache on disk [%s]";
	private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
	private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";
	private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";

	private static final String ERROR_NO_IMAGE_STREAM = "No stream for image [%s]";
	private static final String ERROR_PRE_PROCESSOR_NULL = "Pre-processor returned null [%s]";
	private static final String ERROR_POST_PROCESSOR_NULL = "Post-processor returned null [%s]";
	private static final String ERROR_PROCESSOR_FOR_DISK_CACHE_NULL = "Bitmap processor for disk cache returned null [%s]";

	private final VideoLoaderEngine engine;
	private final VideoLoadingInfo videoLoadingInfo;
	private final Handler handler;

	// Helper references
	private final VideoLoaderConfiguration configuration;
	private final ImageDownloader downloader;
	private final ImageDownloader networkDeniedDownloader;
	private final ImageDownloader slowNetworkDownloader;
	private final ImageDecoder decoder;
	final String uri;
	final DisplayImageOptions options;
	final VideoLoadingListener listener;
	final VideoLoadingProgressListener progressListener;
	private boolean syncLoading = true;

	public boolean isSyncLoading() {
		return syncLoading;
	}

	public void setSyncLoading(boolean syncLoading) {
		this.syncLoading = syncLoading;
	}

	// State vars
	private LoadedFrom loadedFrom = LoadedFrom.NETWORK;

	public LoadVideoTask(VideoLoaderEngine engine,
			VideoLoadingInfo videoLoadingInfo, Handler handler) {
		this.engine = engine;
		this.videoLoadingInfo = videoLoadingInfo;
		this.handler = handler;

		configuration = engine.configuration;
		downloader = configuration.downloader;
		networkDeniedDownloader = configuration.networkDeniedDownloader;
		slowNetworkDownloader = configuration.slowNetworkDownloader;
		decoder = configuration.decoder;
		uri = videoLoadingInfo.uri;
		options = videoLoadingInfo.options;
		listener = videoLoadingInfo.listener;
		progressListener = videoLoadingInfo.progressListener;
//		syncLoading = options.isSyncLoading();
	}

	@Override
	public void run() {
//		 if (waitIfPaused()) return;

		ReentrantLock loadFromUriLock = videoLoadingInfo.loadFromUriLock;
		if (loadFromUriLock.isLocked()) {
			// L.d(LOG_WAITING_FOR_IMAGE_LOADED, memoryCacheKey);
		}

		loadFromUriLock.lock();
		String strVideo;
		try {
			// checkTaskNotActual();

			strVideo = tryLoadVideo();
			if (strVideo == null)
				return; // listener callback already was fired
			// checkTaskNotActual();
			checkTaskInterrupted();

			// checkTaskNotActual();
			checkTaskInterrupted();
		} catch (TaskCancelledException e) {
			// fireCancelEvent();
			return;
		} catch (IOException e) {
			
		} finally {
			loadFromUriLock.unlock();
		}

		// DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bmp,
		// imageLoadingInfo, engine, loadedFrom);
		// runTask(displayBitmapTask, syncLoading, handler, engine);
	}

	/**
	 * @return <b>true</b> - if task should be interrupted; <b>false</b> -
	 *         otherwise
	 */
	private boolean waitIfPaused() {
		AtomicBoolean pause = engine.getPause();
		if (pause.get()) {
			synchronized (engine.getPauseLock()) {
				if (pause.get()) {
					// L.d(LOG_WAITING_FOR_RESUME, memoryCacheKey);
					try {
						engine.getPauseLock().wait();
					} catch (InterruptedException e) {
						// L.e(LOG_TASK_INTERRUPTED, memoryCacheKey);
						return true;
					}
					// L.d(LOG_RESUME_AFTER_PAUSE, memoryCacheKey);
				}
			}
		}
		return true;
	}

	private String tryLoadVideo() throws TaskCancelledException, IOException {
		try {
			File videoFile = configuration.diskCache.get(uri);
			if (videoFile != null && videoFile.exists()) {
				// L.d(LOG_LOAD_IMAGE_FROM_DISK_CACHE, memoryCacheKey);
				loadedFrom = LoadedFrom.DISC_CACHE;
				String VideoUriForDecoding = Scheme.FILE.wrap(videoFile
						.getAbsolutePath());
				listener.onLoadingComplete(VideoUriForDecoding);
				return VideoUriForDecoding;
			} else {
				loadedFrom = LoadedFrom.NETWORK;
				String VideoUriForDecoding = uri;
				if (tryCacheVideoOnDisk()) {
					videoFile = configuration.diskCache.get(uri);
					if (videoFile != null) {
						VideoUriForDecoding = Scheme.FILE.wrap(videoFile
								.getAbsolutePath());
						listener.onLoadingComplete(VideoUriForDecoding);
					}
				}
				return VideoUriForDecoding;
			}
		} catch (TaskCancelledException e) {
			throw e;

		} catch (Throwable e) {
			L.e(e);
			// fireFailEvent(FailType.UNKNOWN, e);
		}
		return null;
	}

	/**
	 * @return <b>true</b> - if image was downloaded successfully; <b>false</b> -
	 *         otherwise
	 */
	private boolean tryCacheVideoOnDisk() throws TaskCancelledException {
		// L.d(LOG_CACHE_IMAGE_ON_DISK, memoryCacheKey);

		boolean loaded;
		try {
			loaded = downloadImage();
		} catch (IOException e) {
			L.e(e);
			loaded = false;
		}
		return loaded;
	}

	private boolean downloadImage() throws IOException {
		InputStream is = getDownloader().getStream(uri,
				options.getExtraForDownloader());
		if (is == null) {
			// L.e(ERROR_NO_IMAGE_STREAM, memoryCacheKey);
			return false;
		} else {
			try {
				return configuration.diskCache.save(uri, is, this);
			} finally {
				IoUtils.closeSilently(is);
			}
		}
	}

	@Override
	public boolean onBytesCopied(int current, int total) {
		fireProgressEvent(current, total);
		return syncLoading;
	}

	/** @return <b>true</b> - if loading should be continued; <b>false</b> - if loading should be interrupted */
	private boolean fireProgressEvent(final int current, final int total) {
//		if (isTaskInterrupted()) return false;
		if (progressListener != null) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					progressListener.onProgressUpdate(uri, current, total);
				}
			};
			runTask(r, false, handler, engine);
		}
		return true;
	}
	//
	// private void fireFailEvent(final FailType failType, final Throwable
	// failCause) {
	// if (syncLoading || isTaskInterrupted()) return;
	// Runnable r = new Runnable() {
	// @Override
	// public void run() {
	// if (options.shouldShowImageOnFail()) {
	// //
	// imageAware.setImageDrawable(options.getImageOnFail(configuration.resources));
	// }
	// listener.onLoadingFailed(uri, new FailReason(failType, failCause));
	// }
	// };
	// runTask(r, false, handler, engine);
	// }
	//
	// private void fireCancelEvent() {
	// if (syncLoading || isTaskInterrupted()) return;
	// Runnable r = new Runnable() {
	// @Override
	// public void run() {
	// listener.onLoadingCancelled(uri);
	// }
	// };
	// runTask(r, false, handler, engine);
	// }

	private ImageDownloader getDownloader() {
		ImageDownloader d;
		if (engine.isNetworkDenied()) {
			d = networkDeniedDownloader;
		} else if (engine.isSlowNetwork()) {
			d = slowNetworkDownloader;
		} else {
			d = downloader;
		}
		return d;
	}

	/**
	 * @throws TaskCancelledException
	 *             if current task was interrupted
	 */
	private void checkTaskInterrupted() throws TaskCancelledException {
		if (isTaskInterrupted()) {
			throw new TaskCancelledException();
		}
	}

	/**
	 * @return <b>true</b> - if current task was interrupted; <b>false</b> -
	 *         otherwise
	 */
	private boolean isTaskInterrupted() {
		if (Thread.interrupted()) {
			// L.d(LOG_TASK_INTERRUPTED, memoryCacheKey);
			return true;
		}
		return false;
	}

	String getLoadingUri() {
		return uri;
	}

	static void runTask(Runnable r, boolean sync, Handler handler,
			VideoLoaderEngine engine) {
		if (sync) {
			r.run();
		} else if (handler == null) {
			engine.fireCallback(r);
		} else {
			handler.post(r);
		}
	}

	/**
	 * Exceptions for case when task is cancelled (thread is interrupted, image
	 * view is reused for another task, view is collected by GC).
	 * 
	 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
	 * @since 1.9.1
	 */
	class TaskCancelledException extends Exception {
	}
}
