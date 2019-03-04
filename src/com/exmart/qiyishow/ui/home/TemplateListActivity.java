package com.exmart.qiyishow.ui.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.TemplateListAdapter;
import com.exmart.qiyishow.analysisJson.JSONCommon;
import com.exmart.qiyishow.analysisJson.JSONTemplateVideoList;
import com.exmart.qiyishow.bean.CommData;
import com.exmart.qiyishow.bean.TemplateVideo;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Data;
import com.exmart.qiyishow.tools.HttpNetwork;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.frame.BaseActivity;
import com.exmart.qiyishow.ui.view.GridViewWithHeaderAndFooter;
/**
 * 视频模板列表
 * 
 * @author ZhaoYe
 *
 */
public class TemplateListActivity extends BaseActivity implements OnClickListener,OnItemClickListener, 
				SwipeRefreshLayout.OnRefreshListener {
	
	private TextView mText_back;
	private GridViewWithHeaderAndFooter mGridView;
	private Intent intent;
	private TemplateListAdapter adapter;
	private String name;
	private String id;
	private int mPageNo = 1;
	private final int COUTN_OF_PAGE	= 20;
	private List<TemplateVideo> mArrayList;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private View mFootView;
	
	private boolean mIsLoading = false;
	private boolean mHashMoreList = true;
	private boolean mIsFirstLoading = true;

	@Override
	protected void loadLayout() {
		setContentView(R.layout.template_list_layout);
		mText_back = (TextView) findViewById(R.id.bt_back);
		mGridView = (GridViewWithHeaderAndFooter) findViewById(R.id.h_gv_video);
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		mSwipeRefreshLayout.setColorScheme(R.color.purple, R.color.green, R.color.yellow, R.color.release_bg_color);
		mFootView = LayoutInflater.from(this).inflate(R.layout.loading_view, null);
	}

	@Override
	protected void loadListener() {
		mText_back.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnScrollListener(mScrollListener);
		mSwipeRefreshLayout.setOnRefreshListener(this);
	}

	@Override
	protected void logic() {
		mText_back.setVisibility(View.VISIBLE);
		mText_back.setBackgroundResource(R.drawable.back_bg);
		Bundle mBundle = getIntent().getExtras();
		name = mBundle.getString("title");
		id = mBundle.getString("id");
		((TextView)findViewById(R.id.tv_title)).setText(name);
		mArrayList = new ArrayList<TemplateVideo>();
		mGridView.addFooterView(mFootView);
		adapter = new TemplateListAdapter(this,mArrayList);
		mGridView.setAdapter(adapter);
		initCacheData();
		getTypeTemplateList();
	}
	
	@Override
	protected void Request() {
		
	}

	private void initCacheData() {
		SharedPreferences preferences = TemplateListActivity.this.getSharedPreferences("templateList", Context.MODE_PRIVATE);
		String data = preferences.getString(id, "");
		List<TemplateVideo> list;
		try {
			CommData commData = new JSONCommon().getCommonCode(data);
			if(commData.getCode().equals("1")){
				list = new JSONTemplateVideoList().analysisTemplateVideoList(commData.getJosnObj());
				mArrayList.addAll(list);
				adapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			Log.d("data", e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 获取模板列表
	 */
	private void getTypeTemplateList() {
		mIsLoading = true;
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				String result = HttpNetwork.httpNetwork(Data.getTypeTemplateList(id, mPageNo + "", COUTN_OF_PAGE + ""), 
						Constant.TEMPLATE_List, TemplateListActivity.this);
				System.out.println(result);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				mSwipeRefreshLayout.setRefreshing(false);
				mIsLoading = false;
				mFootView.setVisibility(View.GONE);
				List<TemplateVideo> list;
				try {
					CommData commData = new JSONCommon().getCommonCode(result);
					if(commData.getCode().equals("1")){
						list = new JSONTemplateVideoList().analysisTemplateVideoList(commData.getJosnObj());
//						count = mBean.Count;
						if (list.size() == 0) {
							mHashMoreList = false;
						}
						mPageNo++;
						if (list.size() > 0 && mIsFirstLoading) {
							mPageNo = 2;
							mArrayList.clear();
							mIsFirstLoading = false;
							SharedPreferences preferences = TemplateListActivity.this.getSharedPreferences("templateList", Context.MODE_PRIVATE);
							Editor editor = preferences.edit();
							editor.putString(id, result);
							editor.commit();
						} 
						mArrayList.addAll(list);  
						adapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					Log.d("data", e.toString());
					e.printStackTrace();
				}
			}
		}.execute();
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		intent = new Intent(this,TemplateDetailActivity.class);
		intent.putExtra("templateVideo", mArrayList.get(position));
		startActivity(intent);
	}
	
	OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
		@Override
		public synchronized void onScroll(AbsListView view,
				int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if (totalItemCount == 0 || !Tools.isNetworkConnected(TemplateListActivity.this)) {
				Toast.makeText(TemplateListActivity.this, "检查网络连接", Toast.LENGTH_SHORT).show();
				return;
			}
			if (firstVisibleItem + visibleItemCount == totalItemCount) {
				if (!mIsLoading && mHashMoreList) {
					mIsLoading = true;
					mFootView.setVisibility(View.VISIBLE);
					getTypeTemplateList();
				}
			}
		}
	};

	@Override
	public void onRefresh() {
		mIsFirstLoading = true;
		getTypeTemplateList();
//		new VideoListAsysncRequest().execute("1");
	}
}