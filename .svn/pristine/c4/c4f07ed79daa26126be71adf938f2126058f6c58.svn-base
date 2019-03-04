package com.exmart.qiyishow.ui.home;

import java.util.ArrayList;
import java.util.List;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.EffectsListAdapter;
import com.exmart.qiyishow.analysisJson.JSONCommon;
import com.exmart.qiyishow.analysisJson.JSONEfficacyList;
import com.exmart.qiyishow.bean.CommData;
import com.exmart.qiyishow.bean.Efficacy;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Data;
import com.exmart.qiyishow.tools.HttpNetwork;
import com.exmart.qiyishow.ui.frame.BaseActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 特效列表
 * 
 * @author ZhaoYe
 *
 */
public class EffectsListActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	private Intent intent;
	private TextView mText_bcak;
	private TextView mText_title;
	private ListView mListView;
	private EffectsListAdapter adapter;
	private List<Efficacy> mEfficacyList;
	private int mPageNo = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_effects_list);
		mEfficacyList = new ArrayList<Efficacy>();
		mText_bcak = (TextView) findViewById(R.id.bt_back);
		mText_bcak.setVisibility(View.VISIBLE);
		mText_bcak.setBackgroundResource(R.drawable.back_bg);
		mText_title = (TextView) findViewById(R.id.tv_title);
		mText_title.setText(getString(R.string.effects_list));
		mListView = (ListView) findViewById(R.id.List_effects);
		adapter = new EffectsListAdapter(this, mEfficacyList);
		mListView.setAdapter(adapter);
	}


	@Override
	protected void loadListener() {
		mText_bcak.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void Request() {
		
	}
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				break;
			}
			
		}
	};


	@Override
	protected void logic() {
		getEfficacyData();
	}
	
	private void getEfficacyData() {
		
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				String response = HttpNetwork.httpNetwork(Data.getEfficacyList(mPageNo + "", Constant.COUNT_OF_PAGE + ""), Constant.EFFICACY_LIST, EffectsListActivity.this);
				return response;
			}
			@Override
			protected void onPostExecute(String result) {
				List<Efficacy> list = null;
				try {
					CommData commData = new JSONCommon().getCommonCode(result);
					if (commData.getCode().equals("1")) {
						list = new JSONEfficacyList().analysisEfficacyList(commData.getJosnObj());
					} else {
						Toast.makeText(EffectsListActivity.this, commData.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				mEfficacyList.addAll(list);
				adapter.notifyDataSetChanged();
				super.onPostExecute(result);
			}
		}.execute();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Efficacy efficacy = mEfficacyList.get(position);
		intent = new Intent(this,EffectsDetailActivity.class);
		intent.putExtra("id", efficacy.getId());
		intent.putExtra("type", efficacy.getType());
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_back:
			finish();
			break;
		}
		
	}
	
}
