package com.exmart.qiyishow.ui.home;

import java.util.Calendar;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.TabsAdapter;
import com.exmart.qiyishow.ui.frame.ActiveDetailActiviey;
import com.exmart.qiyishow.ui.frame.HomeFrame;
import com.exmart.qiyishow.ui.frame.VideoGridFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
/**
 * 首页
 * 
 *
 */
public class ShowActivity extends Fragment implements OnTabChangeListener,OnClickListener{
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private long exitTime = 0;
	private FragmentActivity mActivity;
	private FragmentManager mFragmentManager;
	private TextView mText_left;
	private TextView mText_title;
	
	private ImageView mIvActive;
	private ImageButton mHideActive;
	private RelativeLayout mRlActive;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.show_layout, container, false);
		return view;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mFragmentManager = getChildFragmentManager();
		mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		mText_left = (TextView) view.findViewById(R.id.bt_back);
		mText_title = (TextView) view.findViewById(R.id.tv_title);
		mText_left.setBackgroundResource(R.drawable.ic_open_left_menu_normal);
		mText_left.setVisibility(View.VISIBLE);
		mText_title.setText(R.string.app_name);
		View viewIndicate = LayoutInflater.from(this.getActivity()).inflate(R.layout.tab_indicate, null);
		TextView tvTitle = (TextView) viewIndicate.findViewById(R.id.tv_tab_title);
		tvTitle.setText("最新");
		TabSpec tabSpecHot = mTabHost.newTabSpec("最新").setIndicator(viewIndicate);
		tabSpecHot.setContent(R.id.tab_1);
		mTabHost.addTab(tabSpecHot);
		mIvActive = (ImageView) view.findViewById(R.id.iv_active);
		mHideActive = (ImageButton) view.findViewById(R.id.ib_hide_active);
		mRlActive = (RelativeLayout) view.findViewById(R.id.rl_active);
		mIvActive.setOnClickListener(this);
		mHideActive.setOnClickListener(this);
		if (getOverTime() > System.currentTimeMillis()) {
			mRlActive.setVisibility(View.VISIBLE);
		}
		View view2 = LayoutInflater.from(this.getActivity()).inflate(R.layout.tab_indicate, null);
		TextView tvTitle2 = (TextView) view2.findViewById(R.id.tv_tab_title);
		tvTitle2.setText("热门");
		TabSpec tabSpecNew = mTabHost.newTabSpec("热门").setIndicator(view2);
		tabSpecNew.setContent(R.id.tab_2);
		mTabHost.addTab(tabSpecNew);
		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(0);
		updateTab("最新", R.id.tab_1, "2");
		
		if(savedInstanceState != null){
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
		
		initListener();
	}
	private long getOverTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 3, 5);
		return calendar.getTimeInMillis();
	}
	
	protected void initListener() {
		mText_left.setOnClickListener(ShowActivity.this);
	}
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}
	@Override
	public void onTabChanged(String tabId) {
		if(tabId.equals("最新")){
			updateTab(tabId, R.id.tab_1, "2");
		}else if(tabId.equals("热门")){
			updateTab(tabId, R.id.tab_2, "1");
		}
	}
	private void updateTab(String tabId, int placeholder, String arg){
		if(mFragmentManager.findFragmentById(placeholder) == null) {
			mFragmentManager.beginTransaction().
			replace(placeholder, VideoGridFragment.getVideoGridFragmentInstance(arg), tabId).commit();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_back:
			HomeFrame.ShowSlideMenu();
			break;
		case R.id.iv_active:
			WebView view = new WebView(this.getActivity());
			final String url = "http://weibo.com/p/1001603808728141969769?mod=zwenzhang";
			view.loadUrl(url);
//			Intent intent = new Intent(getActivity(), ActiveDetailActiviey.class);
//			startActivity(intent);
			break;
		case R.id.ib_hide_active:
			mRlActive.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		
	}
}