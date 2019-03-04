package com.exmart.qiyishow.ui.frame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mobstat.StatService;
import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.BottmTabAdapter;
import com.exmart.qiyishow.loader.cache.disc.impl.UnlimitedDiskCache;
import com.exmart.qiyishow.loader.cache.disc.naming.Md5FileNameGenerator;
import com.exmart.qiyishow.loader.cache.memory.impl.LruMemoryCache;
import com.exmart.qiyishow.loader.core.DisplayImageOptions;
import com.exmart.qiyishow.loader.core.ImageLoader;
import com.exmart.qiyishow.loader.core.ImageLoaderConfiguration;
import com.exmart.qiyishow.loader.core.VideoLoader;
import com.exmart.qiyishow.loader.core.VideoLoaderConfiguration;
import com.exmart.qiyishow.loader.core.assist.QueueProcessingType;
import com.exmart.qiyishow.loader.core.display.RoundedBitmapDisplayer;
import com.exmart.qiyishow.loader.core.display.SimpleBitmapDisplayer;
import com.exmart.qiyishow.loader.utils.StorageUtils;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.tools.UpdateManager;
import com.exmart.qiyishow.ui.home.EditDataActivity;
import com.exmart.qiyishow.ui.home.ShowActivity;
import com.exmart.qiyishow.ui.home.VideoOperateSelectFragment;
import com.exmart.qiyishow.ui.setting.SettingActivity;
import com.exmart.qiyishow.ui.user.LoginActivity;
import com.exmart.qiyishow.ui.user.UserInfoActivity;
import com.exmart.qiyishow.ui.view.SlideMenu;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TextView;

/**
 * 首页框架
 * 
 * @author ZhaoYe
 * 
 */
public class HomeFrame extends FragmentActivity implements OnClickListener,OnTouchListener {
	public RadioButton r1, r2, r3;
	public TabHost localHost;
	private static SlideMenu slideMenu;
	private LinearLayout home_layout;
	private LinearLayout setting_layout;
	public static boolean isFirst;
	private BottmTabAdapter mTabAdapter;
	private ViewPager mPager;
	private long exitTime = 0;
	public static Activity instance;
	private List<Fragment> mFragments;
	private RadioGroup group;
	public static boolean isBack = false;
	private static LinearLayout mLayout_menu_bg;
	
	private ImageView mImage_user_photo;
	private TextView mText_user_name;
	private String SP_user_id;
	private String SP_user_name;
	private String SP_user_photo;
	private DisplayImageOptions optionsAvatar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		File cacheDir = StorageUtils.getIndividualCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		.memoryCacheSize(2 * 1024 * 1024)
		.denyCacheImageMultipleSizesInMemory()
		.diskCache(new UnlimitedDiskCache(cacheDir))
		.diskCacheFileCount(100)
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs()
		.build();
		ImageLoader.getInstance().init(config);
		
		File cacheVideoDir = StorageUtils.getIndividualVideoDirectory(getApplicationContext());
		VideoLoaderConfiguration videoConfig = new VideoLoaderConfiguration.Builder(getApplicationContext())
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.diskCache(new UnlimitedDiskCache(cacheVideoDir))
		.writeDebugLogs()
		.build();
		VideoLoader.getInstance().init(videoConfig);
		Tools.setWindow(this);
		setContentView(R.layout.homeframe);
		instance = this;
		init();
		IntentFilter filter = new IntentFilter("com.login.status");
		registerReceiver(new LoginStatusReciver(), filter);
	}

	/**
	 * 初始化底部按钮
	 */

	public void init() {
		mFragments = new ArrayList<Fragment>();
		r1 = ((RadioButton) findViewById(R.id.radio_home));
		r1.setOnClickListener(this);
		r1.setChecked(true);
		r2 = ((RadioButton) findViewById(R.id.radio_template));
		r3 = ((RadioButton) findViewById(R.id.radio_info));
		slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
		home_layout = (LinearLayout) findViewById(R.id.menu_home_layout);
		setting_layout = (LinearLayout) findViewById(R.id.menu_setting_layout);
		mLayout_menu_bg = (LinearLayout) findViewById(R.id.layout_slideMenu_bg);
		
		mLayout_menu_bg.setOnTouchListener(this);
		home_layout.setOnClickListener(this);
		setting_layout.setOnClickListener(this);
		group = (RadioGroup) findViewById(R.id.myShedle_radio);
		mPager = (ViewPager) findViewById(R.id.pager);
		mFragments.add(new ShowActivity());
		mFragments.add(new VideoOperateSelectFragment());
		mFragments.add(new UserInfoActivity());
		mTabAdapter = new BottmTabAdapter(getSupportFragmentManager(), group, mPager, mFragments);
//		mTabAdapter.addTab(ShowActivity.class, null);
//		mTabAdapter.addTab(VideoOperateSelectFragment.class, null);
//		mTabAdapter.addTab(UserInfoActivity.class, null);
		mImage_user_photo = (ImageView) findViewById(R.id.user_title_image);
		mText_user_name = (TextView) findViewById(R.id.tv_user_name);
		mImage_user_photo.setOnClickListener(this);
		
		SP_user_id = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.USERID);
		SP_user_name = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.NAME);
		SP_user_photo = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.PHOTO);
		
		optionsAvatar = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.displayer(new SimpleBitmapDisplayer())
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(180))
		.build();
		mText_user_name.setText(SP_user_name);
		ImageLoader.getInstance().displayImage(SP_user_photo, mImage_user_photo, optionsAvatar);
		
		Log.d("data", "-------------------------版本更新执行-------------------------");
		UpdateManager um = new UpdateManager(this, false);
		um.checkUpdate();
	}

	/**
	 * 显示关闭SlideMenu
	 */
	public static void ShowSlideMenu() {
		if (slideMenu.isMainScreenShowing()) {
			slideMenu.openMenu();
			mLayout_menu_bg.setVisibility(View.VISIBLE);
		} else {
			slideMenu.closeMenu();
			mLayout_menu_bg.setVisibility(View.GONE);
		}
	}


	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.menu_home_layout:
			HomeFrame.slideMenu.closeMenu();
			mLayout_menu_bg.setVisibility(View.GONE);
			// this.mHost.setCurrentTabByTag("VideoHome");
			r1.setChecked(true);
			mPager.setCurrentItem(0);
			break;
		case R.id.menu_setting_layout:
			intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		case R.id.user_title_image:
			String strUserId = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.USERID);
			if (TextUtils.isEmpty(strUserId)) {
				intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent (this,EditDataActivity.class);
				startActivity(intent);
			}
			break;
		}

	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(!slideMenu.isMainScreenShowing()){
			slideMenu.closeMenu();
			mLayout_menu_bg.setVisibility(View.GONE);
		}
		return true;
	}
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
		if(Constant.isRefresh){
			SP_user_name = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.NAME);
			SP_user_photo = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.PHOTO);
			mText_user_name.setText(SP_user_name);
			ImageLoader.getInstance().displayImage(SP_user_photo, mImage_user_photo, optionsAvatar);
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if(isBack){
				mTabAdapter.setback(isBack);
				isBack = false;
			}else if(!slideMenu.isMainScreenShowing()){
				slideMenu.closeMenu();
				mLayout_menu_bg.setVisibility(View.GONE);
			}else if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(),getString(R.string.Home_Back_Toast), Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				isBack = false;
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	class LoginStatusReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.login.status")) {
				replayView();
			}
		}
	}
	private void replayView() {
		String strUserId = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.USERID);
		if (TextUtils.isEmpty(strUserId)) {
			mText_user_name.setText("未登录");
			mImage_user_photo.setImageResource(R.drawable.login_default_avatar);
		} else {
			String strUserName = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.NAME);
			mText_user_name.setText(strUserName);
			SP_user_photo = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.PHOTO);
			ImageLoader.getInstance().displayImage(SP_user_photo, mImage_user_photo, optionsAvatar);
		}
	}
	@Override
	protected void onNewIntent(Intent intent) {
		r3.setChecked(true);
		Intent intentInfo = new Intent("com.info.status");
		sendBroadcast(intentInfo);
		super.onNewIntent(intent);
	}
}