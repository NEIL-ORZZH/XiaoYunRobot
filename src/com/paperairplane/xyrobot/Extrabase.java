package com.paperairplane.xyrobot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class Extrabase {
	private static ArrayList<ArrayList<String>> itemQ,itemA = new ArrayList<ArrayList<String>>();
	private static int FileCount = 0;
	private static boolean isInit = false;
	
	public static void InitData() {
		itemQ = new ArrayList<ArrayList<String>>();
		itemA = new ArrayList<ArrayList<String>>();
		File f = new File("mnt/sdcard/Xybot");
		List<File> fileList = getFile(f);
		int i,FileError = 0;
		FileCount = fileList.size();
		for (i=0;i<FileCount;i++){
			try{
	    	BufferedReader br = new BufferedReader(new FileReader(fileList.get(i)));
		    String line="";
		    StringBuffer buffer = new StringBuffer();
		    while ((line=br.readLine())!=null){
		    	buffer.append(line);
		    }
	    	String fileContent = buffer.toString();
			JSONTokener jsonParser = new JSONTokener(fileContent); 
		    JSONObject person = (JSONObject) jsonParser.nextValue();
		    int JSONVersion = 0;
		    try {
		    	JSONVersion = person.getInt("Ver");
		    	Log.i("JSON Loader","当前文件"+fileList.get(i)+"为"+Integer.toString(JSONVersion)+"版本词库");
		    } catch(JSONException e){
		    	Log.i("JSON Loader","当前文件"+fileList.get(i)+"为旧版本词库");
		    }

	    	int j = 0;
	    	ArrayList<String> Temp = new ArrayList<String>();
		    switch (JSONVersion){
		    case 1:
		    	JSONArray ArrayQ,ArrayA = new JSONArray();
		    	ArrayQ = person.getJSONArray("Q");
		    	ArrayA = person.getJSONArray("A");
		    	for (j = 0;j < ArrayQ.length();j++) Temp.add(ArrayQ.getString(j));
		    	itemQ.add(Temp);
		    	Temp = new ArrayList<String>();
		    	for (j = 0;j < ArrayA.length();j++) Temp.add(ArrayA.getString(j));
		    	itemA.add(Temp);
		    	break;
		    
		    default:
		    	int CountQ = (Integer) person.get("CountQ");
		    	int CountA = (Integer) person.get("CountA");
		    	for (j = 0;j < CountQ;j++) Temp.add(person.getString("Q"+Integer.toString(j+1)));
		    	itemQ.add(Temp);
		    	Temp = new ArrayList<String>();
		    	for (j = 0;j < CountA;j++){
		    		Temp.add(person.getString("A"+Integer.toString(j+1)));
		    		}
		    	itemA.add(Temp);
		    	break;
		    }
			} catch (Exception e){
				FileError++;
				Log.e("JSON Loader","当前文件"+fileList.get(i).toString()+"读入失败。");
			}
		};
		FileCount -= FileError;
		isInit = true;
	}
	
	private static List<File> getFile(File file){
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
	
	public static String getAnswer(String Q){
		int i = 0;
		if (!isInit){
			Log.i("ExtraBase","Initing Data....");
			InitData();
		}
		
		for(i=0;i<FileCount;i++){
			int j = 0;
			ArrayList<String> ListTEMP = itemQ.get(i);
			for (j=0;j<ListTEMP.size();j++){
			 if (ListTEMP.get(j).toLowerCase().indexOf(Q.toLowerCase()) != -1){
				String result = itemA.get(i).get(getRandom(itemA.get(i).size()));
				if (result.indexOf("{Date}") != -1){
					result = ReplaceStr(result,"{Date}",(new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime()));
				}
				if (result.indexOf("{Time}") != -1){
					result = ReplaceStr(result,"{Date}",(new SimpleDateFormat("hh:mm:ss")).format(Calendar.getInstance().getTime()));
				}
				if (result.indexOf("{Model}") != -1){
					result = ReplaceStr(result,"{Model}",Build.MODEL);
				}
				if (result.indexOf("{CPU}") != -1){
					result = ReplaceStr(result,"{CPU}",Build.CPU_ABI);
				}
				if (result.indexOf("{SystemVersion}") != -1){
					result = ReplaceStr(result,"{SystemVersion}",Build.VERSION.RELEASE);
				}
		   		return result;
			 }
			}
		}
		return RobotAI.BaseNotFound;
	}
	
	private static String ReplaceStr(String str1,String str2,String str3){
		String result = str1;
		result = str1.substring(0, str1.indexOf(str2))
				 + str3
				 + str1.substring(result.indexOf(str2)+str2.length(),
						          str1.length());
		return result;
	}
	
	private static int getRandom(int Maxnum){
		return (int) (System.currentTimeMillis() % (Maxnum));
	}
	
}
