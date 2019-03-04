package com.exmart.qiyishow.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;

import com.exmart.qiyishow.R;
import com.exmart.qiyishow.analysisJson.BaseJson;
import com.exmart.qiyishow.analysisJson.VersionCodeJson;
import com.exmart.qiyishow.bean.BaseBean;
import com.exmart.qiyishow.bean.VersionBean;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
/**
 * APK更新管理类
 * 
 * @author Royal
 * 
 */
@SuppressLint("HandlerLeak")
public class UpdateManager {

	// 上下文对象
	private Activity mContext;
	private String mResult;
	private BaseBean mBaseBean;
	private VersionBean mVersionCodeBean;

	// 下载进度条
	private ProgressBar progressBar;
	// 是否终止下载
	private boolean isInterceptDownload = false;
	//进度条显示数值
	private int progress = 0;
	private int versionCode;
	private boolean ToastFlag; //是否弹出提示
	
	private AlertDialog dialog;
	private String APK_NAME = "Funnyvideo.apk";
	private String APK_FILE = "FunnyvideoUpdateFile";
	
	private SharedPreferences spf_new;  
	public SharedPreferences.Editor editor;

	/**
	 * 参数为Context(上下文activity)的构造函数
	 * 
	 * @param context
	 */
	public UpdateManager(Activity context,boolean ToastFlag) {
		this.mContext = context;
		this.ToastFlag = ToastFlag;
		spf_new = context.getSharedPreferences("up_new", context.MODE_PRIVATE);
		editor = spf_new.edit();
	}

	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	public void checkUpdate() {
		// 从服务端获取版本信息
		 getVersionInfoFromServer();
	}

	/**
	 * 从服务端获取版本信息
	 * 
	 * @return
	 */
	private VersionBean getVersionInfoFromServer() {
		try {
			// 获取当前软件包信息
			PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			// 当前软件版本号
			versionCode = pi.versionCode;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		
		new Thread(){
			public void run() {
				try {
					mResult = HttpNetwork.httpNetwork(Data.SysAppInfo(mContext), Constant.SYSAPPINFO, mContext);
					Log.d("data", "VersionCode="+mResult);
					BaseJson mBaseJson = new BaseJson(mResult);
					mBaseBean = mBaseJson.parse();
					if(mBaseBean.Code.equals("1")){
						VersionCodeJson mVersionCodeJson =new VersionCodeJson(mBaseBean.Data);
						mVersionCodeBean = mVersionCodeJson.parse();
						handler.sendMessage(handler.obtainMessage(2));
					}else{
//						handler.sendMessage(handler.obtainMessage(Constant.CODE_ONE));
					}
					} catch (Exception e) {
						handler.sendMessage(handler.obtainMessage(3));
						e.printStackTrace();
					}
			};
		}.start();
		
		return mVersionCodeBean;
	}

	/**
	 * 提示更新对话框
	 * 
	 * @param info
	 *            版本信息对象
	 */
	private void showUpdateDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("新版本更新内容");
		builder.setMessage(mVersionCodeBean.getVersion()); 
		builder.setPositiveButton(mContext.getString(R.string.Upgrade), new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 弹出下载框
				showDownloadDialog();
			}
		});
		builder.setNegativeButton(mContext.getString(R.string.Later), new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 弹出下载框
	 */
	private void showDownloadDialog() {
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update_progress, null);
		progressBar = (ProgressBar) v.findViewById(R.id.pb_update_progress);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
		dialog = alertDialog.setTitle(R.string.Upgrading).setView(v)
		.setNegativeButton(mContext.getString(R.string.no), new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//终止下载
				isInterceptDownload = true;
			}
		})
		.show();
		//下载apk
		downloadApk();
	}
	
	/**
	 * 下载apk
	 */
	private void downloadApk(){
		//开启另一线程下载
		Thread downLoadThread = new Thread(downApkRunnable);
		downLoadThread.start();
	}
	
	/**
	 * 从服务器下载新版apk的线程
	 */
	private Runnable downApkRunnable = new Runnable(){
		
		public void run() {
			if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				//如果没有SD卡
				Builder builder = new Builder(mContext);
				builder.setTitle(mContext.getString(R.string.Prompt));
				builder.setMessage(mContext.getString(R.string.SD_card));
				builder.setPositiveButton(mContext.getString(R.string.Yes), new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.show();
				return;
			}else{
				try {
					//服务器上新版apk地址
					URL url = new URL(mVersionCodeBean.getAppUrl());
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+APK_FILE+"/");
					if(!file.exists()){
						//如果文件夹不存在,则创建
						file.mkdir();
					}
					//下载服务器中新版本软件（写文件）
					String apkFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+APK_FILE+"/" +APK_NAME;
					File ApkFile = new File(apkFile);
					@SuppressWarnings("resource")
					FileOutputStream fos = new FileOutputStream(ApkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do{
						int numRead = is.read(buf);
						count += numRead;
						//更新进度条
						progress = (int) (((float) count / length) * 100);
						handler.sendEmptyMessage(1);
						if(numRead <= 0){
							//下载完成通知安装
							handler.sendEmptyMessage(0);
							break;
						}
						fos.write(buf,0,numRead);
						//当点击取消时，则停止下载
					}while(!isInterceptDownload);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	/**
	 * 声明一个handler来跟进进度条
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 更新进度情况
				progressBar.setProgress(progress);
				break;
			case 0:
				progressBar.setVisibility(View.INVISIBLE);
				dialog.dismiss();
				// 安装apk文件
				installApk();
				break;
			case 2:
				if (mVersionCodeBean != null) {
//						Log.d("data", "本地版本="+versionCode+"服务器版本号="+mVersionCodeBean.versioncode);
						if (versionCode < Integer.parseInt(mVersionCodeBean.getVersion())) {
							// 如果当前版本号小于服务端版本号,则弹出提示更新对话框
							showUpdateDialog();
							editor.putBoolean("flag_new", true);
							editor.commit();
						}else{
							editor.putBoolean("flag_new", false);
							editor.commit();
							if(ToastFlag){
								Toast.makeText(mContext, mBaseBean.Msg, Toast.LENGTH_SHORT).show();
							}
							
						}
				}else{
				}
				break;
			case 3:
				if(ToastFlag){
					Toast.makeText(mContext, mContext.getString(R.string.No_update), Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		};
	};
	
	/**
	 * 安装apk
	 */
	private void installApk() {
		// 获取当前sdcard存储路径
		File apkfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+APK_FILE+"/" + APK_NAME);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		// 安装，如果签名不一致，可能出现程序未安装提示
		i.setDataAndType(Uri.fromFile(new File(apkfile.getAbsolutePath())), "application/vnd.android.package-archive"); 
		mContext.startActivity(i);
	}
}
