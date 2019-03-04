package com.exmart.qiyishow.adapter;

import java.util.List;
import com.exmart.qiyishow.R;
import com.exmart.qiyishow.bean.Message;
import com.exmart.qiyishow.tools.MediaAsync;
import com.exmart.qiyishow.tools.VideoLoadAsync;
import com.exmart.qiyishow.ui.view.TextureVideoView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
/**
 * 本地视频列表适配
 * @author 
 *
 */
public class LocalVideoListAdapter extends BaseAdapter{
	private List<String> mList;
	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder mViewHoler;
	
	public LocalVideoListAdapter(Context context, List<String> list){
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mList = list;
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
			convertView = mInflater.inflate(R.layout.local_list_asset, null);
			mViewHoler.mVideoCover = (ImageView) convertView.findViewById(R.id.iv_video_cover);
			mViewHoler.textView = (TextView) convertView.findViewById(R.id.tv_video_title);
			convertView.setTag(mViewHoler);
		} else {
			mViewHoler = (ViewHolder)convertView.getTag();
		}
//		LayoutParams imageParams = (LayoutParams) mViewHoler.imageView.getLayoutParams();
//		imageParams.width  = mWidth/2;
//		imageParams.height = mWidth/2;

//		holder.imageView.setLayoutParams(imageParams);
		String strPath = mList.get(position);
		mViewHoler.textView.setText(strPath.substring(strPath.lastIndexOf("/") + 1, strPath.length()));
		new VideoLoadAsync((Activity)mContext, mViewHoler.mVideoCover, false, 100).executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, mList.get(position));
		
		return convertView;
	}
	class ViewHolder {
		private ImageView mVideoCover;
		private TextView textView;
	}
}
