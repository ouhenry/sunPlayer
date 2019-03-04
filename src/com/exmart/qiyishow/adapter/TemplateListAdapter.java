package com.exmart.qiyishow.adapter;

import java.util.ArrayList;
import java.util.List;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.bean.TemplateVideo;
import com.exmart.qiyishow.loader.core.DisplayImageOptions;
import com.exmart.qiyishow.loader.core.ImageLoader;
import com.exmart.qiyishow.loader.core.display.SimpleBitmapDisplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 视频模板列表
 * @author ye
 *
 */
public class TemplateListAdapter extends BaseAdapter{
	
	private Holder mHolder;
	private Context context;
	private List<TemplateVideo> mArrayList;
	private DisplayImageOptions options;
	
	public TemplateListAdapter(Context context,List<TemplateVideo> mArrayList){
		this.context = context;
		this.mArrayList = mArrayList;
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.displayer(new SimpleBitmapDisplayer())
		.considerExifParams(true)
		.build();
	}

	@Override
	public int getCount() {
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			mHolder = new Holder();
			convertView = LayoutInflater.from(context).inflate(R.layout.video_list_asset, null);
			mHolder.mImageVideoAsset = (ImageView)convertView.findViewById(R.id.iv_video_picture);
			mHolder.mImageUserAvatar = (ImageView)convertView.findViewById(R.id.iv_user_avatar);
			mHolder.mTextName = (TextView)convertView.findViewById(R.id.tv_video_title);
			convertView.setTag(mHolder);
		}else{
			mHolder = (Holder) convertView.getTag();
		}
		mHolder.mTextName.setText(mArrayList.get(position).getTname());
		ImageLoader.getInstance().displayImage(mArrayList.get(position).getPicurl(), mHolder.mImageVideoAsset, options);
		return convertView;
	}
	
	private class Holder {
		private ImageView mImageVideoAsset;
		private ImageView mImageUserAvatar;
		private TextView mTextName;
	}

}
