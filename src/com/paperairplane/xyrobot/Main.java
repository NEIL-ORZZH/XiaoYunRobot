package com.paperairplane.xyrobot;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
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

public class Main extends Activity {

	static int i=0;
	String UserMsg;
	static String AI_UnknowMsg;
	List<String> data = new ArrayList<String>();
	static List<String> Title,Text = new ArrayList<String>();
	AlertDialog dialogAbout;
	
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
				
				/* ��������Ƿ�Ϊ�� */
				if (UserMsg.equals("")){
					editText1.setText("");
					CreateToast(getString(R.string.AI_StringEMPTY),Toast.LENGTH_SHORT);
					return;
				}
				
				editText1.setText(""); //���EditText
				PB.setVisibility(ProgressBar.VISIBLE); //��ʾLoading����
				
				/* �����¼���� */
				addItem(getString(R.string.myname),UserMsg);
				addItem(getString(R.string.robotname), 
				((UserMsg.indexOf("����") != -1) & (UserMsg.indexOf("����") != -1)
				             ? IWantToShareMusic() : RobotAI.getAnswer(UserMsg)));
				
				listView1.setSelection(listView1.getCount()); //����������������һ��Item
				PB.setVisibility(ProgressBar.INVISIBLE); //���ض���
			}
		});
		AI_UnknowMsg = getString(R.string.AI_unknow);
		addItem(getString(R.string.robotname),getString(R.string.AI_hello));
	}

	public void addItem(String title,String text){
		/* ԭList��ʾ���� */
		data.add(title +": " + text); 
		((ListView) findViewById(R.id.listView1)).setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data));
	}
	
	public String IWantToShareMusic(){
		if (StartMusicShare()) {
			return "�õ�! ����ʹ�����ַ���Ϊ�����.";
		} else {
			CreateToast("OMG! ��û�а�װֽ�ɻ����ַ���!",Toast.LENGTH_SHORT);
			return "������˼,��ʱ���ܷ�������";
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
         return false;
        }
	}
	
	/* UI���� Code */
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
	
	public void showAbout(){
		DialogInterface.OnClickListener listenerAbout = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				switch (whichButton) {
				case DialogInterface.BUTTON_POSITIVE:
					dialogAbout.cancel();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					Uri uri = Uri.parse("http://m-sky.lofter.com");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
					break;
				}
			}
		};
		dialogAbout = new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(getString(R.string.menu_about))
		.setMessage(getString(R.string.about_context))
		.setPositiveButton(android.R.string.ok, listenerAbout)
		.setNegativeButton(R.string.about_contact, listenerAbout)
		.show();
	}
	
}
