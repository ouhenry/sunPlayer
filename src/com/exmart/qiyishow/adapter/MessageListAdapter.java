package com.exmart.qiyishow.adapter;

import java.util.List;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.bean.Message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageListAdapter extends BaseAdapter{
	private List<Message> mList;
	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder mViewHoler;
	
	public MessageListAdapter(Context context, List<Message> list){
		this.mContext = context;
		this.mList = list;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Message getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.message_list_asset, null);
			mViewHoler = new ViewHolder();
			mViewHoler.ivMessagePicture = (ImageView) convertView.findViewById(R.id.iv_sender_avatar);
			mViewHoler.tvMessageContent = (TextView) convertView.findViewById(R.id.tv_message_content);
			mViewHoler.tvMessageSender = (TextView) convertView.findViewById(R.id.tv_message_sender);
			mViewHoler.tvMessageDate = (TextView) convertView.findViewById(R.id.tv_message_date);
			mViewHoler.tvMessageType = (TextView) convertView.findViewById(R.id.tv_message_type);
			convertView.setTag(mViewHoler);
		} else {
			mViewHoler = (ViewHolder)convertView.getTag();
		}
		Message message = mList.get(position);
		mViewHoler.tvMessageContent.setText(message.getMessageContent());
		mViewHoler.tvMessageDate.setText(message.getMessageDate());
		mViewHoler.tvMessageSender.setText(message.getMessageTitle());
		if(message.getMessageType().endsWith("1")){
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.purple));
		}
		return convertView;
	}
	class ViewHolder {
		private ImageView ivMessagePicture;
		private TextView tvMessageContent;
		private TextView tvMessageType;
		private TextView tvMessageDate;
		private TextView tvMessageSender;
	}
}
