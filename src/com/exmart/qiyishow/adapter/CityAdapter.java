package com.exmart.qiyishow.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.bean.CityBaseBean;

/**
 * @author WuHongBo
 * 
 */
public class CityAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<CityBaseBean> mArrayList;

	public CityAdapter(ArrayList<CityBaseBean> mArrayList, Context mContext) {
		this.mContext = mContext;
		this.mArrayList = mArrayList;
	}

	@Override
	public int getCount() {
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder mHolder = null;
		if (convertView == null) {
			mHolder = new Holder();
			convertView = LinearLayout.inflate(mContext, R.layout.city_item, null);
			mHolder.mTextName = (TextView)convertView.findViewById(R.id.name);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}
		mHolder.mTextName.setText(mArrayList.get(position).DivisionName);
		return convertView;
	}

	private class Holder {
		TextView mTextName;
	}
}
