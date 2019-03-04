package com.exmart.qiyishow.adapter;




import java.util.List;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.bean.Comment;
import com.exmart.qiyishow.loader.core.DisplayImageOptions;
import com.exmart.qiyishow.loader.core.ImageLoader;
import com.exmart.qiyishow.loader.core.display.RoundedBitmapDisplayer;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.video.VideoDetailActivity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 评论列表
 *
 */

public class CommentAdapter extends BaseAdapter{
	private Context context;
	private Holder mHolder;
	private List<Comment> mList;
	private DisplayImageOptions optionsAvatar;
	
	public CommentAdapter(Context context, List<Comment> list){
		this.context = context;
		this.mList = list;
		optionsAvatar = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(60))
		.build();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Comment getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			mHolder = new Holder();
			convertView = LayoutInflater.from(context).inflate(R.layout.comment_list_item, null);
			mHolder.R_layout = (RelativeLayout) convertView.findViewById(R.id.layout_frame);
			mHolder.commentContent = (TextView) convertView.findViewById(R.id.comment_content);
			mHolder.commentDate = (TextView) convertView.findViewById(R.id.comment_time);
			mHolder.commentUser = (TextView) convertView.findViewById(R.id.comment_user);
			mHolder.roundImageView = (ImageView) convertView.findViewById(R.id.user_photo);
			mHolder.ivReplay = (ImageView) convertView.findViewById(R.id.iv_comment_replay);
			convertView.setTag(mHolder);
		}else{
			mHolder = (Holder) convertView.getTag();
		}
		if (position%2 == 0) {
			mHolder.R_layout.setBackgroundResource(R.color.list_single);
		} else {
			mHolder.R_layout.setBackgroundResource(R.color.gray_author);
		}
		
		Comment comment = mList.get(position);
		mHolder.commentContent.setText(comment.getContent());
		mHolder.commentDate.setText(Tools.stringDate2jiange(comment.getDate()));
		mHolder.commentUser.setText(comment.getCommentUserName());
//		String replayCommentId = comment.getReplayCommentId();
		String replayUserId = comment.getReplayUserId();
		
		if(!TextUtils.isEmpty(replayUserId)){
			mHolder.commentContent.setText("回复" + Html.fromHtml("@" + comment.getReplayUserName()) + ": " + comment.getContent());
		}
		ImageLoader.getInstance().displayImage(comment.getAutherAvatarUrl(), mHolder.roundImageView, optionsAvatar);
		mHolder.ivReplay.setTag(comment);
		mHolder.ivReplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Comment comment = (Comment)v.getTag();
				Handler handler = ((VideoDetailActivity)context).getmHandler();
				Message msg = handler.obtainMessage();
				msg.what = VideoDetailActivity.REPLAY;
				msg.obj = comment;
				msg.sendToTarget();
			}
		});
		return convertView;
	}
	
	private class Holder {
		public RelativeLayout R_layout;
		private ImageView roundImageView;
		private TextView commentUser;
		private TextView commentContent;
		private TextView commentDate;
		private ImageView ivReplay;
	}
	

}
