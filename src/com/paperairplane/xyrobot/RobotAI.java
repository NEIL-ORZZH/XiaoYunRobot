package com.paperairplane.xyrobot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RobotAI {

	private static int FindStr(String str0,String str1){
		return str0.indexOf(str1);
	}
	
	// 中考倒计时
	public static String getZKDJS() throws ParseException{
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 Date b= sdf.parse("2013-6-19");
		 Date a= Calendar.getInstance().getTime();
		 long time  = (b.getTime()-a.getTime())/1000/60/60/24;
		 return Long.toString(time);
	}
	// 高考倒计时
	public static String getGKDJS() throws ParseException{
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 Date b= sdf.parse("2013-6-7");
		 Date a= Calendar.getInstance().getTime();
		 long time  = (b.getTime()-a.getTime())/1000/60/60/24;
		 return Long.toString(time);
	}
	
	public static String getAnswer(String question){
		if (FindStr(question.toLowerCase(),"hello") != -1){
			return Math.round(Math.random()) != 0 ? "Hey guys!" : "Hello!";
		}
		if ((FindStr(question,"你好") != -1)|(FindStr(question,"嗨") != -1)){
			return Math.round(Math.random()) != 0 ? "嗨！" : "你好！";
		}
		if (FindStr(question.toLowerCase(),"time") != -1){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "It's "+formatter.format(date);
		}
		if ((FindStr(question,"几点") != -1)|(FindStr(question,"时间") != -1)){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "现在的时间是 "+formatter.format(date);
		}
		if ((FindStr(question,"几号") != -1)|(FindStr(question,"日期") != -1)|(FindStr(question,"今天多少") != -1)){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "现在的时间是 "+formatter.format(date);
		}
		if ((FindStr(question,"中考") != -1)){
			try {
				return "离中考还有 "+getZKDJS()+" 天!加油!";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return "啊啊啊啊啊啊！程序错误！！";
			}
		}

		if ((FindStr(question,"高考") != -1)){
			try {
				return "离高考还有 "+getZKDJS()+" 天!加油!";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return "啊啊啊啊啊啊！程序错误！！";
			}
		}
		return Locale.getDefault() != Locale.CHINA ? "Sorry, I don't know what you mean." : "对不起,我不理解你的意思.";
	}

}
