package com.pearson.thenewbusinessroadtest;

import java.util.List;

import com.pearson.thenewbusinessroadtest.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

public class Profile extends Activity {
	
	private static final String TAG = "MyActivity";
	final Context context = this;
	private QuestionsDataStore datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		datasource = new QuestionsDataStore(this);
		
		Intent intent = getIntent();
		String flag = intent.getStringExtra("flag");
		if( flag == null ){
			init();
		}
		else{
			initLayout();
		}
	}
	
	public void init(){
		Thread checkIdea = new Thread(){
			public void run(){
				datasource.open();
				Integer idea_count = datasource.getIdeasCount();
				if(idea_count > 0){
					moveToNextPage(true);
				}
				else{
					runOnUiThread(new Runnable(){
						public void run(){
							initLayout();
						}
					});
				}
				datasource.close();
			}
		};
		checkIdea.start();
	}
	
	public void initLayout(){
		final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.idea, null);
		ScrollView ll = (ScrollView) findViewById(R.id.scrollview1);
		ll.addView(vg);
	}
	
	public void moveToNextPage(boolean flag_remove_from_stack){
		Intent startNewActivityOpen = new Intent(Profile.this, MatrixSelection.class);
		startActivityForResult(startNewActivityOpen, 0);
		if(flag_remove_from_stack){
			finish();
		}
	}
	
	public void ShowNextScreen(View view){
		if(updateIdea()){
			moveToNextPage(false);
		}
	}
	
	public boolean updateIdea(){
		EditText idea_name = (EditText) findViewById(R.id.editText1);
		String idea = idea_name.getText().toString();
		EditText problem = (EditText) findViewById(R.id.editText2);
		String problem_text = problem.getText().toString();
		EditText segment = (EditText) findViewById(R.id.editText3);
		String segment_text = segment.getText().toString();
		EditText solution = (EditText) findViewById(R.id.editText4);
		String solution_string = solution.getText().toString();
		EditText industry = (EditText) findViewById(R.id.editText5);
		String industry_string = industry.getText().toString();
		if( idea != null && !idea.isEmpty() ){
			Log.v(TAG,idea);
			if( problem_text != null && !problem_text.isEmpty() ){
				Log.v(TAG,problem_text);
				if(segment_text != null && !segment_text.isEmpty()){
					Log.v(TAG,segment_text);
					if( solution_string != null && !solution_string.isEmpty() ){
						Log.v(TAG,solution_string);
						if( industry_string != null && !industry_string.isEmpty() ){
							Log.v(TAG,industry_string);
							datasource.open();
							
							datasource.insertIdea(1, idea, problem_text, segment_text, solution_string, industry_string);
							datasource.close();
							return true;
						}
						else{
							industry.requestFocus();
							Toast.makeText(context, "Provide an industry your business will operate!" , 1000).show();
							return false;
						}
					}
					else{
						solution.requestFocus();
						Toast.makeText(context, "Write a solution that you propose!" , 1000).show();
						return false;
					}
				}
				else{
					segment.requestFocus();
					Toast.makeText(context, "Provide a segment in which your business will operate!" , 1000).show();
					return false;
				}
				
			}else{
				problem.requestFocus();
				Toast.makeText(context, "Tell a problem you aim to solve" , 1000).show();
				return false;
			}
		}
		else{
			idea_name.requestFocus();
			Toast.makeText(context, "Hey! provide a name for your startup" , 1000).show();
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}
}
