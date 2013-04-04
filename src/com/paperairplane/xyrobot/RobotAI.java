package com.paperairplane.xyrobot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import android.util.Log;

public class RobotAI {
	
	static int GoodMorning_events,IhadACold = 0; //�¼���¼��
	public static String BaseNotFound = "f91e5ce9c2fef8063eb44df100c2d53c"; //�²�����ʲô ������
	
	private static int FindStr(String str0,String str1){
		return str0.indexOf(str1);
	}

	private static String getDJS(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
	
	public static String Translate(String src,String from,String to){
		String json,result = null;
		HttpResponse httpResponse;
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
	
	public static boolean OpenURI(String uriString,Context context){
		try{
			Uri uri = Uri.parse(uriString);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} catch (Exception e){
			Log.e("RobotAI","���Է��ʸõ�ַʱ���ִ���:"+uriString);
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String getAnswer(String question,Context context){
		int NowHours = Calendar.getInstance().getTime().getHours();
		int NowYears = Calendar.getInstance().getTime().getYear();
		
		/* �Զ����ʴ�� */
		String extra = Extrabase.getAnswer(question);
		if (extra != BaseNotFound){
			return extra;
		}
		
		/* ʵ�ò��� */
		if ((question.indexOf("����") != -1) & (question.indexOf("����") != -1)){
			return StartAndroidAPP("com.paperairplane.music.share",context) ? "�����ɹ�������ʹ�����ַ���Ϊ������" : "��Ǹ����û�а�װ���ַ�������ʹ�ñ��������¼http://www.paperairplane.tk";
		}
		if ((question.indexOf("��绰") != -1)|(question.indexOf("���") != -1)){
			String str1 = question;
			if (question.indexOf("��") != -1) str1 = Extrabase.ReplaceStr(str1, "��", "");
			if (question.indexOf("�绰") != -1) str1 = Extrabase.ReplaceStr(str1, "�绰", "");
			if (question.indexOf("��") != -1) str1 = Extrabase.ReplaceStr(str1, "��", "");
			if (question.indexOf("��") != -1) str1 = Extrabase.ReplaceStr(str1, "��", "");
			return PhoneTo(str1.trim(),context) ? "���ڴ�绰��"+str1 : "����ʧ�ܡ�";
		}
		if ((question.indexOf("����") != -1)|(question.indexOf("�ٶ�") != -1)){
			String str1 = question;
			if (question.indexOf("����") != -1) str1 = Extrabase.ReplaceStr(str1, "����", "");
			if (question.indexOf("�ٶ�") != -1) str1 = Extrabase.ReplaceStr(str1, "�ٶ�", "");
			return OpenURI("http://m.baidu.com/search?bd_page_type=1?word="+str1,context) ? "��������" + str1 : "������������ʱ���������⡣";
		}
		if (question.indexOf("����") != -1){
			String str1 = question;
			String from = "auto" , to = "auto";
			if (question.indexOf("����") != -1) str1 = Extrabase.ReplaceStr(str1, "����", "");
			if (question.indexOf("������") != -1) {
				from = "zh";
				to = "jp";
				str1 = Extrabase.ReplaceStr(str1, "������", "");
			}
			if (question.indexOf("������") != -1) {
				from = "zh";
				to = "jp";
				str1 = Extrabase.ReplaceStr(str1, "������", "");
			}
			if (question.indexOf("��Ӣ��") != -1) {
				to = "en";
				str1 = Extrabase.ReplaceStr(str1, "��Ӣ��", "");
			}
			if (question.indexOf("��Ӣ��") != -1) {
				to = "en";
				str1 = Extrabase.ReplaceStr(str1, "��Ӣ��", "");
			}
			if (question.indexOf("������") != -1) {
				to = "zh";
				str1 = Extrabase.ReplaceStr(str1, "������", "");
			}
			if (question.indexOf("������") != -1) {
				to = "zh";
				str1 = Extrabase.ReplaceStr(str1, "������", "");
			}
			return Translate(str1,from,to);
		}
		
		/* Ԥ�õ��ʴ�� */
		if ((FindStr(question.toLowerCase(),"hello") != -1)| (FindStr(question.toLowerCase(),"hi")) != -1){
			return Math.round(Math.random()) != 0 ? "Hey guys!" : "Hello!";
		}
		if (FindStr(question.toLowerCase(),"good morning") != -1){
			return Math.round(Math.random()) != 0 ? "Good morning!!" : "Hello!";
		}
		if ((FindStr(question.toLowerCase(),"what") != -1)| (FindStr(question.toLowerCase(),"u doing")) != -1){
			return Math.round(Math.random()) != 0 ? "I'm chatting with you." : "Chatting with a boring human.";
		}
		if ((question.indexOf("new") != -1) | (question.indexOf("����") != -1) | (question.indexOf("�汾") != -1) | (question.indexOf("˵��") != -1)){
			return "What's New:\n"+context.getString(R.string.whatsnew);
		}
		if ((FindStr(question,"�糿") != -1)|(FindStr(question,"����") != -1 & FindStr(question,"��") != -1)|(FindStr(question,"�簲") != -1)){
			if (GoodMorning_events != 0) return "��ող���˵����!?";
			if (NowHours >= 0 & NowHours < 6) return "�簡!��ô���������?";
			if (NowHours >= 6 & NowHours < 9) return "���Ϻ�!";
			if (NowHours >= 9 & NowHours < 11) { GoodMorning_events = 1; return "�ٲ����ͳٵ���!";}
			if (NowHours >= 11 & NowHours < 16){ GoodMorning_events = 2; return "������?˯����ô��.";}
			if (NowHours >= 16) return "�����Ϻð������ⶼ�����ˡ���";
		} 
		if ((FindStr(question,"��ĩ") != -1)|(FindStr(question,"������") != -1)|(FindStr(question,"������") != -1)|(FindStr(question,"������") != -1)|(FindStr(question,"����") != -1)|(FindStr(question,"����") != -1)){
			if (GoodMorning_events == 1){
				GoodMorning_events = -1;
				return "�ޣ�������ѧ��";
			}
			if (GoodMorning_events == 2){
				GoodMorning_events = -1;
				return "������˯�ɣ����������ˡ�";
			}
			return "��!���.";
		}
		if ((FindStr(question,"�ż�") != -1)|(FindStr(question,"����") != -1)|(FindStr(question,"����") != -1)|(FindStr(question,"��һ") != -1)|(FindStr(question,"����") != -1)|(FindStr(question,"����") != -1)|(FindStr(question,"���") != -1)){
			if (GoodMorning_events == 1){
				GoodMorning_events = -1;
				return "�ޣ�������ѧ��";
			}
			if (GoodMorning_events == 2){
				GoodMorning_events = -1;
				return "������һ���ɱ������ڱ�����ͷ��";
			}
			return "�ٺ�,���ȥ����ɡ�";
		}
		if ((FindStr(question,"�ձ�") != -1)|(FindStr(question,"fython") != -1))return Math.round(Math.random()) != 0 ? "���Ǹ�˧�磬������������������(�������ķ�������)" : "����ɵ�ģ�һ�����Ҳ�˵����...(�꣡�𴫳�ȥ)";
		if ((FindStr(question,"���") != -1)|(FindStr(question,"��") != -1))return Math.round(Math.random()) != 0 ? "�ˣ�" : "��ã�";
		if ((FindStr(question,"���") != -1)|(FindStr(question,"��") != -1)) return Math.round(Math.random()) != 0 ? "�ˣ�" : "��ã�";
		if ((FindStr(question,"�ڸ�") != -1)|(FindStr(question,"��ʲô") != -1))return Math.round(Math.random()) != 0 ? "���������¡�" : "�ڸ�һ������Գ����";
		if ((FindStr(question,"����") != -1)|(FindStr(question,"����") != -1)|(FindStr(question,"�ٺ�") != -1)){
			if (GoodMorning_events == 1){
				GoodMorning_events = -1;
				return "��Ц��ȥ�ѡ�";
			}
			if (GoodMorning_events == 2){
				GoodMorning_events = -1;
				return "��ô��Ц�ó���....";
			}
			return Math.round(Math.random()) != 0 ? "����������������,��Цʲô?" : "��ʲô��Ц��?";
		}
		if (FindStr(question,"����") != -1) return "�ҿ��Ǹ����̻����ˣ�ȴ���ɵ�����ֻ����������죬��Ҫ���ö����������������ԣ�ι��˵���������������";
		if (FindStr(question,"С��") != -1) return "С�ƻ����ˣ���ʮһ�����������ĵĸ����̻����ˣ�";
		if (FindStr(question,"��") != -1) return "�ţ��������˯��";
		if (FindStr(question,"���˯") != -1) return "û���⣬ǰ������Ҫ�Ȱ��Ҹ����ˡ�";
		if (FindStr(question,"�ݰ�") != -1|FindStr(question,"88") != -1|FindStr(question,"bye") != -1|FindStr(question,"see you") != -1|FindStr(question,"�ټ�") != -1|FindStr(question,"�����") != -1) return "See you.�ټ���";
		if ((FindStr(question,"��") != -1 | FindStr(question,"��") != -1)&FindStr(question,"��ð") != -1){
			IhadACold = 1;
			return "û�°ɣ�Ҫ��Ҫȥ��ҽ��";
		}
		if (IhadACold == 1){
			if (FindStr(question,"û��") != -1) return "û�¾ͺã�����Ϣ���ˮ��";
			if (FindStr(question,"��") != -1) return "�����Լ�ע����£�����Ҫ��ҽ������";
			if (FindStr(question,"Ҫ") != -1|FindStr(question,"�õ�")!= -1) return "�Ҹ��ֵܻ������Ѵ���ȥ��";
			if (FindStr(question,"��") != -1 & (FindStr(question,"��") != -1|FindStr(question,"��") != -1)) return "�ţ��ǵð�ʱ��ҩ�����ˮ��";
			if (FindStr(question,"��") != -1) return "�����ظ�ð��Ҫ��ҽ������";
			if (FindStr(question,"��Ϣ") != -1) return "����Ϣ�ɡ�";
		}
		if (FindStr(question,"����") != -1 & FindStr(question,"ʲô") != -1 & FindStr(question,"��") != -1){
			int year = (int) NowYears % 12;
			String yearname = "������ʲô����֪����һ���ǳ�������ˣ������ⲻ�������޷����������ʵ����һ�������ֻ������˲Ŷԡ�";
			switch (year) {
			case 0:yearname = "��"; break;
			case 1:yearname = "ţ"; break;
			case 2:yearname = "��"; break;
			case 3:yearname = "��"; break;
			case 4:yearname = "��"; break;
			case 5:yearname = "��"; break;
			case 6:yearname = "��"; break;
			case 7:yearname = "��"; break;
			case 8:yearname = "��"; break;
			case 9:yearname = "��"; break;
			case 10:yearname = "��"; break;
			case 11:yearname = "��"; break;
			}
			return "������"+yearname+"��";
		}
		if (FindStr(question,"С��") != -1 & (FindStr(question,"����") != -1 | FindStr(question,"ɵb") != -1 | FindStr(question,"2b") != -1 | FindStr(question,"ɵ��") != -1 | FindStr(question,"SB") != -1)) return "�Ų����أ����ɵ�ƣ���Ŷ�����";
		if (FindStr(question,"�Ǻ�") != -1) return "�Ǻ�..";
		if (FindStr(question.toLowerCase(),"time") != -1){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			return "It's "+formatter.format(date);
		}
		if (FindStr(question.toLowerCase(),"date") != -1){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			return "It's "+formatter.format(date);
		}
		if (FindStr(question,"����") != -1){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
			String temp = formatter.format(date);
			if ((FindStr(temp,"��") != -1)|(FindStr(temp,"��") != -1)|(FindStr(temp,"��") != -1)){
				return "����"+temp+"!̫���˽�������ĩ��";
			}
			return "����"+temp+"!";
		}
		if ((FindStr(question,"����") != -1)|(FindStr(question,"ʱ��") != -1)){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			return "���ڵ�ʱ���� "+formatter.format(date);
		}
		if ((FindStr(question,"����") != -1)|(FindStr(question,"����") != -1)|(FindStr(question,"�������") != -1)){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy��MM��dd��");
			String temp = new SimpleDateFormat("EEEE").format(date);
			if ((FindStr(temp,"��") != -1)|(FindStr(temp,"��") != -1)|(FindStr(temp,"��") != -1)){
				return "������ "+formatter.format(date)+"!��������ĩҮ��";
			}
			return "������ "+formatter.format(date);
		}
		if (FindStr(question,"�п�") != -1){
			try {
				return "���п����� "+getDJS("2013-6-19")+" ��!����!";
			} catch (ParseException e) {
				return "";
			}
		}

		if (FindStr(question,"�߿�") != -1){
			try {
				return "��߿����� "+getDJS("2013-6-7")+" ��!����!";
			} catch (ParseException e) {
				return "";
			}
		}
		if ((FindStr(question,"��ȥ�����˸���") != -1)|
			(FindStr(question,"��") != -1)|
			(FindStr(question,"fuck") != -1)|
			(FindStr(question,"cao") != -1)|
			(FindStr(question,"��") != -1)|
			(FindStr(question,"��") != -1)|
			(FindStr(question,"��") != -1)|
			(FindStr(question,"��") != -1)|
			(FindStr(question,"��ĸ") != -1)|
			(FindStr(question,"��ʺ") != -1)|
			(FindStr(question,"�϶�") != -1)){
			Main.i++;
			if (Main.i == 2){
				return "������������?";
			}
			if (Main.i == 3){
				int a = 1/0;
				return Integer.toString(a);
			}
			return Math.round(Math.random()) != 0 ? "Fuck you��" : "��ȥ�����˸���";
		}
		
		if (FindStr(question,"��") != -1){
			return "��ʲô������";
		}
		return Main.AI_UnknowMsg;
	}

}
