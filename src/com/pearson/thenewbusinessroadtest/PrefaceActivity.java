package com.pearson.thenewbusinessroadtest;


import com.pearson.thenewbusinessroadtest.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class PrefaceActivity extends Activity{
	
	private Context context;
	private int slider_index;
	private int slides_count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_menu);
        
        slider_index = 1;
        slides_count = 3;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public void onResume(){
    	super.onResume();
    	
    	initView();
    	context = this;
    	
    	TextView preface = (TextView) findViewById(R.id.textView5);
    	preface.setText(Html.fromHtml("This app includes material from <i>The New Business Road Test: What Entrepreneurs Should Do <u>Before</u> Launching a Lean Start-Up (fourth edition)</i> written by John Mullins and published by Pearson Education Limited, 2013"));

    }
    
    public void showDomain(View view){
    	Intent intent = new Intent(context, Profile.class);
    	startActivity(intent);
    }
    
    public void initView(){
    	updateControlPanel();
    }
    
    public void updateControlPanel(){
    	if(slider_index == slides_count){
    		View next_wrapper = findViewById(R.id.next_wrapper);
    		next_wrapper.setVisibility(View.INVISIBLE);
    		View con = findViewById(R.id.continue_wrapper);
    		con.setVisibility(View.VISIBLE);
    	}
    	else if(slider_index == 1){
    		View prev_wrapper = findViewById(R.id.prev_wrapper);
    		prev_wrapper.setVisibility(View.INVISIBLE);
    	}
    	else{
    		View next_wrapper = findViewById(R.id.next_wrapper);
    		next_wrapper.setVisibility(View.VISIBLE);
    		View prev_wrapper = findViewById(R.id.prev_wrapper);
    		prev_wrapper.setVisibility(View.VISIBLE);
    		View con = findViewById(R.id.continue_wrapper);
    		con.setVisibility(View.GONE);
    	}
    }
    
    public void showNext(View view){
    	if( slider_index <= slides_count ){
    		showSlide(slider_index+1);
    		slider_index++;
    	}
    	updateControlPanel();
    }
    
    public void showPrev(View view){
    	if( slider_index > 0 ){
    		showSlide(slider_index - 1);
    		slider_index--;
    	}
    	updateControlPanel();
    }
    
    public void showSlide(int i){
    	View slide1 = (View) findViewById(R.id.slide1);
    	View slide2 = (View) findViewById(R.id.slide2);
    	View slide3 = (View) findViewById(R.id.slide3);
    	switch(i){
    		case 1 :
    			slide3.setVisibility(View.GONE);
    			slide2.setVisibility(View.GONE);
    			slide1.setVisibility(View.VISIBLE);
    			break;
    		case 2:
    			slide1.setVisibility(View.GONE);
    			slide3.setVisibility(View.GONE);
    			slide2.setVisibility(View.VISIBLE);
    			break;
    		case 3:
    			slide1.setVisibility(View.GONE);
    			slide2.setVisibility(View.GONE);
    			slide3.setVisibility(View.VISIBLE);
    			break;
    	}
    	
    }
    
    public void hideSlide(int i){
    	switch(i){
			case 1 :
				View slide1 = (View) findViewById(R.id.slide1);
				slide1.setVisibility(View.GONE);
				break;
			case 2:
				View slide2 = (View) findViewById(R.id.slide2);
				slide2.setVisibility(View.GONE);
				break;
			case 3:
				View slide3 = (View) findViewById(R.id.slide3);
				slide3.setVisibility(View.GONE);
				break;
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}
    

}
