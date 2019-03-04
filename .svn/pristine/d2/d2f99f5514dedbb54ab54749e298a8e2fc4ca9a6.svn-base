package com.exmart.qiyishow.ui.setting;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.ui.frame.BaseActivity;
/**
 * 如何使用app界面
 * 
 *@author shujun
 */
public class HowUseActivity extends BaseActivity implements OnClickListener{
	@Override
	protected void loadLayout() {
		setContentView(R.layout.how_use_layout);
		((TextView)findViewById(R.id.tv_title)).setText(getString(R.string.use_tip));
		TextView tvBack = (TextView)findViewById(R.id.bt_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setBackgroundResource(R.drawable.back_bg);
		tvBack.setOnClickListener(this);
	}

	@Override
	protected void loadListener() {
		
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

		default:
			break;
		}
		
	}
	


}
