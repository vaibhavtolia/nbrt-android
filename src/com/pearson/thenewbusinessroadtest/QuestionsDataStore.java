package com.pearson.thenewbusinessroadtest;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class QuestionsDataStore {
	private SQLiteDatabase database;
	private DatabaseHandler dbHandler;
	
	public QuestionsDataStore(Context context) {
		dbHandler = new DatabaseHandler(context);
		database = dbHandler.getWritableDatabase();
	}
	
	public void open() throws SQLException {
	    database = dbHandler.getWritableDatabase();
	}

	public void close() {
		dbHandler.close();
	}
	
	public void insertUser(String name){
		String query = "INSERT INTO users (name,profile) VALUES ("+name+",0)";
		database.execSQL(query);
	}
	
	public List<String[]> getUser(String name){
		List<String[]> user = new ArrayList<String[]>();
		String query = "SELECT * from users WHERE name = "+name;
		Cursor cursor = database.rawQuery(query,null);
		cursor.moveToFirst();
		String[] u = new String[]{ cursor.getString(0),cursor.getString(1) };
		user.add(u);
		return user;
	}
	
	public void insertQuestion(Integer chapter_id,String question){
		String query = "INSERT INTO questions (chapter_id,question) VALUES ("+chapter_id+","+question+")";
		database.execSQL(query);
	}
	
	public String getQuestion(String qid){
		String query = "SELECT question from questions WHERE id = "+qid;
		Cursor cursor = database.rawQuery(query,null);
		cursor.moveToFirst();
		return cursor.getString(cursor.getColumnIndex("question"));
	}
	
	public void insertAnswer(String idea_id,String question_id, String response_type, String response){
		String query = "INSERT INTO answers (idea_id,question_id,response_type,response) VALUES ("+idea_id+","+question_id+",'"+response_type+"', ?)";
		SQLiteStatement stmt = database.compileStatement(query);
		stmt.bindString(1, response);
		stmt.execute();
	}
	
	public void insertRisk(String idea_id,String question_id, String response_type, String response){
		String query = "INSERT INTO risks (idea_id,question_id,response_type,response) VALUES ("+idea_id+","+question_id+",'"+response_type+"', ?)";
		SQLiteStatement stmt = database.compileStatement(query);
		stmt.bindString(1, response);
		stmt.execute();
	}
	
	public void insertJudgement(String idea_id,String chapter_id, String response){
		String query = "INSERT INTO judgements (idea_id,chapter_id,judgement) VALUES ("+idea_id+","+chapter_id+", ?)";
		SQLiteStatement stmt = database.compileStatement(query);
		stmt.bindString(1, response);
		stmt.execute();
	}
	
	public void updateAnswer(String id,String response){
		String query = "UPDATE answers SET response = ? WHERE id = "+id;
		SQLiteStatement stmt = database.compileStatement(query);
		stmt.bindString(1, response);
		stmt.execute();
	}
	
	public void updateRisk(String id,String response){
		String query = "UPDATE risks SET response = ? WHERE id = "+id;
		SQLiteStatement stmt = database.compileStatement(query);
		stmt.bindString(1, response);
		stmt.execute();
	}
	
	public void updateJudgement(String id,String response){
		String query = "UPDATE judgements SET response = ? WHERE id = "+id;
		SQLiteStatement stmt = database.compileStatement(query);
		stmt.bindString(1, response);
		stmt.execute();
	}
	
	public void insertIdea(Integer user_id, String idea_name,String problem, String segment, String solution, String industry){
		String query = "INSERT INTO ideas (user_id, name, problem, segment, solution, industry) VALUES (?,?,?,?,?,?)";
		SQLiteStatement stmt = database.compileStatement(query);
		String[] bindArgs = new String[]{user_id.toString(),idea_name,problem,segment,solution,industry };
		stmt.bindAllArgsAsStrings(bindArgs);
		stmt.execute();
	}
	
	public Integer getIdeasCount(){
		List<String[]> ideas = new ArrayList<String[]>();
		String query = "SELECT id,name,active FROM ideas";
		Cursor cursor = database.rawQuery(query,null);
	    Integer count = cursor.getCount();
	    return count;
	}
	
	public List<String[]> getIdeas(){
		List<String[]> ideas = new ArrayList<String[]>();
		String query = "SELECT id,name,active FROM ideas";
		Cursor cursor = database.rawQuery(query,null);
	    Integer count = cursor.getCount();
	    if(count > 0){
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	String[] answer = new String[]{ cursor.getString(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("active")) };
		    	ideas.add(answer);
		    	cursor.moveToNext();
		    }
		    cursor.close();
	    }
	    return ideas;
	}
	
	public List<String[]> getIdea(String idea_id){
		List<String[]> idea = new ArrayList<String[]>();
		String query = "SELECT * FROM ideas WHERE id = "+idea_id;
		Cursor cursor = database.rawQuery(query,null);
	    Integer count = cursor.getCount();
	    if(count > 0){
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	String[] answer = new String[]{ cursor.getString(cursor.getColumnIndex("name")),cursor.getString(cursor.getColumnIndex("problem")),cursor.getString(cursor.getColumnIndex("segment")), cursor.getString(cursor.getColumnIndex("solution")), cursor.getString(cursor.getColumnIndex("industry")) };
		    	idea.add(answer);
		    	cursor.moveToNext();
		    }
		    cursor.close();
	    }
		return idea;
	}
	
	public void insertChapter(String name, String summary){
		String query = "INSERT INTO chapters (name,summary) VALUES ("+name+","+summary+")";
		database.execSQL(query);
	}
	
	public void updateChapterRating(String idea_id, Integer chapter_id, String rating){
		String query = "SELECT rating from rating whERE idea_id = "+idea_id+" AND chapter_id = "+chapter_id;
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.getCount() > 0){
			query = "UPDATE rating SET rating = "+rating+" WHERE idea_id = "+idea_id+" AND chapter_id = "+chapter_id;
			database.execSQL(query);
		}
		else{
			query = "INSERT INTO rating (idea_id,chapter_id,rating) VALUES ("+idea_id+","+chapter_id+","+rating+")";
			database.execSQL(query);
		}
	}
	
	public List<String[]> getIdeawiseRatings(String idea_id){
		List<String[]> ratings = new ArrayList<String[]>();
		String query = "SELECT chapter_id,rating from rating WHERE idea_id = "+idea_id;
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String[] rating = new String[]{ cursor.getString(cursor.getColumnIndex("chapter_id")), cursor.getString(cursor.getColumnIndex("rating")) };
				ratings.add(rating);
				cursor.moveToNext();
			}
		}
		return ratings;
	}
	
	public String getChapterRating(Integer idea_id, Integer chapter_id){
		String rating;
		String query = "SELECT rating from rating whERE idea_id = "+idea_id+" AND chapter_id = "+chapter_id;
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			rating = cursor.getString(cursor.getColumnIndex("rating"));
		}
		else{
			rating = "null";
		}
		return rating;
	}
	
//	public void updateAnswer(Integer idea_id,Integer question_id, String response){
//		String query = "UPDATE answers SET response = "+response+" WHERE idea_id = "+idea_id+" AND question_id = "+question_id;
//		database.execSQL(query);
//	}
//	
	public ArrayList<ContentValues> getAllQuestions(Integer chapter_id) {
		ArrayList<ContentValues> questions = new ArrayList<ContentValues>();
		String query = "SELECT id,question FROM questions WHERE chapter_id = "+chapter_id;
	    Cursor cursor = database.rawQuery(query,null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	ContentValues map = new ContentValues();
	    	DatabaseUtils.cursorRowToContentValues(cursor, map);
	    	questions.add(map);
	    	cursor.moveToNext();
	    }
	    cursor.close();
	    return questions;
	}
	
	public List<String[]> getAllAnswers(String idea_id, String question_id){
		List<String[]> answers = new ArrayList<String[]>();
		String query = "SELECT id, response, response_type FROM answers WHERE idea_id = "+idea_id+" AND question_id = "+question_id;
	    Cursor cursor = database.rawQuery(query,null);
	    Integer count = cursor.getCount();
	    if(count > 0){
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	String[] answer = new String[]{ cursor.getString(cursor.getColumnIndex("response")),cursor.getString(cursor.getColumnIndex("response_type")),cursor.getString(cursor.getColumnIndex("id")) };
		    	answers.add(answer);
		    	cursor.moveToNext();
		    }
		    cursor.close();
	    }
	    return answers;
	}
	
	public List<String[]> getAllRisks(String idea_id, String question_id){
		List<String[]> answers = new ArrayList<String[]>();
		String query = "SELECT id, response, response_type FROM risks WHERE idea_id = "+idea_id+" AND question_id = "+question_id;
	    Cursor cursor = database.rawQuery(query,null);
	    Integer count = cursor.getCount();
	    if(count > 0){
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	String[] answer = new String[]{ cursor.getString(cursor.getColumnIndex("response")),cursor.getString(cursor.getColumnIndex("response_type")),cursor.getString(cursor.getColumnIndex("id")) };
		    	answers.add(answer);
		    	cursor.moveToNext();
		    }
		    cursor.close();
	    }
	    return answers;
	}
	
	public List<String[]> getAllJudgements(String idea_id, String chapter_id){
		List<String[]> judgements = new ArrayList<String[]>();
		String query = "SELECT id, judgement FROM judgements WHERE idea_id = "+idea_id+" AND chapter_id = "+chapter_id;
	    Cursor cursor = database.rawQuery(query,null);
	    Integer count = cursor.getCount();
	    if(count > 0){
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	String[] answer = new String[]{ cursor.getString(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("judgement")) };
		    	judgements.add(answer);
		    	cursor.moveToNext();
		    }
		    cursor.close();
	    }
	    return judgements;
	}
	
	public List<String> getChapterData(Integer chapter_id){
		List<String> chapter_data = new ArrayList<String>();
		String query = "SELECT name, summary FROM chapters WHERE id = "+chapter_id;
		Cursor cursor = database.rawQuery(query,null);
	    cursor.moveToFirst();
	    chapter_data.add(cursor.getString(cursor.getColumnIndex("name")));
	    chapter_data.add(cursor.getString(cursor.getColumnIndex("summary")));
		return chapter_data;
	}
	
	
}
