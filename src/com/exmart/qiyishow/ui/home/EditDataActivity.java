package com.exmart.qiyishow.ui.home;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.analysisJson.JSONCommon;
import com.exmart.qiyishow.analysisJson.UserJson;
import com.exmart.qiyishow.bean.CityBaseBean;
import com.exmart.qiyishow.bean.CommData;
import com.exmart.qiyishow.bean.Comment;
import com.exmart.qiyishow.bean.User;
import com.exmart.qiyishow.loader.core.DisplayImageOptions;
import com.exmart.qiyishow.loader.core.ImageLoader;
import com.exmart.qiyishow.loader.core.display.RoundedBitmapDisplayer;
import com.exmart.qiyishow.loader.core.display.SimpleBitmapDisplayer;
import com.exmart.qiyishow.tools.Constant;
import com.exmart.qiyishow.tools.Data;
import com.exmart.qiyishow.tools.HttpNetwork;
import com.exmart.qiyishow.tools.Tools;
import com.exmart.qiyishow.ui.frame.BaseActivity;
import com.exmart.qiyishow.ui.user.UserInfoActivity;
import com.tencent.connect.UserInfo;

/**
 * 编辑个人信息
 * 
 * @author ye
 * 
 */
public class EditDataActivity extends BaseActivity implements OnClickListener {
	private TextView mText_bt_title_right;
	private ImageView mImage_photo;
	private RelativeLayout mLayout_name;
	private RelativeLayout mLayout_sex;
	private RelativeLayout mLayout_address;

	private static final int INTNET_CAMERA = 0;
	private static final int GALLERY_KITKAT_INTENT_CALLED = 1;
	private static final int GALLERY_INTENT_CALLED = 2;
	private static final int CROP_PIC = 3;
	private Uri mCameraUri;
	private Intent intent;
	private String strName;
	private String strSex;
	private String mProvinceCode;
	private String mCityCode;
	private String mAreaCode; 
	
	
	private TextView tvNike;
	private TextView tvSex;
	private TextView tvArea;
	private CommData commData;
	private User user;
	
	String SP_user_id;
	String SP_user_name;
	String SP_user_sex;
	String SP_user_photo;
	String SP_user_province;
	String SP_user_city;
	String SP_user_area ;
	
	private byte[] Pic;
	private DisplayImageOptions optionsAvatar;
	
	
	public static ArrayList<CityBaseBean> arrayList;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadLayout() {
		setContentView(R.layout.edit_data_layout);
		if(arrayList == null){
			arrayList = new ArrayList<CityBaseBean>();
		}
		mText_bt_title_right = (TextView) findViewById(R.id.bt_title_right);
		mImage_photo = (ImageView) findViewById(R.id.img_user_photo);
		mLayout_name = (RelativeLayout) findViewById(R.id.Layout_name);
		mLayout_sex = (RelativeLayout) findViewById(R.id.Layout_sex);
		mLayout_address = (RelativeLayout) findViewById(R.id.Layout_address);
		tvNike = (TextView) findViewById(R.id.Text_name);
		tvSex = (TextView) findViewById(R.id.Text_sex);
		tvArea = (TextView) findViewById(R.id.Text_address);
		
		SP_user_id = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.USERID);
		SP_user_name = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.NAME);
		SP_user_sex = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.SEX);
		SP_user_photo = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.PHOTO);
		SP_user_province = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.PROVINCE);
		SP_user_city = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.CITY);
		SP_user_area = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.AREA);
		if(SP_user_sex.equals("1")){
			strSex = "1";
			SP_user_sex = "男";
		}else if(SP_user_sex.equals("2")){
			strSex = "2";
			SP_user_sex = "女";
		}else{
			strSex = "1";
			SP_user_sex = "男";
		}
		tvNike.setText(SP_user_name);
		tvSex.setText(SP_user_sex);
//		tvArea.setText(SP_user_province+"\t"+SP_user_city+"\t"+SP_user_area);
		optionsAvatar = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.displayer(new SimpleBitmapDisplayer())
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(180))
		.build();
		ImageLoader.getInstance().displayImage(SP_user_photo, mImage_photo, optionsAvatar);
	}
	
	@Override
	public void onResume() {
		if(arrayList.size() != 0){
			Log.d("data", "if");
			mProvinceCode = arrayList.get(0).DivisionName;
			mCityCode = arrayList.get(1).DivisionName;
			mAreaCode = arrayList.get(2).DivisionName;
			if(mProvinceCode.isEmpty()&&mCityCode.isEmpty()&&mAreaCode.isEmpty()){
				Log.d("data", "empty if");
				tvArea.setText(SP_user_province+"\t"+SP_user_city+"\t"+SP_user_area);
			}else{
				Log.d("data", "empty else");
				tvArea.setText(mProvinceCode+"\t"+mCityCode+"\t"+mAreaCode);
			}
		}else{
			Log.d("data", "else");
			tvArea.setText(SP_user_province+"\t"+SP_user_city+"\t"+SP_user_area);
			mProvinceCode = SP_user_province;
			mCityCode = SP_user_city;
			mAreaCode = SP_user_area;
		}
		super.onResume();
	}

	@Override
	protected void loadListener() {
		TextView tvBack = (TextView) findViewById(R.id.bt_back);
		tvBack.setOnClickListener(this);
		tvBack.setVisibility(View.VISIBLE);
		tvBack.setBackgroundResource(R.drawable.back_bg);

		mText_bt_title_right.setVisibility(View.VISIBLE);
		mText_bt_title_right.setOnClickListener(this);

		findViewById(R.id.bt_title_right).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title))
				.setText(getString(R.string.edit_data));

		mImage_photo.setOnClickListener(this);
		mLayout_name.setOnClickListener(this);
		mLayout_sex.setOnClickListener(this);
		mLayout_address.setOnClickListener(this);

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
		case R.id.bt_title_right:
			String name = tvNike.getText().toString();
			String address = tvArea.getText().toString();
			if(name.isEmpty()){
				Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
			}else if(address.isEmpty()){
				Toast.makeText(this, "请选择地址", Toast.LENGTH_SHORT).show();
			}else{
				String userId = Tools.getValueInSharedPreference(this, Constant.SPNAME, Constant.USERID);
				updateUserDetail(userId, name, strSex, mProvinceCode, mCityCode, mAreaCode);
			}
			break;
		case R.id.img_user_photo:
			showDialogPhoto();
			break;
		case R.id.Layout_name:
			showDialogName();
			break;
		case R.id.Layout_sex:
			showDialogSex();
//			onResume();
			break;
		case R.id.Layout_address:
			intent =new Intent(this,ShowCityActivity.class);
			startActivity(intent);
			arrayList.clear();
			break;
		default:
			break;
		}
	}

	private void showDialogPhoto() {
		Builder builder = new AlertDialog.Builder(this);

		builder.setItems(R.array.dialog_select_picture,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = null;
						switch (which) {
						case 0:
//							String timeTag = "" + System.currentTimeMillis();
//							ContentValues values = new ContentValues();
//							values.put(Media.TITLE, timeTag);
//							mCameraUri = getContentResolver()
//									.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//											values);
//							intent = new Intent(
//									"android.media.action.IMAGE_CAPTURE");
//							intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraUri);
//							startActivityForResult(intent, 10);
							mCameraUri = Tools.goCamera(EditDataActivity.this);
							break;
						case 1:
//							intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
//							intent.addCategory(Intent.CATEGORY_OPENABLE);
//							intent.setType("image/*");
//							if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//								startActivityForResult(intent,
//										GALLERY_KITKAT_INTENT_CALLED);
//							} else {
//								startActivityForResult(intent,
//										20);
//							}
							
							Tools.goImageChoice(EditDataActivity.this);
							break;

						default:
							break;
						}
					}
				});
		builder.setTitle(getString(R.string.select_picture));
		builder.show();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
        case 10:
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            if (uri == null) {
//                Toast.makeText(this, getResources().getString(R.string.select_photo), Toast.LENGTH_SHORT).show();
                return;
            }
            Tools.goZoomImage(EditDataActivity.this,uri, 300, 300,true);
            break;
        case 20:
        	Tools.goZoomImage(EditDataActivity.this,mCameraUri, 300, 300,true);
            break;
        case 2000:
            Bundle extras = data.getExtras();
            Bitmap bitmap = extras.getParcelable("data");
            Pic = Tools.BitmaptoBytes(bitmap);
            uploadPic();
            break;
        default:
            break;
        }
    }
	
	private void uploadPic(){
		new Thread() {
			public void run() {
				String result = Tools.UploadFile(Pic, Constant.USERUPDATEIMG,"1");
				Log.d("data", "result="+result);
				try {
					commData = new JSONCommon().getCommonCode(result);
					if (commData.getCode().equals("1")) {
						UserJson json = new UserJson(commData.getJosnObj());
						user = json.parse();
						handler.sendEmptyMessage(2);
					}else{
						handler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		
	}
	
	private void showDialogName(){
		final EditText inputServer = new EditText(this);
		inputServer.setBackgroundColor(Color.WHITE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_title_name)).setView(inputServer)
                .setNegativeButton(getString(R.string.Cancel), null);
        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            	strName =  inputServer.getText().toString();
            	tvNike.setText(strName);
             }
        });
        builder.show();
		
	}

	private void showDialogSex() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setItems(R.array.dialog_select_sex,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							strSex = "1";
							tvSex.setText("男");
							break;
						case 1:
							strSex = "2";
							tvSex.setText("女");
							break;
						}
					}
				});
		builder.setTitle(getString(R.string.select_sex));
		builder.show();

	}
	
	private void updateUserDetail(String UserId,String nike, String sex, String ProvinceCode, String CityCode, String AreaCode) {
		new AsyncTask<String, Void, String>() {
			@Override
			protected String doInBackground(String... params) {
				String result = HttpNetwork.httpNetwork(Data.updateUserInfo(
						params[0], params[1], params[2], params[3], params[4], params[5]),
						Constant.USER_UPDATE_INFO, EditDataActivity.this);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				Log.d("data", "修改个信息   result="+result);
				
				try {
					commData = new JSONCommon().getCommonCode(result);
					if (commData.getCode().equals("1")) {
						UserJson json = new UserJson(commData.getJosnObj());
						user = json.parse();
						handler.sendEmptyMessage(0);
					}else{
						handler.sendEmptyMessage(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.execute(UserId,nike,sex,ProvinceCode,CityCode,AreaCode);
	}
	
	Handler handler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				Log.d("data", "user.getSex()="+user.getSex());
				Tools.setValueInSharedPreference(EditDataActivity.this, Constant.SPNAME, Constant.USERID, user.getId());
				Tools.setValueInSharedPreference(EditDataActivity.this, Constant.SPNAME, Constant.NAME, user.getNikeName());
				Tools.setValueInSharedPreference(EditDataActivity.this, Constant.SPNAME, Constant.SEX, user.getSex());
				Tools.setValueInSharedPreference(EditDataActivity.this, Constant.SPNAME, Constant.PHOTO, user.getImageUrl());
				Tools.setValueInSharedPreference(EditDataActivity.this, Constant.SPNAME, Constant.PROVINCE, user.getProvince());
				Tools.setValueInSharedPreference(EditDataActivity.this, Constant.SPNAME, Constant.CITY, user.getCity());
				Tools.setValueInSharedPreference(EditDataActivity.this, Constant.SPNAME, Constant.AREA, user.getArea());
				Constant.isRefresh = true;
				Toast.makeText(EditDataActivity.this, commData.getMsg(), Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 1:
				Toast.makeText(EditDataActivity.this, commData.getMsg(), Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Tools.setValueInSharedPreference(EditDataActivity.this, Constant.SPNAME, Constant.PHOTO, user.getImageUrl());
				ImageLoader.getInstance().displayImage(user.getImageUrl(), mImage_photo, optionsAvatar); 
				Constant.isRefresh = true;
				Intent intent = new Intent("com.login.status");
				sendBroadcast(intent);
				break;
			};
		}
	};
	protected void onDestroy() {
		super.onDestroy();
		arrayList.clear();
	};
}
