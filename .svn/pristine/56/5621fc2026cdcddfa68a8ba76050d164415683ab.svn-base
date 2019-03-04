package com.exmart.qiyishow.ui.home;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.analysisJson.JSONCommon;
import com.exmart.qiyishow.analysisJson.JSONEfficacy;
import com.exmart.qiyishow.bean.CommData;
import com.exmart.qiyishow.bean.Efficacy;
import com.exmart.qiyishow.record.FFmpegRecorderActivity;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 特效详情
 * 
 * @author ZhaoYe
 *
 */
public class EffectsDetailActivity extends BaseActivity implements OnClickListener{
	private Intent intent;
	private TextView mText_bcak;
	private TextView mText_title;
	private Button mButton_video;
	private String id;
	private String type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.activity_effects_detail);
		id = getIntent().getStringExtra("id");
		type = getIntent().getStringExtra("type");
		mText_bcak = (TextView) findViewById(R.id.bt_back);
		mText_bcak.setVisibility(View.VISIBLE);
		mText_bcak.setBackgroundResource(R.drawable.back_bg);
		mText_title = (TextView) findViewById(R.id.tv_title);
		mText_title.setText(getString(R.string.effects_detail));
		mButton_video = (Button) findViewById(R.id.bt_make_face);
		
	}

	@Override
	protected void loadListener() {
		mText_bcak.setOnClickListener(this);
		mButton_video.setOnClickListener(this);

	}

	@Override
	protected void Request() {
		new AsyncTask<String, Void, String>() {
			
			@Override
			protected String doInBackground(String... params) {
				String result = HttpNetwork.httpNetwork(Data.getEfficacyDetail(id), Constant.EFFICACY_DETAIL, EffectsDetailActivity.this);
				return result;
			}
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				Efficacy efficacy = null;
				try {
					CommData commData = new JSONCommon().getCommonCode(result);
					if (commData.getCode().equals("1")) {
						efficacy = new JSONEfficacy().analysisEfficacy(commData.getJosnObj());
					} else {
						Toast.makeText(EffectsDetailActivity.this, commData.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				((TextView)findViewById(R.id.text_content_description)).setText(efficacy.getContent());
				((TextView)findViewById(R.id.text_title_skill)).setText(efficacy.getName());
				
			}
		}.execute();
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
		

	}



	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_back:
			finish();
			break;
		case R.id.bt_make_face:
			intent = new Intent(this,FFmpegRecorderActivity.class);
			startActivity(intent);
			break;
		}
	}
}
