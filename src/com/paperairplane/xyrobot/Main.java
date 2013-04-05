package com.paperairplane.xyrobot;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

@SuppressLint("HandlerLeak")
public class Main extends Activity {

	static int i=0;
	Context mc;
	String UserMsg,AIMsg;
	static String AI_UnknowMsg;
	List<String> data = new ArrayList<String>();
	static List<String> Title,Text = new ArrayList<String>();
	AlertDialog dialogAbout,dialogExit;
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				data.add(AIMsg);
				((ListView) findViewById(R.id.listView1)).setAdapter(new ArrayAdapter<String>(mc, android.R.layout.simple_list_item_1,data));
				((ListView) findViewById(R.id.listView1)).setSelection(((ListView) findViewById(R.id.listView1)).getCount()); //保持在视线在最下一个Item
				((ProgressBar) findViewById(R.id.progressBar1)).setVisibility(ProgressBar.VISIBLE); //隐藏动画
				break;
			case 2:
				data.add(AIMsg);
				((ListView) findViewById(R.id.listView1)).setAdapter(new ArrayAdapter<String>(mc, android.R.layout.simple_list_item_1,data));
				((ListView) findViewById(R.id.listView1)).setSelection(((ListView) findViewById(R.id.listView1)).getCount()); //保持在视线在最下一个Item
				((ProgressBar) findViewById(R.id.progressBar1)).setVisibility(ProgressBar.INVISIBLE); //隐藏动画
			break;
			}
		}
	};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mc = getApplicationContext();
		
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				EditText editText1 = (EditText) findViewById(R.id.editText1);
				
				UserMsg = editText1.getText().toString();
				editText1.setText(""); //清空EditText
				
				Thread thread = new Thread(){
					public void run(){
						/* 检查内容是否为空 */
						if (UserMsg.trim().equals("")){
							CreateToast(getString(R.string.AI_StringEMPTY),Toast.LENGTH_SHORT);
							return;
							}
						
						/* 聊天记录反馈 */
						addItem(handler,getString(R.string.myname),UserMsg,true);
						addItem(handler,getString(R.string.robotname),RobotAI.getAnswer(UserMsg,getApplicationContext()),false);
						
					}
				};
				thread.start();
				
			}
		});
		AI_UnknowMsg = getString(R.string.AI_unknow);
		addItem(handler,getString(R.string.robotname),getString(R.string.AI_hello),false);
	}

	public void addItem(Handler handler,String title,String text,boolean showAni){
		Message msg = new Message();
		if (showAni) msg.what = 1; else msg.what = 2;
		AIMsg = title +": " + text;
		handler.sendMessage(msg);
	}
	
	/* UI部分 Code */
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
			showExit();
			break;
		}
		return true;
	}
	
	public void CreateToast(String string,int ToastLength){
		Toast.makeText(getApplication(),string,ToastLength).show();
	}
	
	private void showAbout(){
		DialogInterface.OnClickListener listenerAbout = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				switch (whichButton) {
				case DialogInterface.BUTTON_POSITIVE:
					dialogAbout.cancel();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					Uri uri = Uri.parse("http://pap.xp3.biz");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
					break;
				}
			}
		};
		dialogAbout = new AlertDialog.Builder(this)
		.setIcon(R.drawable.ic_launcher)
		.setTitle(getString(R.string.menu_about))
		.setMessage(getString(R.string.about_context))
		.setPositiveButton(android.R.string.ok, listenerAbout)
		.setNegativeButton(R.string.about_contact, listenerAbout)
		.show();
	}
	
	private void showExit(){
		DialogInterface.OnClickListener listenerAbout = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				switch (whichButton) {
				case DialogInterface.BUTTON_POSITIVE:
					finish();
					System.exit(0);
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					dialogExit.cancel();
					break;
				}
			}
		};
		dialogExit = new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle(getString(R.string.menu_areyouwant2exit))
		.setMessage(getString(R.string.exit_context))
		.setPositiveButton(R.string.exit_yes, listenerAbout)
		.setNegativeButton(R.string.exit_no, listenerAbout)
		.show();
	}
	
}
