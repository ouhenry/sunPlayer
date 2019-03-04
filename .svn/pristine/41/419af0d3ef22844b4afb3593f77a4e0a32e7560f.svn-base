package com.exmart.qiyishow.ui.setting;

import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.loader.core.ImageLoader;
import com.exmart.qiyishow.loader.core.VideoLoader;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.tools.UpdateManager;
import com.exmart.qiyishow.ui.frame.BaseActivity;
import com.exmart.qiyishow.ui.frame.HomeFrame;
import com.exmart.qiyishow.ui.view.Mydialog;
import com.exmart.qiyishow.ui.view.SlideMenu;
import com.exmart.qiyishow.ui.view.SlipButton;
/**
 * 设置界面
 * 
 *@author henry
 */
public class SettingActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener{
	private RelativeLayout notice_layout;
	private RelativeLayout how_use_layout;
	private RelativeLayout feedback_layout;
	private RelativeLayout check_update_layout;
	private RelativeLayout clear_catch_layout;
	private TabHost mHost;
	public TabHost localHost;
	private static SlideMenu slideMenu;
	private TextView tv_back;
	
	
	private RelativeLayout mydialog_title;
	private RelativeLayout mydialog_content;
	private RelativeLayout mydialog_yes;
	private Mydialog md;
	
	private long cacheSize;
	private Handler mHandler;
	
	private String VcodeName;
	private TextView mText_code;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void loadLayout() {
		setContentView(R.layout.setting_layout);
		notice_layout = (RelativeLayout) findViewById(R.id.setting_notice_layout);
		mText_code = (TextView) findViewById(R.id.Text_code);
		
		tv_back = (TextView) findViewById(R.id.bt_back);
		tv_back.setBackgroundResource(R.drawable.back_bg);
		tv_back.setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.tv_title)).setText(R.string.setting);
		how_use_layout = (RelativeLayout) findViewById(R.id.how_use_layout);
		feedback_layout = (RelativeLayout) findViewById(R.id.feedback_layout);
		check_update_layout = (RelativeLayout) findViewById(R.id.check_update_layout);
		clear_catch_layout = (RelativeLayout) findViewById(R.id.delete_catch_layout);
		String userId = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.USERID);
		if (TextUtils.isEmpty(userId)) {
			findViewById(R.id.btn_logout).setVisibility(View.GONE);
		}
		
		// 获取当前软件包信息
		try {
			PackageInfo pi = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			VcodeName = pi.versionName;
		    } catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			mText_code.setText("V"+VcodeName);
		
		try {
			queryPackageSize(this.getPackageName());
		} catch (Exception e) {
		}
		initHandler();
		
	}
	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				TextView text = (TextView)findViewById(R.id.catch_count_tv);
				text.setText(Tools.getMbCache(cacheSize));
				super.handleMessage(msg);
			}
		};
		
	}
	@Override
	protected void loadListener() {
		notice_layout.setOnClickListener(this);
		how_use_layout.setOnClickListener(this);
		feedback_layout.setOnClickListener(this);
		check_update_layout.setOnClickListener(this);
		clear_catch_layout.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		findViewById(R.id.btn_logout).setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.setting_notice_layout:
			
			break;
		case R.id.how_use_layout:
			intent = new Intent(this, HowUseActivity.class);
			startActivity(intent);
			break;
		case R.id.feedback_layout:
			intent = new Intent(this, FeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.check_update_layout:
			UpdateManager um = new UpdateManager(this, true);
			um.checkUpdate();
			break;
		case R.id.menu_home_layout:
			intent = new Intent(this,HomeFrame.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("rb", 1);
			startActivity(intent);
			break;
		case R.id.menu_setting_layout:
			SettingActivity.slideMenu.closeMenu(); 
			break;
		case R.id.bt_back:
			finish();
			break;
		case R.id.delete_catch_layout:
			ImageLoader.getInstance().getDiskCache().clear();
			VideoLoader.getInstance().getDiskCache().clear();
			TextView text = (TextView)findViewById(R.id.catch_count_tv);
			text.setText("0Mb");
			break;
		case R.id.btn_logout:
			AlertDialog dialog = new AlertDialog.Builder(this)
			.setMessage("确定退出吗?")
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			})
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences preferececs = getSharedPreferences(Constant.SPNAME, Context.MODE_WORLD_WRITEABLE);
					Editor editor = preferececs.edit();
					editor.clear();
					editor.commit();
					Intent intent = new Intent("com.login.status");
					sendBroadcast(intent);
					findViewById(R.id.btn_logout).setVisibility(View.GONE);
				}
			}).create();
			dialog.show();
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 显示关闭SlideMenu
	 */
	public static void ShowSlideMenu(){
		if(slideMenu.isMainScreenShowing()){
			slideMenu.openMenu(); 
		}else{
			slideMenu.closeMenu(); 
		}
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
			if (isChecked) {
				switch (buttonView.getId()) {
				case R.id.radio_home:
					this.mHost.setCurrentTabByTag("VideoHome1");
					break;
				case R.id.radio_template:
					this.mHost.setCurrentTabByTag("VideoTemplate1");
					break;
				case R.id.radio_info:
					this.mHost.setCurrentTabByTag("UserInfo1");
					break;
				}
			}
		
	}
	@Override
	protected void Request() {
		
	}
	@Override
	protected void logic() {
		
	}
	
	private void showMyDialog(){
		View view = getLayoutInflater().inflate(R.layout.mydialog_layout, null);
		mydialog_title = (RelativeLayout) view.findViewById(R.id.mydialog_title_layout);
		mydialog_content = (RelativeLayout) view.findViewById(R.id.content_layout);
		mydialog_yes = (RelativeLayout) view.findViewById(R.id.yes_layout);
		
		mydialog_title.setOnClickListener(this);
		mydialog_content.setOnClickListener(this);
		mydialog_yes.setOnClickListener(this);
		md = new Mydialog(this, 200, 280, view, R.style.mydialog_style);
		md.show();
		
	}
	public class PkgSizeObserver extends IPackageStatsObserver.Stub {

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			cacheSize = pStats.externalCacheSize;
			mHandler.sendEmptyMessage(1);
		}

	}
	
	public void queryPackageSize(String pkgName) throws Exception {
		if (pkgName != null) {
			PackageManager pm = getPackageManager();
			try {
				Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
				getPackageSizeInfo.invoke(pm, pkgName, new PkgSizeObserver());
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	}
	

}
