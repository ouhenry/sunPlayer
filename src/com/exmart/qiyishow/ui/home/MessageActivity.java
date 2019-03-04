package com.exmart.qiyishow.ui.home;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.adapter.MessageListAdapter;
import com.exmart.qiyishow.analysisJson.JSONCommon;
import com.exmart.qiyishow.analysisJson.JSONMessageList;
import com.exmart.qiyishow.bean.CommData;
import com.exmart.qiyishow.bean.Message;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Data;
import com.exmart.qiyishow.tools.HttpNetwork;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.frame.BaseActivity;
/**
 * 消息中心
 * @author ye
 *
 */
public class MessageActivity extends BaseActivity implements OnClickListener {
	private ListView mListView;
	private MessageListAdapter mAdapter;
	private List<Message> mList;
	private TextView mText_back;
	private int mPageNo = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.message_layout);
		mListView = (ListView) findViewById(R.id.lv_message);
		mText_back = (TextView) findViewById(R.id.bt_back);
		mText_back.setVisibility(View.VISIBLE);
		mText_back.setBackgroundResource(R.drawable.back_bg);
		((TextView) findViewById(R.id.tv_title)).setText(R.string.message_center);
		mList = new ArrayList<Message>();
		mAdapter = new MessageListAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		
	}

	@Override
	protected void loadListener() {
		mText_back.setOnClickListener(this);
	}

	@Override
	protected void Request() {
		String userId = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.USERID);
		new UserMessageAsyc().execute(userId);
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
		}
	}
	
	class UserMessageAsyc extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			String result = HttpNetwork.httpNetwork(Data.userMessage(params[0], "0", mPageNo + "", Constant.COUNT_OF_PAGE + ""), Constant.USER_MESSAGE, MessageActivity.this);
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			try {
				CommData commData = new JSONCommon().getCommonCode(result);
				if(commData.getCode().equals("1")){
					mList.addAll(new JSONMessageList().analysisMessageList(commData.getJosnObj()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mAdapter.notifyDataSetChanged();
		}
	}

}
