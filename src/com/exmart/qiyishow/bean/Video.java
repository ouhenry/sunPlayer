package com.exmart.qiyishow.bean;

public class Video {

	private String id;
	private String imageUrl;// 视频图片
	private String videoTitle;// 视频标题
	// private String userAvatarUrl;
	private String url;// 视频地址
	private String countOfComment;
	private String countOflike;
	private String userId;
	private String shareUrl;
	private String templateId;
	private String subTitles;
	private String content;
	
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getSubTitles() {
		return subTitles;
	}

	public void setSubTitles(String subTitles) {
		this.subTitles = subTitles;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAuthorNikeName() {
		return authorNikeName;
	}

	public void setAuthorNikeName(String authorNikeName) {
		this.authorNikeName = authorNikeName;
	}

	public String getAuthorImageUrl() {
		return authorImageUrl;
	}

	public void setAuthorImageUrl(String authorImageUrl) {
		this.authorImageUrl = authorImageUrl;
	}

	private String isLiked;
	private String uploadTime;
	private String clickNum;
	private String authorImageUrl;// 上传者头像
	private String authorNikeName;// 上传者昵称

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCountOfComment() {
		return countOfComment;
	}

	public void setCountOfComment(String countOfComment) {
		this.countOfComment = countOfComment;
	}

	public String getCountOflike() {
		return countOflike;
	}

	public void setCountOflike(String countOflike) {
		this.countOflike = countOflike;
	}

	public String getIsLiked() {
		return isLiked;
	}

	public void setIsLiked(String isLiked) {
		this.isLiked = isLiked;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getClickNum() {
		return clickNum;
	}

	public void setClickNum(String clickNum) {
		this.clickNum = clickNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

}
