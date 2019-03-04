package com.exmart.qiyishow.analysisJson;

import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.Efficacy;

public class JSONEfficacy {
	
	public Efficacy analysisEfficacy(JSONObject obj) throws JSONException {
		Efficacy efficacy = new Efficacy();
		efficacy.setUrl(obj.getString("Url"));
		efficacy.setSkill(obj.getString("Skill"));
		efficacy.setContent(obj.getString("Content"));
		return efficacy;
	}

}
