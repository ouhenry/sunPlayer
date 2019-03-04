package com.exmart.qiyishow.adapter;

import java.util.ArrayList;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
/**
 * Help Adapter
 * @author ZhaoYe
 *
 */
public class HelpViewPagerAdapter extends PagerAdapter{
	private ArrayList<View> arraylist;
	
	public HelpViewPagerAdapter(ArrayList<View> arraylist){
		this.arraylist = arraylist;
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {

		((ViewPager) container).removeView(arraylist.get(position));

	}
	@Override
	public Object instantiateItem(View container, int position) {

		((ViewPager) container).addView(arraylist.get(position));

		return arraylist.get(position);

	}
}
