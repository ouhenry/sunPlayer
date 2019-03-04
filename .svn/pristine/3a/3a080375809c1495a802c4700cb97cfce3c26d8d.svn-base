package com.exmart.qiyishow.analysisJson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.Efficacy;

public class JSONEfficacyList {
	
	public List<Efficacy> analysisEfficacyList(JSONObject obj) throws JSONException {
		JSONArray jsonArr = obj.getJSONArray("Content");
		List<Efficacy> list = new ArrayList<Efficacy>();
		for (int i = 0; i < jsonArr.length(); i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			list.add(analysisEfficacy(jsonObj));
		}
		return list;
	}
	
	private Efficacy analysisEfficacy(JSONObject obj) throws JSONException {
		Efficacy efficacy = new Efficacy();
		efficacy.setId(obj.getString("Id"));
		efficacy.setType(obj.getString("Type"));
		efficacy.setName(obj.getString("Name"));
		return efficacy;
	}

}
