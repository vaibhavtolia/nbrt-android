package com.pearson.thenewbusinessroadtest;

import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MatrixSelection extends Activity implements OnItemSelectedListener {
	
	private QuestionsDataStore datasource;
	private Spinner spinner;
	private List<String[]> ideas;
	private String selected_idea;
//	private int very_unattractive_color = 0xFFf47d5f;
//	private int unattractive_color = 0xFFf4a875;
//	private int mixed_color = 0xFFf8f8f0;
//	private int attractive_color = 0xFF94c8d3;
//	private int very_attractive_color = 0xFF5ba0a0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matrix_selection);
		
		spinner = (Spinner) findViewById(R.id.spinner1);
		datasource = new QuestionsDataStore(this);
	}
	
	protected void onResume(){
		super.onResume();
		initLayout();
	}
	
	public void initLayout(){
		
		Thread get_idea_details = new Thread(){
			public void run(){
				datasource.open();
				ideas = datasource.getIdeas();
				if(ideas != null && ideas.size() > 0){
					selected_idea = ideas.get(0)[0];
					runOnUiThread(new Runnable(){
						public void run(){
							makeIdeasList(ideas);
						}
					});
				}
				datasource.close();
				//colorizeMatrix();
			}
		};
		get_idea_details.start();
	}
	
//	public void colorizeMatrix(){
//		Log.v("SELECTED IDEA",selected_idea);
//		Thread colorize = new Thread(){
//			public void run(){
//				datasource.open();
//				final List<String[]> ratings = datasource.getIdeawiseRatings(selected_idea);
//				runOnUiThread(new Runnable(){
//					public void run(){
//						updateRatings(ratings);
//					}
//				});
//				datasource.close();
//			}
//		};
//		colorize.start();		
//	}
	
	public void GoBack(View view){
		onBackPressed();
	}
	
	public void ShowChapters(View view){
		Intent i = new Intent(MatrixSelection.this, ListViewActivity.class);
		Log.v("selected idea",selected_idea);
		if( selected_idea != null ){
			i.putExtra("idea", selected_idea);
		}
		startActivityForResult(i, 0);
	}
	
	public void showChapter(View view){
		String ch = (String) view.getTag();
		Log.v("ch",ch);
		Intent i = new Intent(MatrixSelection.this, ListViewActivity.class);
		i.putExtra("ch", ch);
		Log.v("selected idea",selected_idea);
		if( selected_idea != null ){
			i.putExtra("idea", selected_idea);
		}
		startActivityForResult(i, 0);
	}
	
	public void makeIdeasList(List<String[]> ideas){
		 ArrayList<String> idea = new ArrayList<String>();
		 for(int i=0; i<ideas.size();i++){
			 idea.add(ideas.get(i)[1]);
		 }
		 idea.add("New Idea");
		 
		 ArrayAdapter<String> dataAdapter = new ListAdapter(this,R.layout.answer_type_text, idea);
		 dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinner.setAdapter(dataAdapter);
		 spinner.setOnItemSelectedListener(this);
		 
	}
	
//	public void updateRatings(List<String[]> ratings){
//		if( ratings != null && ratings.size()>0 ){
//			for(int i =0;i<ratings.size();i++){
//				String[] rating = ratings.get(i);
//				Log.v("RATINGS",rating[0]+":"+rating[1]);
//				if( new String("1").equals(rating[0]) ){
//					View view = findViewById(R.id.ch1);
//					updateColor(view,rating);
//				}
//				else if( new String("2").equals(rating[0]) ){
//					View view = findViewById(R.id.ch2);
//					updateColor(view,rating);
//				}
//				else if( new String("3").equals(rating[0]) ){
//					View view = findViewById(R.id.ch3);
//					updateColor(view,rating);
//				}
//				else if( new String("4").equals(rating[0]) ){
//					View view = findViewById(R.id.ch4);
//					updateColor(view,rating);
//				}
//			}
//		}
//	}
	
//	public void updateColor(View view,String[] rating){
//		if( new String("1").equals(rating[1]) ){
//			view.setBackgroundColor(very_unattractive_color);
//		}
//		else if(new String("2").equals(rating[1])){
//			view.setBackgroundColor(unattractive_color);
//		}
//		else if(new String("3").equals(rating[1])){
//			view.setBackgroundColor(mixed_color);
//		}
//		else if(new String("4").equals(rating[1])){
//			view.setBackgroundColor(attractive_color);
//		}
//		else if(new String("5").equals(rating[1])){
//			view.setBackgroundColor(very_attractive_color);
//		}
//	}
//	
//	public void resetColors(){
//		View view = findViewById(R.id.ch4);
//		view.setBackgroundResource(R.drawable.border);
//		view = findViewById(R.id.ch3);
//		view.setBackgroundResource(R.drawable.border);
//		view = findViewById(R.id.ch2);
//		view.setBackgroundResource(R.drawable.border);
//		view = findViewById(R.id.ch1);
//		view.setBackgroundResource(R.drawable.border);
//	}
//	
	@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();
        if( new String("New Idea").equals(label) ){
        	Toast.makeText(parent.getContext(), "opening new idea", Toast.LENGTH_SHORT).show();
        	Intent i = new Intent(MatrixSelection.this, Profile.class);
        	i.putExtra("flag", "true");
    		startActivityForResult(i, 0);
        }
        else{
        	selected_idea = ideas.get(position)[0];
//        	resetColors();
//        	colorizeMatrix();
        }
        
 
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public class ListAdapter extends ArrayAdapter<String>{
		
		private Activity context;
        ArrayList<String> data = null;
		
		public ListAdapter(Activity context, int resource, ArrayList<String> data){
            super(context, resource, data);
            this.context = context;
            this.data = data;
        }
		
		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
			if( convertView == null ){
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.idea_list_item, null);
				TextView t = (TextView) convertView.findViewById(R.id.idea);
				t.setText(data.get(position));
			}
			return convertView;
        }


		
	}

}
