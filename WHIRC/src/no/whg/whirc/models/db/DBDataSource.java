package no.whg.whirc.models.db;

import android.content.Context;
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
	
	// TODO: Create methods for all DB fields
}
