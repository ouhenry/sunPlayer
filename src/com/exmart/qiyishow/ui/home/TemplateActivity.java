package com.exmart.qiyishow.ui.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.analysisJson.JSONCommon;
import com.exmart.qiyishow.analysisJson.JSONTemplate;
import com.exmart.qiyishow.bean.CommData;
import com.exmart.qiyishow.bean.Template;
import com.exmart.qiyishow.loader.core.DisplayImageOptions;
import com.exmart.qiyishow.loader.core.ImageLoader;
import com.exmart.qiyishow.loader.core.display.SimpleBitmapDisplayer;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Data;
import com.exmart.qiyishow.tools.HttpNetwork;
import com.exmart.qiyishow.ui.frame.BaseActivity;

/**
 * 
 * 视频模板类
 * 
 * ZhaoYe
 */
public class TemplateActivity extends BaseActivity implements OnClickListener {

	private TextView mText_back;
	private TextView mTextTitle;
	private ImageView mImageType1;
	private ImageView mImageType2;
	private ImageView mImageType3;
	private ImageView mImageType4;
	private ImageView mImageType5;
	private ImageView mImageType6;
//	private TextView mTextType1;
//	private TextView mTextType2;
//	private TextView mTextType3;
//	private TextView mTextType4;
//	private TextView mTextType5;
//	private TextView mTextType6;
	private List<Template> mArrayList;
	private DisplayImageOptions options;
	@Override
	protected void loadLayout() {
		setContentView(R.layout.template_layout);
		mText_back = (TextView) findViewById(R.id.bt_back);
		mTextTitle = ((TextView) findViewById(R.id.tv_title));
		mImageType1 = (ImageView)findViewById(R.id.type1);
		mImageType2 = (ImageView)findViewById(R.id.type2);
		mImageType3 = (ImageView)findViewById(R.id.type3);
		mImageType4 = (ImageView)findViewById(R.id.type4);
		mImageType5 = (ImageView)findViewById(R.id.type5);
		mImageType6 = (ImageView)findViewById(R.id.type6);
//		mTextType1 = (TextView)findViewById(R.id.type1_name);
//		mTextType2 = (TextView)findViewById(R.id.type2_name);
//		mTextType3 = (TextView)findViewById(R.id.type3_name);
//		mTextType4 = (TextView)findViewById(R.id.type4_name);
//		mTextType5 = (TextView)findViewById(R.id.type5_name);
//		mTextType6 = (TextView)findViewById(R.id.type6_name);
		mArrayList = new ArrayList<Template>();
	}

	@Override
	protected void loadListener() {
		mText_back.setOnClickListener(this);
		mImageType1.setOnClickListener(this);
		mImageType2.setOnClickListener(this);
		mImageType3.setOnClickListener(this);
		mImageType4.setOnClickListener(this);
		mImageType5.setOnClickListener(this);
		mImageType6.setOnClickListener(this);
	}

	@Override
	protected void logic() {
		mTextTitle.setText(getString(R.string.template_type));
		mText_back.setVisibility(View.VISIBLE);
		mText_back.setBackgroundResource(R.drawable.back_bg);
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.displayer(new SimpleBitmapDisplayer())
		.considerExifParams(true)
		.build();
	}
	
	@Override
	protected void Request() {
		initCacheData();
		getTypeTemplateDetail();
	}

	private void initCacheData() {
		SharedPreferences prefereces = TemplateActivity.this.getSharedPreferences("TypeTemplate", Context.MODE_PRIVATE);
		String data = prefereces.getString("typeList", "");
		if(TextUtils.isEmpty(data)){
			return;
		}
		mArrayList.clear();
		try {
			CommData commData = new JSONCommon().getCommonCode(data);
			if(commData.getCode().equals("1")){
				mArrayList.addAll(new JSONTemplate().analysisTemplateList(commData.getJosnObj()));
				int size = mArrayList.size();
				if(size == 1){
//					mTextType1.setText(mArrayList.get(0).getName());
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
				}else if(size == 2){
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(1).getImageUrl(), mImageType2, options);
//					mTextType1.setText(mArrayList.get(0).getName());
//					mTextType2.setText(mArrayList.get(1).getName());
				}else if(size ==3){
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(1).getImageUrl(), mImageType2, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(2).getImageUrl(), mImageType3, options);
//					mTextType1.setText(mArrayList.get(0).getName());
//					mTextType2.setText(mArrayList.get(1).getName());
//					mTextType3.setText(mArrayList.get(2).getName());
				}else if(size == 4){
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(1).getImageUrl(), mImageType2, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(2).getImageUrl(), mImageType3, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(3).getImageUrl(), mImageType4, options);
//					mTextType1.setText(mArrayList.get(0).getName());
//					mTextType2.setText(mArrayList.get(1).getName());
//					mTextType3.setText(mArrayList.get(2).getName());
//					mTextType4.setText(mArrayList.get(3).getName());
				}else if(size == 5){
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(1).getImageUrl(), mImageType2, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(2).getImageUrl(), mImageType3, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(3).getImageUrl(), mImageType4, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(4).getImageUrl(), mImageType5, options);
//					mTextType1.setText(mArrayList.get(0).getName());
//					mTextType2.setText(mArrayList.get(1).getName());
//					mTextType3.setText(mArrayList.get(2).getName());
//					mTextType4.setText(mArrayList.get(3).getName());
//					mTextType5.setText(mArrayList.get(4).getName());
				}else if(size > 5){
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(1).getImageUrl(), mImageType2, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(2).getImageUrl(), mImageType3, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(3).getImageUrl(), mImageType4, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(4).getImageUrl(), mImageType5, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(5).getImageUrl(), mImageType6, options);
//					mTextType1.setText(mArrayList.get(0).getName());
//					mTextType2.setText(mArrayList.get(1).getName());
//					mTextType3.setText(mArrayList.get(2).getName());
//					mTextType4.setText(mArrayList.get(3).getName());
//					mTextType5.setText(mArrayList.get(4).getName());
//					mTextType6.setText(mArrayList.get(5).getName());
				}
			}
		} catch (Exception e) {
			Log.d("data", e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有模板视频类型
	 */
	private void getTypeTemplateDetail() {
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				String result = HttpNetwork.httpNetwork(Data.getTypeTemplate(),Constant.TEMPLATE_TYPE, TemplateActivity.this);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				SharedPreferences prefereces = TemplateActivity.this.getSharedPreferences("TypeTemplate", Context.MODE_PRIVATE);
				Editor editor = prefereces.edit();
				editor.putString("typeList", result);
				editor.commit();
				try {
					CommData commData = new JSONCommon().getCommonCode(result);
					if(commData.getCode().equals("1")){
						List<Template> list = new JSONTemplate().analysisTemplateList(commData.getJosnObj());
						mArrayList.addAll(new JSONTemplate().analysisTemplateList(commData.getJosnObj()));
						
					}
				} catch (Exception e) {
					Log.d("data", e.toString());
					e.printStackTrace();
				}
				int size = mArrayList.size();
				if(size == 1){
//					mTextType1.setText(mArrayList.get(0).getName());
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
				}else if(size == 2){
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(1).getImageUrl(), mImageType2, options);
//					mTextType1.setText(mArrayList.get(0).getName());
//					mTextType2.setText(mArrayList.get(1).getName());
				}else if(size ==3){
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(1).getImageUrl(), mImageType2, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(2).getImageUrl(), mImageType3, options);
//					mTextType1.setText(mArrayList.get(0).getName());
//					mTextType2.setText(mArrayList.get(1).getName());
//					mTextType3.setText(mArrayList.get(2).getName());
				}else if(size == 4){
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(1).getImageUrl(), mImageType2, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(2).getImageUrl(), mImageType3, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(3).getImageUrl(), mImageType4, options);
//					mTextType1.setText(mArrayList.get(0).getName());
//					mTextType2.setText(mArrayList.get(1).getName());
//					mTextType3.setText(mArrayList.get(2).getName());
//					mTextType4.setText(mArrayList.get(3).getName());
				}else if(size == 5){
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(1).getImageUrl(), mImageType2, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(2).getImageUrl(), mImageType3, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(3).getImageUrl(), mImageType4, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(4).getImageUrl(), mImageType5, options);
//					mTextType1.setText(mArrayList.get(0).getName());
//					mTextType2.setText(mArrayList.get(1).getName());
//					mTextType3.setText(mArrayList.get(2).getName());
//					mTextType4.setText(mArrayList.get(3).getName());
//					mTextType5.setText(mArrayList.get(4).getName());
				}else if(size > 5){
					ImageLoader.getInstance().displayImage(mArrayList.get(0).getImageUrl(), mImageType1, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(1).getImageUrl(), mImageType2, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(2).getImageUrl(), mImageType3, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(3).getImageUrl(), mImageType4, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(4).getImageUrl(), mImageType5, options);
					ImageLoader.getInstance().displayImage(mArrayList.get(5).getImageUrl(), mImageType6, options);
//					mTextType1.setText(mArrayList.get(0).getName());
//					mTextType2.setText(mArrayList.get(1).getName());
//					mTextType3.setText(mArrayList.get(2).getName());
//					mTextType4.setText(mArrayList.get(3).getName());
//					mTextType5.setText(mArrayList.get(4).getName());
//					mTextType6.setText(mArrayList.get(5).getName());
				}
			}
		}.execute();
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.type1:
			intent = new Intent(this, TemplateListActivity.class);
			intent.putExtra("title", mArrayList.get(0).getName());
			intent.putExtra("id", mArrayList.get(0).getId());
			startActivity(intent);
			break;
		case R.id.type2:
			intent = new Intent(this, TemplateListActivity.class);
			intent.putExtra("title", mArrayList.get(1).getName());
			intent.putExtra("id", mArrayList.get(1).getId());
			startActivity(intent);
			break;
		case R.id.type3:
			intent = new Intent(this, TemplateListActivity.class);
			intent.putExtra("title", mArrayList.get(2).getName());
			intent.putExtra("id", mArrayList.get(2).getId());
			startActivity(intent);
			break;
		case R.id.type4:
			intent = new Intent(this, TemplateListActivity.class);
			intent.putExtra("title", mArrayList.get(3).getName());
			intent.putExtra("id", mArrayList.get(3).getId());
			startActivity(intent);
			break;
		case R.id.type5:
			intent = new Intent(this, TemplateListActivity.class);
			intent.putExtra("title", mArrayList.get(4).getName());
			intent.putExtra("id", mArrayList.get(4).getId());
			startActivity(intent);
			break;
		case R.id.type6:
			intent = new Intent(this, TemplateListActivity.class);
			intent.putExtra("title", mArrayList.get(5).getName());
			intent.putExtra("id", mArrayList.get(5).getId());
			startActivity(intent);
			break;
		case R.id.bt_back:
			finish();
			break;
		default:
			break;
		}
	}
}
