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
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
			Log.e("RobotAI","���Է��ʸõ�ַʱ���ִ���:"+uriString);
			e.printStackTrace();
			return false;
		}
	}

	public static String Translate(String src,String from,String to){
		String json,result = null;
		HttpResponse httpResponse;
		if(src.charAt(0) == ' ') {
			src = src.substring(1);
		}
		try {
			HttpGet httpGet = new HttpGet("http://openapi.baidu.com/public/2.0/bmt/translate?client_id=ffidX2b30phZThDxFHsOj1W9&q="+src+"&from="+from+"&to="+to);
			Log.v("Translate","������ѯ�ĵ�ַ:"+"http://openapi.baidu.com/public/2.0/bmt/translate?client_id=ffidX2b30phZThDxFHsOj1W9&q="+src+"&from="+from+"&to="+to);
			httpResponse = new DefaultHttpClient().execute(httpGet);
			Log.v("Translate", "���е�HTTP GET����״̬Ϊ"+ httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				json = EntityUtils.toString(httpResponse.getEntity());
				Log.v("Translate", "���ؽ��Ϊ" + json);
			} else {
				json = null;
				return "����ʧ�ܣ���ȡ�����������";
			}
		} catch (Exception e) {
			Log.v("Translate", "�׳�����" + e.getMessage());
			e.printStackTrace();
			json = null;
			return "����ʧ�ܣ�����������������";
		}
		try {
			JSONTokener jsonParser = new JSONTokener(json); 
			JSONObject person = (JSONObject) jsonParser.nextValue();
			JSONArray array = person.getJSONArray("trans_result");
			int i = 0;
			String json2 = null;
			result = "������:\n";
			for (i=0;i<array.length();i++){
				json2 = array.get(i).toString();
				JSONTokener jsonParser2 = new JSONTokener(json2); 
				JSONObject person2 = (JSONObject) jsonParser2.nextValue();
				result = result + "ԭ��:" + person2.getString("src") + "\n����:" + person2.getString("dst") + "\n";
			}
			return result+"\n�ɰٶ��ṩ�������\n(http://fanyi.baidu.com)";
		} catch (JSONException e) {
			e.printStackTrace();
			return "����ʧ�ܣ�����˷����˴������Ϣ";
		}
	}
	
	public static void update(Context context,Handler handler){
		String json = null;
		int newversion = 0;
		HttpResponse httpResponse;
		try {
			HttpGet httpGet = new HttpGet(C.update_ver_url);
			httpResponse = new DefaultHttpClient().execute(httpGet);
			Log.v("Translate", "���е�HTTP GET����״̬Ϊ"+ httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				json = EntityUtils.toString(httpResponse.getEntity());
				Log.v("Translate", "���ؽ��Ϊ" + json);
				} else {
					json = null;
					Log.e("update","Server Error"+httpResponse.getStatusLine().getStatusCode());
				}
		} catch (Exception e){
			e.printStackTrace();
			Toast.makeText(context, "���޸���", Toast.LENGTH_SHORT).show();
			return;
		}
		
		try {
			JSONTokener jsonParser = new JSONTokener(json); 
			JSONObject person = (JSONObject) jsonParser.nextValue();
			newversion = person.getInt("version");
			Log.i("update",person.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(context, "��ȡ����", Toast.LENGTH_SHORT).show();
			return;
		}
		
		try {
			int version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
			if (version < newversion) {
				try {
					HttpGet httpGet = new HttpGet(C.update_info_url);
					httpResponse = new DefaultHttpClient().execute(httpGet);
					Log.v("Translate", "���е�HTTP GET����״̬Ϊ"+ httpResponse.getStatusLine().getStatusCode());
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						json = EntityUtils.toString(httpResponse.getEntity());
						Log.v("Translate", "���ؽ��Ϊ" + json);
						} else {
							json = null;
							Log.e("update","Server Error"+httpResponse.getStatusLine().getStatusCode());
						}
				} catch (Exception e){
					e.printStackTrace();
					return;
				}
				try {
					JSONTokener jsonParser = new JSONTokener(json); 
					JSONObject person = (JSONObject) jsonParser.nextValue();
					Main.update_text = person.getString("text");
					Main.update_url = person.getString("url");
					Log.i("update",person.toString());
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}
			} else {
				Toast.makeText(context, "�Ѹ��µ����°汾", Toast.LENGTH_SHORT).show();
				return;
			}
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
