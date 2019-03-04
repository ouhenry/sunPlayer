package com.exmart.qiyishow.ui.home;

import java.io.InputStream;
import java.util.ArrayList;
import org.apache.http.util.EncodingUtils;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.CityAdapter;
import com.exmart.qiyishow.analysisJson.CityJson;
import com.exmart.qiyishow.bean.City;
import com.exmart.qiyishow.bean.CityBaseBean;
import com.exmart.qiyishow.bean.CityBean;
import com.exmart.qiyishow.bean.Province;
import com.exmart.qiyishow.ui.frame.BaseActivity;
/**
 * 城市列表
 * @author ye
 *
 */
public class ShowCityActivity extends BaseActivity implements OnClickListener{

	private ListView mListView;
	private CityAdapter mAdapter;
	private ArrayList<CityBaseBean> mArrayList;
	private ArrayList<Province> mArrayListProvince = new ArrayList<Province>();;
	private ArrayList<City> mArrayListCity = new ArrayList<City>();
	private ArrayList<CityBaseBean> mArrayListArea = new ArrayList<CityBaseBean>();
	private String flag = "";
	private TextView mText_back;
	private TextView mText_title;

	@Override
	protected void loadLayout() {
		 setContentView(R.layout.show_city);
		 mText_back = (TextView) findViewById(R.id.bt_back);
		 mText_back.setBackgroundResource(R.drawable.back_bg);
		 mText_back.setVisibility(View.VISIBLE);
		 mText_title = (TextView) findViewById(R.id.tv_title);
		 mText_title.setText(getString(R.string.edit_info_area));
		 
		 mListView = (ListView)findViewById(R.id.list);
		 mArrayList = new ArrayList<CityBaseBean>();
		 mAdapter = new CityAdapter(mArrayList, this);
		 try {
			 InputStream is = getResources().getAssets().open("data.txt");
			 int len = is.available();
			 byte []buffer = new byte[len];
			 is.read(buffer);
			 String Result = EncodingUtils.getString(buffer, "utf-8");
			 is.close();
	            CityJson json = new CityJson(Result);
	            CityBean mBean = json.parse();
	            mArrayListProvince.clear();
	            mArrayListProvince = mBean.mArrayListProvince;
	            mArrayList.addAll(mArrayListProvince);
	            flag = "province";
	            mListView.setAdapter(mAdapter);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	
	}

	@Override
	protected void loadListener() {
		 mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					if(flag.equals("province")){
						mArrayList.clear();
						mArrayListCity = mArrayListProvince.get(position).mArrayListCity;
						
						CityBaseBean bean = new CityBaseBean();
						bean.DivisionCode = mArrayListProvince.get(position).DivisionCode;
						bean.DivisionName = mArrayListProvince.get(position).DivisionName;
						
						EditDataActivity.arrayList.add(0,bean);
						
						mArrayList.addAll(mArrayListCity);
						mListView.setAdapter(mAdapter);
						flag = "city";
						Log.d("data", "province:Code="+bean.DivisionCode+"Name="+bean.DivisionName);
					}else if(flag.equals("city")){
						mArrayList.clear();
						mArrayListArea = mArrayListCity.get(position).mArrayListArea;
						
						CityBaseBean bean = new CityBaseBean();
						bean.DivisionCode = mArrayListCity.get(position).DivisionCode;
						bean.DivisionName = mArrayListCity.get(position).DivisionName;
						
						EditDataActivity.arrayList.add(1,bean);
						
						
						mArrayList.addAll(mArrayListArea);
						mListView.setAdapter(mAdapter);
						flag = "area";
						Log.d("data", "city:Code="+bean.DivisionCode+"Name="+bean.DivisionName);
					}else{
						CityBaseBean bean = new CityBaseBean();
						bean.DivisionCode = mArrayListArea.get(position).DivisionCode;
						bean.DivisionName = mArrayListArea.get(position).DivisionName;
						
						EditDataActivity.arrayList.add(2,bean);
						Log.d("data", "area:Code="+bean.DivisionCode+"Name="+bean.DivisionName);
						finish();
					}
				}
			});
		 mText_back.setOnClickListener(this);
	}

	@Override
	protected void Request() {

	}

	@Override
	protected void logic() {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (flag.equals("city")) {
				mArrayList.clear();
				mArrayList.addAll(mArrayListProvince);
				flag = "province";
				mListView.setAdapter(mAdapter);
				EditDataActivity.arrayList.clear();
				return true;
			} else if (flag.equals("area")) {
				mArrayList.clear();
				mArrayList.addAll(mArrayListCity);
				mListView.setAdapter(mAdapter);
				flag = "city";
				EditDataActivity.arrayList.clear();
				return true;
			} else {
				EditDataActivity.arrayList.clear();
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_back:
			if (flag.equals("city")) {
				mArrayList.clear();
				mArrayList.addAll(mArrayListProvince);
				flag = "province";
				mListView.setAdapter(mAdapter);
				EditDataActivity.arrayList.clear();
			} else if (flag.equals("area")) {
				mArrayList.clear();
				mArrayList.addAll(mArrayListCity);
				mListView.setAdapter(mAdapter);
				flag = "city";
				EditDataActivity.arrayList.clear();
			} else {
				EditDataActivity.arrayList.clear();
				finish();
			}
			break;
		}
		
	}
}
