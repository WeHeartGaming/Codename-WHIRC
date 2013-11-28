package no.whg.whirc.helpers;

import java.util.LinkedList;
import java.util.List;

import no.whg.whirc.models.Server;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WhircDB extends SQLiteOpenHelper {
	
	/**
	 *  The table data for the WHIRC_TABLE
	 */
	public static final String WHIRC_TABLE = "whircTable";
	public static final String COLUMNID = "id";
	public static final String COLUMN_NICKONE = "nickOne";
	public static final String COLUMN_NICKTWO = "nickTwo";
	public static final String COLUMN_NICKTHREE = "nickthree";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_HOST = "host";
	public static final String COLUMN_PORT = "port";
	public static final String COLUMN_SIMPLENAME = "simpleName";

	private static final String DATABASE_NAME = "whirc.db";
	private static final int DATABASE_VERSION = 1;
	
	private String[] whircAllColumns = { COLUMNID, COLUMN_NICKONE, COLUMN_NICKTWO, 
			COLUMN_NICKTHREE, COLUMN_NAME, COLUMN_HOST, COLUMN_PORT, COLUMN_SIMPLENAME };
	
	private static final String  DATABASE_CREATE = "create table if not exists " 
			+ WHIRC_TABLE + "("
			+ COLUMNID + " integer primary key autoincrement, " 
			+ COLUMN_NICKONE + " text not null, "
			+ COLUMN_NICKTWO + " text not null, "
			+ COLUMN_NICKTHREE + " text not null, "
			+ COLUMN_NAME + " text, "
			+ COLUMN_HOST + " text not null, "
			+ COLUMN_PORT + " integer, " 
			+ COLUMN_SIMPLENAME + " text not null);";
	
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

	/**
	 * Adds a server to the database
	 * 
	 * @param nickOne Main nickname on the server
	 * @param nickTwo Alternate nickname on the server
	 * @param nickThree Second alternate
	 * @param name Real name (optional)
	 * @param host Hostname of the server
	 * @param port Server port
	 * @param simpleName The servers simple name
	 */
	public void addServer(String nickOne, String nickTwo, String nickThree, String name,
			String host, String port, String simpleName) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(WhircDB.COLUMN_NICKONE, nickOne);
		values.put(WhircDB.COLUMN_NICKTWO, nickTwo);
		values.put(WhircDB.COLUMN_NICKTHREE, nickThree);
		values.put(WhircDB.COLUMN_NAME, name);
		values.put(WhircDB.COLUMN_HOST, host);
		values.put(WhircDB.COLUMN_PORT, port);
		values.put(WhircDB.COLUMN_SIMPLENAME, simpleName);
		
		db.insert(WhircDB.WHIRC_TABLE, null, values);
		
		db.close();
	}
	
	/**
	 * Returns all servers stored in the database
	 * 
	 * @return LinkedList containing all servers in DB
	 */
	public List<Server> getAllServers() {	
		List<Server> servers = new LinkedList<Server>();
		
		String query = "SELECT * FROM " + WHIRC_TABLE;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		
		Server server = null;
		if (cursor.moveToFirst()) {
			do {
				server = new Server();
				server.setNickOne(cursor.getString(1));
				server.setNickTwo(cursor.getString(2));
				server.setNickThree(cursor.getString(3));
				server.setName(cursor.getString(4));
				server.setHost(cursor.getString(5));
				server.setPort(cursor.getString(6));
				server.setSimpleName(cursor.getString(7));
				
				servers.add(server);
			} while (cursor.moveToNext());
		}
		
		return servers;
	}
	
	/**
	 * Returns a Server object that matches the given simpleName field
	 * 
	 * @param simpleName The server you want returned
	 * @return Server object
	 */
	public Server getServer(String simpleName) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(WHIRC_TABLE,
				whircAllColumns,
	            " simpleName = ?",
	            new String[] { simpleName },
	            null,
	            null,
	            null,
	            null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		// Assemble the new server object from cursor
		Server server = new Server();
		server.setNickOne(cursor.getString(1));
		server.setNickTwo(cursor.getString(2));
		server.setNickThree(cursor.getString(3));
		server.setName(cursor.getString(4));
		server.setHost(cursor.getString(5));
		server.setPort(cursor.getString(6));
		server.setSimpleName(cursor.getString(7));
		
		return server;
	}
}
