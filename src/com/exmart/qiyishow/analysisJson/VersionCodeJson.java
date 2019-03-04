package com.exmart.qiyishow.analysisJson;

import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.VersionBean;



/**
 * 检测版本JSON
 * @author
 *
 */
public class VersionCodeJson {

	private JSONObject mjsonObject;
	private VersionBean mVersionBean;
	
	public VersionCodeJson(String data)throws JSONException{
		mjsonObject = new JSONObject(data);
	}
	
	public VersionBean parse()throws JSONException{
		mVersionBean = new VersionBean();
		mVersionBean.setAppUrl(mjsonObject.getString("AppUrl"));
		mVersionBean.setVersion(mjsonObject.getString("Version"));
		mVersionBean.setExpired(mjsonObject.getString("Expired"));
		return mVersionBean;
	}
	
}
