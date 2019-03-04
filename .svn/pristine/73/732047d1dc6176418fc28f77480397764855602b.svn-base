package com.exmart.qiyishow.loader.core;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.exmart.qiyishow.loader.cache.disc.DiskCache;
import com.exmart.qiyishow.loader.core.listener.ImageLoadingListener;
import com.exmart.qiyishow.loader.core.listener.SimpleImageLoadingListener;
import com.exmart.qiyishow.loader.core.listener.SimpleVideoLoadingListener;
import com.exmart.qiyishow.loader.core.listener.VideoLoadingListener;
import com.exmart.qiyishow.loader.core.listener.VideoLoadingProgressListener;
import com.exmart.qiyishow.loader.utils.L;

/**
 * singleton 视频加载类
 * 
 * @author henry
 *
 */

public class VideoLoader {
	public static final String TAG = VideoLoader.class.getSimpleName();
	
	static final String LOG_INIT_CONFIG = "Initialize ImageLoader with configuration";
	static final String LOG_DESTROY = "Destroy VideoLoader";
	
	private static final String WARNING_RE_INIT_CONFIG = "Try to initialize VideoLoader which had already been initialized before. " + "To re-init VideoLoader with new configuration call ImageLoader.destroy() at first.";
	private static final String ERROR_NOT_INIT = "ImageLoader must be init with configuration before using";
	private static final String ERROR_INIT_CONFIG_WITH_NULL = "VideoLoader configuration can not be initialized with null";
	
	private static final String ERROR_WRONG_ARGUMENTS = "Wrong arguments were passed to displayImage() method (ImageView reference must not be null)";
	
	private VideoLoaderConfiguration configuration;
	private VideoLoaderEngine engine;
	
	private final VideoLoadingListener emptyListener = new SimpleVideoLoadingListener();
	
	private volatile static VideoLoader instance;
	
	private HashMap<String, LoadVideoTask> tasks = new HashMap<String, LoadVideoTask>();
	
	public HashMap<String, LoadVideoTask> getTasks() {
		return tasks;
	}

	public void setTasks(HashMap<String, LoadVideoTask> tasks) {
		this.tasks = tasks;
	}

	/** Returns singleton class instance */
	public static VideoLoader getInstance() {
		if (instance == null) {
			synchronized (VideoLoader.class) {
				if (instance == null) {
					instance = new VideoLoader();
				}
			}
		}
		return instance;
	}

	protected VideoLoader() {
	}
	
	public synchronized void init(VideoLoaderConfiguration configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
		}
		if (this.configuration == null) {
			L.d(LOG_INIT_CONFIG);
			engine = new VideoLoaderEngine(configuration);
			this.configuration = configuration;
		} else {
			L.w(WARNING_RE_INIT_CONFIG);
		}
	}
	public boolean isInited() {
		return configuration != null;
	}
	
	/**
	 * Adds display image task to execution pool. Image will be set to ImageAware when it's turn.<br />
	 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
	 *
	 * @param uri              Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
	 * @param imageAware       {@linkplain com.nostra13.universalimageloader.core.imageaware.ImageAware Image aware view}
	 *                         which should display image
	 * @param options          {@linkplain com.nostra13.universalimageloader.core.DisplayImageOptions Options} for image
	 *                         decoding and displaying. If <b>null</b> - default display image options
	 *                         {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
	 *                         from configuration} will be used.
	 * @param listener         {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires
	 *                         events on UI thread if this method is called on UI thread.
	 * @param progressListener {@linkplain com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener
	 *                         Listener} for image loading progress. Listener fires events on UI thread if this method
	 *                         is called on UI thread. Caching on disk should be enabled in
	 *                         {@linkplain com.nostra13.universalimageloader.core.DisplayImageOptions options} to make
	 *                         this listener work.
	 * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
	 * @throws IllegalArgumentException if passed <b>imageAware</b> is null
	 */
	public void displayVideo(String uri, DisplayImageOptions options,
			VideoLoadingListener listener, VideoLoadingProgressListener progressListener) {
		checkConfiguration();
		if (listener == null) {
			listener = emptyListener;
		}
		if (options == null) {
			options = configuration.defaultDisplayImageOptions;
		}

//
//		ImageSize targetSize = ImageSizeUtils.defineTargetSizeForView(imageAware, configuration.getMaxImageSize());
//		String memoryCacheKey = MemoryCacheUtils.generateKey(uri, targetSize);
//		engine.prepareDisplayTaskFor(imageAware, memoryCacheKey);

//		listener.onLoadingStarted(uri);
		
		
//			if (uri.contains("ypjgw/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("ypjgw.mp4");
//			} else if (uri.contains("xhrzge/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("xhrzge.mp4");
//			} else if (uri.contains("mkbl/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("mkbl.mp4");
//			} else if (uri.contains("adbb/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("adbb.mp4");
//			} else if (uri.contains("ai/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("ai.mp4");
//			} else if (uri.contains("blh/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("blh.mp4");
//			} else if (uri.contains("dxsw/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("dxsw.mp4");
//			} else if (uri.endsWith("gxhxg/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("gxhxg.mp4");
//			} else if (uri.endsWith("sdrb/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("sdrb.mp4");
//			} else if (uri.endsWith("sskt/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("sskt.mp4");
//			} else if (uri.endsWith("xcm/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("xcm.mp4");
//			} else if (uri.endsWith("xhrzge/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("xhrzge.mp4"); 
//			} else if (uri.endsWith("ydwgc/1.mp4")) { 
//				uri = Scheme.ASSETS.wrap("ydwgc.mp4");
//			} else if (uri.endsWith("yrgx/1.mp4")) {
//				uri = Scheme.ASSETS.wrap("yrgx.mp4"); 
//			}
//			System.out.println(uri);
			VideoLoadingInfo videoLoadingInfo = new VideoLoadingInfo(uri, options, listener, progressListener, engine.getLockForUri(uri));
			LoadVideoTask displayTask = new LoadVideoTask(engine, videoLoadingInfo,
					defineHandler(options));
			new Thread(displayTask).start();
			tasks.put(uri, displayTask);
//			if (options.isSyncLoading()) {
//				displayTask.run();
//			} 
	}


	/**
	 * Checks if ImageLoader's configuration was initialized
	 *
	 * @throws IllegalStateException if configuration wasn't initialized
	 */
	private void checkConfiguration() {
		if (configuration == null) {
			throw new IllegalStateException(ERROR_NOT_INIT);
		}
	}



	/**
	 * Returns disk cache
	 *
	 * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
	 * @deprecated Use {@link #getDiskCache()} instead
	 */
	@Deprecated
	public DiskCache getDiscCache() {
		return getDiskCache();
	}

	/**
	 * Returns disk cache
	 *
	 * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
	 */
	public DiskCache getDiskCache() {
		checkConfiguration();
		return configuration.diskCache;
	}

	/**
	 * Clears disk cache.
	 *
	 * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
	 * @deprecated Use {@link #clearDiskCache()} instead
	 */
	@Deprecated
	public void clearDiscCache() {
		clearDiskCache();
	}

	/**
	 * Clears disk cache.
	 *
	 * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
	 */
	public void clearDiskCache() {
		checkConfiguration();
		configuration.diskCache.clear();
	}

	

	

	/**
	 * Denies or allows ImageLoader to download images from the network.<br />
	 * <br />
	 * If downloads are denied and if image isn't cached then
	 * {@link ImageLoadingListener#onLoadingFailed(String, View, FailReason)} callback will be fired with
	 * {@link FailReason.FailType#NETWORK_DENIED}
	 *
	 * @param denyNetworkDownloads pass <b>true</b> - to deny engine to download images from the network; <b>false</b> -
	 *                             to allow engine to download images from network.
	 */
	public void denyNetworkDownloads(boolean denyNetworkDownloads) {
		engine.denyNetworkDownloads(denyNetworkDownloads);
	}

	/**
	 * Sets option whether ImageLoader will use {@link FlushedInputStream} for network downloads to handle <a
	 * href="http://code.google.com/p/android/issues/detail?id=6066">this known problem</a> or not.
	 *
	 * @param handleSlowNetwork pass <b>true</b> - to use {@link FlushedInputStream} for network downloads; <b>false</b>
	 *                          - otherwise.
	 */
	public void handleSlowNetwork(boolean handleSlowNetwork) {
		engine.handleSlowNetwork(handleSlowNetwork);
	}

	/**
	 * Pause ImageLoader. All new "load&display" tasks won't be executed until ImageLoader is {@link #resume() resumed}.
	 * <br />
	 * Already running tasks are not paused.
	 */
	public void pause() {
		engine.pause();
	}

	/** Resumes waiting "load&display" tasks */
	public void resume() {
		engine.resume();
	}

	/**
	 * Cancels all running and scheduled display image tasks.<br />
	 * <b>NOTE:</b> This method doesn't shutdown
	 * {@linkplain com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder#taskExecutor(java.util.concurrent.Executor)
	 * custom task executors} if you set them.<br />
	 * ImageLoader still can be used after calling this method.
	 */
	public void stop() {
		engine.stop();
	}

	/**
	 * {@linkplain #stop() Stops ImageLoader} and clears current configuration. <br />
	 * You can {@linkplain #init(ImageLoaderConfiguration) init} ImageLoader with new configuration after calling this
	 * method.
	 */
	public void destroy() {
		if (configuration != null) L.d(LOG_DESTROY);
		stop();
		configuration.diskCache.close();
		engine = null;
		configuration = null;
	}

	private static Handler defineHandler(DisplayImageOptions options) {
		Handler handler = options.getHandler();
		if (options.isSyncLoading()) {
			handler = null;
		} else if (handler == null && Looper.myLooper() == Looper.getMainLooper()) {
			handler = new Handler();
		}
		return handler;
	}

	/**
	 * Listener which is designed for synchronous image loading.
	 *
	 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
	 * @since 1.9.0
	 */
	private static class SyncImageLoadingListener extends SimpleImageLoadingListener {

		private Bitmap loadedImage;

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			this.loadedImage = loadedImage;
		}

		public Bitmap getLoadedBitmap() {
			return loadedImage;
		}
	}
}
