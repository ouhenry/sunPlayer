package com.exmart.qiyishow.analysisJson;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exmart.qiyishow.bean.City;
import com.exmart.qiyishow.bean.CityBaseBean;
import com.exmart.qiyishow.bean.CityBean;
import com.exmart.qiyishow.bean.Province;

public class CityJson {

	private JSONObject mJsonObject;
	private JSONObject mJsonObjectData;
	private CityBean mBean;
	private JSONArray mJsonArrayProvince;
	private JSONArray mJsonArrayCity;
	private JSONArray mJsonArrayArea;
	private Province mProvinceBean;
	private City mCityBean;
	private CityBaseBean mAreaBean;
	
	public CityJson(String data) throws JSONException{
		mJsonObject = new JSONObject(data);
	}
	
	public CityBean parse() throws JSONException{
		mBean = new CityBean();
		mBean.Code = mJsonObject.getString("Code");
		mBean.Msg = mJsonObject.getString("Msg");
		mBean.mArrayListProvince = new ArrayList<Province>();
		mJsonObjectData = mJsonObject.getJSONObject("Data");
		mBean.DivisionVersion = mJsonObjectData.getString("DivisionVersion");
		mJsonArrayProvince = mJsonObjectData.getJSONArray("Division");
		for(int i=0;i<mJsonArrayProvince.length();i++){
			mProvinceBean = new Province();
			mProvinceBean.DivisionName = mJsonArrayProvince.getJSONObject(i).getString("DivisionName");
			mProvinceBean.DivisionCode = mJsonArrayProvince.getJSONObject(i).getString("DivisionCode");
			mProvinceBean.mArrayListCity = new ArrayList<City>();
			mJsonArrayCity = mJsonArrayProvince.getJSONObject(i).getJSONArray("DivisionSub");
			for(int j=0;j<mJsonArrayCity.length();j++){
				mCityBean = new City();
				mCityBean.DivisionName = mJsonArrayCity.getJSONObject(j).getString("DivisionName");
				mCityBean.DivisionCode = mJsonArrayCity.getJSONObject(j).getString("DivisionCode");
				mCityBean.mArrayListArea = new ArrayList<CityBaseBean>();
				mJsonArrayArea = mJsonArrayCity.getJSONObject(j).getJSONArray("DivisionSub");
				for(int n = 0;n<mJsonArrayArea.length();n++){
					mAreaBean = new CityBaseBean();
					mAreaBean.DivisionName = mJsonArrayArea.getJSONObject(n).getString("DivisionName");
					mAreaBean.DivisionCode = mJsonArrayArea.getJSONObject(n).getString("DivisionCode");
					mCityBean.mArrayListArea.add(mAreaBean);
				}
				mProvinceBean.mArrayListCity.add(mCityBean);
			}
			mBean.mArrayListProvince.add(mProvinceBean);
		}
		return mBean;
	}
}
