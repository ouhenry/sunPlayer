package com.exmart.qiyishow.ui.user;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.analysisJson.JSONCommon;
import com.exmart.qiyishow.analysisJson.UserJson;
import com.exmart.qiyishow.bean.CommData;
import com.exmart.qiyishow.bean.User;
import com.exmart.qiyishow.loader.core.DisplayImageOptions;
import com.exmart.qiyishow.loader.core.ImageLoader;
import com.exmart.qiyishow.loader.core.display.RoundedBitmapDisplayer;
import com.exmart.qiyishow.loader.core.display.SimpleBitmapDisplayer;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Data;
import com.exmart.qiyishow.tools.HttpNetwork;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.frame.UserVideoGridFragment;
import com.exmart.qiyishow.ui.home.EditDataActivity;
import com.exmart.qiyishow.ui.home.MessageActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.SparseArrayCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 
 * 用户主页
 * 
 */
public class UserInfoActivity extends Fragment implements OnClickListener,
		OnCheckedChangeListener, PlatformActionListener, Callback, ScrollTabHolder {
	private TextView mTvName;

	private ImageView mIvAvatar;
	private ScrollTabHolderFragment mAlreadyPublic;
	private ScrollTabHolderFragment mNoPublic;
	private RadioButton mRbAready;
	private RadioButton mRbWill;
	private Activity mActiviy;
	private TextView mText_title_right;
	private DisplayImageOptions optionsAvatar;
	
	private final static String ALREADY_REALSE = "3";
	private final static String WILL_REALSE = "4";

	String SP_user_id;
	String SP_user_name;
	String SP_user_photo;
	
	private TextView mText_QQ;
	private TextView mText_Sina;
	private View mView;
	private Handler mHandler;
	
	private static AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();

	private View mHeader;

	private int mMinHeaderHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;

	private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
	private FrameLayout flAlLogin;
//	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_layout, container, false);
		mActiviy = getActivity();
		IntentFilter filter = new IntentFilter("com.login.status");
		mActiviy.registerReceiver(new LoginStatusReciver(), filter);
		IntentFilter filterInfo = new IntentFilter("com.info.status");
		mActiviy.registerReceiver(new InfoStatusReciver(), filterInfo);
		ShareSDK.initSDK(mActiviy);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mView = view;
		mHandler = new Handler(this);
		SP_user_id = Tools.getValueInSharedPreference(getActivity(),
				Constant.SPNAME, Constant.USERID);
		mText_QQ = (TextView) view.findViewById(R.id.qq_login);
		mText_Sina = (TextView) view.findViewById(R.id.sina_login);
		mText_QQ.setOnClickListener(this);
		mText_Sina.setOnClickListener(this);
		TextView title = (TextView) view.findViewById(R.id.tv_title);
		title.setText(getString(R.string.edit_my_info_title));
		mTvName = (TextView) view.findViewById(R.id.tv_user_name);
		mIvAvatar = (ImageView) view.findViewById(R.id.header_logo);
		mIvAvatar.setOnClickListener(this);
		flAlLogin = (FrameLayout) view.findViewById(R.id.fl_al_login);
		replaceView(mView);
		
		
		mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
		mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
		mMinHeaderTranslation = -mMinHeaderHeight;

		mHeader = view.findViewById(R.id.header);
	}
	

	private void initView(View view) {
		view.findViewById(R.id.realease_fragment_content).setVisibility(View.VISIBLE);
		SP_user_id = Tools.getValueInSharedPreference(getActivity(),
				Constant.SPNAME, Constant.USERID);
		SP_user_name = Tools.getValueInSharedPreference(getActivity(),
				Constant.SPNAME, Constant.NAME);
		SP_user_photo = Tools.getValueInSharedPreference(getActivity(),
				Constant.SPNAME, Constant.PHOTO);
		
//		mText_title_left = (TextView) view.findViewById(R.id.bt_back);
		mText_title_right = (TextView) view.findViewById(R.id.bt_title_right);
		
//		mText_title_left.setVisibility(View.VISIBLE);
		mText_title_right.setVisibility(View.VISIBLE);

//		mText_title_left.setBackgroundResource(R.drawable.userinfo_edit_bg);
		mText_title_right.setBackgroundResource(R.drawable.userinfo_message_bg);

//		mText_title_left.setOnClickListener(this);
		mText_title_right.setOnClickListener(this);

		view.findViewById(R.id.rg_tab).setVisibility(View.VISIBLE);
		
		((RadioGroup) view.findViewById(R.id.rg_tab))
				.setOnCheckedChangeListener(this);
		mRbAready = (RadioButton) view.findViewById(R.id.rb_already);
		mRbWill = (RadioButton) view.findViewById(R.id.rb_will);
		mRbAready.setChecked(true);
		
		/** 加载头像 */
		optionsAvatar = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).displayer(new SimpleBitmapDisplayer())
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(160)).build();
		mTvName.setText(SP_user_name);
		ImageLoader.getInstance().displayImage(SP_user_photo, mIvAvatar,
				optionsAvatar);
	}
	
	private void replaceView(View view){
		RadioGroup group = ((RadioGroup) view.findViewById(R.id.rg_tab));
		group.setOnCheckedChangeListener(this);
		SP_user_id = Tools.getValueInSharedPreference(mActiviy, Constant.SPNAME, Constant.USERID);
		if (TextUtils.isEmpty(SP_user_id)) {
			group.setVisibility(View.GONE);
			flAlLogin.setVisibility(View.GONE);
			view.findViewById(R.id.realease_fragment_content).setVisibility(View.GONE);
			view.findViewById(R.id.rl_login_tip).setVisibility(View.VISIBLE);
			view.findViewById(R.id.ll_login_way).setVisibility(View.VISIBLE);
			mTvName.setText("");
			mIvAvatar.setBackgroundResource(R.drawable.login_default_avatar);
			mIvAvatar.setImageBitmap(null);
		} else {
			flAlLogin.setVisibility(View.VISIBLE);
			view.findViewById(R.id.rl_login_tip).setVisibility(View.GONE);
			view.findViewById(R.id.ll_login_way).setVisibility(View.GONE);
			initView(mView);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.bt_title_right:
			intent = new Intent(mActiviy, MessageActivity.class);
			startActivity(intent);
			break;
		case R.id.qq_login:
			authorize(new QQ(mActiviy));
			break;
		case R.id.sina_login:
			authorize(new SinaWeibo(mActiviy));
			break;
		case R.id.header_logo:// ==============
			if (TextUtils.isEmpty(SP_user_id)) {
				
			} else {
				intent = new Intent(mActiviy, EditDataActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}
	
	private void authorize(Platform plat) {
		if (plat.isValid()) {
			plat.removeAccount();
		}
		plat.setPlatformActionListener(this);
		plat.showUser(null);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		if (mRbAready.getId() == checkedId) {
			FragmentTransaction transaction = getChildFragmentManager()
					.beginTransaction();
			if (mAlreadyPublic == null) {
				mAlreadyPublic = UserVideoGridFragment
						.getUserVideoGridFragmentInstance(ALREADY_REALSE);
				transaction.add(R.id.realease_fragment_content, mAlreadyPublic);
				if (mNoPublic != null) {
					transaction.hide(mNoPublic);
				}
				transaction.commit();
				mAlreadyPublic.setScrollTabHolder(this);
				mScrollTabHolders.put(0, mAlreadyPublic);
				
			} else {
				transaction = getChildFragmentManager().beginTransaction();
				transaction.show(mAlreadyPublic);
				if (mNoPublic != null) {
					transaction.hide(mNoPublic);
				}
				 transaction.commitAllowingStateLoss();
				mAlreadyPublic.setScrollTabHolder(this);
				mScrollTabHolders.put(0, mAlreadyPublic);
			}
			mRbAready.setTextColor(getResources().getColor(R.color.blue_user));
			mRbWill.setTextColor(getResources().getColor(R.color.white));
		} else if (mRbWill.getId() == checkedId) {
			FragmentTransaction transaction = getChildFragmentManager()
					.beginTransaction();
			if (mNoPublic == null) {

				mNoPublic = UserVideoGridFragment.getUserVideoGridFragmentInstance(WILL_REALSE);
				transaction.add(R.id.realease_fragment_content, mNoPublic);
				transaction.hide(mAlreadyPublic);
				transaction.commit();
				mNoPublic.setScrollTabHolder(this);
				mScrollTabHolders.put(1, mNoPublic);
			} else {
				transaction.show(mNoPublic);
				transaction.hide(mAlreadyPublic);
				transaction.commit();
				mNoPublic.setScrollTabHolder(this);
				mScrollTabHolders.put(1, mNoPublic);
			}
			mRbWill.setTextColor(getResources().getColor(R.color.blue_user));
			mRbAready.setTextColor(getResources().getColor(R.color.white));
		}
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		if (arg1 == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(Constant.MSG_AUTH_CANCEL, this);
		}
	}

	@Override
	public void onComplete(final Platform arg0, int action,HashMap<String, Object> arg2) {
		if (action == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(Constant.MSG_AUTH_COMPLETE, this);
			arg0.setPlatformActionListener(null);
			String userId = arg0.getDb().getUserId();
			String userName =  arg0.getDb().getUserName();
			String userPhoto = arg0.getDb().getUserIcon();
//			String token = arg0.getDb().getToken();
			LoginRequest(userId, userPhoto, userName);
		}
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		if (arg1 == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(Constant.MSG_AUTH_ERROR, this);
		}
		arg2.printStackTrace();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constant.MSG_USERID_FOUND: 
			Toast.makeText(mActiviy, R.string.userid_found, Toast.LENGTH_SHORT).show();
			break;
		case Constant.MSG_LOGIN: 
			String text = getString(R.string.logining, msg.obj);
			Toast.makeText(mActiviy, text, Toast.LENGTH_SHORT).show();
			initView(mView);
			break;
		case Constant.MSG_AUTH_CANCEL: 
			Toast.makeText(mActiviy, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
			break;
		case Constant.MSG_AUTH_ERROR: 
			Toast.makeText(mActiviy, R.string.auth_error, Toast.LENGTH_SHORT).show();
			break;
		case Constant.MSG_AUTH_COMPLETE: 
			Toast.makeText(mActiviy, R.string.auth_complete, Toast.LENGTH_SHORT).show();
			break;
		case 10:
			User user = (User) msg.obj;
			Tools.setValueInSharedPreference(mActiviy, Constant.SPNAME, Constant.USERID, user.getId());
			Tools.setValueInSharedPreference(mActiviy, Constant.SPNAME, Constant.NAME, user.getNikeName());
			Tools.setValueInSharedPreference(mActiviy, Constant.SPNAME, Constant.SEX, user.getSex());
			Tools.setValueInSharedPreference(mActiviy, Constant.SPNAME, Constant.PHOTO, user.getImageUrl());
			Tools.setValueInSharedPreference(mActiviy, Constant.SPNAME, Constant.PROVINCE, user.getProvince());
			Tools.setValueInSharedPreference(mActiviy, Constant.SPNAME, Constant.CITY, user.getCity());
			Tools.setValueInSharedPreference(mActiviy, Constant.SPNAME, Constant.AREA, user.getArea());
			mView.findViewById(R.id.rl_login_tip).setVisibility(View.GONE);
			mView.findViewById(R.id.ll_login_way).setVisibility(View.GONE);
			Intent intent = new Intent("com.login.status");
			mActiviy.sendBroadcast(intent);
			initView(mView);
			break;
		case 20:
			String str = (String)msg.obj;
			Toast.makeText(mActiviy, str, Toast.LENGTH_SHORT).show();
			break;
		}
		return false;
	}
	private void LoginRequest(final String LoginId,final String PhotoUrl,final String UserName){
		new Thread(){
			public void run() {
				String result = HttpNetwork.httpNetwork(Data.ThreeUserLogin(mActiviy, LoginId, PhotoUrl, UserName), Constant.THREEUSERLOGIN, mActiviy);
				CommData commData = null;
				User user = null;
				try {
					commData = new JSONCommon().getCommonCode(result);
					if (commData.getCode().equals("1")) {
						UserJson json = new UserJson(commData.getJosnObj());
						user = json.parse();
						Message msg = mHandler.obtainMessage(10);
						msg.obj = user;
						msg.sendToTarget();
					}else{
						Message msg = mHandler.obtainMessage(20);
						msg.obj = commData.getMsg();
						msg.sendToTarget();
						msg.sendToTarget();	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
			
		}.start();
	}
	class LoginStatusReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.login.status")) {
				replaceView(mView);
			}
		}
	}
	class InfoStatusReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.info.status")) {
				mRbWill.setChecked(true);
			}
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		if (mViewPager.getCurrentItem() == pagePosition) {
			int scrollY = getScrollY(view);
//			ViewHelper.setTranslationY(mHeader, Math.max(-scrollY, mMinHeaderTranslation));
			mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
		}
	

	@Override
	public void adjustScroll(int scrollHeight) {
		// nothing
	}

	public int getScrollY(AbsListView view) {
		View c = view.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = view.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mHeaderHeight;
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}


	private RectF getOnScreenRect(RectF rect, View view) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
		return rect;
	}

}