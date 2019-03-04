package com.exmart.qiyishow.analysisJson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.TemplateVideo;

public class JSONTemplateVideoList {
	
	public List<TemplateVideo> analysisTemplateVideoList(JSONObject jsonObj) throws JSONException {
		List<TemplateVideo> list = new ArrayList<TemplateVideo>();
		JSONArray jsonArr = jsonObj.getJSONArray("Content");
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject obj = jsonArr.getJSONObject(i);
			list.add(analysisTemplateVideo(obj));
		}
		return list;
	}
	
	private TemplateVideo analysisTemplateVideo(JSONObject obj) throws JSONException {
		TemplateVideo templateVideo = new TemplateVideo();
		templateVideo.setId(obj.getString("Id"));
		templateVideo.setTname(obj.getString("Tname"));
		templateVideo.setPicurl(obj.getString("Picurl"));
		templateVideo.setUrl(obj.getString("Url"));
		templateVideo.setDesc(obj.getString("Desc"));
		templateVideo.setSubTitle1(obj.getString("Subtitles1"));
		templateVideo.setSubTitle2(obj.getString("Subtitles2"));
		templateVideo.setSubTitle3(obj.getString("Subtitles3"));
		templateVideo.setSubTitle4(obj.getString("Subtitles4"));
		templateVideo.setSubTitle5(obj.getString("Subtitles5"));
		return templateVideo;
	}

}
