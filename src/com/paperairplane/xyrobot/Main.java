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
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

@SuppressLint("HandlerLeak")
public class Main extends Activity {

	static int i=0 , clickedlist=0;
	static Context mc;
	private String UserMsg,AIMsg1,AIMsg2,text;
	public static String AI_UnknowMsg,update_text,update_url;
	private List<String> data = new ArrayList<String>();
	private AlertDialog dialogAbout,dialogExit;
	private boolean isSendAfterSpeaking,isAutoCheckUpdate;
	SharedPreferences config;
	
	public Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case C.handlermsg.addItem_human:
				data.add(AIMsg1);
				((ListView) findViewById(R.id.listView1)).setAdapter(new ArrayAdapter<String>(mc, android.R.layout.simple_list_item_1,data));
				((ListView) findViewById(R.id.listView1)).setSelection(((ListView) findViewById(R.id.listView1)).getCount()); //保持在视线在最下一个Item
				((ProgressBar) findViewById(R.id.progressBar1)).setVisibility(ProgressBar.VISIBLE); //隐藏动画
				break;
			case C.handlermsg.addItem_robot:
				data.add(AIMsg2);
				((ListView) findViewById(R.id.listView1)).setAdapter(new ArrayAdapter<String>(mc, android.R.layout.simple_list_item_1,data));
				((ListView) findViewById(R.id.listView1)).setSelection(((ListView) findViewById(R.id.listView1)).getCount()); //保持在视线在最下一个Item
				((ProgressBar) findViewById(R.id.progressBar1)).setVisibility(ProgressBar.INVISIBLE); //隐藏动画
				break;
			case C.handlermsg.internet_error:
				Toast.makeText(mc, mc.getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
				break;
			case C.handlermsg.version_least:
				Toast.makeText(mc, mc.getString(R.string.version_least), Toast.LENGTH_SHORT).show();
				break;
			case C.handlermsg.version_new:
				updateApp((String[]) msg.obj);
				break;
			case C.handlermsg.voice_api_nullmsg:
				Toast.makeText(mc, mc.getString(R.string.voice_api_null), Toast.LENGTH_SHORT).show();
				break;
			case C.handlermsg.voice_api_gotmsg:
				EditText et = (EditText) findViewById(R.id.editText1);
				et.setText(msg.getData().getString("text"));
				if (isSendAfterSpeaking) {
					SendAction();
				}
				break;
			}
		}
	};
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mc = getApplicationContext();
		GetConfigAction();
		
		// API 初始化
		try{
			StatService.setAppKey(C.baidu_tongji_key);
			StatService.setAppChannel(C.baidu_tongji_market);
			StatService.setOn(this,StatService.EXCEPTION_LOG);
			StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START, 1);
			} catch (Exception e){
				Log.e("baidu tongji","OH!shit.还好我躲过了FC");
		}
		
		// 程序初始化
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				SendAction();
			}
		});
		
		((ImageButton) findViewById(R.id.imageButton2)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
						getVoice();
			}
			
		});
		
		((ListView) findViewById(R.id.listView1)).setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				clickedlist = position;
				showListMenu(data.get(position));
				return false;
			    }
			});
		
		AI_UnknowMsg = getString(R.string.AI_unknow);
		addItem(handler,getString(R.string.robotname),getString(R.string.AI_hello),false);
		
		Thread updateThread = new Thread(){
			public void run(){
				Looper.prepare();
				try {
					Tools.update(handler,getPackageManager().getPackageInfo(getPackageName(), 0).versionCode,mc);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		};
		
		if (isAutoCheckUpdate) {
			updateThread.start();
		}
		
	}

	public void SendAction(){
		EditText editText1 = (EditText) findViewById(R.id.editText1);
		
		UserMsg = editText1.getText().toString();
		editText1.setText(""); //清空EditText

		/* 检查内容是否为空 */
		if (UserMsg.trim().equals("")){
			Toast.makeText(getApplicationContext(),getString(R.string.AI_StringEMPTY),Toast.LENGTH_SHORT).show();
			return;
		}
		
		new Thread(){
			public void run(){
				
				/* 聊天记录反馈 */
				addItem(handler,getString(R.string.myname),UserMsg,true);
				addItem(handler,getString(R.string.robotname),RobotAI.getAnswer(UserMsg,getApplicationContext()),false);
				
			}
		}.start();
	}
	
	public void GetConfigAction(){
		config = getSharedPreferences("config", Context.MODE_APPEND);
		isSendAfterSpeaking = config.getBoolean("send_after_speaking", true);
		isAutoCheckUpdate = config.getBoolean("auto_check_update", true);
	}
	
	public void addItem(Handler handler,String title,String text,boolean showAni){
		if (showAni) {
			AIMsg1 = title +": " + text;
			handler.sendEmptyMessage(C.handlermsg.addItem_human);
		} else {
			AIMsg2 = title +": " + text;
			handler.sendEmptyMessage(C.handlermsg.addItem_robot);
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
		case R.id.menu_about:
			showAbout();
			break;
		case R.id.menu_help:
			showHelp();
			break;
		case R.id.menu_settings:
			showSettings();
			break;
		}
		return true;
	}
	
	private void showAbout(){
		String version = "Unknow";
		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "(" + getPackageManager().getPackageInfo(getPackageName(), 0).versionCode + ")";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
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
		.setMessage(Extrabase.ReplaceStr(getString(R.string.about_context),"[Version]",version))
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

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void showListMenu(String text){
		DialogInterface.OnClickListener listenerAbout = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				switch (whichButton) {
				case DialogInterface.BUTTON_POSITIVE:
					int currentapiVersion = android.os.Build.VERSION.SDK_INT;
					if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
						android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
						ClipData clip = ClipData.newPlainText("label", data.get(clickedlist));
						clipboard.setPrimaryClip(clip);
						} else {
							android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							clipboard.setText(data.get(clickedlist));
						}
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_SUBJECT , getString(R.string.app_name));
					intent.putExtra(Intent.EXTRA_TEXT ,
							        getString(R.string.shareinfo_left) + data.get(clickedlist) + getString(R.string.shareinfo_right));
					startActivity(Intent.createChooser(intent,getString(R.string.how_to_share)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					break;
				case DialogInterface.BUTTON_NEUTRAL:
					playVoice(data.get(clickedlist));
					break;
				}
			}
		};
		
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(getString(R.string.dialogList_title))
		.setMessage(text)
		.setPositiveButton(R.string.copy_str, listenerAbout)
		.setNegativeButton(R.string.share_str, listenerAbout)
		.setNeutralButton("Listen", listenerAbout)
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
	
	private void updateApp(final String[] info) {
		new AlertDialog.Builder(Main.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(R.string.update_found)
				.setMessage(info[C.ArraySubscript.UPDATE_INFO])
				.setPositiveButton(R.string.update_download, new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Uri uri = Uri.parse(info[C.ArraySubscript.DOWNLOAD_URL]);
						Intent intent = new Intent(Intent.ACTION_VIEW,uri);
						startActivity(intent);
					}
				})
				.setNegativeButton(R.string.update_nothanks, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				})
				.show();

	}
	
	public void showSettings(){
		LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.setting, null);

		CheckBox chk1 = (CheckBox) view.findViewById(R.id.checkBox1);
		CheckBox chk2 = (CheckBox) view.findViewById(R.id.checkBox2);
		CheckBox chk3 = (CheckBox) view.findViewById(R.id.checkBox3);
		
		if (config.getBoolean("send_after_speaking", true)){
			chk1.setChecked(true);
		} else {
			chk1.setChecked(false);
		}
		
		if (config.getBoolean("showSplash", true)){
			chk2.setChecked(true);
		} else {
			chk2.setChecked(false);
		}
		
		if (config.getBoolean("auto_check_update", true)){
			chk3.setChecked(true);
		} else {
			chk3.setChecked(false);
		}
		
    	DialogInterface.OnClickListener listenerAbout = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				switch (whichButton) {
				case DialogInterface.BUTTON_POSITIVE:
					CheckBox chk1 = (CheckBox) view.findViewById(R.id.checkBox1);
					CheckBox chk2 = (CheckBox) view.findViewById(R.id.checkBox2);
					CheckBox chk3 = (CheckBox) view.findViewById(R.id.checkBox3);
					Editor editor = config.edit();
					editor.putBoolean("send_after_speaking", chk1.isChecked());
					editor.putBoolean("showSplash", chk2.isChecked());
					editor.putBoolean("auto_check_update", chk3.isChecked());
					editor.commit();
					GetConfigAction();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					dialogExit.cancel();
					break;
				}
			}
		};
		dialogExit = new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(getString(R.string.setting_title))
		.setView(view)
		.setPositiveButton(android.R.string.ok, listenerAbout)
		.setNegativeButton(android.R.string.no, listenerAbout)
		.show();
	}

	public void showHelp(){
		LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.help, null);
    	DialogInterface.OnClickListener listenerAbout = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				switch (whichButton) {
				case DialogInterface.BUTTON_POSITIVE:
					Uri uri = Uri.parse(C.our_website);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					dialogExit.cancel();
					break;
				}
			}
		};
		dialogExit = new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(getString(R.string.help_title))
		.setView(view)
		.setPositiveButton(getString(R.string.help_visitus), listenerAbout)
		.setNegativeButton(android.R.string.ok, listenerAbout)
		.show();
	}
	
	public void getVoice(){
		RecognizerDialog isrDialog = new RecognizerDialog(Main.this, C.voice_api_key);

		isrDialog.setEngine("sms", null, null);
		isrDialog.setListener(recoListener);
		isrDialog.show();
	}
	
	public void playVoice(String string){
		SynthesizerPlayer player = SynthesizerPlayer.createSynthesizerPlayer(Main.this, C.voice_api_key);
		player.setVoiceName("vivixiaomei");
		player.playText(string, "ent=vivi21,bft=5",null);
	}
	
	RecognizerDialogListener recoListener = new RecognizerDialogListener() {
		
		@Override
		public void onResults(ArrayList<RecognizerResult> results,boolean isLast) {
			text = results.get(0).text;
			System.out.println(text);
		}

		@Override
		public void onEnd(SpeechError error) {
			if (error == null) {
				sendMsg(text);
			} else{
				sendNull();
			}

		}

	};

	public void sendMsg(String string){
		Message msg = new Message();
		Bundle data = new Bundle();
		msg.what = C.handlermsg.voice_api_gotmsg;
		data.putString("text", string);
		msg.setData(data);
		handler.sendMessage(msg);
	}
	
	public void sendNull(){
		handler.sendEmptyMessage(C.handlermsg.voice_api_nullmsg);
	}
}
