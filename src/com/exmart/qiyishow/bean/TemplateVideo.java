package com.exmart.qiyishow.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TemplateVideo implements Parcelable{
	
	private String tname;
	private String picurl;
	private String url;
	private String id;
	private String desc;
	private String subTitle1;
	private String subTitle2;
	private String subTitle3;
	private String subTitle4;
	private String subTitle5;
	
	
	public TemplateVideo() {
		
	}
	
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	public String getSubTitle1() {
		return subTitle1;
	}

	public void setSubTitle1(String subTitle1) {
		this.subTitle1 = subTitle1;
	}

	public String getSubTitle2() {
		return subTitle2;
	}

	public void setSubTitle2(String subTitle2) {
		this.subTitle2 = subTitle2;
	}

	public String getSubTitle3() {
		return subTitle3;
	}

	public void setSubTitle3(String subTitle3) {
		this.subTitle3 = subTitle3;
	}

	public String getSubTitle4() {
		return subTitle4;
	}

	public void setSubTitle4(String subTitle4) {
		this.subTitle4 = subTitle4;
	}

	public String getSubTitle5() {
		return subTitle5;
	}

	public void setSubTitle5(String subTitle5) {
		this.subTitle5 = subTitle5;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(tname);
		dest.writeString(desc);
		dest.writeString(picurl);
		dest.writeString(url);
		dest.writeString(subTitle1);
		dest.writeString(subTitle2);
		dest.writeString(subTitle3);
		dest.writeString(subTitle4);
		dest.writeString(subTitle5);
	}
	
	public static final Parcelable.Creator<TemplateVideo> CREATOR = new Creator<TemplateVideo>() {
		
		@Override
		public TemplateVideo[] newArray(int size) {
			return new TemplateVideo[size];
		}
		
		@Override
		public TemplateVideo createFromParcel(Parcel source) {
			return new TemplateVideo(source);
		}
	};
	private TemplateVideo(Parcel in) {
		id = in.readString();
		tname = in.readString();
		desc = in.readString();
		picurl = in.readString();
		url = in.readString();
		subTitle1 = in.readString();
		subTitle2 = in.readString();
		subTitle3 = in.readString();
		subTitle4 = in.readString();
		subTitle5 = in.readString();
	}
}
