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
	
	// �п�����ʱ
	public static String getZKDJS() throws ParseException{
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 Date b= sdf.parse("2013-6-19");
		 Date a= Calendar.getInstance().getTime();
		 long time  = (b.getTime()-a.getTime())/1000/60/60/24;
		 return Long.toString(time);
	}
	// �߿�����ʱ
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
		if ((FindStr(question,"���") != -1)|(FindStr(question,"��") != -1)){
			return Math.round(Math.random()) != 0 ? "�ˣ�" : "��ã�";
		}
		if (FindStr(question.toLowerCase(),"time") != -1){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "It's "+formatter.format(date);
		}
		if ((FindStr(question,"����") != -1)|(FindStr(question,"ʱ��") != -1)){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "���ڵ�ʱ���� "+formatter.format(date);
		}
		if ((FindStr(question,"����") != -1)|(FindStr(question,"����") != -1)|(FindStr(question,"�������") != -1)){
			Date date = Calendar.getInstance().getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "���ڵ�ʱ���� "+formatter.format(date);
		}
		if ((FindStr(question,"�п�") != -1)){
			try {
				return "���п����� "+getZKDJS()+" ��!����!";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return "��������������������󣡣�";
			}
		}

		if ((FindStr(question,"�߿�") != -1)){
			try {
				return "��߿����� "+getZKDJS()+" ��!����!";
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				return "��������������������󣡣�";
			}
		}
		return Locale.getDefault() != Locale.CHINA ? "Sorry, I don't know what you mean." : "�Բ���,�Ҳ���������˼.";
	}

}
