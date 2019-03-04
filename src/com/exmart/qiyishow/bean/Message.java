package com.exmart.qiyishow.bean;

public class Message {
	private String id;
	private String messageType;//已读／未读
	private String messageDate;
	private String messageContent;
	private String messageTitle;
	private String messageVideoId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getMessageDate() {
		return messageDate;
	}
	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getMessageTitle() {
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	public String getMessageVideoId() {
		return messageVideoId;
	}
	public void setMessageVideoId(String messageVideoId) {
		this.messageVideoId = messageVideoId;
	}
}
