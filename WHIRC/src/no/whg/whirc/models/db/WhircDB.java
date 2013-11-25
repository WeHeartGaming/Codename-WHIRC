package no.whg.whirc.models.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WhircDB extends SQLiteOpenHelper {
	
	/**
	 *  The table data for the WHIRC_TABLE
	 */
	public static final String WHIRC_TABLE = "whircTable";
	public static final String COLUMNID = "id";
	public static final String COLUMN_CHANNELNAME = "channelName";
	public static final String COLUMN_CHANNELSERVER = "channelServer";
	public static final String COLUMN_CHANNELPASS = "channelPass";
	public static final String COLUMN_SERVER = "serverName";
	public static final String COLUMN_HOSTNAME = "hostName";
	public static final String COLUMN_PORT = "port";
	
	/**
	 * 
	 *  The table data for the USER_TABLE
	 */
	public static final String USER_TABLE = "userTable";
	public static final String USER_COLUMNID = "id";
	public static final String COLUMN_USERONE = "userNameOne";
	public static final String COLUMN_USERTWO = "userNaWmeTwo";
	public static final String COLUMN_USERTHREE = "userNameThree";
	public static final String COLUMN_NAME = "userRealName";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_USERPASS = "userPass";
	
	private static final String DATABASE_NAME = "whirc.db";
	private static final int DATABASE_VERSION = 1;
	
	
	private static final String  DATABASE_CREATE = "Create table " 
			+ WHIRC_TABLE + "("
			+ COLUMNID + " integer primary key autoincrement, " 
			+ COLUMN_CHANNELNAME + " text not null, "
			+ COLUMN_CHANNELSERVER + " text not null, "
			+ COLUMN_SERVER + " text not null, "
			+ COLUMN_CHANNELPASS + " text, "
			+ COLUMN_HOSTNAME + "text not null, "
			+ COLUMN_PORT + "integer" + ");";
			
	private static final String CREATE_USERTABLE = "Create table "
			+ USER_TABLE + "(" 
			+ USER_COLUMNID + " integer primary key autoincrement, "
			+ COLUMN_USERONE + " text not null, " 
			+ COLUMN_USERTWO + " text, " 
			+ COLUMN_USERTHREE + " text not null, " 
			+ COLUMN_NAME + " text, " 
			+ COLUMN_ADDRESS + " text, "
			+ COLUMN_USERPASS + " text);";
	
	/**
	 * Constructor for the database
	 * @param context
	 */
	public WhircDB(Context context)
	{
		super(context, DATABASE_NAME , null, DATABASE_VERSION);
	}
	
	
	/**
	 * 	function run on creation
	 *  running sql-statements for
	 *  creating tables
	 *  
	 *  @param database
	 */
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
		database.execSQL(CREATE_USERTABLE);
	}
	
	/**
	 *  function run when the database is upgraded
	 *  
	 *  @param db
	 *  @param oldVersion
	 *  @param newVersion
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(WhircDB.class.getName(), "Upgreading database from version " + oldVersion + " to "
				+ newVersion + " , this will destroy old data");
		db.execSQL("DROP TABLE IF EXISTS " + WHIRC_TABLE);
		onCreate(db);
	}

}