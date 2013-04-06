package com.paperairplane.xyrobot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class RobotAI {
	
	static int GoodMorning_events,IhadACold = 0; //�¼���¼��
	public static String BaseNotFound = "f91e5ce9c2fef8063eb44df100c2d53c";
	
	public static int FindStr(String str0,String str1){
		return str0.indexOf(str1);
	}
	
	@SuppressWarnings("deprecation")
	public static String getAnswer(String question,Context context){
		int NowHours = Calendar.getInstance().getTime().getHours();
		int NowYears = Calendar.getInstance().getTime().getYear();
		
		
		/* ʵ�ò��� */
		if ((question.indexOf("����") != -1) & (question.indexOf("����") != -1)){
			return Tools.StartAndroidAPP("com.paperairplane.music.share",context) ? "�����ɹ�������ʹ�����ַ���Ϊ������" : "��Ǹ����û�а�װ���ַ�������ʹ�ñ��������¼http://www.paperairplane.tk";
		}
		if ((question.indexOf("����") != -1) | (question.indexOf("CPU") != -1) | (question.indexOf("�ֻ�") != -1)){
			String result =
					"��Android �ֻ�������Ϣ����\nCPU:"+Build.CPU_ABI
					+" ϵͳ�汾:"+Build.VERSION.RELEASE
					+"\nSDK�汾:"+((Integer) Build.VERSION.SDK_INT).toString()+"\n��������:";
			String phonenumber = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
			return result+phonenumber;
		}
		if ((question.indexOf("��绰") != -1)|(question.indexOf("���") != -1)){
			String str1 = question;
			if (question.indexOf("��") != -1) str1 = Extrabase.ReplaceStr(str1, "��", "");
			if (question.indexOf("�绰") != -1) str1 = Extrabase.ReplaceStr(str1, "�绰", "");
			if (question.indexOf("��") != -1) str1 = Extrabase.ReplaceStr(str1, "��", "");
			if (question.indexOf("��") != -1) str1 = Extrabase.ReplaceStr(str1, "��", "");
			return Tools.PhoneTo(str1.trim(),context) ? "���ڴ�绰��"+str1 : "����ʧ�ܡ�";
		}
		if ((question.indexOf("����") != -1)|(question.indexOf("�ٶ�") != -1)){
			String str1 = question;
			if (question.indexOf("����") != -1) str1 = Extrabase.ReplaceStr(str1, "����", "");
			if (question.indexOf("�ٶ�") != -1) str1 = Extrabase.ReplaceStr(str1, "�ٶ�", "");
			return Tools.OpenURI("www.baidu.com/baidu?word="+str1+"&tn=xiaoyunrobot",context) ? "��������" + str1 : "������������ʱ���������⡣";
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
			return Tools.Translate(str1,from,to);
		}

		
		/* �Զ����ʴ�� */
		String extra = Extrabase.getAnswer(question);
		if (extra != BaseNotFound){
			return extra;
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
		if ((FindStr(question,"ι") != -1)|(FindStr(question,"��") != -1)) return Math.round(Math.random()) != 0 ? "�����" : "-  - Can I help you?";
		if ((FindStr(question,"�ڸ�") != -1)|(FindStr(question,"��ʲô") != -1))return Math.round(Math.random()) != 0 ? "���������¡�" : "�ڸ�һ������Գ����";
		if (((FindStr(question,"��") != -1)|(FindStr(question,"ȥ") != -1)) & question.length() <= 3) return Math.round(Math.random()) != 0 ? "555..." : "���";
		if (((FindStr(question,"��") != -1)|(FindStr(question,"��") != -1)) & question.length() <= 3) return "�����˰�";
		if (FindStr(question,"��") != -1 & question.length() <= 3) return "���˳Է�ȥ��";
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
				return "���п����� "+Tools.getDJS("2013-6-19")+" ��!����!";
			} catch (ParseException e) {
				return "";
			}
		}
		if (FindStr(question,"�߿�") != -1){
			try {
				return "��߿����� "+Tools.getDJS("2013-6-7")+" ��!����!";
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
