package com.exmart.qiyishow.adapter;


import java.util.List;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.bean.Efficacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 特效列表
 * @author ye
 *
 */

public class EffectsListAdapter extends BaseAdapter{
	private Context mContext;
	private ViewHolder mViewHodler;
	private List<Efficacy> mList;
	private LayoutInflater mInflater;
	
	public EffectsListAdapter(Context context, List<Efficacy> list) {
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Efficacy getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.effects_list_item, null);
			mViewHodler = new ViewHolder();
			mViewHodler.ivEffecacyIcon = (ImageView) convertView.findViewById(R.id.iv_efficacy_icon);
			mViewHodler.tvName = (TextView) convertView.findViewById(R.id.effects_title);
			mViewHodler.btnMake = (Button) convertView.findViewById(R.id.btn_record);
			convertView.setTag(mViewHodler);
		} else {
			mViewHodler = (ViewHolder)convertView.getTag();
		}
		Efficacy efficacy = mList.get(position);
		mViewHodler.tvName.setText(efficacy.getName());
		
		return convertView;
	}
	
	class ViewHolder {
		private ImageView ivEffecacyIcon;
		private TextView tvName;
		private Button btnMake;
	}
	
}
