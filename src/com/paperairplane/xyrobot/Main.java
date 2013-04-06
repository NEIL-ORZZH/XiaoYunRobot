package com.paperairplane.xyrobot;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;

@SuppressLint("HandlerLeak")
public class Main extends Activity {

	static int i=0;
	static Context mc;
	private String UserMsg,AIMsg1,AIMsg2;
	public static String AI_UnknowMsg,update_text,update_url;
	private List<String> data = new ArrayList<String>();
	private AlertDialog dialogAbout,dialogExit;
	
	public Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				data.add(AIMsg1);
				((ListView) findViewById(R.id.listView1)).setAdapter(new ArrayAdapter<String>(mc, android.R.layout.simple_list_item_1,data));
				((ListView) findViewById(R.id.listView1)).setSelection(((ListView) findViewById(R.id.listView1)).getCount()); //保持在视线在最下一个Item
				((ProgressBar) findViewById(R.id.progressBar1)).setVisibility(ProgressBar.VISIBLE); //隐藏动画
				break;
			case 2:
				data.add(AIMsg2);
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
		StatService.setAppKey(C.baidu_tongji_key);
		StatService.setAppChannel(C.baidu_tongji_market);
		StatService.setOn(this,StatService.EXCEPTION_LOG);
		StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START, 1);
		
		
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				EditText editText1 = (EditText) findViewById(R.id.editText1);
				
				UserMsg = editText1.getText().toString();
				editText1.setText(""); //清空EditText

				/* 检查内容是否为空 */
				if (UserMsg.trim().equals("")){
					Toast.makeText(getApplicationContext(),getString(R.string.AI_StringEMPTY),Toast.LENGTH_SHORT).show();
					return;
				}
				
				Thread thread = new Thread(){
					public void run(){
						
						/* 聊天记录反馈 */
						addItem(handler,getString(R.string.myname),UserMsg,true);
						addItem(handler,getString(R.string.robotname),RobotAI.getAnswer(UserMsg,getApplicationContext()),false);
						
					}
				};
				thread.start();
				
			}
		});
		
		((ImageButton) findViewById(R.id.imageButton1)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				((EditText) findViewById(R.id.editText1)).setText("");
			}
			
		});
		
		AI_UnknowMsg = getString(R.string.AI_unknow);
		addItem(handler,getString(R.string.robotname),getString(R.string.AI_hello),false);
		
		new Thread(){
			public void run(){
				Looper.prepare();
				Tools.update(mc,handler);
			}
		}.start();
	}

	public void addItem(Handler handler,String title,String text,boolean showAni){
		if (showAni) {
			AIMsg1 = title +": " + text;
			handler.sendEmptyMessage(1);
		} else {
			AIMsg2 = title +": " + text;
			handler.sendEmptyMessage(2);
		}
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
	
	private void showAbout(){
		DialogInterface.OnClickListener listenerAbout = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				switch (whichButton) {
				case DialogInterface.BUTTON_POSITIVE:
					dialogAbout.cancel();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					Uri uri = Uri.parse(C.author_blog_url);
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
	
	public void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    switch (keyCode) {
	        case KeyEvent.KEYCODE_BACK:
	        	showExit();
	    }
	    return super.onKeyDown(keyCode, event);
	    
	}
	
}
