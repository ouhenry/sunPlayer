package com.exmart.qiyishow.ui.home;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.LocalVideoListAdapter;
import com.exmart.qiyishow.record.FFmpegPreviewActivity;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.frame.BaseActivity;

public class LocalVideoList extends BaseActivity implements OnClickListener, OnItemClickListener{
	private LocalVideoListAdapter mAdapter;
	private ListView mListView;
	private final static String MEDIA_DATA = MediaStore.Video.Media.DATA;
	private final static Uri MEDIA_EXTERNAL_CONTENT_URL = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	private Cursor mCursor;
	private List<String> mVideoUrlList = new ArrayList<String>();
	
	@Override
	protected void loadLayout() { 
		setContentView(R.layout.local_video_layout);
		((TextView)findViewById(R.id.tv_title)).setText(getString(R.string.local_video));
		TextView tvBack = (TextView)findViewById(R.id.bt_back);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setBackgroundResource(R.drawable.back_bg);
		tvBack.setOnClickListener(this);
		initVideos();
		filterVideo();
		mListView = (ListView) findViewById(R.id.lv_local_video);
		mAdapter = new LocalVideoListAdapter(this, mVideoUrlList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	private void initVideos() {
		try {
			final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
			String[] proj = {MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID};
			mCursor = this.getContentResolver().query(MEDIA_EXTERNAL_CONTENT_URL, proj, null, null, orderBy + " DESC");
			setAdapter();
		} catch (Exception e) {
			mCursor.close();
			e.printStackTrace();
			
		}
	}

	private void setAdapter() {
		int count = mCursor.getCount();
		if(count > 0){
			int dataColumnIndex = mCursor.getColumnIndex(MEDIA_DATA);
			mCursor.moveToFirst();
			for(int i = 0; i < count; i++){
				mCursor.moveToPosition(i);
				String url = mCursor.getString(dataColumnIndex);
				mVideoUrlList.add(url);
			}
			mCursor.close();
			
		}
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
	public LocalVideoListAdapter getAdapter() {
		return mAdapter;
	}
	private void filterVideo(){
		for(int i = 0; i < mVideoUrlList.size(); i ++){
			String videoPath = mVideoUrlList.get(i);
			if(!videoPath.endsWith(".mp4")){
				mVideoUrlList.remove(i);
				continue;
			}
			File mediaFile = new File(videoPath);
			if(mediaFile.exists()){
				if(Tools.CheckMediaFileSize(mediaFile) > 10){
					mVideoUrlList.remove(i);
					continue;
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String strPath = mVideoUrlList.get(position);
		Intent intent = new Intent(this, FFmpegPreviewActivity.class);
		intent.putExtra("path", strPath);
		startActivity(intent);
	}
}
