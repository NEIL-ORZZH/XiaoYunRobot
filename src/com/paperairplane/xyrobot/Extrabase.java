package com.paperairplane.xyrobot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class Extrabase {
	public static ArrayList<ArrayList<String>> itemQ = new ArrayList<ArrayList<String>>();
	public static ArrayList<ArrayList<String>> itemA = new ArrayList<ArrayList<String>>();
	private static int FileCount = 0;
	public static boolean isInit = false;
	
	public static void InitData() throws Exception{
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
		    JSONObject person = (JSONObject) jsonParser.nextValue();
		    int CountQ = (Integer) person.get("CountQ");
		    int CountA = (Integer) person.get("CountA");
		    int j = 0;
		    ArrayList<String> Temp = new ArrayList<String>();
		    for (j=0;j<CountQ;j++){
		    	Temp.add(person.getString("Q"+Integer.toString(j+1)));
		    }
		    itemQ.add(Temp);
		    Temp = new ArrayList<String>();
		    for (j=0;j<CountA;j++){
		    	Temp.add(person.getString("A"+Integer.toString(j+1)));
		    }
		    itemA.add(Temp);
		};
		isInit = true;
	}
	
	public static String getAnswer(String Q){
		int i = 0;
		if (!isInit){
			Log.i("XIAOYUN DEBUG","First initing....");
			try {
				InitData();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(i=0;i<FileCount;i++){
			if (itemQ.get(i).toString().toLowerCase().indexOf(Q.toLowerCase()) != -1){
				return itemA.get(i).get(getRandom(itemA.size()));
			}
		}
		return RobotAI.BaseNotFound;
	}
	
	public static int getRandom(int Maxnum){
		return (int) (System.currentTimeMillis() % Maxnum);
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
