package com.paperairplane.xyrobot;

public class C {
	
	static String
	        update_ver_url = "http://1.paperairplane.sinaapp.com/xy-robot/update.php",
			author_blog_url = "http://pap.xp3.biz/",
			baidu_tongji_key = "cab1d67453",
			baidu_tongji_market = "unknow",
			baidu_app_key = "iBUqGSXofz5AmZ1dX5EbylsR",
			voice_api_key = "appid=513b064e"
			;
	
	public static class handlermsg{
		public static final int addItem_human = 0,
				addItem_robot = 1,
				internet_error = 2,
				version_least = 3,
				version_new = 4,
				voice_api_gotmsg = 5,
				voice_api_nullmsg = 6
				;
	}

	public final class ArraySubscript {
		public static final int UPDATE_INFO = 0;
		public static final int DOWNLOAD_URL = 1;
	}
	
}
