package com.exmart.qiyishow.analysisJson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.Template;

public class JSONTemplate {
	
	public List<Template> analysisTemplateList(JSONObject jsonObj) throws JSONException {
		List<Template> list = new ArrayList<Template>();
		JSONArray jsonArr = jsonObj.getJSONArray("Content");
		for(int i = 0; i < jsonArr.length(); i++){
			JSONObject obj = jsonArr.getJSONObject(i);
			list.add(analyTemplate(obj));
			int count = 0;
		}
		return list;
	}
	private Template analyTemplate(JSONObject jsonObj) throws JSONException {
		Template template = new Template();
		template.setId(jsonObj.getString("TypeTemplateId"));
		template.setName(jsonObj.getString("Name"));
		template.setImageUrl(jsonObj.getString("Url"));
		return template;
	}
}
