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
	
	static int GoodMorning_events,IhadACold = 0; //事件记录器
	public static String BaseNotFound = "f91e5ce9c2fef8063eb44df100c2d53c"; //猜猜这是什么 哈哈哈
	
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
			Log.v("Translate","即将查询的地址:"+"http://openapi.baidu.com/public/2.0/bmt/translate?client_id=ffidX2b30phZThDxFHsOj1W9&q="+src+"&from="+from+"&to="+to);
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
				result = result + "原文:" + person2.getString("src") + "\n译文:" + person2.getString("dst") + "\n";
			}
			return result+"\n由百度提供翻译服务\n(http://fanyi.baidu.com)";
		} catch (JSONException e) {
			e.printStackTrace();
			return "翻译失败，服务端返回了错误的信息";
		}
	}
	
	public static boolean OpenURI(String uriString,Context context){
		try{
			Uri uri = Uri.parse(uriString);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			return true;
		} catch (Exception e){
			Log.e("RobotAI","尝试访问该地址时出现错误:"+uriString);
			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String getAnswer(String question,Context context){
		int NowHours = Calendar.getInstance().getTime().getHours();
		int NowYears = Calendar.getInstance().getTime().getYear();
		
		/* 自定义问答库 */
		String extra = Extrabase.getAnswer(question);
		if (extra != BaseNotFound){
			return extra;
		}
		
		/* 实用部分 */
		if ((question.indexOf("音乐") != -1) & (question.indexOf("分享") != -1)){
			return StartAndroidAPP("com.paperairplane.music.share",context) ? "启动成功！现在使用音乐分享为您服务。" : "抱歉，您没有安装音乐分享不可以使用本服务。请登录http://www.paperairplane.tk";
		}
		if ((question.indexOf("打电话") != -1)|(question.indexOf("打给") != -1)){
			String str1 = question;
			if (question.indexOf("打") != -1) str1 = Extrabase.ReplaceStr(str1, "打", "");
			if (question.indexOf("电话") != -1) str1 = Extrabase.ReplaceStr(str1, "电话", "");
			if (question.indexOf("给") != -1) str1 = Extrabase.ReplaceStr(str1, "给", "");
			if (question.indexOf("到") != -1) str1 = Extrabase.ReplaceStr(str1, "到", "");
			return PhoneTo(str1.trim(),context) ? "现在打电话给"+str1 : "拨打失败。";
		}
		if ((question.indexOf("搜索") != -1)|(question.indexOf("百度") != -1)){
			String str1 = question;
			if (question.indexOf("搜索") != -1) str1 = Extrabase.ReplaceStr(str1, "搜索", "");
			if (question.indexOf("百度") != -1) str1 = Extrabase.ReplaceStr(str1, "百度", "");
			return OpenURI("http://m.baidu.com/search?bd_page_type=1?word="+str1,context) ? "现在搜索" + str1 : "额……启动浏览器时出现了问题。";
		}
		if (question.indexOf("翻译") != -1){
			String str1 = question;
			String from = "auto" , to = "auto";
			if (question.indexOf("翻译") != -1) str1 = Extrabase.ReplaceStr(str1, "翻译", "");
			if (question.indexOf("到日文") != -1) {
				from = "zh";
				to = "jp";
				str1 = Extrabase.ReplaceStr(str1, "到日文", "");
			}
			if (question.indexOf("成日文") != -1) {
				from = "zh";
				to = "jp";
				str1 = Extrabase.ReplaceStr(str1, "成日文", "");
			}
			if (question.indexOf("到英文") != -1) {
				to = "en";
				str1 = Extrabase.ReplaceStr(str1, "到英文", "");
			}
			if (question.indexOf("成英文") != -1) {
				to = "en";
				str1 = Extrabase.ReplaceStr(str1, "成英文", "");
			}
			if (question.indexOf("到中文") != -1) {
				to = "zh";
				str1 = Extrabase.ReplaceStr(str1, "到中文", "");
			}
			if (question.indexOf("成中文") != -1) {
				to = "zh";
				str1 = Extrabase.ReplaceStr(str1, "成中文", "");
			}
			return Translate(str1,from,to);
		}
		
		/* 预置的问答库 */
		if ((FindStr(question.toLowerCase(),"hello") != -1)| (FindStr(question.toLowerCase(),"hi")) != -1){
			return Math.round(Math.random()) != 0 ? "Hey guys!" : "Hello!";
		}
		if (FindStr(question.toLowerCase(),"good morning") != -1){
			return Math.round(Math.random()) != 0 ? "Good morning!!" : "Hello!";
		}
		if ((FindStr(question.toLowerCase(),"what") != -1)| (FindStr(question.toLowerCase(),"u doing")) != -1){
			return Math.round(Math.random()) != 0 ? "I'm chatting with you." : "Chatting with a boring human.";
		}
		if ((question.indexOf("new") != -1) | (question.indexOf("更新") != -1) | (question.indexOf("版本") != -1) | (question.indexOf("说明") != -1)){
			return "What's New:\n"+context.getString(R.string.whatsnew);
		}
		if ((FindStr(question,"早晨") != -1)|(FindStr(question,"早上") != -1 & FindStr(question,"好") != -1)|(FindStr(question,"早安") != -1)){
			if (GoodMorning_events != 0) return "你刚刚不是说了吗!?";
			if (NowHours >= 0 & NowHours < 6) return "早啊!这么早就醒来了?";
			if (NowHours >= 6 & NowHours < 9) return "早上好!";
			if (NowHours >= 9 & NowHours < 11) { GoodMorning_events = 1; return "再不快点就迟到了!";}
			if (NowHours >= 11 & NowHours < 16){ GoodMorning_events = 2; return "你是猪啊?睡到这么晚.";}
			if (NowHours >= 16) return "还早上好啊！？这都下午了……";
		} 
		if ((FindStr(question,"周末") != -1)|(FindStr(question,"星期六") != -1)|(FindStr(question,"星期天") != -1)|(FindStr(question,"星期日") != -1)|(FindStr(question,"周日") != -1)|(FindStr(question,"周六") != -1)){
			if (GoodMorning_events == 1){
				GoodMorning_events = -1;
				return "噢，不用上学。";
			}
			if (GoodMorning_events == 2){
				GoodMorning_events = -1;
				return "那慢慢睡吧，不打扰你了。";
			}
			return "嗯!真好.";
		}
		if ((FindStr(question,"放假") != -1)|(FindStr(question,"节日") != -1)|(FindStr(question,"国庆") != -1)|(FindStr(question,"五一") != -1)|(FindStr(question,"过年") != -1)|(FindStr(question,"寒假") != -1)|(FindStr(question,"暑假") != -1)){
			if (GoodMorning_events == 1){
				GoodMorning_events = -1;
				return "噢，不用上学。";
			}
			if (GoodMorning_events == 2){
				GoodMorning_events = -1;
				return "起来动一动吧别老窝在被窝里头。";
			}
			return "嘿嘿,多出去玩玩吧。";
		}
		if ((FindStr(question,"烧饼") != -1)|(FindStr(question,"fython") != -1))return Math.round(Math.random()) != 0 ? "他是个帅哥，哈哈哈哈哈哈哈哈。(纯属无聊非自恋啊)" : "他很傻的，一般人我不说出来...(嘘！别传出去)";
		if ((FindStr(question,"你好") != -1)|(FindStr(question,"嗨") != -1))return Math.round(Math.random()) != 0 ? "嗨！" : "你好！";
		if ((FindStr(question,"你好") != -1)|(FindStr(question,"嗨") != -1)) return Math.round(Math.random()) != 0 ? "嗨！" : "你好！";
		if ((FindStr(question,"在干") != -1)|(FindStr(question,"做什么") != -1))return Math.round(Math.random()) != 0 ? "陪你聊天呗。" : "在跟一个类人猿聊天";
		if ((FindStr(question,"哈哈") != -1)|(FindStr(question,"嘻嘻") != -1)|(FindStr(question,"嘿嘿") != -1)){
			if (GoodMorning_events == 1){
				GoodMorning_events = -1;
				return "还笑快去把。";
			}
			if (GoodMorning_events == 2){
				GoodMorning_events = -1;
				return "这么晚还笑得出来....";
			}
			return Math.round(Math.random()) != 0 ? "哈哈哈哈哈哈哈哈,在笑什么?" : "有什么好笑的?";
		}
		if (FindStr(question,"你是") != -1) return "我可是高智商机器人，却被派到你的手机里陪你聊天，还要讲得都是我听不懂的语言，喂，说点机器语言听听。";
		if (FindStr(question,"小云") != -1) return "小云机器人，二十一世纪上最无聊的高智商机器人！";
		if (FindStr(question,"晚安") != -1) return "嗯，晚安！早点睡。";
		if (FindStr(question,"早点睡") != -1) return "没问题，前提是你要先把我给放了。";
		if (FindStr(question,"拜拜") != -1|FindStr(question,"88") != -1|FindStr(question,"bye") != -1|FindStr(question,"see you") != -1|FindStr(question,"再见") != -1|FindStr(question,"明天见") != -1) return "See you.再见！";
		if ((FindStr(question,"我") != -1 | FindStr(question,"了") != -1)&FindStr(question,"感冒") != -1){
			IhadACold = 1;
			return "没事吧？要不要去看医生";
		}
		if (IhadACold == 1){
			if (FindStr(question,"没事") != -1) return "没事就好，多休息多喝水。";
			if (FindStr(question,"不") != -1) return "那你自己注意点呗，生病要看医生啊。";
			if (FindStr(question,"要") != -1|FindStr(question,"好的")!= -1) return "找个兄弟或是朋友带你去吧";
			if (FindStr(question,"看") != -1 & (FindStr(question,"过") != -1|FindStr(question,"了") != -1)) return "嗯，记得按时吃药，多喝水。";
			if (FindStr(question,"重") != -1) return "得了重感冒更要看医生啊。";
			if (FindStr(question,"休息") != -1) return "你休息吧。";
		}
		if (FindStr(question,"今年") != -1 & FindStr(question,"什么") != -1 & FindStr(question,"是") != -1){
			int year = (int) NowYears % 12;
			String yearname = "啊！我什么都不知道，一定是程序出错了，啊不这不可能我无法接受这个现实！！一定是你手机出错了才对。";
			switch (year) {
			case 0:yearname = "鼠"; break;
			case 1:yearname = "牛"; break;
			case 2:yearname = "虎"; break;
			case 3:yearname = "兔"; break;
			case 4:yearname = "龙"; break;
			case 5:yearname = "蛇"; break;
			case 6:yearname = "马"; break;
			case 7:yearname = "羊"; break;
			case 8:yearname = "猴"; break;
			case 9:yearname = "鸡"; break;
			case 10:yearname = "狗"; break;
			case 11:yearname = "猪"; break;
			}
			return "今年是"+yearname+"年";
		}
		if (FindStr(question,"小云") != -1 & (FindStr(question,"二货") != -1 | FindStr(question,"傻b") != -1 | FindStr(question,"2b") != -1 | FindStr(question,"傻逼") != -1 | FindStr(question,"SB") != -1)) return "才不是呢！你才傻逼，你才二货。";
		if (FindStr(question,"呵呵") != -1) return "呵呵..";
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
		if (FindStr(question,"星期") != -1){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
			String temp = formatter.format(date);
			if ((FindStr(temp,"六") != -1)|(FindStr(temp,"日") != -1)|(FindStr(temp,"天") != -1)){
				return "今天"+temp+"!太好了今天是周末。";
			}
			return "今天"+temp+"!";
		}
		if ((FindStr(question,"几点") != -1)|(FindStr(question,"时间") != -1)){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			return "现在的时间是 "+formatter.format(date);
		}
		if ((FindStr(question,"几号") != -1)|(FindStr(question,"日期") != -1)|(FindStr(question,"今天多少") != -1)){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
			String temp = new SimpleDateFormat("EEEE").format(date);
			if ((FindStr(temp,"六") != -1)|(FindStr(temp,"日") != -1)|(FindStr(temp,"天") != -1)){
				return "今天是 "+formatter.format(date)+"!今天是周末耶。";
			}
			return "今天是 "+formatter.format(date);
		}
		if (FindStr(question,"中考") != -1){
			try {
				return "离中考还有 "+getDJS("2013-6-19")+" 天!加油!";
			} catch (ParseException e) {
				return "";
			}
		}

		if (FindStr(question,"高考") != -1){
			try {
				return "离高考还有 "+getDJS("2013-6-7")+" 天!加油!";
			} catch (ParseException e) {
				return "";
			}
		}
		if ((FindStr(question,"我去年买了个表") != -1)|
			(FindStr(question,"逼") != -1)|
			(FindStr(question,"fuck") != -1)|
			(FindStr(question,"cao") != -1)|
			(FindStr(question,"草") != -1)|
			(FindStr(question,"冚") != -1)|
			(FindStr(question,"叼") != -1)|
			(FindStr(question,"屌") != -1)|
			(FindStr(question,"老母") != -1)|
			(FindStr(question,"吃屎") != -1)|
			(FindStr(question,"老豆") != -1)){
			Main.i++;
			if (Main.i == 2){
				return "你再骂我试试?";
			}
			if (Main.i == 3){
				int a = 1/0;
				return Integer.toString(a);
			}
			return Math.round(Math.random()) != 0 ? "Fuck you！" : "我去年买了个表";
		}
		
		if (FindStr(question,"啊") != -1){
			return "啊什么啊……";
		}
		return Main.AI_UnknowMsg;
	}

}
