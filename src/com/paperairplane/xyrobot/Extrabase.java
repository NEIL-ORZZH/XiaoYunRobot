package com.paperairplane.xyrobot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class Extrabase {
	public static ArrayList<String> itemQ = new ArrayList<String>();
	public static ArrayList<String> itemA = new ArrayList<String>();
	private static int FileCount = 0;
	public static boolean isInit = false;
	
	public static void InitData() throws IOException, JSONException{
		File f = new File("mnt/sdcard/Xybot");
		List<File> fileList = getFile(f);
		int i = 0;
		FileCount = fileList.size();
		for (i=0;i<FileCount;i++){
			BufferedReader br = new BufferedReader(new FileReader(fileList.get(i)));
			String line="";
			StringBuffer buffer = new StringBuffer();
			while ((line=br.readLine())!=null){
				buffer.append(line);
			}
			String fileContent = buffer.toString();
			JSONTokener jsonParser = new JSONTokener(fileContent); 
		    Log.i("XIAOYUN DEBUG","JSONTokener Created");
		    JSONObject person = (JSONObject) jsonParser.nextValue();
		    Log.i("XIAOYUN DEBUG","JSON Object Created");
		    
		    int CountQ = (Integer) person.get("CountQ");
		    
		    Log.i("XIAOYUN DEBUG","getQuestionCount:"+Integer.toString(CountQ));
		    
		    int CountA = (Integer) person.get("CountA");
		    
		    Log.i("XIAOYUN DEBUG","getAnswerCount:"+Integer.toString(CountA));
		    
		    int j = 0;
		    for (j=0;j<CountQ;j++){
		    	itemQ.add(i,person.getString("Q"+Integer.toString(j+1)));
		    }
		    for (j=0;j<CountA;j++){
		    	itemA.add(i,person.getString("A"+Integer.toString(j+1)));
		    }
		};
		isInit = true;
	}
	
	public static String getAnswer(String Q){
		int i = 0;
		if (!isInit){
			Log.i("XIAOYUN DEBUG","First initing....");
			try {
				InitData();
			} catch (IOException e) {
				Log.e("XIAOYUN DEBUG","File Not Found!");
				return RobotAI.BaseNotFound;
			} catch (JSONException e){
				Log.e("XIAOYUN DEBUG","JSON Load failed!!!");
				e.printStackTrace();
				return RobotAI.BaseNotFound;
			}
		}
		
		for(i=0;i<FileCount;i++){
			if (itemQ.toString().toLowerCase().indexOf(Q.toLowerCase()) != -1){
				return itemA.get(getRandom(itemA.size()-1));
			}
		}
		return RobotAI.BaseNotFound;
	}
	
	public static int getRandom(int Maxnum){
		Random random = new Random();
		return Math.abs(random.nextInt()) % Maxnum;
	}
	
	public static List<File> getFile(File file){
		List<File> mFileList = new ArrayList<File>();
		File[] fileArray =file.listFiles();
		for (File f : fileArray) {
			if(f.isFile()){
				mFileList.add(f);
			}else{
				getFile(f);
			}
		}
		return mFileList;
	}
}
