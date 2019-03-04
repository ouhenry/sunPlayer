package com.exmart.qiyishow.ui.home;

import com.exmart.qiyishow.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VideoOperateSelectFragment extends Fragment implements OnClickListener{
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.video_operate_select_layout, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.bt_back).setVisibility(View.GONE);
		((TextView)view.findViewById(R.id.tv_title)).setText(getString(R.string.video_operate));
		((Button)view.findViewById(R.id.bt_video_replace)).setOnClickListener(this);
		((Button)view.findViewById(R.id.bt_video_rcord)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.bt_video_replace:
			intent = new Intent(getActivity(), TemplateActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_video_rcord:
			intent = new Intent(getActivity(), EffectsListActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
