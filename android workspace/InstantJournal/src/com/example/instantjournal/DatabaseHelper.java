package com.example.instantjournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	// Logcat tag
	private static final String CLASS_NAME = DatabaseHelper.class.getName();

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ijdb";

	// Table Names
	public static final String TABLE_JNLENTRY = "jnlentry";

	// Common column names
	public static final String KEY_ID = "_id";
	public static final String KEY_TIMESTAMP = "timestamp";
	public static final String KEY_KEY = "key";
	public static final String KEY_VALUE = "value";

	// Table Create Statements
	// Todo table create statement
	private static final String CREATE_TABLE_JNLENTRY = "CREATE TABLE "
			+ TABLE_JNLENTRY + "(" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TIMESTAMP + " TEXT,"
			+ KEY_KEY + " TEXT," + KEY_VALUE + " TEXT" + ")";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_JNLENTRY);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_JNLENTRY);
		// create new tables
		onCreate(db);
	}

	// ------------------------ "jnlentry" table methods ----------------//

	/**
	 * Creating jnlentry
	 */
	public long createJournalEntry(String timestamp, String key, String value)
			throws SQLiteException {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TIMESTAMP, timestamp);
		values.put(KEY_KEY, key);
		values.put(KEY_VALUE, value);

		long id = db.insert(TABLE_JNLENTRY, null, values);
		Log.i(CLASS_NAME, "Inserted row " + timestamp + " : " + key + " = "
				+ value + " with id:" + id);
		return id;
	}

	/**
	 * get single journal entry by key
	 */
	public Cursor getEntryByCol(String col, String value) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_JNLENTRY + " WHERE "
				+ col + " = '" + value+"'";

		Log.i(CLASS_NAME, selectQuery);

		return db.rawQuery(selectQuery, null);
	}

	/**
	 * get all entries desc ordered by timestamp
	 */
	public Cursor getAllEntries() {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_JNLENTRY + " ORDER BY "
				+ KEY_TIMESTAMP + " DESC";

		Log.i(CLASS_NAME, selectQuery);

		return db.rawQuery(selectQuery, null);
	}

	/**
	 * search entries using like
	 */
	public Cursor getEntryLike(String value){
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_JNLENTRY + " WHERE "
				+ KEY_KEY + " LIKE '%" + value + "%' OR " + KEY_VALUE + " LIKE '%"
				+ value + "%'";

		Log.i(CLASS_NAME, selectQuery);

		return db.rawQuery(selectQuery, null);

	}

	// /**
	// * get single journal entry by key
	// */
	// public List<JnlEntry> getEntryByCol(String col, String value) {
	// SQLiteDatabase db = this.getReadableDatabase();
	//
	// String selectQuery = "SELECT  * FROM " + TABLE_JNLENTRY + " WHERE "
	// + col + " = " + value;
	//
	// Log.e(CLASS_NAME, selectQuery);
	//
	// Cursor c = db.rawQuery(selectQuery, null);
	// List<JnlEntry> list = new ArrayList<JnlEntry>();
	// JnlEntry jentry;
	// if (c != null && c.moveToFirst()) {
	// do {
	// jentry = new JnlEntry(c.getString(c
	// .getColumnIndex(KEY_TIMESTAMP)), c.getString(c
	// .getColumnIndex(KEY_KEY)), c.getString(c
	// .getColumnIndex(KEY_VALUE)));
	// list.add(jentry);
	// } while (c.moveToNext());
	// }
	// return list;
	// }

	// /**
	// * get single journal entry by key
	// */
	// public List<JnlEntry> getEntryLike(String value) {
	// SQLiteDatabase db = this.getReadableDatabase();
	//
	// String selectQuery = "SELECT  * FROM " + TABLE_JNLENTRY + " WHERE "
	// + KEY_KEY + " Like " + value + " OR " + KEY_VALUE + " Like "
	// + value;
	//
	// Log.e(CLASS_NAME, selectQuery);
	//
	// Cursor c = db.rawQuery(selectQuery, null);
	// List<JnlEntry> list = new ArrayList<JnlEntry>();
	// JnlEntry jentry;
	// if (c != null && c.moveToFirst()) {
	// do {
	// jentry = new JnlEntry(c.getString(c
	// .getColumnIndex(KEY_TIMESTAMP)), c.getString(c
	// .getColumnIndex(KEY_KEY)), c.getString(c
	// .getColumnIndex(KEY_VALUE)));
	// list.add(jentry);
	// } while (c.moveToNext());
	// }
	// return list;
	// }

	/**
	 * Updating a todo
	 */
	public int updateEntry(JnlEntry entry) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TIMESTAMP, entry.getTimestamp());
		values.put(KEY_KEY, entry.getKey());
		values.put(KEY_VALUE, entry.getValue());

		// updating row
		return db.update(TABLE_JNLENTRY, values, KEY_ID + " = ?",
				new String[] { String.valueOf(entry.getId()) });
	}

	/**
	 * Deleting a todo
	 */
	public int deleteEntry(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(TABLE_JNLENTRY, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}
}
