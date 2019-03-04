package com.exmart.qiyishow.ui.user;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
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
import com.exmart.qiyishow.loader.core.ImageLoader;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Data;
import com.exmart.qiyishow.tools.HttpNetwork;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.frame.BaseActivity;
import com.exmart.qiyishow.ui.home.EditDataActivity;

/**
 * 登录页面
 * 
 * @author
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener, PlatformActionListener, Callback {
	
	private TextView mText_QQ;
	private TextView mText_Sina;
	private String mUserId;
	private String mUserName;
	private String mToken;
	private String mUserPhoto;
	private CommData commData;
	private User user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.login_layout);
		TextView tvBack = (TextView) findViewById(R.id.bt_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setBackgroundResource(R.drawable.back_bg);
		tvBack.setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title)).setText(getString(R.string.user_login));
		mText_QQ = (TextView) findViewById(R.id.qq_login);
		mText_Sina = (TextView) findViewById(R.id.sina_login);
	}

	@Override
	protected void loadListener() {
		mText_QQ.setOnClickListener(this);
		mText_Sina.setOnClickListener(this);
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
		case R.id.qq_login:
			authorize(new QQ(LoginActivity.this));
			break;
		case R.id.sina_login:
			authorize(new SinaWeibo(LoginActivity.this));
			break;
		case R.id.bt_back:
			finish();
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
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}
	
	private void LoginRequest(final String LoginId,final String PhotoUrl,final String UserName){
		new Thread(){
			public void run() {
				String result = HttpNetwork.httpNetwork(Data.ThreeUserLogin(LoginActivity.this, LoginId, PhotoUrl, UserName), Constant.THREEUSERLOGIN, LoginActivity.this);
				Log.d("data", "result="+result);
				try {
					commData = new JSONCommon().getCommonCode(result);
					if (commData.getCode().equals("1")) {
						UserJson json = new UserJson(commData.getJosnObj());
						user = json.parse();
						handler.sendEmptyMessage(10);
					}else{
						handler.sendEmptyMessage(20);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
			
		}.start();
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constant.MSG_USERID_FOUND: 
			Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT).show();
			break;
		case Constant.MSG_LOGIN: 
			String text = getString(R.string.logining, msg.obj);
			Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			finish();
			break;
		case Constant.MSG_AUTH_CANCEL: 
			Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
			break;
		case Constant.MSG_AUTH_ERROR: 
			Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
			break;
		case Constant.MSG_AUTH_COMPLETE: 
			Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
			break;
		}
		return false;
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
			mUserId = arg0.getDb().getUserId();
			mUserName =  arg0.getDb().getUserName();
			mUserPhoto = arg0.getDb().getUserIcon();
			String token = arg0.getDb().getToken();
			Log.e("data", mUserId+"===name:"+mUserName+"===icon:"+mUserPhoto);
			LoginRequest(mUserId, mUserPhoto, mUserName);
		}
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		if (arg1 == Platform.ACTION_USER_INFOR) {
			UIHandler.sendEmptyMessage(Constant.MSG_AUTH_ERROR, this);
		}
		arg2.printStackTrace();
	}
	
	Handler handler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 10:
				Tools.setValueInSharedPreference(LoginActivity.this, Constant.SPNAME, Constant.USERID, user.getId());
				Tools.setValueInSharedPreference(LoginActivity.this, Constant.SPNAME, Constant.NAME, user.getNikeName());
				Tools.setValueInSharedPreference(LoginActivity.this, Constant.SPNAME, Constant.SEX, user.getSex());
				Tools.setValueInSharedPreference(LoginActivity.this, Constant.SPNAME, Constant.PHOTO, user.getImageUrl());
				Tools.setValueInSharedPreference(LoginActivity.this, Constant.SPNAME, Constant.PROVINCE, user.getProvince());
				Tools.setValueInSharedPreference(LoginActivity.this, Constant.SPNAME, Constant.CITY, user.getCity());
				Tools.setValueInSharedPreference(LoginActivity.this, Constant.SPNAME, Constant.AREA, user.getArea());
				Constant.isRefresh = true;
				Toast.makeText(LoginActivity.this, commData.getMsg(), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent("com.login.status");
				sendBroadcast(intent);
				finish();
				break;
			case 20:
				Toast.makeText(LoginActivity.this, commData.getMsg(), Toast.LENGTH_SHORT).show();
				break;
			};
		}
	};

}
