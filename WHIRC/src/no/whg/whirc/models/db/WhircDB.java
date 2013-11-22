package no.whg.whirc.models.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WhircDB extends SQLiteOpenHelper {
	
	public static final String WHIRC_TABLE = "whircTable";
	public static final String COLUMNID = "id";
	public static final String COLUMN_TEST = "test";
	
	private static final String DATABASE_NAME = "whirc.db";
	private static final int DATABASE_VERSION = 1;
	
	
	private static final String  DATABASE_CREATE = "Create table" 
			+ WHIRC_TABLE + "(" + COLUMNID
			+ " integer primary key autoincrement, " + COLUMN_TEST
			+ " text not null);";
	
	public WhircDB(Context context)
	{
		super(context, DATABASE_NAME , null, DATABASE_VERSION);
	}
	
	
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(WhircDB.class.getName(), "Upgreading database from version " + oldVersion + " to "
				+ newVersion + " , this will destroy old data");
		db.execSQL("DROP TABLE IF EXISTS " + WHIRC_TABLE);
		onCreate(db);
	}

}
