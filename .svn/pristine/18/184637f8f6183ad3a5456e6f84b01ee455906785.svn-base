package com.exmart.qiyishow.adapter;

import java.util.List;

import com.exmart.qiyishow.ui.frame.HomeFrame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BottmTabAdapter extends PagerAdapter 
		implements OnPageChangeListener, OnCheckedChangeListener{
	private ViewPager mViewPager;
	private RadioGroup mGroup;
	private  List<Fragment> mFragments ;
	private FragmentManager mFragmentManager;
	private int currentPageIndex = 0; 
	
	public BottmTabAdapter(FragmentManager fragmentManager, RadioGroup group, ViewPager pager, List<Fragment> fragments) {
        mViewPager = pager;
        mGroup = group;
        mFragments = fragments;
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
        mGroup.setOnCheckedChangeListener(this);
        mFragmentManager = fragmentManager;
    }
	
	public void setback(boolean isback){
		if(isback){
			mViewPager.setCurrentItem(0);
		}
		
	}
	
	public void addFragemnts(final Fragment fragment) {
		mFragments.add(fragment);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}
	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mFragments.get(position).getView());
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = mFragments.get(position);
		if(!fragment.isAdded()){
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			ft.add(fragment, fragment.getClass().getSimpleName());
			ft.commitAllowingStateLoss();
			/**
			 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
			 * 会在进程的主线程中，用异步的方式来执行。
			 * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
			 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
			 */
			mFragmentManager.executePendingTransactions();
		}
		if(fragment.getView().getParent() == null){
			container.addView(fragment.getView());
		}
		return fragment.getView();
	}
	public int getCurrentPageIndex(){
		return currentPageIndex;
	}
	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	@Override
	public void onPageScrolled(int postion, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		mFragments.get(currentPageIndex).onPause();
		if(mFragments.get(position).isAdded()) {
			mFragments.get(position).onResume();
		}
		currentPageIndex = position;
		((RadioButton)mGroup.getChildAt(position)).setChecked(true);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
//		int position = mViewPager.getCurrentItem();
		if(getIndexOfRadioButton(checkedId) == 0){
			HomeFrame.isBack = false;
		}else if(getIndexOfRadioButton(checkedId) == 1){
			HomeFrame.isBack = true;
		}else if(getIndexOfRadioButton(checkedId) == 2){
			HomeFrame.isBack = true;
		}
		mViewPager.setCurrentItem(getIndexOfRadioButton(checkedId));
	}
	private int getIndexOfRadioButton (int checkedId){
		for(int i = 0; i < mGroup.getChildCount(); i++){
			if(mGroup.getChildAt(i).getId() == checkedId){
				return i;
			}
		}
		return 0;
	}
}
