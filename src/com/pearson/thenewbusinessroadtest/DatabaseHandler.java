package com.pearson.thenewbusinessroadtest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "newbusinessroadtest";
	
	DatabaseHandler(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
    public void onCreate(SQLiteDatabase database) {
		Log.v("dbhandler","oncreate called");
		
		String query = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL)";
		database.execSQL(query);
		Log.v("dbhandler","create table success");
		
		query = "CREATE TABLE questions (id INTEGER PRIMARY KEY AUTOINCREMENT ,chapter_id INTEGER NOT NULL ,question TEXT NOT NULL)";
		database.execSQL(query);
		Log.v("dbhandler","create table success");
		
		query = "CREATE TABLE answers (id INTEGER PRIMARY KEY AUTOINCREMENT ,idea_id INTEGER NOT NULL ,question_id INTEGER NOT NULL ,response_type TEXT NOT NULL ,response TEXT NOT NULL)";
		database.execSQL(query);
		Log.v("dbhandler","create table success");
		
		query = "CREATE TABLE ideas (id INTEGER PRIMARY KEY AUTOINCREMENT,user_id INTEGER NOT NULL ,name TEXT NOT NULL, problem TEXT NOT NULL, segment TEXT NOT NULL, solution TEXT NOT NULL, industry TEXT NOT NULL, active INTEGER NOT NULL DEFAULT 0 )";
		database.execSQL(query);
		Log.v("dbhandler","create table success");
		
		query = "CREATE TABLE chapters (id INTEGER PRIMARY KEY AUTOINCREMENT ,name INTEGER NOT NULL ,summary TEXT NOT NULL)";
		database.execSQL(query);
		Log.v("dbhandler","create table success");
		
		query = "CREATE TABLE rating (idea_id INTEGER NOT NULL ,chapter_id INTEGER NOT NULL ,rating INTEGER NOT NULL)";
		database.execSQL(query);
		Log.v("dbhandler","create table success");
		
		query = "CREATE TABLE risks (id INTEGER PRIMARY KEY AUTOINCREMENT ,idea_id INTEGER NOT NULL ,question_id INTEGER NOT NULL ,response_type TEXT NOT NULL ,response TEXT NOT NULL)";
		database.execSQL(query);
		Log.v("dbhandler","create table risks success");
		
		query = "CREATE TABLE judgements (id INTEGER PRIMARY KEY AUTOINCREMENT, idea_id INTEGER NOT NULL, chapter_id INTEGER NOT NULL, judgement TEXT NOT NULL)";
		database.execSQL(query);
		Log.v("dbhandler","create table judgements success");
		
		// inserting first chapter and its questions
		query = "INSERT INTO chapters (name,summary) VALUES ('Micro-Market test', 'In Chapter 2, you�ll read the stories of three product introductions that got their micro-market understanding just right: Japan�s iMode, which shrewdly figured out some of the surprising things Japanese customers wanted from their mobile phones; Miller Lite, which kicked off the long-running light beer craze in the USA way back in 1975; and Nike, today�s runaway leader in athletic footwear and apparel. Even better, you�ll learn from the mistakes of OurBeginning.com, which wasted $5 million on ill-advised Super Bowl advertising, a mistake we hope you won�t make! All topped off with six pages of �What investors want to know� and lessons learned. ')";
		database.execSQL(query);
		Log.v("dbhandler","insert chapter success");
		query = "SELECT id FROM chapters WHERE name = 'Micro-Market test'";
		Cursor cursor = database.rawQuery(query,null);
		cursor.moveToFirst();
		Integer id = cursor.getInt(cursor.getColumnIndex("id"));
		
		query = "INSERT INTO questions (chapter_id,question) VALUES ("+id+",'Who, precisely, are the customers who have the pain or will be given delight?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "INSERT INTO questions (chapter_id,question) VALUES ("+id+",'What differentiated benefits does your offering provide ?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "INSERT INTO questions (chapter_id,question) VALUES ("+id+",'What evidence do you have that customers will buy?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "INSERT INTO questions (chapter_id,question) VALUES ("+id+",'Does your target market have the potential to grow? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "INSERT INTO questions (chapter_id,question) VALUES ("+id+",'What other segments could benefit?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "INSERT INTO questions (chapter_id,question) VALUES ("+id+",'Can you develop capabilities that are transferable?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		cursor.close();
		
		
		//inserting next chapter and its questions.
		query = "INSERT INTO chapters (name,summary) VALUES ('Macro-Market test', 'Chapter 3 tells the remarkable stories of three iconic companies that rode compelling market waves: India�s Hero Honda, which came out of nowhere, riding India�s demographic boom, to become one of the world�s largest motorcycle makers; Whole Foods, whose meteoric rise has been fuelled by trends toward organic and natural foods; and EMC, the high-tech data storage provider that has remade itself time after time in response to technological change.  Sadly, you�ll also read the story of Thinking Machines, whose early demise might have been preventable. Plus five pages of investor insights and lessons learned. ')";
		database.execSQL(query);
		Log.v("dbhandler","insert chapter success");
		query = "SELECT id FROM chapters WHERE name = 'Macro-Market test' ";
		cursor = database.rawQuery(query,null);
		cursor.moveToFirst();
		id = cursor.getInt(cursor.getColumnIndex("id"));
		query = "INSERT INTO questions (chapter_id,question) VALUES ("+id+",'What sort of business do you want: huge or lifestyle?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'How large is the market? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'How fast has it grown? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'How quickly will it grow? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'What economic trends can you identify? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'What demographic trends can you identify? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'What sociocultural trends can you identify? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'What technological trends can you identify? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'What regulatory trends can you identify? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'What natural trends can you identify? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		cursor.close();
		
		
		//inserting next chapter and its questions.
		query = "INSERT INTO chapters (name,summary) VALUES ('Macro-Industry test', 'Chapter 4 is all about tools for understanding your industry, which, if you are fortunate, may be as attractive as �big pharma� was for many years, though its attractiveness is eroding now, as its story indicates. Or you may be less fortunate, as were the entrepreneurs who pioneered the American digital subscriber line industry (DSL), who lost most of the battles � as well as the war � to the big telcos, who ate their lunch. Choosing an industry where your chances of survival are better than in DSL is worth considering, as these stories and four pages of lessons and investor perspectives will show.')";
		database.execSQL(query);
		Log.v("dbhandler","insert chapter success");
		query = "SELECT id FROM chapters WHERE name = 'Macro-Industry test' ";
		cursor = database.rawQuery(query,null);
		cursor.moveToFirst();
		id = cursor.getInt(cursor.getColumnIndex("id"));
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'What industry will you competein?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Is it easy or difficult for companies to enter? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Do suppliers have the power to set terms and conditions? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Do buyers have the power to set terms and conditions? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Is it easy or difficult for substitute products to steal your market? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Is competitive rivalry intense or genteel? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		cursor.close();
		
		
		//inserting next chapter and its questions.
		query = "INSERT INTO chapters (name,summary) VALUES ('Micro-Industry test', 'Getting into business without knowing you can achieve both competitive and economic sustainability is probably a trap. Just ask those who bought shares in online game-maker Zynga or daily deals merchantGrouponat their IPO�s!Chapter 5 addresses competitive sustainability through the stories of the British anti-ulcer drug Zantac, Finland�s Nokia, and CAT-scan inventor EMI through their rise (Zantac), and rise and fall (Nokia and EMI). The economic sustainability of the business models that underlie eBay�s online success, along with that of British grocery retailer Tesco, and WebVan�s spectacular online groceryfailure, plusfour pages of lessons and investor insights, will give you tools you�ll need to critically examine your own business model. ')";
		database.execSQL(query);
		Log.v("dbhandler","insert chapter success");
		query = "SELECT id FROM chapters WHERE name = 'Micro-Industry test' ";
		cursor = database.rawQuery(query,null);
		cursor.moveToFirst();
		id = cursor.getInt(cursor.getColumnIndex("id"));
		cursor.close();
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Do you possess proprietary elements? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Superior organizational processes, capabilities or resources? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Will your revenue be adequate? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'How much will it cost to acquire & retain customers?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'How long will it take to attract customers?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Will your contribution margins be adequate? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'How much cash must be tiedup in working capital? for howlong?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'How quickly will customers pay? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'How slowly will suppliers and employees be paid? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		
		
		//inserting next chapter and its questions.
		query = "INSERT INTO chapters (name,summary) VALUES ('Mission Test', 'The story of Howard Schultzs entrepreneurial dream and his journey in building Starbucks into the worlds leading coffee bar chain provide the backdrop for Chapter 6s examination of the critical issues in understanding your entrepreneurial dream, and what investors will want to know about that dream. Six pages of investor perspectives and lessons bring the learning home. ')";
		database.execSQL(query);
		Log.v("dbhandler","insert chapter success");
		query = "SELECT id FROM chapters WHERE name = 'Mission Test' ";
		cursor = database.rawQuery(query,null);
		cursor.moveToFirst();
		id = cursor.getInt(cursor.getColumnIndex("id"));
		cursor.close();
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Whats your entrepreneurial mission?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'What level of aspirations do you have?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'What sorts of risk are you and are you not willing to take?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		
		
		
		//inserting next chapter and its questions.
		query = "INSERT INTO chapters (name,summary) VALUES ('Ability to Execute Test', 'Planning and iterating are wonderful, but your ability to execute on your industrys critical success factors (CSFs) will be crucial, as the stories of early PDA innovator Palm and the classic American bicycle maker Schwinn, which bit the dust in 1992, attest. Palms story and its top management teams ability to execute for many years offer powerful and positive lessons for any tech start-up. Schwinns sad tale showswhat not to do. Understanding your industrys CSFs, and building a team that can execute on them, is an oft-overlooked piece of the entrepreneurial puzzle. Chapter 7s stories and four pages of investor insights and lessons learned bring this key issue to life.')";
		database.execSQL(query);
		Log.v("dbhandler","insert chapter success");
		query = "SELECT id FROM chapters WHERE name = 'Ability to Execute Test' ";
		cursor = database.rawQuery(query,null);
		cursor.moveToFirst();
		id = cursor.getInt(cursor.getColumnIndex("id"));
		cursor.close();
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'What are the few critical success factors in your industry? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Can you demonstrate that your team taken together can execute? Evidence?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'On which CSFs is yourteam not well prepared?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		
		
		
		//inserting next chapter and its questions.
		query = "INSERT INTO chapters (name,summary) VALUES ('Connectedness Test', 'For the UK�s Virata and for mini-computer pioneer Digital Equipment Corporation (DEC), connections were crucial, as you�ll read in Chapter 8. Virata�s connections led from its early struggles to resounding success, while DEC�s lack of connections caused it to miss the PC revolution, with disastrous consequences. Their stories, along with three pages of �What investors want to know� and lessons learned, complete the book�s journey around the seven domains. The remaining chapters 9-17 deliver the hands-on, how-to tools into your toolkit to enable you to put a seven domains analysis of your opportunity into practice! ')";
		database.execSQL(query);
		Log.v("dbhandler","insert chapter success");
		query = "SELECT id FROM chapters WHERE name = 'Connectedness Test' ";
		cursor = database.rawQuery(query,null);
		cursor.moveToFirst();
		id = cursor.getInt(cursor.getColumnIndex("id"));
		cursor.close();
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Who do you and your team know up the value chain : suppliers?')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "insert into questions (chapter_id,question) VALUES ("+id+",'Who do you and your team know down the value chain : distributors and customers')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
		query = "INSERT INTO questions (chapter_id,question) VALUES ("+id+",'Who do you and your team know across the value chain : competitors and substitutes')";
		database.execSQL(query);
		Log.v("dbhandler","create question success");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
}
