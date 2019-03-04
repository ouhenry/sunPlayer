package com.exmart.qiyishow.record;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.CustomMultipartEntity;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.tools.CustomMultipartEntity.ProgressListener;
import com.exmart.qiyishow.ui.frame.HomeFrame;
import com.exmart.qiyishow.ui.user.LoginActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class FFmpegPreviewActivity extends Activity implements
		TextureView.SurfaceTextureListener, OnClickListener,
		OnCompletionListener {

	private String path;
	private TextureView surfaceView;
	private Button cancelBtn;
	private MediaPlayer mediaPlayer;
	private ImageView imagePlay;
	private ProgressDialog pd;

	private ProgressListener mProgressListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ffmpeg_preview);
		((TextView) findViewById(R.id.tv_title))
				.setText(getString(R.string.video_preview));
		TextView tvBack = (TextView) findViewById(R.id.bt_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setBackgroundResource(R.drawable.back_bg);
		tvBack.setOnClickListener(this);

		cancelBtn = (Button) findViewById(R.id.play_cancel);
		cancelBtn.setOnClickListener(this);
		findViewById(R.id.btn_video_upload).setOnClickListener(this);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		surfaceView = (TextureView) findViewById(R.id.preview_video);

		RelativeLayout preview_video_parent = (RelativeLayout) findViewById(R.id.preview_video_parent);
		LayoutParams layoutParams = (LayoutParams) preview_video_parent
				.getLayoutParams();
		layoutParams.width = displaymetrics.widthPixels;
		layoutParams.height = displaymetrics.widthPixels;
		preview_video_parent.setLayoutParams(layoutParams);

		surfaceView.setSurfaceTextureListener(this);
		surfaceView.setOnClickListener(this);

		path = getIntent().getStringExtra("path");

		imagePlay = (ImageView) findViewById(R.id.previre_play);
		imagePlay.setOnClickListener(this);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);

	}

	@Override
	protected void onStop() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			imagePlay.setVisibility(View.GONE);
		}
		super.onStop();
	}

	private void prepare(Surface surface) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			// 设置需要播放的视频
			mediaPlayer.setDataSource(path);
			// 把视频画面输出到Surface
			mediaPlayer.setSurface(surface);
			mediaPlayer.setLooping(true);
			mediaPlayer.prepare();
			mediaPlayer.seekTo(0);
		} catch (Exception e) {
		}
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
			int arg2) {
		prepare(new Surface(arg0));
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
		return false;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
			int arg2) {

	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture arg0) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_cancel:
			stop();
			break;
		case R.id.previre_play:
			if (!mediaPlayer.isPlaying()) {
				mediaPlayer.start();
			}
			imagePlay.setVisibility(View.GONE);
			break;
		case R.id.preview_video:
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				imagePlay.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.bt_back:
			finish();
			break;
		case R.id.btn_video_upload:
			String bdUserId = Tools.getValueInSharedPreference(this, Constant.OSNAME, Constant.BD_USERID);
			String bdChannelId = Tools.getValueInSharedPreference(this, Constant.OSNAME, Constant.BD_CHANNELID);
			String userId = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.USERID);
			if (TextUtils.isEmpty(userId)) {
				Toast.makeText(this, "用户未登录", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				return;
			}
			uploadVideo(userId, bdUserId, bdChannelId);
			break;
		default:
			break;
		}
	}

	private void uploadVideo(String userId, String bdUserId, String bdChannelId) {

		new AsyncTask<String, Integer, String>() {
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(FFmpegPreviewActivity.this);
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setMax(100);
				pd.show();
			}

			@Override
			protected String doInBackground(String... params) {
				byte[] data = Tools.getBytesFromFile(path);
				if (data == null) {
					return "";
				}
				final long totalSize = data.length;
				String response = "";
				try {

					// arrayEntity = new ByteArrayEntity(pic);
					// arrayEntity.setContentType("application/octet-stream");
					HttpContext httpContext = new BasicHttpContext();
					HttpPost post = new HttpPost(Constant.VIDEO_IMAGE_UPLOAD);
					mProgressListener = new ProgressListener() {

						@Override
						public void transferred(long num) {
							publishProgress((int) ((num / (float) totalSize) * 100));
						}
					};

					CustomMultipartEntity multipartEntiry = new CustomMultipartEntity(
							mProgressListener);

					ByteArrayBody body = new ByteArrayBody(data, "a.mp4");
					StringBody user_id = new StringBody(params[0]);
					StringBody bd_user_id = new StringBody(params[1]);
					StringBody bd_channel_id = new StringBody(params[2]);
					multipartEntiry.addPart("UserId", user_id);
					multipartEntiry.addPart("Video", body);
					multipartEntiry.addPart("bduserId", user_id);
					multipartEntiry.addPart("bdchannelId", bd_channel_id);
					post.setEntity(multipartEntiry);
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,
							40 * 1000);
					HttpConnectionParams.setSoTimeout(httpParams, 40 * 1000);

					HttpClient client = new DefaultHttpClient(httpParams);
					HttpResponse httpResponse = client.execute(post,
							httpContext);
					response = EntityUtils.toString(httpResponse.getEntity());
				} catch (Exception e) {
					e.printStackTrace();
				}

				return response;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				pd.setProgress(values[0]);
			}

			@Override
			protected void onPostExecute(String result) {
				pd.dismiss();
				Intent intent = new Intent(FFmpegPreviewActivity.this,
						HomeFrame.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}.execute(userId, bdUserId, bdChannelId);
	}

	private void stop() {
		mediaPlayer.stop();
		Intent intent = new Intent(this, FFmpegRecorderActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		stop();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		imagePlay.setVisibility(View.VISIBLE);
	}
}