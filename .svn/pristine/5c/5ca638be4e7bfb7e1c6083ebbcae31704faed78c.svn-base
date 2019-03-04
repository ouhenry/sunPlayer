package com.exmart.qiyishow.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 评论实体类
 *
 */
public class Comment implements Parcelable{
	private String id;
	private String content;
	private String commentUserName;
	private String autherAvatarUrl;
	private String date;
	private String userId;
	
	private String replayUserId;
	private String replayUserName;
	
	public Comment() {
		
	}
	
	public String getReplayUserId() {
		return replayUserId;
	}

	public void setReplayUserId(String replayUserId) {
		this.replayUserId = replayUserId;
	}

	public String getReplayUserName() {
		return replayUserName;
	}

	public void setReplayUserName(String replayUserName) {
		this.replayUserName = replayUserName;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCommentUserName() {
		return commentUserName;
	}
	public void setCommentUserName(String commentUserName) {
		this.commentUserName = commentUserName;
	}
	public String getAutherAvatarUrl() {
		return autherAvatarUrl;
	}
	public void setAutherAvatarUrl(String autherAvatarUrl) {
		this.autherAvatarUrl = autherAvatarUrl;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(content);
		dest.writeString(autherAvatarUrl);
		dest.writeString(commentUserName);
		dest.writeString(date);
		dest.writeString(userId);
		dest.writeString(replayUserId);
		dest.writeString(replayUserName);
	}
	
	private static final Parcelable.Creator<Comment> CREATOR = new Creator<Comment>() {
		
		@Override
		public Comment[] newArray(int size) {
			return new Comment[size];
		}
		
		@Override
		public Comment createFromParcel(Parcel source) {
			return new Comment(source);
		}
	};
	
	private Comment(Parcel in) {
		id = in.readString();
		content = in.readString();
		autherAvatarUrl = in.readString();
		commentUserName = in.readString();
		date = in.readString();
		userId = in.readString();
		replayUserId = in.readString();
		replayUserName = in.readString();
	}
}
