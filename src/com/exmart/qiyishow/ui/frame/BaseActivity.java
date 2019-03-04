package com.exmart.qiyishow.ui.frame;



import com.baidu.mobstat.StatService;
import com.exmart.qiyishow.tools.Tools;

import android.app.Activity;
import android.os.Bundle;
/**
 * Activity父类
 * @author ZhaoYe
 *
 */
public abstract class BaseActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Tools.setWindow(this);
		 initView();
		 StatService.setAppChannel(this, "Google Play", false);
	     StatService.setDebugOn(false);
	}
	private void initView() {
		loadLayout();
		Request();
		logic();
		loadListener();
	}
	@Override
	public void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	/**
	 * 初始化控件
	 */

	protected abstract void loadLayout();

	/**
	 * 监听事件
	 */
	protected abstract void loadListener();

	/**
	 * 请求代码
	 */

	protected abstract void Request();

	/**
	 * 逻辑代码
	 */

	protected abstract void logic();
}