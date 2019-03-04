package com.exmart.qiyishow.ui.frame;

import java.util.ArrayList;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.HelpViewPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 帮助页
 * 
 * @author ZhaoYe
 * 
 */
public class GuideActivity extends BaseActivity implements OnClickListener {
	private ViewPager mViewPager;
	private View mView_one;
	private View mView_two;
	private View mView_three;
	private ArrayList<View> mView_list;
	private PagerAdapter mHelpViewPagerAdapter;
	private Intent intent;
	private Button mButton_begin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_guide);
		mViewPager = (ViewPager) findViewById(R.id.Help_ViewPager);
		LayoutInflater lf = LayoutInflater.from(this);
		mView_one = lf.inflate(R.layout.view_help_one, null);
		mView_two = lf.inflate(R.layout.view_help_two, null);
		mView_three = lf.inflate(R.layout.view_help_three, null);
		mButton_begin = (Button) mView_three.findViewById(R.id.btn_begin);

		mView_list = new ArrayList<View>();
		mView_list.add(mView_one);
		mView_list.add(mView_two);
		mView_list.add(mView_three);

		mHelpViewPagerAdapter = new HelpViewPagerAdapter(mView_list);
		mViewPager.setAdapter(mHelpViewPagerAdapter);

	}

	@Override
	protected void loadListener() {
		mButton_begin.setOnClickListener(this);
	}

	@Override
	protected void Request() {

	}

	@Override
	protected void logic() {

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_begin:
			intent = new Intent(GuideActivity.this,HomeFrame.class);
			startActivity(intent);
			finish();
			break;
		}

	}

}
