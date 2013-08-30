package com.pearson.thenewbusinessroadtest;

import java.io.File;

import com.pearson.thenewbusinessroadtest.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends Activity {

	
	private QuestionsDataStore datasource;
	Context context;
	private String qid;
	private String type;
	private String idea;
	private boolean saved = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_activity);
		context = this;
		
		Intent intent = getIntent();
		qid = intent.getStringExtra("qid");
		idea = intent.getStringExtra("idea");
		type = intent.getStringExtra("type");
		
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
	
	public void saveNote(View view){
		EditText ed = (EditText) this.findViewById(R.id.editText1);
		final String text = ed.getText().toString();
		Log.v("text","a="+text);
		if(text != null && !text.isEmpty() && !saved){
			Thread save_answer = new Thread(){
				public void run(){
					datasource.open();
					String toast = "";
					if( new String("answer").equals(type)){
						datasource.insertAnswer(idea, qid , "text", text);
						toast = "Answer saved successfully";
						saved = true;
					}
					else{
						datasource.insertRisk(idea, qid , "text", text);
						toast = "Risk saved successfully";
						saved = true;
					}
					datasource.close();
					final String t = toast;
					runOnUiThread(new Runnable(){
	            		public void run(){
	            			Toast.makeText(context, t , 2000).show();
	            			onBackPressed();
	            		}
	            	});
					
				}
			};
			save_answer.start();
		}
		else if(saved){
			Toast.makeText(context, "Already saved!", 2000).show();
		}
		else{
			Toast.makeText(context, "Plese enter some text!", 2000).show();
		}
	}
	
	public void cancelNote(View view){
		final EditText ed = (EditText) this.findViewById(R.id.editText1);
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Confirm Not Save")
        .setMessage("Do not save this entry? ")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            	runOnUiThread(new Runnable(){
            		public void run(){
            			ed.setText("");
            			onBackPressed();
            		}
            	});

            }

        })
        .setNegativeButton("No", null)
        .show();
	}
}
