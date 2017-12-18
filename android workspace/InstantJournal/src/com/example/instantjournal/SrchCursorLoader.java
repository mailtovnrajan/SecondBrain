package com.example.instantjournal;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.util.Log;

public class SrchCursorLoader extends CursorLoader {

//	private final ForceLoadContentObserver mObserver = new ForceLoadContentObserver();
	private String query = "";
	private Context context;

	private Cursor loadQuery() {
		DatabaseHelper db;
		try {
			db = new DatabaseHelper(context);
		} catch (Exception e) {
			Log.e(SrchCursorLoader.class.getName(), e.getMessage());
			return null;
		}
		if (query.length() == 0)
			return db.getAllEntries();

		return db.getEntryLike(query);
	}

	public SrchCursorLoader(Context context, String query) {
		super(context);
		this.context = context;
		this.query = query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public Cursor loadInBackground() {
		Log.i("SrchCursorLoader", "Loading data");
		Cursor c = loadQuery();
//		if (c != null) {
//			// Ensure the cursor window is filled
//			c.getCount();
//			c.registerContentObserver(mObserver);
//		}
//
//		c.setNotificationUri(getContext().getContentResolver(), getUri());
		return c;
	}

}
