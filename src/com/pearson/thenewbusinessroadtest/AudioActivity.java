package com.pearson.thenewbusinessroadtest;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.pearson.thenewbusinessroadtest.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AudioActivity extends Activity {

	private QuestionsDataStore datasource;
	Context context;
	private Timer timer;
	private TimerTask timer_task;
	private AudioRecord record;
	public float time = 0;
	private String qid;
	private String type;
	private String idea;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_activity);
		context = this;
		
		Intent intent = getIntent();
		qid = intent.getStringExtra("qid");
		type = intent.getStringExtra("type");
		idea = intent.getStringExtra("idea");
		
		Thread th1 = new Thread(){
			public void run(){
				final String question = getQuestion(qid);
				final TextView text = (TextView) ((Activity) context).findViewById(R.id.question);
				runOnUiThread(new Runnable(){
					public void run(){
						text.setText(question);
					}
				});
			}
		};
		th1.start();
	}
	
	private String getQuestion(String qid){
		datasource = new QuestionsDataStore(this);
	    datasource.open();
	    String question_data = datasource.getQuestion(qid);
	    Log.v("Question",question_data);
	    datasource.close();
	    return question_data;
	}
	
	public void initView(){
		View stop = this.findViewById(R.id.stop);
		stop.setVisibility(View.INVISIBLE);
		View play = this.findViewById(R.id.play);
		play.setVisibility(View.INVISIBLE);
		View sl = this.findViewById(R.id.savinglayout);
		sl.setVisibility(View.INVISIBLE);
		View pause = this.findViewById(R.id.pause);
		pause.setVisibility(View.GONE);
		View record = this.findViewById(R.id.start);
		record.setVisibility(View.VISIBLE);
		TextView time = (TextView) this.findViewById(R.id.timer);
		time.setText("0:00");
	}
	
	public void startRecording(View view){
		record = new AudioRecord();
		record.onRecord(true);
		View time = this.findViewById(R.id.timer);
		startTimer(time);
		View stop = this.findViewById(R.id.stop);
		stop.setVisibility(View.VISIBLE);
		View start = this.findViewById(R.id.start);
		start.setClickable(false);
	}
	
	public void stopRecording(View view){
		record.onRecord(false);
		this.stopTimer();
		View record = this.findViewById(R.id.start);
		record.setVisibility(View.GONE);
		View play = this.findViewById(R.id.play);
		play.setVisibility(View.VISIBLE);
		View sl = this.findViewById(R.id.savinglayout);
		sl.setVisibility(View.VISIBLE);
	}
	
	public void startPlaying(View view){
		record.onPlay(true);
		View play = this.findViewById(R.id.play);
		play.setVisibility(View.GONE);
		View pause = this.findViewById(R.id.pause);
		pause.setVisibility(View.VISIBLE);
	}
	
	public void stopPlaying(View view){
		record.onPlay(false);
		View pause = this.findViewById(R.id.pause);
		pause.setVisibility(View.GONE);
		View play = this.findViewById(R.id.play);
		play.setVisibility(View.VISIBLE);
	}
	
	public void saveAudio(View view){
		Log.v("Save file","function called");
		datasource.open();
		final String file = record.filename;
		Log.v("Save file",file);
		final String response_type = "audio";
		if( new String("answer").equals(type)){
			datasource.insertAnswer(idea, qid, response_type, file);
		}
		else{
			datasource.insertRisk(idea, qid, response_type, file);
		}
		Toast.makeText(this, "Audio file saved successfully",Toast.LENGTH_LONG ).show();
		datasource.close();
		onBackPressed();
	}
	
	public void cancelAudio(View view){
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Confrim Not Save")
        .setMessage("Do not save this entry? ")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            	runOnUiThread(new Runnable(){
            		public void run(){
            			initView();
            		}
            	});
            	
	            Thread th2 = new Thread(){
	            	public void run(){
	            		try{
		            		final String selectedFilePath = record.mFileName; 
			            	File file = new File(selectedFilePath);
			            	file.delete();
			            	Toast.makeText(context, "Audio file deleted",Toast.LENGTH_SHORT ).show();
	            		}catch(Exception e){
	            			e.printStackTrace();
	                        Log.d("File delete", " "+e);
	            		}
	            		////
	            	}
	            };
	            th2.start();
            }

        })
        .setNegativeButton("No", null)
        .show();

	}
	
	public void updateRecordingTime(View view,String time){
		final TextView t = (TextView) view;
		final String s = time;
		runOnUiThread(new Runnable(){
			public void run(){
				t.setText(s);
			}	
		});
	}
	
	public void startTimer(View view){
		final View v = view;
		timer = new Timer();
		timer_task = new TimerTask(){
			public void run() {
        		time = (float) (time + 0.01);
        		time = (float) (Math.round(time*100.0)/100.0);
        		String s = Float.toString(time);
        		updateRecordingTime(v,s);
        	}
		};
		timer.scheduleAtFixedRate(timer_task, 100,100);
	}
	
	public void pauseTimer(){
		timer_task.cancel();
	}
	
	public void stopTimer(){
		timer_task.cancel();
		time = (float) 0.00;
	}
	
	public void resumeTimer(View view){
		this.startTimer(view);
	}
	
}
