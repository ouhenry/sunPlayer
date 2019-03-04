package com.exmart.qiyishow.tools;

import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.exmart.qiyishow.tools.CustomMultipartEntity.ProgressListener;

import android.content.Context;
import android.os.AsyncTask;

public class HttpNetwork {
	
	/**
	 * HTTP联网
	 * list 请求数据封装集合
	 * Url 请求URL
	 * context 当前类
	 */
	public static String httpNetwork(List<NameValuePair> list,String Url,Context context){
		String request = "";
		HttpPost post = new HttpPost(Url);
		try {
			post.setEntity(new UrlEncodedFormEntity(list,HTTP.UTF_8));
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 60 * 1000); // 设置超时时间
			HttpConnectionParams.setSoTimeout(params, 60 * 1000);
			HttpClient client = new DefaultHttpClient(params);
			HttpResponse httpResponse = client.execute(post);
			request  = EntityUtils.toString(httpResponse.getEntity());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}
}
