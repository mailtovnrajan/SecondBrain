package com.example.instantjournal;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SearchListActivity extends ActionBarActivity implements
		LoaderCallbacks<Cursor> {
	private static final int LOADER_ID = 1;

	private SimpleCursorAdapter mAdapter;

	private String query = "";
	
	private LoaderManager.LoaderCallbacks<Cursor> mCallbacks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_list);

		String[] dataColumns = { DatabaseHelper.KEY_ID,
				DatabaseHelper.KEY_TIMESTAMP, DatabaseHelper.KEY_KEY,
				DatabaseHelper.KEY_VALUE };
		int[] viewIDs = { R.id.TextViewId, R.id.TextViewTimestamp,
				R.id.textViewKey, R.id.textViewValue };

		mAdapter = new SimpleCursorAdapter(this, R.layout.row_layout, null,
				dataColumns, viewIDs, 0);
		// Associate the (now empty) adapter with the ListView.
		ListView searchList = (ListView) findViewById(R.id.listViewSearch);
		searchList.setAdapter(mAdapter);
		searchList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				// create intent and start another activity
				Intent intent = new Intent(listView.getContext(),
						AddUpdateActivity.class);
				intent.putExtra(DatabaseHelper.KEY_ID, cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.KEY_ID)));
				intent.putExtra(DatabaseHelper.KEY_TIMESTAMP, cursor
						.getString(cursor
								.getColumnIndex(DatabaseHelper.KEY_TIMESTAMP)));
				intent.putExtra(DatabaseHelper.KEY_KEY, cursor.getString(cursor
						.getColumnIndex(DatabaseHelper.KEY_KEY)));
				intent.putExtra(DatabaseHelper.KEY_VALUE, cursor
						.getString(cursor
								.getColumnIndex(DatabaseHelper.KEY_VALUE)));
				startActivity(intent);

			}
		});
		// get query
		Intent intent = getIntent();
		query = intent.getStringExtra(getString(R.string.extraQuery)).trim();
		// // init loader manager
		mCallbacks = this;
		getLoaderManager().initLoader(LOADER_ID, null, mCallbacks);
	}

	
	@Override
	protected void onRestart() {
		super.onRestart();
		getLoaderManager().restartLoader(LOADER_ID, null, mCallbacks);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// final Uri CONTENT_URI =
		// Uri.parse(DatabaseHelper.TABLE_JNLENTRY);
		// return new CursorLoader(getApplicationContext(), CONTENT_URI, null,
		// null, null, null);
		return new SrchCursorLoader(getApplicationContext(), query);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

		switch (loader.getId()) {
		case LOADER_ID:
			// The asynchronous load is complete and the data
			// is now available for use. Only now can we associate
			// the queried Cursor with the SimpleCursorAdapter.
			mAdapter.swapCursor(cursor);
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// For whatever reason, the Loader's data is now unavailable.
		// Remove any references to the old data by replacing it with
		// a null Cursor.
		mAdapter.swapCursor(null);
	}
}
