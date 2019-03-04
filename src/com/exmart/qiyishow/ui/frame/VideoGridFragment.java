package com.exmart.qiyishow.ui.frame;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.baidu.mobstat.StatService;
import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.VideoListAdapter;
import com.exmart.qiyishow.analysisJson.JSONCommon;
import com.exmart.qiyishow.analysisJson.JSONVideoList;
import com.exmart.qiyishow.bean.CommData;
import com.exmart.qiyishow.bean.Video;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Data;
import com.exmart.qiyishow.tools.HttpNetwork;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.video.VideoDetailActivity;
import com.exmart.qiyishow.ui.view.GridViewWithHeaderAndFooter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * 视频列表界面 最热 <b>1<b/> 最新 <b>2</b> 已发布 <b>3</b> 未发布 <b>4</b>
 * 
 */
public class VideoGridFragment extends Fragment implements OnItemClickListener,
		SwipeRefreshLayout.OnRefreshListener, OnScrollListener{
	private GridViewWithHeaderAndFooter mGridView;
	private List<Video> mListVideo;
	private VideoListAdapter mAdapter;
	private Activity mActiviy;
	private Intent intent;
	private int pageNo = 1;
	private String mVideoOrder = "0";
	private String mVideoType;
	private String mlistDataCache;
	private boolean mIsFirstLoad = true;
	private final static String UPDATE_GRID = "update_grid";
	private final static String RELEASE_GRID = "release_grid";
	private boolean mHasMoreList = true;
	private View footView;
	private boolean isLoading = false;
	

	private SwipeRefreshLayout mSwipeLayout;

	public static VideoGridFragment getVideoGridFragmentInstance(String arg) {
		VideoGridFragment videoGridFragment = new VideoGridFragment();
		Bundle bundle = new Bundle();
		bundle.putString("type", arg);
		videoGridFragment.setArguments(bundle);
		return videoGridFragment;
	}
//	public static VideoGridFragment getVideoGridFragmentInstance(String arg) {
//		VideoGridFragment videoGridFragment = new VideoGridFragment();
//		Bundle bundle = new Bundle();
//		bundle.putString("type", arg);
//		videoGridFragment.setArguments(bundle);
//		return videoGridFragment;
//	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		Bundle bundle = getArguments();
		mVideoType = (String) bundle.get("type");
		view = inflater.inflate(R.layout.home_video_list_layout, null);
		
		return view;
	}

	private long getOverTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 3, 5);
		return calendar.getTimeInMillis();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mActiviy = getActivity();
		mListVideo = new ArrayList<Video>();
		mAdapter = new VideoListAdapter(mActiviy, mListVideo);
		footView = LayoutInflater.from(mActiviy).inflate(R.layout.loading_view, null);
		mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		mSwipeLayout.setColorScheme(R.color.purple, R.color.green, R.color.yellow, R.color.release_bg_color);
		
		mGridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.h_gv_video);
		mGridView.addFooterView(footView);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnScrollListener(this);
		
		if (mVideoType.equals("1") || mVideoType.equals("2")) {
			
		}
		initCacheData();
//		new VideoListAsysncRequest().execute(pageNo + "");
		mSwipeLayout.setOnRefreshListener(this);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UPDATE_GRID);
		intentFilter.addAction(RELEASE_GRID);
		updateListBroadCastReviver reviver = new updateListBroadCastReviver();
		mActiviy.registerReceiver(reviver, intentFilter);
	}

	private void initCacheData() {
		SharedPreferences preferences = mActiviy.getSharedPreferences("List", Context.MODE_PRIVATE);
		String data = preferences.getString(mVideoType, "");
		if (TextUtils.isEmpty(data)) {
			return;
		}
		CommData commData = null;
		try {
			commData = new JSONCommon().getCommonCode(data);
			if (commData.getCode().equals("1")) {
				List<Video> list = new JSONVideoList().getVideoList(commData
						.getJosnObj());
				mListVideo.addAll(list);
			} else if (commData.getCode().equals(0)) {
				Toast.makeText(mActiviy, commData.getMsg(), Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Video video;
		video = mListVideo.get(position);
		intent = new Intent(mActiviy, VideoDetailActivity.class);
		intent.putExtra("id", video.getId());
		intent.putExtra("type", mVideoType);
		intent.putExtra("image",video.getImageUrl());
		startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	class VideoListAsysncRequest extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {
			String result = HttpNetwork.httpNetwork(
					Data.VideoList(mActiviy, mVideoType, mVideoOrder, args[0]
							+ "", Constant.COUNT_OF_PAGE), Constant.VIDEOLIST,
					mActiviy);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			 mSwipeLayout.setRefreshing(false);
			 isLoading = false;
			 footView.setVisibility(View.GONE);
			 CommData commData = null;
			try {
				commData = new JSONCommon().getCommonCode(result);
				if (commData.getCode().equals("1")) {
					pageNo ++;
					if (mIsFirstLoad) {
						pageNo = 2;
						mListVideo.clear();
						mIsFirstLoad = false;
						mHasMoreList = true;
						mlistDataCache = result;
						SharedPreferences preferences = mActiviy.getSharedPreferences(
								"List", 0);
						Editor editor = preferences.edit();
						if (!TextUtils.isEmpty(mlistDataCache)) {
							editor.putString(mVideoType, mlistDataCache);
						}
						editor.commit();
					}
					
					List<Video> list = new JSONVideoList()
							.getVideoList(commData.getJosnObj());
					if (list.size() == 0) {
						mHasMoreList = false;
					}
					mListVideo.addAll(list);
				} else if (commData.getCode().equals(0)) {
					Toast.makeText(mActiviy, commData.getMsg(),
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				mHasMoreList = false;
				e.printStackTrace();
			}
			mAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}

//	class VideoListByUserAsysncRequest extends AsyncTask<String, Void, String> {
//
//		@Override
//		protected String doInBackground(String... args) {
//			String strUserId = Tools.getValueInSharedPreference(mActiviy, Constant.SPNAME, Constant.USERID);
//			String result = HttpNetwork
//					.httpNetwork(Data.VideoListByUser(mActiviy, strUserId,
//							mVideoType, pageNo + "", Constant.COUNT_OF_PAGE),
//							Constant.VideoListByUser, mActiviy);
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			mlistDataCache = result;
//			CommData commData = null;
//			try {
//				commData = new JSONCommon().getCommonCode(result);
//				if (commData.getCode().equals("1")) {
//					pageNo ++;
//					if (mIsFirstLoad) {
//						pageNo = 2;
//						mListVideo.clear();
//						mIsFirstLoad = false;
//						mHasMoreList = true;
//						mlistDataCache = result;
//						SharedPreferences preferences = mActiviy.getSharedPreferences(
//								"List", 0);
//						Editor editor = preferences.edit();
//						if (!TextUtils.isEmpty(mlistDataCache)) {
//							editor.putString(mVideoType, mlistDataCache);
//						}
//						editor.commit();
//					}
//					List<Video> list = new JSONVideoList()
//							.getVideoList(commData.getJosnObj());
//					mListVideo.addAll(list);
//				} else if (commData.getCode().equals(0)) {
//					Toast.makeText(mActiviy, commData.getMsg(),
//							Toast.LENGTH_SHORT).show();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			mGridView.setAdapter(mAdapter);
//			mAdapter.notifyDataSetChanged();
//			super.onPostExecute(result);
//		}
//	}

	private class updateListBroadCastReviver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(UPDATE_GRID)) {
				String strVideoId = intent.getStringExtra("videoId");
				for (Video video : mListVideo) {
					if (video.getId().equals(strVideoId)) {
						mListVideo.remove(video);
					}
				}
				mAdapter.notifyDataSetChanged();
			} else if (intent.getAction().equals(RELEASE_GRID)) {

				if (mVideoType.equals("4")) {// 未发布界面
					String strVideoId = intent.getStringExtra("videoId");
					for (Video video : mListVideo) {
						if (video.getId().equals(strVideoId)) {
							mListVideo.remove(video);
						}
					}
					mAdapter.notifyDataSetChanged();
				} else if (mVideoType.equals("3")) {// 用户已发布界面
					// 添加一条数据
				} else if (mVideoType.equals(1)) {// 最新数据
					// 添加一条数据
				}
			}
		}
	}

	@Override
	public void onRefresh() {
		mIsFirstLoad = true;
		new VideoListAsysncRequest().execute("1");
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (totalItemCount == 0) {
			return;
		}
		if (!Tools.isNetworkConnected(mActiviy)) {
			Toast.makeText(mActiviy, "检查网络连接", Toast.LENGTH_SHORT).show();
			return;
		}
		if (firstVisibleItem + visibleItemCount == totalItemCount){
			if (!isLoading && mHasMoreList) {
				isLoading = true;
				footView.setVisibility(View.VISIBLE);
				new VideoListAsysncRequest().execute(pageNo + "");
			} 
		}
	}

//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//			case R.id.iv_active:
//				Intent intent = new Intent(mActiviy, ActiveDetailActiviey.class);
//				startActivity(intent);
//				break;
//			case R.id.ib_hide_active:
//				mRlActive.setVisibility(View.GONE);
//				break;
//			default:
//				break;
//		}
//		
//	}

	
}