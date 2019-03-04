/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.exmart.qiyishow.loader.core;


import java.util.concurrent.locks.ReentrantLock;

import com.exmart.qiyishow.loader.core.assist.ImageSize;
import com.exmart.qiyishow.loader.core.imageaware.ImageAware;
import com.exmart.qiyishow.loader.core.listener.ImageLoadingListener;
import com.exmart.qiyishow.loader.core.listener.ImageLoadingProgressListener;
import com.exmart.qiyishow.loader.core.listener.VideoLoadingListener;
import com.exmart.qiyishow.loader.core.listener.VideoLoadingProgressListener;

/**
 * Information for load'n'display image task
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see com.nostra13.universalimageloader.utils.MemoryCacheUtils
 * @see DisplayImageOptions
 * @see ImageLoadingListener
 * @see com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener
 * @since 1.3.1
 */
final class VideoLoadingInfo {

	final String uri;
	final DisplayImageOptions options;
	final VideoLoadingListener listener;
	final VideoLoadingProgressListener progressListener;
	final ReentrantLock loadFromUriLock;

	public VideoLoadingInfo(String uri,DisplayImageOptions options, VideoLoadingListener listener,
			VideoLoadingProgressListener progressListener, ReentrantLock loadFromUriLock) {
		this.uri = uri;
		this.options = options;
		this.listener = listener;
		this.progressListener = progressListener;
		this.loadFromUriLock = loadFromUriLock;
	}
}
