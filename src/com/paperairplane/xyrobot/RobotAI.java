package com.paperairplane.xyrobot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RobotAI {

	static int GoodMorning_events = 0; //早上问候事件记录器
	public static String BaseNotFound = "f91e5ce9c2fef8063eb44df100c2d53c"; //猜猜这是什么 哈哈哈
	private static int FindStr(String str0,String str1){
		return str0.indexOf(str1);
	}
	
	// 倒计时
	private static String getDJS(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date thatday = sdf.parse(date);
		Date nowtime = Calendar.getInstance().getTime();
		long time  = (thatday.getTime()-nowtime.getTime())/1000/60/60/24;
		return Long.toString(time);
	}
	
	@SuppressWarnings("deprecation")
	public static String getAnswer(String question){
		/* 自定义问答库 */
		String extra = Extrabase.getAnswer(question);
		if (extra != BaseNotFound){
			return extra;
		}
		
		/* 预置的问答库 */
		if ((FindStr(question.toLowerCase(),"hello") != -1)| (FindStr(question.toLowerCase(),"hello")) != -1){
			return Math.round(Math.random()) != 0 ? "Hey guys!" : "Hello!";
		}
		if ((FindStr(question,"早晨") != -1)|(FindStr(question,"早上") != -1 & FindStr(question,"好") != -1)|(FindStr(question,"早安") != -1)){
			if (GoodMorning_events != 0){
				return "你刚刚不是说了吗!?";
			}
			if (Calendar.getInstance().getTime().getHours() >= 0 & Calendar.getInstance().getTime().getHours() < 6){
				return "早啊!这么早就醒来了?";
			}
			if (Calendar.getInstance().getTime().getHours() >= 6 & Calendar.getInstance().getTime().getHours() < 9){
				return "早上好!";
			}
			if (Calendar.getInstance().getTime().getHours() >= 9 & Calendar.getInstance().getTime().getHours() < 11){
				GoodMorning_events = 1;
				return "再不快点就迟到了!";
			}
			if (Calendar.getInstance().getTime().getHours() >= 11 & Calendar.getInstance().getTime().getHours() < 16){
				GoodMorning_events = 2;
				return "你是猪啊?睡到这么晚.";
			}
			if (Calendar.getInstance().getTime().getHours() >= 16){
				return "还早上好啊！？这都下午了……";
			}
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
		if ((FindStr(question,"烧饼") != -1)|(FindStr(question,"fython") != -1)){
			return Math.round(Math.random()) != 0 ? "他是个帅哥，哈哈哈哈哈哈哈哈。(纯属无聊非自恋啊)" : "他很傻的，一般人我不说出来...(嘘！别传出去)";
		}
		if ((FindStr(question,"你好") != -1)|(FindStr(question,"嗨") != -1)){
			return Math.round(Math.random()) != 0 ? "嗨！" : "你好！";
		}
		if ((FindStr(question,"你好") != -1)|(FindStr(question,"嗨") != -1)){
			return Math.round(Math.random()) != 0 ? "嗨！" : "你好！";
		}
		if ((FindStr(question,"在干") != -1)|(FindStr(question,"做什么") != -1)){
			return Math.round(Math.random()) != 0 ? "陪你聊天呗。" : "在跟一个类人猿聊天";
		}
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
		if (FindStr(question,"呵呵") != -1){
			return "呵呵..";
		}
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
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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
			(FindStr(question,"老豆") != -1)){
			Main.i++;
			if (Main.i == 2){
				return "你再骂我试试?";
			}
			if (Main.i == 3){
				int a = 1/0;
				return Integer.toString(a);
			}
			return Math.round(Math.random()) != 0 ? "你妹" : "我去年买了个表";
		}
		return Main.AI_UnknowMsg;
	}

}
