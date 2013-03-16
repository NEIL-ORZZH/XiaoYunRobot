package com.paperairplane.xyrobot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RobotAI {

	static int GoodMorning_events = 0; //�����ʺ��¼���¼��
	public static String BaseNotFound = "f91e5ce9c2fef8063eb44df100c2d53c"; //�²�����ʲô ������
	private static int FindStr(String str0,String str1){
		return str0.indexOf(str1);
	}
	
	// ����ʱ
	private static String getDJS(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date thatday = sdf.parse(date);
		Date nowtime = Calendar.getInstance().getTime();
		long time  = (thatday.getTime()-nowtime.getTime())/1000/60/60/24;
		return Long.toString(time);
	}
	
	@SuppressWarnings("deprecation")
	public static String getAnswer(String question){
		/* �Զ����ʴ�� */
		String extra = Extrabase.getAnswer(question);
		if (extra != BaseNotFound){
			return extra;
		}
		
		/* Ԥ�õ��ʴ�� */
		if ((FindStr(question.toLowerCase(),"hello") != -1)| (FindStr(question.toLowerCase(),"hello")) != -1){
			return Math.round(Math.random()) != 0 ? "Hey guys!" : "Hello!";
		}
		if ((FindStr(question,"�糿") != -1)|(FindStr(question,"����") != -1 & FindStr(question,"��") != -1)|(FindStr(question,"�簲") != -1)){
			if (GoodMorning_events != 0){
				return "��ող���˵����!?";
			}
			if (Calendar.getInstance().getTime().getHours() >= 0 & Calendar.getInstance().getTime().getHours() < 6){
				return "�簡!��ô���������?";
			}
			if (Calendar.getInstance().getTime().getHours() >= 6 & Calendar.getInstance().getTime().getHours() < 9){
				return "���Ϻ�!";
			}
			if (Calendar.getInstance().getTime().getHours() >= 9 & Calendar.getInstance().getTime().getHours() < 11){
				GoodMorning_events = 1;
				return "�ٲ����ͳٵ���!";
			}
			if (Calendar.getInstance().getTime().getHours() >= 11 & Calendar.getInstance().getTime().getHours() < 16){
				GoodMorning_events = 2;
				return "������?˯����ô��.";
			}
			if (Calendar.getInstance().getTime().getHours() >= 16){
				return "�����Ϻð������ⶼ�����ˡ���";
			}
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
		if ((FindStr(question,"�ձ�") != -1)|(FindStr(question,"fython") != -1)){
			return Math.round(Math.random()) != 0 ? "���Ǹ�˧�磬������������������(�������ķ�������)" : "����ɵ�ģ�һ�����Ҳ�˵����...(�꣡�𴫳�ȥ)";
		}
		if ((FindStr(question,"���") != -1)|(FindStr(question,"��") != -1)){
			return Math.round(Math.random()) != 0 ? "�ˣ�" : "��ã�";
		}
		if ((FindStr(question,"���") != -1)|(FindStr(question,"��") != -1)){
			return Math.round(Math.random()) != 0 ? "�ˣ�" : "��ã�";
		}
		if ((FindStr(question,"�ڸ�") != -1)|(FindStr(question,"��ʲô") != -1)){
			return Math.round(Math.random()) != 0 ? "���������¡�" : "�ڸ�һ������Գ����";
		}
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
		if (FindStr(question,"�Ǻ�") != -1){
			return "�Ǻ�..";
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
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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
			(FindStr(question,"�϶�") != -1)){
			Main.i++;
			if (Main.i == 2){
				return "������������?";
			}
			if (Main.i == 3){
				int a = 1/0;
				return Integer.toString(a);
			}
			return Math.round(Math.random()) != 0 ? "����" : "��ȥ�����˸���";
		}
		return Main.AI_UnknowMsg;
	}

}
