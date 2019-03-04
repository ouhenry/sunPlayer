package com.exmart.qiyishow.ui.setting;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.ui.frame.BaseActivity;
import com.exmart.qiyishow.ui.view.Mydialog;
/**
 * 检查更新界面
 * 
 *@author shujun
 */
public class CheckUpdateActivity extends BaseActivity implements OnClickListener{
	private TextView tv_back;
	private RelativeLayout mydialog_title;
	private RelativeLayout mydialog_content;
	private RelativeLayout mydialog_yes;
	private Mydialog md;
	@Override
	protected void loadLayout() {
		setContentView(R.layout.check_update_layout);
		tv_back = (TextView) findViewById(R.id.tv_back);
		mydialog_yes = (RelativeLayout) findViewById(R.id.yes_layout);
		showMyDialog();
	}

	@Override
	protected void loadListener() {
		tv_back.setOnClickListener(this);
		mydialog_yes.setOnClickListener(this);
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
		case R.id.tv_back:
			finish();
			break;
		case R.id.yes_layout:
			md.dismiss();
			break;
		default:
			break;
		}
		
	}
	
	private void showMyDialog(){

		View view = getLayoutInflater().inflate(R.layout.mydialog_layout, null);
		mydialog_title = (RelativeLayout) view.findViewById(R.id.mydialog_title_layout);
		mydialog_content = (RelativeLayout) view.findViewById(R.id.content_layout);
		mydialog_yes = (RelativeLayout) view.findViewById(R.id.yes_layout);
		
		mydialog_title.setOnClickListener(this);
		mydialog_content.setOnClickListener(this);
		mydialog_yes.setOnClickListener(this);
		md = new Mydialog(this, 200, 280, view, R.style.mydialog_style);
		md.show();
		
	}

}
