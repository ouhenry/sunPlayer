package com.exmart.qiyishow.ui.frame;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.exmart.qiyishow.R;

public class ActiveDetailActiviey extends BaseActivity implements OnClickListener{
	private WebView wv;
	int loadUrlTimeOut = 0;
	protected int loadUrlTimeoutValue = 20000;
	private long timeout = 5000;
	private Handler mHandler = new Handler();
	private Timer timer;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.active_detail_layout);
		((TextView)findViewById(R.id.tv_title)).setText("活动详情");
		TextView tvBack = (TextView)findViewById(R.id.bt_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setBackgroundResource(R.drawable.back_bg);
		tvBack.setOnClickListener(this);
		wv = (WebView) findViewById(R.id.wv_active_detail);
		WebSettings webSettings = wv.getSettings();
		webSettings.setJavaScriptEnabled(true);
//		wv.getSettings().setBuiltInZoomControls(false);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setRenderPriority(RenderPriority.HIGH);
		wv.getSettings().setBlockNetworkImage(true); 
		wv.getSettings().setAllowFileAccess(true);
		wv.getSettings().setAppCacheEnabled(true);
		wv.getSettings().setSaveFormData(false);
		wv.getSettings().setLoadsImagesAutomatically(true);
		wv.getSettings().setBlockNetworkImage(true);
//		String user_agent =
//	            "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124 (KHTML, like Gecko) Safari/125.1";
//		wv.getSettings().setUserAgentString(user_agent);
		wv.getSettings().setSupportZoom(false);
		final String url = "http://weibo.com/p/1001603808728141969769?mod=zwenzhang";
		wv.setWebViewClient(new ActiveWebViewClient());
		wv.loadUrl(url);
		
//		wv.post(new Runnable() {
//			
//			@Override
//			public void run() {
//				wv.loadUrl(url);
//				
//			}
//		});
	}
	private class ActiveWebViewClient extends WebViewClient {
		
//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			super.onPageStarted(view, url, favicon);
//			 timer = new Timer();
//             TimerTask tt = new TimerTask() {
//                 @Override
//                 public void run() {
//                     /*
//                      * 超时后,首先判断页面加载进度,超时并且进度小于100,就执行超时后的动作
//                      */
//                     if (ActiveDetailActiviey.this.wv.getProgress() < 100) {
//                         Message msg = new Message();
//                         msg.what = 1;
//                         mHandler.sendMessage(msg);
//                         timer.cancel();
//                         timer.purge();
//                     }
//                 }
//             };
//             timer.schedule(tt, timeout, 1);
//		}
//		
//		@Override
//		public void onPageFinished(WebView view, String url) {
//			super.onPageFinished(view, url);
//			timer.cancel();
//            timer.purge();
//		}
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
//	@Override
//	public void onPause() {
//		super.onPause();
//		wv.pauseTimers();
//	}
//	@Override
//	public void onResume() {
//		super.onResume();
//		wv.resumeTimers();
//	}

	@Override
	protected void loadLayout() {
		
		
	}

	@Override
	protected void loadListener() {
		
	}

	@Override
	protected void Request() {
		
	}

	@Override
	protected void logic() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			finish();
			break;

		default:
			break;
		}
	}
}
