package com.exmart.qiyishow.ui.frame;

import java.util.ArrayList;
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
import com.exmart.qiyishow.ui.user.ScrollTabHolderFragment;
import com.exmart.qiyishow.ui.video.VideoDetailActivity;
import com.exmart.qiyishow.ui.view.GridViewWithHeaderAndFooter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class UserVideoGridFragment extends ScrollTabHolderFragment implements
		OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
		OnScrollListener {
	
	private GridViewWithHeaderAndFooter mGridView;
	private List<Video> mListVideo;
	private VideoListAdapter mAdapter;
	private Activity mActiviy;
	private Intent intent;
	private int pageNo = 1;
	private String mVideoOrder = "0";
	private String mVideoType;
	private String mUserId;
	private String mlistDataCache;
	private boolean mIsFirstLoad = true;
	private final static String UPDATE_GRID = "delete_video";
	private final static String RELEASE_GRID = "release_grid";
	private final static int DELETE_VIDEO = 1;
	private final static int RELEASE_VIDEO = 2;
	private boolean mHasMoreList = true;
	private View footView;
	private boolean isLoading = false;
	
	private Handler mHandler;

	private SwipeRefreshLayout mSwipeLayout;

	public static UserVideoGridFragment getUserVideoGridFragmentInstance(String arg) {
		UserVideoGridFragment videoGridFragment = new UserVideoGridFragment();
		Bundle bundle = new Bundle();
		bundle.putString("type", arg);
		videoGridFragment.setArguments(bundle);
		return videoGridFragment;
	}

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
		mActiviy = getActivity();
		view = inflater.inflate(R.layout.home_video_list_layout, null);
		mGridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.h_gv_video);
		footView = LayoutInflater.from(mActiviy).inflate(R.layout.loading_view, null);
		View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mGridView, false);
		mGridView.addHeaderView(placeHolderView);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mListVideo = new ArrayList<Video>();
		mAdapter = new VideoListAdapter(mActiviy, mListVideo);
		
		mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		mSwipeLayout.setColorScheme(R.color.purple, R.color.green, R.color.yellow, R.color.release_bg_color);
		
		
		mGridView.addFooterView(footView);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
		mGridView.setOnScrollListener(this);
		
		initCacheData();
		
//		new VideoListByUserAsysncRequest().execute();
		mSwipeLayout.setOnRefreshListener(this);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(UPDATE_GRID);
		intentFilter.addAction(RELEASE_GRID);
		updateListBroadCastReviver reviver = new updateListBroadCastReviver();
		mActiviy.registerReceiver(reviver, intentFilter);
		initHandler();
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			isLoading = true;
			mIsFirstLoad = true;
			new VideoListByUserAsysncRequest().execute("1");
		}
		super.onHiddenChanged(hidden);
	}

	private void initHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == DELETE_VIDEO) {
					String strVideoId = intent.getStringExtra("videoId");
					deleteVideo(strVideoId);
					mAdapter.notifyDataSetChanged();
				} else if (msg.what == RELEASE_VIDEO) {
					if (mVideoType.equals("4")) {// 未发布界面
						String strVideoId = intent.getStringExtra("videoId");
						deleteVideo(strVideoId);
						mAdapter.notifyDataSetChanged();
					} else if (mVideoType.equals("3")) {// 用户已发布界面
						// 添加一条数据
					} else if (mVideoType.equals(1)) {// 最新数据
						// 添加一条数据
					}
				}
				super.handleMessage(msg);
			}
		};
		
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
		if (position == 0 || position == mListVideo.size() + 2) {
			return;
		}
		Video video;
		video = mListVideo.get(position - 2);
		intent = new Intent(mActiviy, VideoDetailActivity.class);
		intent.putExtra("id", video.getId());
		intent.putExtra("UserId", mUserId);
		intent.putExtra("type", mVideoType);
		intent.putExtra("image", video.getImageUrl());
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
	private synchronized void deleteVideo(String strId) {
		for (int i = 0; i < mListVideo.size(); i++) {
			Video video = mListVideo.get(i);
			if (video.getId().equals(strId)) {
				mListVideo.remove(video);
			}
		}
	}

	class VideoListByUserAsysncRequest extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {
			String strUserId = Tools.getValueInSharedPreference(mActiviy, Constant.SPNAME, Constant.USERID);
			String result = HttpNetwork
					.httpNetwork(Data.VideoListByUser(mActiviy, strUserId,
							mVideoType, args[0], Constant.COUNT_OF_PAGE),
							Constant.VideoListByUser, mActiviy);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			mSwipeLayout.setRefreshing(false);
			isLoading = false;
			footView.setVisibility(View.GONE);
			mlistDataCache = result;
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

	private class updateListBroadCastReviver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(UPDATE_GRID)) {
				Message message = mHandler.obtainMessage();
				message.what = DELETE_VIDEO;
				UserVideoGridFragment.this.intent = intent;
				message.sendToTarget();
			} else if (intent.getAction().equals(RELEASE_GRID)) {
				UserVideoGridFragment.this.intent = intent;
				Message message = mHandler.obtainMessage();
				message.what = RELEASE_VIDEO;
				message.sendToTarget();
			}
		}
	}

	@Override
	public void onRefresh() {
		mIsFirstLoad = true;
		new VideoListByUserAsysncRequest().execute("1");
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mScrollTabHolder != null) {
			mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
		if (totalItemCount == 2) {
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
				new VideoListByUserAsysncRequest().execute(pageNo + "");
			} 
		}
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		if (scrollHeight == 0 && mGridView.getFirstVisiblePosition() >= 1) {
			return;
		}
		mGridView.setSelection(1);
	}

}
