package com.exmart.qiyishow.analysisJson;

import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.User;


public class UserJson {
	private JSONObject mJsonObject;
	private User user;
	
	public UserJson(JSONObject data) throws JSONException {
		mJsonObject = data;
	}
	
	public User parse() throws JSONException {
		user = new User();
		user.setId(mJsonObject.getString("UserId"));
		user.setNikeName(mJsonObject.getString("NickName"));
		user.setSex(mJsonObject.getString("Sex"));
		user.setImageUrl(mJsonObject.getString("ImageUrl"));
		user.setProvince(mJsonObject.getString("ProvinceCode"));
		user.setCity(mJsonObject.getString("CityCode"));
		user.setArea(mJsonObject.getString("AreaCode"));
		return user;
	}

}
