package com.paperairplane.xyrobot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

public class splashScreen extends Activity {

	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash);

        new Thread(){
        	public void run(){
        		Extrabase.InitData();
        	}
        }.start();
        
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(splashScreen.this, Main.class);
                splashScreen.this.startActivity(mainIntent);
                splashScreen.this.finish();
            }
        }, 2000);

    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    switch (keyCode) {
	        case KeyEvent.KEYCODE_BACK:
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}