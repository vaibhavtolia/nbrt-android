package com.pearson.thenewbusinessroadtest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.darvds.ribbonmenu.RibbonMenuView;
import com.darvds.ribbonmenu.iRibbonMenuCallback;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class ListViewActivity extends Activity implements iRibbonMenuCallback {
	
	final Context context = this;
	private QuestionsDataStore datasource;
	private ArrayList<ListItem> model;
	private CustomAdapter adapter = null;
	private RibbonMenuView rbmView;
	private int selected_rating = 0xFF5BA0A0;
	private int default_rating = 0xFFA7CFCF;
	private String idea_id;
	private String chapter_id;
	List<String> chapter_data;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chapter_activity);
		
		rbmView = (RibbonMenuView) findViewById(R.id.ribbonMenuView1);
        rbmView.setMenuClickCallback(this);
        rbmView.setMenuItems(R.menu.ribbon_menu);
        
        chapter_id = "1";
        idea_id = "1";
        Intent intent = getIntent();
        String ch = intent.getStringExtra("ch");
        String idea = intent.getStringExtra("idea");
        if(ch != null){
        	chapter_id = ch;
        }
        if( idea != null ){
        	idea_id = idea;
        }
		
	}
	
	protected void onResume(){
		super.onResume();
		createPage(idea_id,chapter_id);
	}
	
	public void createPage(String idea_id,String chapter){
		chapter_id = chapter;
		createLayout(idea_id,chapter);
		updateChapterData(idea_id,chapter);
	}
	
	public void toggleSideMenu(View view){
    	rbmView.toggleMenu();
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == android.R.id.home) {
			
			rbmView.toggleMenu();
			
			return true;
		
		} else {
			return super.onOptionsItemSelected(item);
		}
	}


	public void RibbonMenuItemClick(int itemId) {
		Integer item = itemId;
		
		switch(itemId){
			case 0 : 
				createPage(idea_id,"1");
				break;
			case 1 : 
				createPage(idea_id,"2");
				break;
			case 2 : 
				createPage(idea_id,"3");
				break;
			case 3 : 
				createPage(idea_id,"4");
				break;
			case 4 : 
				createPage(idea_id,"5");
				break;
			case 5 : 
				createPage(idea_id,"6");
				break;
			case 6 : 
				createPage(idea_id,"7");
				break;
		}
		
	}
	
	public void updateChapterData(final String idea_id,final String chapter_id){
		datasource = new QuestionsDataStore(this);
	    datasource.open();
	    chapter_data = datasource.getChapterData(Integer.parseInt(chapter_id));
	    datasource.close();
	    TextView text = (TextView) this.findViewById(R.id.chaptername);
	    text.setText(chapter_data.get(0));
	    resetChapterRating();
	    Thread get_rating = new Thread(){
	    	public void run(){
	    		getChapterRating(idea_id,chapter_id);
	    	}
	    };
	    get_rating.start();
	}
	
	
	public void getChapterRating(String idea_id,String chapter_id){
		datasource.open();
		final String rating = datasource.getChapterRating(Integer.parseInt(idea_id), Integer.parseInt(chapter_id));
		Log.v("CHAPTER RATING",rating);
		datasource.close();
		if(rating != "null"){
			runOnUiThread(new Runnable(){
				public void run(){
					View view = null;
					if(rating.equals("1")){
						view = findViewById(R.id.vunattractive);
					}
					else if(rating.equals("2")){
						view = findViewById(R.id.unattractive);
					}
					else if(rating.equals("3")){
						view = findViewById(R.id.mixed);
					}
					else if(rating.equals("4")){
						view = findViewById(R.id.attractive);
					}
					else if(rating.equals("5")){
						view = findViewById(R.id.vattractive);
					}
					view.setBackgroundColor(selected_rating);
				}
			});
		}
	}
	
	public void createLayout(String idea_id,String ch_id){
		
		model = new ArrayList<ListItem>();
		ArrayList<String> data = new ArrayList<String>();
		final ListView list = (ListView) findViewById(R.id.listView);
		list.setDivider(null);
		list.setDividerHeight(5);
		
		datasource = new QuestionsDataStore(this);
		datasource.open();
		ArrayList<ContentValues> questions = datasource.getAllQuestions(Integer.parseInt(ch_id));
		
		
		for( int i=0; i<questions.size(); i++ ){
			String question = questions.get(i).get("question").toString();
			String question_id = questions.get(i).get("id").toString();
			String type = "question";
			ListItem item = new ListItem(type,question_id,question,null);
			model.add(item);
			data.add("i");
			List<String[]> answers = datasource.getAllAnswers(idea_id, question_id);
			for(int j = 0; j<answers.size(); j++){
				String[] ans = answers.get(j);
				ListItem answer = new ListItem("answer",ans[2],ans[0],ans[1]);
				model.add(answer);
				data.add("i");
			}
			List<String[]> risks = datasource.getAllRisks(idea_id, question_id);
			for(int k = 0; k<risks.size(); k++){
				String[] ans = risks.get(k);
				ListItem risk = new ListItem("risk",ans[2],ans[0],ans[1]);
				model.add(risk);
				data.add("i");
			}
		}
		
		datasource.close();
		
		adapter = new CustomAdapter(this,R.layout.question_template, data);
        runOnUiThread(new Runnable(){
        	public void run(){
        		list.setAdapter(adapter);
        		//Log.v("TAG","called");
        	}
        });
		
	}
	
	public void takeNote(View view){
		String qid = view.getTag().toString();
		//Log.v("action qid","gfrjhg5itgjo5j");
		Intent note = new Intent(ListViewActivity.this, NoteActivity.class);
		note.putExtra("qid",qid);
		note.putExtra("idea",idea_id);
		note.putExtra("type","answer");
		startActivityForResult(note, 0);
	}
	
	public void takeCameraInput(View view){
		String qid = view.getTag().toString();
		//Log.v("action qid",qid);
		Intent camera = new Intent(ListViewActivity.this, CameraActivity.class);
		camera.putExtra("qid",qid);
		camera.putExtra("idea",idea_id);
		camera.putExtra("type","answer");
		startActivityForResult(camera, 0);
	}
	
	public void takeAudioInput(View view){
		String qid = view.getTag().toString();
		//Log.v("action qid",qid);
		Intent audio = new Intent(ListViewActivity.this, AudioActivity.class);
		audio.putExtra("qid",qid);
		audio.putExtra("idea",idea_id);
		audio.putExtra("type","answer");
		startActivityForResult(audio, 0);
	}
	
	public void showJudgement(View view){
		Intent judgement = new Intent(ListViewActivity.this, JudgementActivity.class);
		judgement.putExtra("ch",chapter_id);
		judgement.putExtra("idea",idea_id);
		startActivityForResult(judgement, 0);
	}
	
	public void takeRisk(View view){
		String qid = view.getTag().toString();
		//Log.v("action qid",qid);
		
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.add_risk_view);
		dialog.setTitle("Add Risk");
		
		RadioButton note = (RadioButton) dialog.findViewById(R.id.radioButton1);
		note.setTag(qid);
		note.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String qid = v.getTag().toString();
				Intent note = new Intent(ListViewActivity.this, NoteActivity.class);
				note.putExtra("qid",qid);
				note.putExtra("idea",idea_id);
				note.putExtra("type", "risk");
				startActivityForResult(note, 0);
				dialog.dismiss();
			}
		});
		
		RadioButton media = (RadioButton) dialog.findViewById(R.id.radioButton2);
		media.setTag(qid);
		media.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String qid = v.getTag().toString();
				Intent camera = new Intent(ListViewActivity.this, CameraActivity.class);
				camera.putExtra("qid",qid);
				camera.putExtra("idea",idea_id);
				camera.putExtra("type", "risk");
				startActivityForResult(camera, 0);
				dialog.dismiss();
			}
		});
		
		RadioButton audio = (RadioButton) dialog.findViewById(R.id.radioButton3);
		audio.setTag(qid);
		audio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String qid = v.getTag().toString();
				Intent audio = new Intent(ListViewActivity.this, AudioActivity.class);
				audio.putExtra("qid",qid);
				audio.putExtra("idea",idea_id);
				audio.putExtra("type", "risk");
				startActivityForResult(audio, 0);
				dialog.dismiss();
			}
		});
		
		
		dialog.show();
	}
	
	public void openResponse(View view){
		String[] tags = (String[]) view.getTag();
		if( new String("text").equals(tags[2]) ){
			openNote(view);
		}
		else if( new String("image").equals(tags[2]) || new String("video").equals(tags[2]) ){
			openMedia(view);
		}
		else if( new String("audio").equals(tags[2]) ){
			openAudio(view);
		}
	}
	
	public void openMedia(View view){
		String[] tags = (String[]) view.getTag();
		String response = tags[1];
		String response_type = tags[2];
		String path = "file://"+Environment.getExternalStorageDirectory()+response;
		Log.v("path",path);
		Uri uri = Uri.parse(path);
		Intent intent = new Intent(Intent.ACTION_VIEW,uri);
		Log.v("RESPONSE TYPE",response_type);
		if(  new String("image").equals(response_type) ){
			intent.setDataAndType(uri,"image/*");
		}else{
			intent.setDataAndType(uri,"video/*");
		}
		startActivity(intent);
	}
	
	public void openAudio(View view){
		String[] tags = (String[]) view.getTag();
		String response = tags[1];
		String path = "file://"+Environment.getExternalStorageDirectory()+"/"+response+".3gp";
		//Log.v("path",path);
		Uri uri = Uri.parse(path);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setDataAndType(uri,"audio/*");
		startActivity(intent);
	}
	
	public void openNote(View view){
		String[] response = (String[]) view.getTag();
		String ans_id = response[0];
		String ans = response[1];
		final String type = response[2];
		
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.edit_text_answer);
		dialog.setTitle("Edit Note");
		
		//set the summary
		EditText text = (EditText) dialog.findViewById(R.id.editText1);
		text.setText(ans);
		
		Button save = (Button) dialog.findViewById(R.id.button1);
		save.setTag(ans_id);
		
		Button dialogButton = (Button) dialog.findViewById(R.id.button2);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		
		save.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				final String ans_id = view.getTag().toString();
				EditText a = (EditText) dialog.findViewById(R.id.editText1); ;
				final String response = a.getText().toString();
				if( response != null && !response.isEmpty() ){
					Thread update_answer = new Thread(){
						public void run(){
							datasource.open();
							if( new String("answer").equals(type) ){
								datasource.updateAnswer(ans_id, response);
							}
							else{
								datasource.updateRisk(ans_id, response);
							}
							datasource.close();
						}
					};
					update_answer.start();
					dialog.dismiss();
				}
				else{
					Toast.makeText(context, "Plese enter some text!", 2000).show();
				}
			}
		});

		dialog.show();
	}

	
	public void updateChapterRating(View view){
		final String rating = view.getTag().toString();
		resetChapterRating();
		view.setBackgroundColor(selected_rating);
		Thread save_rating = new Thread(){
			public void run(){
				datasource.open();
				datasource.updateChapterRating(idea_id, Integer.parseInt(chapter_id), rating);
				datasource.close();
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(context, "Updated Rating successully", 2000).show();
					}
				});
			}
		};
		save_rating.start();
	}
	
	public void resetChapterRating(){
		View view = findViewById(R.id.vunattractive);
		view.setBackgroundColor(default_rating);
		view = findViewById(R.id.unattractive);
		view.setBackgroundColor(default_rating);
		view = findViewById(R.id.mixed);
		view.setBackgroundColor(default_rating);
		view = findViewById(R.id.attractive);
		view.setBackgroundColor(default_rating);
		view = findViewById(R.id.vattractive);
		view.setBackgroundColor(default_rating);
	}
	
	public void showDialog(View view){
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_activity);
		dialog.setTitle(chapter_data.get(0));
		
		//set the summary
		TextView text = (TextView) dialog.findViewById(R.id.summary);
		text.setText(chapter_data.get(1));
		
		
		Button dialogButton = (Button) dialog.findViewById(R.id.close);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	public class ListItem{
		
		public String type;
		public String id;
		public String response;
		public String response_type;
		
		public ListItem(String type, String id, String response, String response_type){
			this.type = type;
			this.id = id;
			this.response = response;
			this.response_type = response_type;
		}
		
	}
	
	public class CustomAdapter extends ArrayAdapter<String> {
    	
    	private final Context context;
	    private final ArrayList<String> Ids;
	    private final int rowResourceId;
	    private static final int TYPE_QUESTION = 1;
	    private static final  int TYPE_RESPONSE = 2;
	    private static final int RISK_COLOR = 0xFFf47d5f;
	    private static final int RISK_TEXTCOLOR = 0xFFffffff;
	    private static final int ANSWER_COLOR = 0xFFf8f8f0;
	    private static final int ANSWER_TEXTCOLOR = 0xFF333333;

	    public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> lakes) {

	        super(context, textViewResourceId, lakes);

	        this.context = context;
	        this.Ids = lakes;
	        Integer i = lakes.size();
	        Log.v("Lenght",i.toString());
	        this.rowResourceId = textViewResourceId;

	    }
	    
	    @Override
        public int getItemViewType(int position) {
	    	ListItem item = model.get(position);
	    	if( new String("question").equals(item.type) ){
	    		return 1;
	    	}
	    	else if(new String("answer").equals(item.type) || new String("risk").equals(item.type) ){
	    		return 2;
	    	}
	    	else{
	    		return 0;
	    	}
        }
	    
	    @Override
        public int getViewTypeCount() {
            return model.size();
        }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	int type = getItemViewType(position);
	    	if (convertView == null) {
	    		switch(type){
	    			case TYPE_QUESTION:
	    				convertView = inflater.inflate(R.layout.question_template, null);
	    				break;
	    			
	    			case TYPE_RESPONSE:
	    				convertView = inflater.inflate(R.layout.answer_type_text, null);
	    				break;
	    			
	    			default:
	    				break;
	    		}
	    	}
	    	ListItem item = model.get(position);
	    	if( type == TYPE_RESPONSE ){
	    		resetLayout(convertView);
	    	}
	    	if( new String("question").equals(item.type) ){
	    		TextView q = (TextView) convertView.findViewById(R.id.ques);
	    		q.setText(item.response);
	    		RelativeLayout text = (RelativeLayout) convertView.findViewById(R.id.qtext);
	    		text.setTag(item.id);
	    		RelativeLayout audio = (RelativeLayout) convertView.findViewById(R.id.qaudio);
	    		audio.setTag(item.id);
	    		RelativeLayout media = (RelativeLayout) convertView.findViewById(R.id.qmedia);
	    		media.setTag(item.id);
	    		RelativeLayout risk = (RelativeLayout) convertView.findViewById(R.id.qrisk);
	    		risk.setTag(item.id);
	    	}
	    	else if( ( new String("answer").equals(item.type) || new String("risk").equals(item.type) ) && new String("text").equals(item.response_type) ){
	    		TextView t = (TextView) convertView.findViewById(R.id.text);
	    		t.setText(item.response);
	    		t.setVisibility(View.VISIBLE);
	    		String[] tag = new String[]{item.id,item.response,item.response_type,item.type};
	    		LinearLayout wrapper = (LinearLayout) convertView.findViewById(R.id.wrapper);
	    		if( new String("risk").equals(item.type) ){
	    			wrapper.setBackgroundColor(RISK_COLOR);
	    			t.setTextColor(RISK_TEXTCOLOR);
	    		}
	    		wrapper.setTag(tag);
	    	}
	    	else if( ( new String("answer").equals(item.type) || new String("risk").equals(item.type) ) && new String("image").equals(item.response_type) ){
	    		String response = "/nbrt/images/"+item.response;
	    		String[] tag = new String[]{item.id,response,item.response_type,item.type};
	    		LinearLayout wrapper = (LinearLayout) convertView.findViewById(R.id.wrapper);
	    		if( new String("risk").equals(item.type) ){
	    			wrapper.setBackgroundColor(RISK_COLOR);
	    		}
	    		wrapper.setTag(tag);
	    		android.widget.RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    		wrapper.setLayoutParams(params);
	    		LinearLayout con = (LinearLayout) convertView.findViewById(R.id.con);
	    		con.setPadding(5, 5, 5, 5);
	    		String path = Environment.getExternalStorageDirectory()+response;
				File imgFile = new  File(path);
				if( imgFile.exists() ){
					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
					float w = myBitmap.getWidth();
					float h = myBitmap.getHeight();
					int new_w = (int) ((w/h)*240);
			        Bitmap thumb = Bitmap.createScaledBitmap(myBitmap, new_w, 240, true);
			        ImageView img = (ImageView) convertView.findViewById(R.id.image);
			        img.setImageBitmap(thumb);
			        img.setVisibility(View.VISIBLE);
				}
	    	}
	    	else if( (new String("answer").equals(item.type) || new String("risk").equals(item.type) ) && new String("audio").equals(item.response_type) ){
	    		String[] tag = new String[]{item.id,item.response,item.response_type,item.type}        ;
	    		LinearLayout wrapper = (LinearLayout) convertView.findViewById(R.id.wrapper);
	    		if( new String("risk").equals(item.type) ){
	    			wrapper.setBackgroundColor(RISK_COLOR);
	    		}
	    		wrapper.setTag(tag);
	    		ImageView img = (ImageView) convertView.findViewById(R.id.audio);
	    		img.setVisibility(View.VISIBLE);
	    	}
	    	else if( (new String("answer").equals(item.type) || new String("risk").equals(item.type) ) && new String("video").equals(item.response_type) ){
	    		String response = "/nbrt/videos/"+item.response; 
				String[] tag = new String[]{item.id,response,item.response_type,item.type};
				LinearLayout wrapper = (LinearLayout) convertView.findViewById(R.id.wrapper);
				if( new String("risk").equals(item.type) ){
	    			wrapper.setBackgroundColor(RISK_COLOR);
	    		}
	    		wrapper.setTag(tag);
	    		ImageView img = (ImageView) convertView.findViewById(R.id.video);
	    		img.setVisibility(View.VISIBLE);
	    	}
	    	else{
	    		convertView = inflater.inflate(rowResourceId, parent, false);
	    	}
	    	
	    	return convertView;
	    }
    	
	    public void resetLayout(View convertView){
	    	TextView t = (TextView) convertView.findViewById(R.id.text);
	    	t.setText("");
	    	t.setTextColor(ANSWER_TEXTCOLOR);
	    	ImageView img = (ImageView) convertView.findViewById(R.id.image);
	    	img.setVisibility(View.GONE);
	    	img = (ImageView) convertView.findViewById(R.id.video);
	    	img.setVisibility(View.GONE);
	    	img = (ImageView) convertView.findViewById(R.id.audio);
	    	img.setVisibility(View.GONE);
	    	LinearLayout wrapper = (LinearLayout) convertView.findViewById(R.id.wrapper);
	    	@SuppressWarnings("deprecation")
			android.widget.RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
    		wrapper.setLayoutParams(params);
    		wrapper.setBackgroundResource(R.drawable.border);
    		LinearLayout con = (LinearLayout) convertView.findViewById(R.id.con);
    		con.setPadding(10, 10, 10, 10);
	    }
	    
    }
}
