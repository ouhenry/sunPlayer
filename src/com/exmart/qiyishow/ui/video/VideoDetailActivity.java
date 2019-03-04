package com.exmart.qiyishow.ui.video;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.renren.Renren;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.CommentAdapter;
import com.exmart.qiyishow.analysisJson.JSONCommentList;
import com.exmart.qiyishow.analysisJson.JSONCommon;
import com.exmart.qiyishow.analysisJson.JSONVideoDetail;
import com.exmart.qiyishow.bean.CommData;
import com.exmart.qiyishow.bean.Comment;
import com.exmart.qiyishow.bean.TemplateVideo;
import com.exmart.qiyishow.bean.Video;
import com.exmart.qiyishow.loader.core.DisplayImageOptions;
import com.exmart.qiyishow.loader.core.ImageLoader;
import com.exmart.qiyishow.loader.core.VideoLoader;
import com.exmart.qiyishow.loader.core.assist.FailReason;
import com.exmart.qiyishow.loader.core.display.RoundedBitmapDisplayer;
import com.exmart.qiyishow.loader.core.display.SimpleBitmapDisplayer;
import com.exmart.qiyishow.loader.core.listener.VideoLoadingListener;
import com.exmart.qiyishow.loader.core.listener.VideoLoadingProgressListener;
import com.exmart.qiyishow.share.OnekeyShare;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Data;
import com.exmart.qiyishow.tools.HttpNetwork;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.frame.BaseActivity;
import com.exmart.qiyishow.ui.home.TemplateDetailActivity;
import com.exmart.qiyishow.ui.user.LoginActivity;
import com.exmart.qiyishow.ui.view.ProgressWheel;
import com.exmart.qiyishow.ui.view.SoftKeyboard;
import com.exmart.qiyishow.ui.view.SoftKeyboard.SoftKeyboardChanged;
import com.exmart.qiyishow.ui.view.TextureVideoView;

/**
 * 视频详情
 * 
 * @author ZhaoYe
 * 
 */
public class VideoDetailActivity extends BaseActivity implements
		OnClickListener, Callback, VideoLoadingListener, VideoLoadingProgressListener
		, OnScrollListener {
	private TextView mText_title;
	private TextView mButton_back;
	private ListView mListView;

	private List<Comment> mCommentList;
	private CommentAdapter adapter;
	private PopupWindow pwView;
	private HashMap<String, Object> reqData = new HashMap<String, Object>();
	private static final int GOT_MSG_PLATFORM_LIST = 1;
	private static final int MSG_TOAST = 2;
	private static final int GONE_RATE = 3;
	public static final int REPLAY = 4;
	public static final int UPDATE_EDITTEXT = 5;
	private final static String DELETE_VIDEO = "delete_video";
	
	private int pageNo = 1;
	private String mVideoId;
	private Button mBtComment;
	private Video mVideo;
	private EditText mEtComment;
	private ImageView mIvIsLike;
	private TextView mTvLikeCount;
	private DisplayImageOptions options;
	private TextureVideoView mTvv;
	private ProgressWheel mTvPercentage;
	private Handler mHandler;
	private CommData commData;
	
	private String replayCommentId = "";
	private String replayUserId = "";
	private String replayUserName = "";
	private ImageView mIvPause;
	private ImageView mHeadImageView;
	private DisplayImageOptions optionsAvatar;
	
	private String Image;
	
	private boolean ShareFlag;
	
	private boolean mIsLoading = false;
	private boolean mHashMoreList = true;
	private boolean mIsFirstLoading = true;
	private View mFootView;
	
	private SoftKeyboard softKeyboard; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		ShareSDK.initSDK(this);
		setContentView(R.layout.activity_video_detail);
		options = new DisplayImageOptions.Builder()
		.cacheInMemory(false)
		.cacheOnDisk(true)
		.displayer(new SimpleBitmapDisplayer())
		.considerExifParams(true)
		.build();
		initView();
		optionsAvatar = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(60))
		.build();
		mCommentList = new ArrayList<Comment>();
		adapter = new CommentAdapter(this, mCommentList);
		mListView.setAdapter(adapter);
		initHandler();

		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... arg0) {
				String userId = Tools.getValueInSharedPreference(VideoDetailActivity.this, Constant.SPNAME, Constant.USERID);
				String result = HttpNetwork.httpNetwork(
						Data.VideoLike(userId, arg0[0]),
						Constant.VIDEODETAIL, VideoDetailActivity.this);
				Log.d("data", "详情="+result);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				CommData commData = null;
				System.out.println(result);
				try {
					commData = new JSONCommon().getCommonCode(result);
					if (commData.getCode().equals("1")) {
						mVideo = new JSONVideoDetail().getVideo(commData
								.getJosnObj());
						((TextView) findViewById(R.id.video_author))
								.setText(mVideo.getAuthorNikeName());
						((TextView) findViewById(R.id.upload_time))
								.setText(Tools.stringDate2jiange(mVideo.getUploadTime()));
						((TextView) findViewById(R.id.play_count))
								.setText("播放次数:"+mVideo.getClickNum());
						((TextView) findViewById(R.id.video_describe))
								.setText(mVideo.getContent());
						((TextView) findViewById(R.id.support_count))
								.setText(mVideo.getCountOflike());
						mBtComment.setText("评论" + mVideo.getCountOfComment());
						mIvIsLike = (ImageView) findViewById(R.id.button_support);
						updateSupport(mVideo.getIsLiked());
						
						ImageLoader.getInstance().displayImage(mVideo.getImageUrl(), mHeadImageView, optionsAvatar);

						/**
						 * 加载视频================================================================================
						 */
						VideoLoader.getInstance().displayVideo(mVideo.getUrl(), options, VideoDetailActivity.this, VideoDetailActivity.this);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.execute(mVideoId);
		mIsLoading = true;
		initComment(mVideoId);
		final RelativeLayout wholeView = (RelativeLayout) findViewById(R.id.video_layout);
		InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
		softKeyboard = new SoftKeyboard(wholeView, im);
		softKeyboard.setSoftKeyboardCallback(new SoftKeyboardChanged() {
			
			@Override
			public void onSoftKeyboardShow() {
				
			}
			
			@Override
			public void onSoftKeyboardHide() {
				mHandler.sendEmptyMessage(UPDATE_EDITTEXT);
			}
		});
	}

	private void initHandler() {
		mHandler = new Handler(this);
	}
	private void initView() {
		Image = getIntent().getExtras().getString("image");
		Log.d("data", "Image on in="+Image);
		mText_title = (TextView) findViewById(R.id.tv_title);
		mButton_back = (TextView) findViewById(R.id.bt_back);
		mButton_back.setBackgroundResource(R.drawable.back_bg);
		mButton_back.setVisibility(View.VISIBLE);
		mListView = (ListView) findViewById(R.id.List_Video);
		View view = LayoutInflater.from(this).inflate(R.layout.video_detail_head, null);
		Button btShare = (Button)view.findViewById(R.id.button_share);
		btShare.setOnClickListener(this);
		view.findViewById(R.id.button_diy).setOnClickListener(this);
		mIvPause = (ImageView) view.findViewById(R.id.iv_pause);
		view.findViewById(R.id.btn_full_screen).setOnClickListener(this);
		mTvPercentage = (ProgressWheel) view.findViewById(R.id.pw_percentage);
		mTvPercentage.spin();
		mEtComment = (EditText) findViewById(R.id.comment_send_edit);
		
//		mEtComment.setFocusable(true);
//		mEtComment.setFocusableInTouchMode(true);
//		mEtComment.requestFocus();
//		mEtComment.requestFocusFromTouch();
		mTvv = (TextureVideoView) view.findViewById(R.id.textureVideoView);
		mTvv.setOnClickListener(this);
		mBtComment = (Button) view.findViewById(R.id.button_comment);
		mTvLikeCount = (TextView) view.findViewById(R.id.support_count);
		
		
		TextView tvReport = (TextView) findViewById(R.id.bt_title_right);
		mHeadImageView = (ImageView) view.findViewById(R.id.title_photo);
		tvReport.setBackgroundResource(R.drawable.video_detail_report);
		tvReport.setVisibility(View.VISIBLE);
		tvReport.setOnClickListener(this);
		LinearLayout ll_support = (LinearLayout) view.findViewById(R.id.ll_support);
		Button btDelete = (Button) view.findViewById(R.id.button_delete);
		ll_support.setOnClickListener(this);
		btDelete.setOnClickListener(this);
		Button btRelease = (Button) view.findViewById(R.id.button_release);
		btRelease.setOnClickListener(this);
		mListView.addHeaderView(view);
		mListView.setOnScrollListener(this);
		Bundle bundle = getIntent().getExtras();
		mVideoId = bundle.getString("id");
		
		if (bundle.containsKey("type")) {
			String type = bundle.getString("type");
			if (type.equals("2")) {
				
			} else if (type.equals("1")) {
				
			} else if (type.equals("3")) {
				ll_support.setVisibility(View.GONE);
				btDelete.setVisibility(View.VISIBLE);
				findViewById(R.id.comment_send_layout).setVisibility(View.GONE);
			} else if (type.equals("4")) {
				mBtComment.setVisibility(View.GONE);
				btRelease.setVisibility(View.VISIBLE);
//				btShare.setVisibility(View.GONE);
				ll_support.setVisibility(View.GONE);
				findViewById(R.id.comment_send_layout).setVisibility(View.GONE);
				btDelete.setVisibility(View.VISIBLE);
			}
		}
	}

	private void initComment(String strId) {
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... arg0) {
				String result = HttpNetwork.httpNetwork(Data.VideoGetComments(
						arg0[0], pageNo + "", Constant.COUNT_OF_PAGE),
						Constant.VIDEODCOMMENTS, VideoDetailActivity.this);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				mIsLoading = false;
				CommData commData = null;
				try {
					commData = new JSONCommon().getCommonCode(result);
					if (commData.getCode().equals("1")) {
						List<Comment> list = new JSONCommentList()
								.analyzeCommentList(commData.getJosnObj());
						if(list.size() == 0) {
							mHashMoreList = false;
						} else {
							pageNo++;
						}
						mCommentList.addAll(list);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}
		}.execute(strId);
	}

	@Override
	protected void loadListener() {
		View viewShare = LayoutInflater.from(this).inflate(
				R.layout.share_layout, null);
		pwView = new PopupWindow(viewShare, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, true);
		pwView.setAnimationStyle(R.style.popupAnimation);
		pwView.setOutsideTouchable(true);
		pwView.setFocusable(true);
		mButton_back.setOnClickListener(this);
		pwView.setBackgroundDrawable(new BitmapDrawable());
		viewShare.findViewById(R.id.ib_copy_share).setOnClickListener(this);
		viewShare.findViewById(R.id.ib_pengyouquan_share).setOnClickListener(
				this);
		viewShare.findViewById(R.id.ib_qq_share).setOnClickListener(this);
		viewShare.findViewById(R.id.ib_qq_zone_share).setOnClickListener(this);
		viewShare.findViewById(R.id.ib_renren_share).setOnClickListener(this);
		viewShare.findViewById(R.id.ib_sina_share).setOnClickListener(this);
		viewShare.findViewById(R.id.ib_weixin_share).setOnClickListener(this);
		viewShare.findViewById(R.id.bt_cancel).setOnClickListener(this);
		findViewById(R.id.comment_send_text).setOnClickListener(this);
		mFootView = LayoutInflater.from(this).inflate(R.layout.loading_view, null);
	}

	@Override
	protected void Request() {

	}

	@Override
	protected void logic() {
		mText_title.setText(getString(R.string.video_title_text));
	}

	@Override
	public void onClick(View v) {
		Platform plat = null;
		switch (v.getId()) {
		case R.id.bt_back:
			finish();
			break;
		case R.id.btn_full_screen:
			if (mVideo == null) {
				return;
			}
			Intent intentFull = new Intent(this, MediaPlayerActivity.class);
			intentFull.putExtra("videoUrl", mVideo.getUrl());
			startActivity(intentFull);
			break;
		case R.id.button_share:
			showShareView();
			break;
		case R.id.button_diy:
			Intent intent = new Intent(this, TemplateDetailActivity.class);
			String strSubTitles = mVideo.getSubTitles();
			String[] subs = strSubTitles.split("#");
			TemplateVideo templateVideo = new TemplateVideo();
			templateVideo.setId(mVideo.getTemplateId());
			templateVideo.setTname(mVideo.getVideoTitle());
			templateVideo.setDesc("");
			templateVideo.setUrl(mVideo.getUrl());
			templateVideo.setPicurl(mVideo.getImageUrl());
			templateVideo.setSubTitle1("天气很好");
			templateVideo.setSubTitle2("节日快乐");
			templateVideo.setSubTitle3("大家好");
			templateVideo.setSubTitle4("很好玩吧");
			templateVideo.setSubTitle5("welcome");
			
			intent.putExtra("templateVideo", templateVideo);
			startActivity(intent);
			break;
		case R.id.ll_support:
			String userId = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.USERID);
			if (TextUtils.isEmpty(userId)) {
				Toast.makeText(this, "用户未登录", Toast.LENGTH_SHORT).show();
				return;
			}
			clickSupport(userId);
			break;
		case R.id.button_release:
			Intent intentRelease = new Intent(this, ReleaseActivity.class);
			intentRelease.putExtra("videoId", mVideoId);
			startActivity(intentRelease);
			break;
		case R.id.ib_copy_share:
			break;
		case R.id.ib_pengyouquan_share:
			ShareFlag = false;
			showShare(true,WechatMoments.NAME,false);
			break;
		case R.id.ib_qq_share:
			ShareFlag = false;
			showShare(true,QQ.NAME,false);
			break;
		case R.id.ib_qq_zone_share:
			ShareFlag = false;
			showShare(true,QZone.NAME,false);
			break;
		case R.id.ib_renren_share:
			ShareFlag = false;
			showShare(true,Renren.NAME,false);
			break;
		case R.id.ib_sina_share:
			ShareFlag = true;
			showShare(true,SinaWeibo.NAME,false);
			break;
		case R.id.ib_weixin_share:
			ShareFlag = false;
			showShare(true,Wechat.NAME,false);
			break;
		case R.id.bt_cancel:
			pwView.dismiss();
			break;
		case R.id.comment_send_text:
			
			mEtComment = (EditText) findViewById(R.id.comment_send_edit);
			String strComment = mEtComment.getText().toString();
			if (strComment.equals("")) {
				return;
			}
			String userCommId = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.USERID);
			if (TextUtils.isEmpty(userCommId)) {
				Toast.makeText(this, "用户未登录", Toast.LENGTH_SHORT).show();
				intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				return;
			}
			addCommet(strComment, userCommId, replayUserId);
			mEtComment.setText("");
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			imm.hideSoftInputFromWindow(mEtComment.getWindowToken(), 0);
			break;
		case R.id.button_delete:
			new DeleteVideo().execute("1", mVideoId, Constant.VIDEO_REMOVE);
			break;
		case R.id.textureVideoView:
		    if(mTvv.isPlaying()){
		    	mTvv.pause();
		    	mIvPause.setVisibility(View.VISIBLE);
		    } else {
		    	mIvPause.setVisibility(View.GONE);
		    	mTvv.play();
		    }
			break;
		case R.id.bt_title_right:
			Builder builder = new AlertDialog.Builder(this);
			builder.setItems(R.array.dialog_select_report,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String userId = Tools.getValueInSharedPreference(VideoDetailActivity.this, Constant.SPNAME, Constant.USERID);
							if (TextUtils.isEmpty(userId)) {
								Toast.makeText(VideoDetailActivity.this, "用户未登录", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(VideoDetailActivity.this, LoginActivity.class);
								startActivity(intent);
								return;
							}
							switch (which) {
								case 0:
									Log.d("data", "mVideoId="+mVideoId);
									VideoReport(userId,mVideoId,"MV带有色情或政治内容");
									break;
								case 1:
									VideoReport(userId,mVideoId,"盗用他人的MV");
									break;
								case 2:
									VideoReport(userId,mVideoId,"其他原因");
									break;
							}
						}
					});
			
			builder.setPositiveButton(getString(R.string.Cancel),
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
						}
					});
			builder.setTitle(getString(R.string.select_report));
			builder.show();
			break;
		}
	}
	/**
	 * 举报接口
	 */
	private void VideoReport(final String UserID,final String VideoID,final String content){
		
		new Thread(){
			public void run() {
				String mResult = HttpNetwork.httpNetwork(Data.VideoReport(UserID, VideoID, content), Constant.VIDOE_REPORT, VideoDetailActivity.this);
				Log.d("data", "举报：mResult="+mResult);
				try {
					commData = new JSONCommon().getCommonCode(mResult);
					if (commData.getCode().equals("1")) {
						mHandler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mTvv.pause();
	}
	
	@Override
	protected void onDestroy() { 
		super.onDestroy();  
		mTvv.stop(); 
		mTvv.relaseMediaPlayer(); 
		softKeyboard.unRegisterSoftKeyboardCallback();
		if (mVideo != null) {
			VideoLoader.getInstance().getTasks().get(mVideo.getUrl()).setSyncLoading(false);
		}
	}

	private void showShare(boolean silent, String platform, boolean captureView) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setTitle(mVideo.getVideoTitle());
		if(ShareFlag){
			oks.setText("这么搞笑的视频快来看看吧"+mVideo.getShareUrl());
		}else{
			oks.setText("这么搞笑的视频快来看看吧");
		}
		
		oks.setUrl(mVideo.getShareUrl());
		oks.setImageUrl(Image);
		oks.setTitleUrl(mVideo.getShareUrl());
		oks.setComment(getResources().getString(R.string.share));
		oks.setSite(getResources().getString(R.string.app_name));
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}
		oks.setDialogMode();
		oks.show(this);
	}

	private void showShareView() {
		pwView.showAtLocation(findViewById(R.id.comment_send_layout),
				Gravity.BOTTOM, 0, 0);
	}

	public Handler getmHandler() {
		return mHandler;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case GOT_MSG_PLATFORM_LIST: {
			// afterGotPlatformList();
		}
			break;
		case MSG_TOAST:
			String str = (String) msg.obj;
			Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
			break;
		
		case GONE_RATE:
			mTvPercentage.setVisibility(View.GONE);
			break;
		case 0:
			Toast.makeText(this,commData.getMsg() , Toast.LENGTH_SHORT).show();
			break;
		case REPLAY:
			Comment comment = (Comment)msg.obj;
//			mEtComment.findFocus();
			mEtComment.setFocusable(true);
			mEtComment.setFocusableInTouchMode(true);
			mEtComment.requestFocus();
			mEtComment.requestFocusFromTouch();
			
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
			mEtComment.setHint("回复@" + comment.getCommentUserName());
			replayCommentId = comment.getId();
			replayUserId = comment.getUserId();
			replayUserName = comment.getCommentUserName();
			break;
		case UPDATE_EDITTEXT:
			if (!replayCommentId.equals("")) {
				mEtComment.setText("");
				mEtComment.setHint("写评论");
			}
			replayCommentId = "";
			replayUserId = "";
			break;
		}
		return false;
	}

	private void addCommet(final String strComment, String userId, final String replayUserId) {
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				String result = HttpNetwork.httpNetwork(
						Data.VideoComment(params[0], mVideoId, strComment, params[2]),
						Constant.ADDCOMMENT, VideoDetailActivity.this);
				return result;
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				CommData commData = null;
				try {
					commData = new JSONCommon().getCommonCode(result);
					String userId = Tools.getValueInSharedPreference(VideoDetailActivity.this, Constant.SPNAME, Constant.USERID);
					String userName = Tools.getValueInSharedPreference(VideoDetailActivity.this, Constant.SPNAME, Constant.NAME);
					if (commData.getCode().equals("1")) {
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String strDate = sdf.format(date);
						strDate += ".0";
						Comment comment = new Comment();
						comment.setCommentUserName(userName);
						comment.setContent(strComment);
						comment.setUserId(userId);
						comment.setDate(strDate);
						comment.setReplayUserId(replayUserId);
						comment.setReplayUserName(replayUserName);
						String strPhotoPath = Tools.getValueInSharedPreference(VideoDetailActivity.this, Constant.SPNAME, Constant.PHOTO);
						comment.setAutherAvatarUrl(strPhotoPath);
						mCommentList.add(0, comment);
					} else {
						Toast.makeText(VideoDetailActivity.this,
								commData.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}
		}.execute(userId, strComment, replayUserId);
	}

	/*
	 * 点赞，已赞取消赞
	 */
	private void clickSupport(String userId) {
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				String result = HttpNetwork.httpNetwork(
						Data.VideoLike(params[0], mVideoId), Constant.VideoLIKE,
						VideoDetailActivity.this);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				try {
					CommData commData = new JSONCommon().getCommonCode(result);
					System.out.println(result);
					if (commData.getCode().equals("1")) {
						String isLiked = "0";
						String countOfLike = null;
						try {
							isLiked = commData.getJosnObj()
									.getString("isLiked");
							countOfLike = commData.getJosnObj().getString(
									"CountOfLike");
						} catch (Exception e) {
							e.printStackTrace();
						}
						updateSupport(isLiked);
						mTvLikeCount.setText(countOfLike);

					} else {
						Toast.makeText(VideoDetailActivity.this,
								commData.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.onPostExecute(result);
			}
		}.execute(userId);
	}

	private void updateSupport(String strIsLiked) {
		
		if (strIsLiked.equals("1")) {
			mIvIsLike.setBackgroundResource(R.drawable.support_bg);
		} else {
			mIvIsLike.setBackgroundResource(R.drawable.unsupport);
		}
	}

	class DeleteVideo extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {
			String result = HttpNetwork.httpNetwork(
					Data.VideoRemove(args[0], args[1]),
					Constant.VIDEO_REMOVE, VideoDetailActivity.this);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				CommData commData = new JSONCommon().getCommonCode(result);
				if (commData.getCode().equals("1")) {
					Intent intent = new Intent();
					intent.putExtra("videoId", mVideoId);
					intent.setAction(DELETE_VIDEO);
					sendBroadcast(intent);
					finish();
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/*
	 * 请求播放次数
	 */
	private void countPlayTimes() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... arg0) {
				String result = HttpNetwork.httpNetwork(
						Data.videoPalyCount(mVideoId),
						Constant.VIDEO_PLAY_COUNT, VideoDetailActivity.this);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				try {
					CommData commData = new JSONCommon().getCommonCode(result);
					if (commData.getCode().equals("1")) {

					} else {
						Toast.makeText(VideoDetailActivity.this,
								commData.getMsg(), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	@Override
	public void onProgressUpdate(String videoUri, int current, int total) {
		
		mTvPercentage.setText(Math.round(100.0f * current / total) + "%");
	}

	@Override
	public void onLoaingStarted(String videoUri) {
		
	}

	@Override
	public void onLoadingFailed(String videoUri, FailReason failReason) {
		
	}

	@Override
	public void onLoadingComplete(String vodeoUri) {
		mHandler.sendEmptyMessage(GONE_RATE);
		mTvv.setDataSource(vodeoUri);
		mTvv.play();
		mIvPause.setVisibility(View.GONE);
	}

	@Override
	public void onLoadingCancelled(String videoUri) {
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		if (totalItemCount == 0 || !Tools.isNetworkConnected(this)) {
//			Toast.makeText(this, "检查网络连接", Toast.LENGTH_SHORT).show();
			return;
		}
		if (firstVisibleItem + visibleItemCount == totalItemCount) {
			if (!mIsLoading && mHashMoreList) {
				mIsLoading = true;
				mFootView.setVisibility(View.VISIBLE);
				initComment(mVideoId);
			}
		}
	}
	
}
