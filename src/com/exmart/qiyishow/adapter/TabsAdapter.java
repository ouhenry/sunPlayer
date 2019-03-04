package com.exmart.qiyishow.adapter;

import java.util.ArrayList;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.support.v4.view.ViewPager;

public class TabsAdapter extends FragmentPagerAdapter 
		implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{
	private Fragment mContext;
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
	
	static final class TabInfo {
		private final String tag;
		private final Class<?> clss;
		private final Bundle args;
		
		TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
	}
	static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }
	public TabsAdapter(Fragment activity, TabHost tabHost, ViewPager pager) {
        super(activity.getChildFragmentManager());
        mContext = activity;
        mTabHost = tabHost;
        mViewPager = pager;
        mTabHost.setOnTabChangedListener(this);
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }
	public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mContext.getActivity()));
        String tag = tabSpec.getTag();
        TabInfo info = new TabInfo(tag, clss, args);
        mTabs.add(info);
        mTabHost.addTab(tabSpec);
        notifyDataSetChanged();
    }

	@Override
	public Fragment getItem(int postion) {
		TabInfo info = mTabs.get(postion);
		String str = info.tag;
		return Fragment.instantiate(mContext.getActivity(), info.clss.getName(), info.args);
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public void onTabChanged(String tabId) {
		int position = mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		
	}

	@Override
	public void onPageScrolled(int postion, float positionOffset, int positionOffsetPixels) {
		
	}

	@Override
	public void onPageSelected(int position) {
		TabWidget widget = mTabHost.getTabWidget();
		int oldFocusability = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		mTabHost.setCurrentTab(position);
		widget.setDescendantFocusability(oldFocusability);
	}
}
