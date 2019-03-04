package com.exmart.qiyishow.ui.home;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.bean.TemplateVideo;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.frame.BaseActivity;
/**
 * 添加字幕类
 * @author zheyi
 *
 */
public class AddCaptionsActivity extends BaseActivity implements OnClickListener{
	private TextView tv_title;
	private TextView btn_right;
	private TextView tv_back;
	private Button mAddEdit;
	private int mCurrentId = 2;
	private boolean isFirst = true;
	private List<EditText> mEditList = new ArrayList<EditText>();
	
	private TemplateVideo mTemplateVideo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.add_captions_layout);
		mTemplateVideo = getIntent().getParcelableExtra("TemplateVideo");
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_right = (TextView) findViewById(R.id.bt_title_right);
		tv_back = (TextView) findViewById(R.id.bt_back);
		tv_back.setVisibility(View.VISIBLE);
		tv_back.setBackgroundResource(R.drawable.back_bg);
//		btn_right.setVisibility(View.VISIBLE);
		tv_title.setText(getString(R.string.add_captions));
		mAddEdit = (Button) findViewById(R.id.bt_add_edit);
		EditText et1 = (EditText) findViewById(R.id.et_captions);
		EditText et2 = (EditText) findViewById(R.id.et_captions2);
		EditText et3 = (EditText) findViewById(R.id.et_captions3);
		EditText et4 = (EditText) findViewById(R.id.et_captions4);
		EditText et5 = (EditText) findViewById(R.id.et_captions5);
		et1.setText(mTemplateVideo.getSubTitle1());
		et2.setText(mTemplateVideo.getSubTitle2());
		et3.setText(mTemplateVideo.getSubTitle3());
		et4.setText(mTemplateVideo.getSubTitle4());
		et5.setText(mTemplateVideo.getSubTitle5());
		mEditList.add(et1);
		mEditList.add(et2);
		mEditList.add(et3);
		mEditList.add(et4);
		mEditList.add(et5);
	}

	@Override
	protected void loadListener() {
		btn_right.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		mAddEdit.setOnClickListener(this);
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
		case R.id.bt_add_edit:
			for(int i = 0; i < mEditList.size(); i++){
				EditText editText = mEditList.get(i);
				String str = editText.getText().toString();
				Class templateVideo = mTemplateVideo.getClass();
				try {
					Method subTitles = templateVideo.getDeclaredMethod("setSubTitle" + (i+1) +"", String.class);
					subTitles.invoke(mTemplateVideo, str);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Intent intent = new Intent(this, TemplateDetailActivity.class);
			intent.putExtra("TemplateVideo", mTemplateVideo);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.bt_title_right:
			
			break;
			

		default:
			break;
		}
	}
//	private void createEditText(){
//		RelativeLayout relLayout = new RelativeLayout(this);
//		RelativeLayout.LayoutParams paramRel = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		if(isFirst){
////			paramRel.addRule(RelativeLayout.BELOW, mEditText.getId());
//			isFirst = false;
//		} else {
////			paramRel.addRule(RelativeLayout.BELOW, mCurrentId);
//			mCurrentId ++;
//			if(mCurrentId == 5){
//				mAddEdit.setVisibility(View.GONE);
//			}
//		}
//		relLayout.setId(mCurrentId);
//		relLayout.setLayoutParams(paramRel);
//		final EditText editText = new EditText(this);
//		RelativeLayout.LayoutParams editParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Tools.dip2px(this, 100));
//		editParams.setMargins(Tools.dip2px(this, 10), 10, Tools.dip2px(this, 10), 10);
//		
//		editText.setLayoutParams(editParams);
//		editText.setBackgroundResource(R.drawable.bg_edit_text);
//		editText.setGravity(Gravity.TOP);
//		editText.setHint(getString(R.string.and_captions_hint));
//		RelativeLayout.LayoutParams imvParam = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		ImageView imageView = new ImageView(this);
//		imvParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		imageView.setBackgroundResource(R.drawable.close_selector);
//		imageView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mRel.removeView((View)v.getParent());
//				mEditList.remove(editText);
//				mCurrentId--;
//			}
//		});
//		imageView.setLayoutParams(imvParam);
//		relLayout.addView(editText);
//		relLayout.addView(imageView);
//		mRel.addView(relLayout);
//		mEditList.add(editText);
//	}
}
