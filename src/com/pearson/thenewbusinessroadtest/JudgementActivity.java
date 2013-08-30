package com.pearson.thenewbusinessroadtest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.pearson.thenewbusinessroadtest.ListViewActivity.ListItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JudgementActivity extends Activity {
	
	private String chapter;
	private String idea;
	private QuestionsDataStore datasource;
	List<String[]> judgements;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.judgement);
		
		Intent intent = getIntent();
        chapter = intent.getStringExtra("ch");
        idea = intent.getStringExtra("idea");
        datasource = new QuestionsDataStore(this);
	}
	
	protected void onResume(){
		super.onResume();
		createPage(idea,chapter);
	}
	
	public void createPage(String idea,String chapter){
		datasource.open();
		
		List<String> chapter_data = datasource.getChapterData(Integer.parseInt(chapter));
		String name = chapter_data.get(0);
		TextView title = (TextView) findViewById(R.id.chaptername);
		title.setText(name);
		
		judgements = datasource.getAllJudgements(idea, chapter);
		
		int size = judgements.size();
		ArrayList<String> arr = new ArrayList<String>();
		for(int i=0; i< size;i++){
			arr.add("i");
		}
		
		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setDivider(null);
		CustomAdapter arrayAdapter = new CustomAdapter(this, R.layout.answer_type_text, arr);
		
		lv.setAdapter(arrayAdapter);
		
		datasource.close();

	}
	
	public void showJudgement(View view){
		View wrapper = findViewById(R.id.newjudgement);
		wrapper.setVisibility(View.VISIBLE);
	}
	
	public void hideJudgement(){
		View wrapper = findViewById(R.id.newjudgement);
		wrapper.setVisibility(View.GONE);
	}
	
	public void saveJudgement(View view){
		datasource.open();
		EditText judgement = (EditText) findViewById(R.id.editText1);
		String text = judgement.getText().toString();
		datasource.insertJudgement(idea, chapter, text);
		datasource.close();
		hideJudgement();
		createPage(idea,chapter);
	}
	
	public void cancelJudgement(View view){
		hideJudgement();
	}
	
	public class CustomAdapter extends ArrayAdapter<String> {
    	
    	private final Context context;
	    private final ArrayList<String> Ids;
	    private final int rowResourceId;

	    public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> lakes) {

	        super(context, textViewResourceId, lakes);

	        this.context = context;
	        this.Ids = lakes;
	        this.rowResourceId = textViewResourceId;

	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	if( convertView == null ){
	    		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    		convertView = inflater.inflate(R.layout.judgement_view, parent, false);
	    	}
	    	TextView t = (TextView) convertView.findViewById(R.id.text);
	    	String[] data = judgements.get(position);
	    	t.setText(data[1]);
	    	t.setTag(data[0]);
	    	return convertView;
	    }
    	
    }

}
