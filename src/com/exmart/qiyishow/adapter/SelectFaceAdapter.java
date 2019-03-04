package com.exmart.qiyishow.adapter;

import java.util.List;
import com.exmart.qiyishow.R;
import com.exmart.qiyishow.bean.Message;
import com.exmart.qiyishow.loader.core.DisplayImageOptions;
import com.exmart.qiyishow.loader.core.ImageLoader;
import com.exmart.qiyishow.loader.core.display.RoundedBitmapDisplayer;
import com.exmart.qiyishow.loader.core.display.SimpleBitmapDisplayer;
import com.exmart.qiyishow.ui.view.TextureVideoView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
/**
 * 选择头像
 * @author ye
 *
 */
public class SelectFaceAdapter extends BaseAdapter{
	private List<String> mList;
	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder mViewHoler;
	private DisplayImageOptions options;
	
	public SelectFaceAdapter(Context context, List<String> pathList){
		this.mContext = context;
		this.mList = pathList;
		mInflater = LayoutInflater.from(mContext);
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.displayer(new SimpleBitmapDisplayer())
		.considerExifParams(true)
		.build();
	}
	

	public DisplayImageOptions getOptions() {
		return options;
	}


	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public String getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			mViewHoler = new ViewHolder();
			convertView = mInflater.inflate(R.layout.select_face_item, null);
			mViewHoler.ivHead = (ImageView)convertView.findViewById(R.id.iv_image_head);
			convertView.setTag(mViewHoler);
		} else {
			mViewHoler = (ViewHolder)convertView.getTag();
		}
		String path = mList.get(position);
		String load = "file://" + path;
		ImageLoader.getInstance().displayImage(load, mViewHoler.ivHead, options, null);
		return convertView;
	}
	class ViewHolder {
		private ImageView ivHead;
	}
}
