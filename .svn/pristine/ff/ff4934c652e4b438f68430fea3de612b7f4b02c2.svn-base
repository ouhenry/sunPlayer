package com.exmart.qiyishow.analysisJson;

import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.CommData;

public class JSONCommon {
	public CommData getCommonCode(String str) throws JSONException{
		JSONObject jsonObj = new JSONObject(str);
		CommData data = new CommData();
		data.setCode(jsonObj.getString("Code"));
		data.setMsg(jsonObj.getString("Msg"));
		data.setJosnObj(jsonObj.getJSONObject("Data"));
		return data;
	}
}
