package com.exmart.qiyishow.analysisJson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.Message;

public class JSONMessageList {
	
	public List<Message> analysisMessageList(JSONObject obj) throws JSONException {
		JSONArray jsonArr = obj.getJSONArray("Content");
		List<Message> list = new ArrayList<Message>();
		for(int i = 0; i < jsonArr.length(); i++) {
			JSONObject jsonObj = jsonArr.getJSONObject(i);
			list.add(analysisMessage(jsonObj));
		}
		return list;
	}
	private Message analysisMessage(JSONObject obj) throws JSONException {
		Message message = new Message();
		message.setId(obj.getString("MessageId"));
		message.setMessageContent(obj.getString("MessageContent"));
		message.setMessageDate(obj.getString("Date"));
		message.setMessageTitle(obj.getString("MessageTitle"));
		message.setMessageType(obj.getString("Type"));
		return message;
	}

}
