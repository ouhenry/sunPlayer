package com.exmart.qiyishow.ui.setting;

import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.analysisJson.JSONCommon;
import com.exmart.qiyishow.bean.CommData;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Data;
import com.exmart.qiyishow.tools.HttpNetwork;
import com.exmart.qiyishow.ui.frame.BaseActivity;
/**
 * 意见反馈界面
 * 
 *@author 
 */
public class FeedbackActivity extends BaseActivity implements OnClickListener{
	private TextView tv_send;
	private EditText mEtContent;
	private EditText mContactWay;
	@Override
	protected void loadLayout() {
		setContentView(R.layout.feedback_layout);
		((TextView)findViewById(R.id.tv_title)).setText(getString(R.string.suggest));
		TextView tvBack = (TextView)findViewById(R.id.bt_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setBackgroundResource(R.drawable.back_bg);
		tvBack.setOnClickListener(this);
		tv_send = (TextView) findViewById(R.id.bt_title_right);
		tv_send.setVisibility(View.VISIBLE);
		mEtContent = (EditText) findViewById(R.id.feedback_edit_content);
		mContactWay = (EditText) findViewById(R.id.feedback_edit_qq);
	}

	@Override
	protected void loadListener() {
		tv_send.setOnClickListener(this);
	}

	@Override
	protected void Request() {

	}

	@Override
	protected void logic() {

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.bt_back:
			finish();
			break;
		case R.id.bt_title_right:
			String strContent =  mEtContent.getText().toString();
			String strContactWay = mContactWay.getText().toString();
			if(strContent.equals("")){
				Toast.makeText(FeedbackActivity.this, "请输入反馈意见", Toast.LENGTH_SHORT).show();
			}else{
				sendReport(strContent,strContactWay);
			}
			
			break;

		default:
			break;
		}
	}
	private void sendReport(String strContent,String StrContactWay){
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... args) {
				String result = HttpNetwork.httpNetwork(Data.feedback("1", args[0],args[1]), Constant.OTHER_REPORT, FeedbackActivity.this);
				return result;
			}
			@Override
			protected void onPostExecute(String result) {
				CommData commData = null;
				try {
					commData = new JSONCommon().getCommonCode(result);
					if(commData.getCode().equals("1")){
						mEtContent.setText("");
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Toast.makeText(FeedbackActivity.this, commData.getMsg(), Toast.LENGTH_SHORT).show();
			}
		}.execute(strContent,StrContactWay);
	}
}
