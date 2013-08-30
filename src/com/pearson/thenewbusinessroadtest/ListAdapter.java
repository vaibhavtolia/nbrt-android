package com.pearson.thenewbusinessroadtest;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<String> {
	
	private final Context context;
    private final ArrayList<String[]> Ids;
    private final int rowResourceId;

	public ListAdapter(Context context, int textViewResourceId, ArrayList<String[]> data) {

        super(context, textViewResourceId);

        this.context = context;
        this.Ids = data;
        this.rowResourceId = textViewResourceId;

    }
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(rowResourceId, parent, false);
		Log.v("count","a");
//		View rowView = null;
//		if( new String("question").equals(Ids.get(position)[0]) ){
//			rowView = inflater.inflate(R.layout.question_template, parent, false);
//			TextView question = (TextView) rowView.findViewById(R.id.ques);
//			question.setText(Ids.get(position)[2]);
//		}
//		else if( new String("answer").equals(Ids.get(position)[0]) && new String("text").equals(Ids.get(position)[2]) ){
//			rowView = inflater.inflate(R.layout.answer_type_text, parent, false);
//			TextView ans = (TextView) rowView.findViewById(R.id.text);
//			ans.setText(Ids.get(position)[3]);
//		}
//		else if( new String("answer").equals(Ids.get(position)[0]) && new String("audio").equals(Ids.get(position)[2]) ){
//			rowView = inflater.inflate(R.layout.answer_type_audio, parent, false);
//		}
//		else if( new String("answer").equals(Ids.get(position)[0]) && new String("image").equals(Ids.get(position)[2]) ){
//			rowView = inflater.inflate(R.layout.answer_type_media, parent, false);
//		}
//		else if( new String("risk").equals(Ids.get(position)[0]) && new String("text").equals(Ids.get(position)[2]) ){
//			rowView = inflater.inflate(R.layout.risk_type_text, parent, false);
//		}
//		else if( new String("risk").equals(Ids.get(position)[0]) && new String("image").equals(Ids.get(position)[2]) ){
//			rowView = inflater.inflate(R.layout.risk_type_media, parent, false);
//		}
//		else if( new String("risk").equals(Ids.get(position)[0]) && new String("audio").equals(Ids.get(position)[2]) ){
//			rowView = inflater.inflate(R.layout.risk_type_audio, parent, false);
//		}
//		//View rowView = inflater.inflate(, parent, false);
		
		return rowView;
	}
}
