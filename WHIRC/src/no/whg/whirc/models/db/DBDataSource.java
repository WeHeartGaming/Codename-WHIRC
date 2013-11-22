package no.whg.whirc.models.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBDataSource {
	private SQLiteDatabase database;
	private WhircDB dbHelper;
	
	private String[] whircAllColumns = { WhircDB.COLUMNID, WhircDB.COLUMN_CHANNELNAME, 
			WhircDB.COLUMN_CHANNELSERVER, WhircDB.COLUMN_CHANNELPASS, WhircDB.COLUMN_SERVER };
	
	private String[] userAllColumns = { WhircDB.USER_COLUMNID, WhircDB.COLUMN_USERONE,
			WhircDB.COLUMN_USERTWO, WhircDB.COLUMN_USERTHREE, WhircDB.COLUMN_NAME,
			WhircDB.COLUMN_ADDRESS, WhircDB.COLUMN_USERPASS };
	
	public DBDataSource(Context context) {
		dbHelper = new WhircDB(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	//////////////////
	// SERVER METHODS
	//////////////////
	
	/**
	 * Adds a server to the DB
	 * 
	 * @param server
	 * @return Returns the added object
	 */
	public Server addServer(String server) {
		ContentValues values = new ContentValues();
		values.put(WhircDB.COLUMN_SERVER, server);
		
		long insertId = database.insert(WhircDB.WHIRC_TABLE, null, values);
		
		Cursor cursor = database.query(WhircDB.WHIRC_TABLE, 
				whircAllColumns, WhircDB.COLUMNID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		
		Server newServer = cursorToServer(cursor);
		cursor.close();
		return newServer;
	}

	/**
	 * 
	 * @param cursor
	 * @return
	 */
	private Server cursorToServer(Cursor cursor) {
		Server server = new Server();
		
		server.setId(cursor.getLong(0));
		server.setServer(cursor.getString(1));
		
		return server;
	}
	
	//////////////////
	// USER METHODS
	//////////////////
	/**
	 * 
	 * @param nickOne
	 * @param nickTwo
	 * @param nickThree
	 * @param name
	 * @param address
	 * @param password
	 * @return
	 */
	public User addUser(String nickOne, String nickTwo, String nickThree, String name, String address, String password) {
		ContentValues values = new ContentValues();
		values.put(WhircDB.COLUMN_USERONE, nickOne);
		values.put(WhircDB.COLUMN_USERTWO, nickTwo);
		values.put(WhircDB.COLUMN_USERTHREE, nickThree);
		values.put(WhircDB.COLUMN_NAME, name);
		values.put(WhircDB.COLUMN_ADDRESS, address);
		values.put(WhircDB.COLUMN_USERPASS, password);
		
		long insertId = database.insert(WhircDB.USER_TABLE, null, values);
		
		Cursor cursor = database.query(WhircDB.USER_TABLE,
				userAllColumns, WhircDB.USER_COLUMNID + " = " + insertId, null, null, null, null);
		
		User newUser = cursorToUser(cursor);
		cursor.close();
		return newUser;
	}
	/**
	 * 
	 * @param cursor
	 * @return
	 */
	private User cursorToUser(Cursor cursor) {
		User user = new User();
		
		user.setId(cursor.getLong(0));
		user.setNickOne(cursor.getString(1));
		user.setNickTwo(cursor.getString(2));
		user.setNickThree(cursor.getString(3));
		user.setName(cursor.getString(4));
		user.setAddress(cursor.getString(5));
		user.setPassword(cursor.getString(6));
		
		return user;
	}
}
