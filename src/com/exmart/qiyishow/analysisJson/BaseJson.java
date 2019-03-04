package com.exmart.qiyishow.analysisJson;

import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.BaseBean;



/**
 * 解析父类JSON
 * @author 
 *
 */
public class BaseJson {

	private JSONObject mJsonObject;
	private BaseBean mBaseBean;

	public BaseJson(String data) throws JSONException {
		mJsonObject = new JSONObject(data);
	}

	public BaseBean parse() throws JSONException {
		mBaseBean = new BaseBean();
		mBaseBean.Code = mJsonObject.getString("Code");
		mBaseBean.Msg = mJsonObject.getString("Msg");
		mBaseBean.Data = mJsonObject.getString("Data");
		return mBaseBean;
	}
}
