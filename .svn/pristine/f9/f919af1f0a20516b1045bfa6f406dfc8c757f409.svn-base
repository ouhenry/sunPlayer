package com.exmart.qiyishow.ui.video;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.fenster.controller.FensterPlayerControllerVisibilityListener;
import com.exmart.qiyishow.fenster.controller.SimpleMediaFensterPlayerController;
import com.exmart.qiyishow.fenster.view.FensterVideoView;
import com.exmart.qiyishow.loader.core.DisplayImageOptions;
import com.exmart.qiyishow.loader.core.VideoLoader;
import com.exmart.qiyishow.loader.core.assist.FailReason;
import com.exmart.qiyishow.loader.core.display.SimpleBitmapDisplayer;
import com.exmart.qiyishow.loader.core.listener.VideoLoadingListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MediaPlayerActivity extends Activity implements FensterPlayerControllerVisibilityListener, VideoLoadingListener{
	private FensterVideoView textureView;
    private SimpleMediaFensterPlayerController fullScreenMediaPlayerController;
    
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple_media_player);

        bindViews();
        initVideo();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
		.cacheInMemory(false)
		.cacheOnDisk(true)
		.displayer(new SimpleBitmapDisplayer())
		.considerExifParams(true)
		.build();
        String strUrl = getIntent().getStringExtra("videoUrl");
        VideoLoader.getInstance().displayVideo(strUrl, options, this, null);
        textureView.start();
    }

    private void bindViews() {
        textureView = (FensterVideoView) findViewById(R.id.play_video_texture);
        fullScreenMediaPlayerController = (SimpleMediaFensterPlayerController) findViewById(R.id.play_video_controller);
    }

    private void initVideo() {
        fullScreenMediaPlayerController.setVisibilityListener(this);
        textureView.setMediaController(fullScreenMediaPlayerController);
        textureView.setOnPlayStateListener(fullScreenMediaPlayerController);
    }

    private void setSystemUiVisibility(final boolean visible) {
        int newVis = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

        if (!visible) {
            newVis |= View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(newVis);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(final int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_LOW_PROFILE) == 0) {
                    fullScreenMediaPlayerController.show();
                }
            }
        });
    }

    @Override
    public void onControlsVisibilityChange(final boolean value) {
        setSystemUiVisibility(value);
    }

	@Override
	public void onLoaingStarted(String videoUri) {
		
	}

	@Override
	public void onLoadingFailed(String videoUri, FailReason failReason) {
		
	}

	@Override
	public void onLoadingComplete(String vodeoUri) {
		textureView.setVideo(vodeoUri, SimpleMediaFensterPlayerController.DEFAULT_VIDEO_START);
		textureView.start();
	}

	@Override
	public void onLoadingCancelled(String videoUri) {
		
	}
}
