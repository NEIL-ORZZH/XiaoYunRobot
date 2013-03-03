package com.paperairplane.xyrobot;

import java.util.Locale;

public class RobotAI {

	private static int FindStr(String str0,String str1){
		return str0.indexOf(str1);
	}
	
	public static String getAnswer(String question){
		if (FindStr(question.toLowerCase(),"hello") != -1){
			return Math.round(Math.random()) != 0 ? "Hey guys!" : "Hello!";
		}
		return Locale.getDefault() != Locale.CHINA ? "Sorry, I don't know what you mean." : "对不起,我不理解你的意思.";
	}

}
