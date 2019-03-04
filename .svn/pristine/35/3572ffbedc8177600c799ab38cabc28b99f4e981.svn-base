package com.exmart.qiyishow.analysisJson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.Video;
/**
 * 解析视频列表
 * @author henry
 *
 */

public class JSONVideoList {
	
	public List<Video> getVideoList(JSONObject data) throws JSONException{
		List<Video> listVideo = new ArrayList<Video>();
		JSONArray jsonArr = data.getJSONArray("Content");
		for(int i = 0; i < jsonArr.length(); i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			listVideo.add(getVideo(jsonObj));
		}
		return listVideo;
	}

	public Video getVideo(JSONObject jsonObj) throws JSONException{
		Video video = new Video();
		video.setId(jsonObj.getString("VideoId"));
		video.setVideoTitle(jsonObj.getString("Title"));
		video.setUrl(jsonObj.getString("Url"));
		if(jsonObj.has("UserImage"))
		video.setAuthorImageUrl(jsonObj.getString("UserImage"));
		video.setImageUrl(jsonObj.getString("Image"));
		return video;
	}
}
