package com.pearson.thenewbusinessroadtest;

import java.util.Timer;
import java.util.TimerTask;

import com.pearson.thenewbusinessroadtest.R;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;

public class SplashScreen extends Activity {
	
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		context = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        	public void run() {
        		startActivity(context);
        	}
        }, 2500);
		
	}
	
	public void startActivity(Context a){
    	Intent intent = new Intent(a, PrefaceActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		
		
		return true;
	}

}
