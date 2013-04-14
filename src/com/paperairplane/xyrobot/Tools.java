package com.paperairplane.xyrobot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Tools {
	public static String getDJS(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Date thatday = sdf.parse(date);
		Date nowtime = Calendar.getInstance().getTime();
		long time  = (thatday.getTime()-nowtime.getTime())/1000/60/60/24;
		return Long.toString(time);
	}
	
	public static boolean StartAndroidAPP(String string,Context context){
		try {
			Intent intent = new Intent();
			intent = context.getPackageManager().getLaunchIntentForPackage(string);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
        	return true;
        } catch (Exception e) {
        	return false;
        }
	}
	
	public static boolean PhoneTo(String str,Context context){
		try{
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+str));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} catch(Exception e){
			Log.v("RobotAI","NowNumber:"+str);
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean OpenURI(String uriString,Context context){
		try{
			Uri uri = Uri.parse("http://"+uriString);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} catch (Exception e){
			Log.e("RobotAI","尝试访问该地址时出现错误:"+uriString);
			e.printStackTrace();
			return false;
		}
	}

	public static String Translate(String src,String from,String to){
		String json,result = null;
		HttpResponse httpResponse;
		if(src.length() > 0 && src.charAt(0) == ' ') {
			src = src.substring(1);
		}
		try {
			HttpGet httpGet = new HttpGet("http://openapi.baidu.com/public/2.0/bmt/translate?client_id="+C.baidu_app_key+"&q="+src+"&from="+from+"&to="+to);
			Log.v("Translate","即将查询的地址:"+"http://openapi.baidu.com/public/2.0/bmt/translate?client_id="+C.baidu_app_key+"&q="+src+"&from="+from+"&to="+to);
			httpResponse = new DefaultHttpClient().execute(httpGet);
			Log.v("Translate", "进行的HTTP GET返回状态为"+ httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				json = EntityUtils.toString(httpResponse.getEntity());
				Log.v("Translate", "返回结果为" + json);
			} else {
				json = null;
				return "翻译失败，读取网络参数错误";
			}
		} catch (Exception e) {
			Log.v("Translate", "抛出错误" + e.getMessage());
			e.printStackTrace();
			json = null;
			return "翻译失败，请检查您的网络设置";
		}
		try {
			JSONTokener jsonParser = new JSONTokener(json); 
			JSONObject person = (JSONObject) jsonParser.nextValue();
			JSONArray array = person.getJSONArray("trans_result");
			int i = 0;
			String json2 = null;
			result = "翻译结果:\n";
			for (i=0;i<array.length();i++){
				json2 = array.get(i).toString();
				JSONTokener jsonParser2 = new JSONTokener(json2); 
				JSONObject person2 = (JSONObject) jsonParser2.nextValue();
				result = result + "原文:" + person2.getString("src") + "\n译文:" + person2.getString("dst");
			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
			return "翻译失败，服务端返回了错误的信息";
		}
	}
	
	public static void update(Handler handler, int versionCode, Context context) {
		String json = null;
		HttpResponse httpResponse;
		try {
			HttpGet httpGet = new HttpGet(C.update_ver_url);
			httpResponse = new DefaultHttpClient().execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				json = EntityUtils.toString(httpResponse.getEntity());
			} else {
				json = null;
				handler.sendEmptyMessage(3);
				return;
			}
		} catch (Exception e) {
			handler.sendEmptyMessage(C.handlermsg.internet_error);
			e.printStackTrace();
			json = null;
			return;
		}
		try {
			JSONObject rootObject = new JSONObject(json);
			int remoteVersion = rootObject.getInt("versionCode");
			if (remoteVersion <= versionCode) {
				handler.sendEmptyMessage(C.handlermsg.version_least);
			} else if (remoteVersion > versionCode) {
				StringBuffer sb = new StringBuffer(
						context.getString(R.string.update_remote_version));
				sb.append(rootObject
						.getString("versionName") + "\n");
				sb.append(context.getString(R.string.update_whats_new));
				sb.append(rootObject
						.getString("whatsNew")+"\n");
				sb.append(context.getString(R.string.update_release_date));
				sb.append(rootObject
						.getString("releaseDate"));
				String[] info = new String[2];
				info[C.ArraySubscript.UPDATE_INFO] = sb.toString();
				info[C.ArraySubscript.DOWNLOAD_URL] = rootObject
						.getString("downloadUrl");
				Message m = handler.obtainMessage(C.handlermsg.version_new,info);
				handler.sendMessage(m);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			handler.sendEmptyMessage(C.handlermsg.internet_error);
		}
	}
	
}
