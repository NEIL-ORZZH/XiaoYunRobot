package com.paperairplane.xyrobot;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Main extends Activity {

	static int i=0;
	String UserMsg;
	static String AI_UnknowMsg;
	List<String> data = new ArrayList<String>();
	static List<String> Title,Text = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				EditText editText1 = (EditText) findViewById(R.id.editText1);
				ListView listView1 = (ListView) findViewById(R.id.listView1);
				ProgressBar PB = (ProgressBar) findViewById(R.id.progressBar1);
				
				UserMsg = editText1.getText().toString();
				
				/* 检查内容是否为空 */
				if (UserMsg.equals("")){
					editText1.setText("");
					CreateToast(getString(R.string.AI_StringEMPTY),Toast.LENGTH_SHORT);
					return;
				}
				
				editText1.setText(""); //清空EditText
				PB.setVisibility(ProgressBar.VISIBLE); //显示Loading动画
				
				/* 聊天记录反馈 */
				addItem(getString(R.string.myname),UserMsg);
				addItem(getString(R.string.robotname), 
				((UserMsg.indexOf("音乐") != -1) & (UserMsg.indexOf("分享") != -1)
				             ? IWantToShareMusic() : RobotAI.getAnswer(UserMsg)));
				
				listView1.setSelection(listView1.getCount()); //保持在视线在最下一个Item
				PB.setVisibility(ProgressBar.INVISIBLE); //隐藏动画
			}
		});
		AI_UnknowMsg = getString(R.string.AI_unknow);
		addItem(getString(R.string.robotname),getString(R.string.AI_hello));
	}

	public void addItem(String title,String text){
		/* 原List显示方法 */
		data.add(title +": " + text); 
		((ListView) findViewById(R.id.listView1)).setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data));
	}
	
	public String IWantToShareMusic(){
		if (StartMusicShare()) {
			return "好的! 现在使用音乐分享为你服务.";
		} else {
			CreateToast("OMG! 您没有安装纸飞机音乐分享!",Toast.LENGTH_SHORT);
			return "不好意思,暂时不能分享音乐";
		}
	}
	
	public boolean StartMusicShare(){
		Intent intent = new Intent();
        intent = getPackageManager().getLaunchIntentForPackage("com.paperairplane.music.share");
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//..FLAG_ACTIVITY_NEW_TASK);                      
        try {
         startActivityForResult(intent,0);
         return true;
        } catch (ActivityNotFoundException e) {
         e.printStackTrace();
         return false;
        }
	}
	
	/* UI部分 Code */
	public Dialog onCreateDialog(final int _id) {
		if (_id == R.layout.about) {
			View about = LayoutInflater.from(this).inflate(R.layout.about, null);
			
			((Button) about.findViewById(R.id.button_about)).setOnClickListener(new OnClickListener() {
				@SuppressWarnings("deprecation")
				public void onClick(View v) {
					removeDialog(R.layout.about);
				}
			});
			
			((Button) about.findViewById(R.id.button_follow)).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Uri uri = Uri.parse("http://m-sky.lofter.com/");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
			});
			return new AlertDialog.Builder(this).setView(about).create();
		}
		return null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		super.onOptionsItemSelected(menu);
		switch (menu.getItemId()) {
		case R.id.menu_settings:
			showAbout();
			break;
		case R.id.menu_reload:
			try {
				Extrabase.InitData();
			} catch (Exception e) {
				Toast.makeText(getApplication(),getString(R.string.AI_LoadFileFailed),Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.menu_exit:
			finish();
			System.exit(0);
			break;
		}
		return true;
	}
	
	public void CreateToast(String string,int ToastLength){
		Toast.makeText(getApplication(),string,ToastLength).show();
	}
	
	@SuppressWarnings("deprecation")
	public void showAbout(){
		showDialog(R.layout.about);
	}

}
