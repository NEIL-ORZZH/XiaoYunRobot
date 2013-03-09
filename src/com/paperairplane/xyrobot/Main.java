package com.paperairplane.xyrobot;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Main extends Activity {

	static int i=0;
	String UserMsg;
	static String AI_UnknowMsg;
	List<String> data = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				EditText editText1 = (EditText) findViewById(R.id.editText1);
				UserMsg = editText1.getText().toString();
				editText1.setText("");
				addItem(getString(R.string.myname) +" : "+ UserMsg);
				addItem(getString(R.string.robotname) +" : "+ RobotAI.getAnswer(UserMsg));
			}
		});
		addItem(getString(R.string.robotname) +" : "+ getString(R.string.AI_hello));
	}

	public void addItem(String string){
		data.add(string);
		((ListView) findViewById(R.id.listView1)).setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data));
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
		}
		return true;
	}
	
	public Dialog onCreateDialog(final int _id) {
		if (_id == R.layout.about) {
			View about = LayoutInflater.from(this)
					.inflate(R.layout.about, null);
			Button button_about = (Button) about
					.findViewById(R.id.button_about);
			button_about.setOnClickListener(new OnClickListener() {
				@SuppressWarnings("deprecation")
				public void onClick(View v) {
					removeDialog(R.layout.about);
				}
			});
			return new AlertDialog.Builder(this).setView(about).create();
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public void showAbout(){
		showDialog(R.layout.about);
	}

}
