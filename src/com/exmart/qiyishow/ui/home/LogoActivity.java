package com.exmart.qiyishow.ui.home;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.exmart.qiyishow.R;
import com.exmart.qiyishow.push.Utils;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.frame.BaseActivity;
import com.exmart.qiyishow.ui.frame.GuideActivity;
import com.exmart.qiyishow.ui.frame.HomeFrame;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
/**
 * 启动页
 * 
 * @author ZhaoYe
 *
 */
public class LogoActivity extends BaseActivity {
	
	private Intent intent;
	private String guideTag;//guideTag = ""; 代表第一次打开 guideTag = "0";代表以打开过
	private String width;
	private String Height;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		width = Tools.getWidth(this)+"";
		Height = Tools.getHeight(this)+"";
		setContentView(R.layout.activity_logo);
		Utils.logStringCache = Utils.getLogText(getApplicationContext());
        // Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                Utils.getMetaValue(LogoActivity.this, "api_key"));
	}

	@Override
	protected void loadListener() {

	}

	@Override
	protected void Request() {
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(1500);
					handler.sendMessage(handler.obtainMessage(0));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}.start();
		
	}
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				guideTag = Tools.getValueInSharedPreference(LogoActivity.this,Constant.SPNAME, Constant.GUIDE);
				if(guideTag.equals("")){
					Tools.setValueInSharedPreference(LogoActivity.this, Constant.SPNAME, Constant.GUIDE, "0");
					intent = new Intent(LogoActivity.this,GuideActivity.class);
					startActivity(intent);
					finish();
				}else{
					intent = new Intent(LogoActivity.this,HomeFrame.class);
//					intent = new Intent(LogoActivity.this,PushDemoActivity.class);
					startActivity(intent);
					finish();
				}
				break;
			}
			
		}
	};


	@Override
	protected void logic() {
		Tools.setValueInSharedPreference(this, Constant.OSNAME, Constant.PLATFORM, Constant.OS);
		Tools.setValueInSharedPreference(this, Constant.OSNAME, Constant.OSVERSION, Tools.getOSVersion());
		Tools.setValueInSharedPreference(this, Constant.OSNAME, Constant.APPVERSIONCODE, Tools.getAppVersion(this, 1));
		Tools.setValueInSharedPreference(this, Constant.OSNAME, Constant.APPVERSIONNAME, Tools.getAppVersion(this, 0));
		Tools.setValueInSharedPreference(this, Constant.OSNAME, Constant.SCREENWIDTH, width);
		Tools.setValueInSharedPreference(this, Constant.OSNAME, Constant.SCREEHEIGHT, Height);
	}
	
}
