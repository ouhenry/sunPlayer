package com.exmart.qiyishow.analysisJson;

import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.Video;

public class JSONVideoDetail {
	
	public Video getVideo(JSONObject data) throws JSONException{
		Video video = new Video();
		video.setId(data.getString("VideoId"));
		video.setImageUrl(data.getString("ImageUrl"));
		video.setClickNum(data.getString("ClickNum"));
		String strTitle = data.getString("Title");
		if (strTitle.equals("null")) {
			strTitle = "";
		}
		video.setVideoTitle(strTitle);
		video.setIsLiked(data.getString("isLiked"));
		video.setUploadTime(data.getString("UploadTime"));
		video.setCountOfComment(data.getString("CountOfComment"));
		video.setCountOflike(data.getString("CountOfLike"));
		video.setUrl(data.getString("Url"));
		video.setAuthorNikeName(data.getString("NikeName"));
		video.setUserId(data.getString("UserId"));
		video.setShareUrl(data.getString("shareUrl"));
		video.setTemplateId(data.getString("TmpId"));
		video.setSubTitles(data.getString("Subtitles"));
		String strContent = data.getString("Content");
		if (strContent.equals("null")) {
			strContent = "";
		}
		video.setContent(strContent);
		return video;
	}
}
