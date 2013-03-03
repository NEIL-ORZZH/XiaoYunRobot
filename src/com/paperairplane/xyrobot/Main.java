package com.paperairplane.xyrobot;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Main extends Activity {

	String UserMsg;
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
				addItem(getString(R.string.myname) +":"+ UserMsg);
				addItem(getString(R.string.robotname) +":"+ RobotAI.getAnswer(UserMsg));
			}
		});
		addItem(getString(R.string.robotname) +":"+ getString(R.string.AI_hello));
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

}
