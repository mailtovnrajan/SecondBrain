package com.example.instantjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	// ******** button response methods *************//
//	Thread thread = new Thread(new Runnable() {
//		@Override
//		public void run() {
//			quickAdd();
//		}
//	});

	public void quickAdd() {
		EditText et = (EditText) findViewById(R.id.etKey);
		String value = et.getText().toString().trim();
		if (value.length() == 0)
			return;
		// find if key is present
		String delimiter = getString(R.string.keyvalue_delimiter);
		String values[] = value.split(delimiter);
		String key;
		if (values.length > 1) {
			key = values[0].trim();
			if (key.charAt(0) != '/')
				key = "/" + key;
			value = values[1].trim();
		} else {
			key = getString(R.string.default_key);
		}
		// if no key is present then insert as value

		String timestamp = Util.getCurrentTimestamp();
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		try {
			db.createJournalEntry(timestamp, key, value);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Error writing to database", Toast.LENGTH_SHORT).show();
		}
	}

	public void btnQuickAddResponse(View v) {
		// add the value to database with default key
		quickAdd();
		EditText et = (EditText) findViewById(R.id.etKey);
		et.setText("");
	}

	public void btnAddResponse(View v) {
		// get query message from Editext
		EditText et = (EditText) findViewById(R.id.etKey);
		String query = et.getText().toString().trim();
		// create intent and start another activity
		Intent intent = new Intent(v.getContext(), AddUpdateActivity.class);
		intent.putExtra(DatabaseHelper.KEY_KEY, query);
		intent.putExtra(DatabaseHelper.KEY_VALUE, query);
		startActivity(intent);
	}

	public void btnSearchResponse(View v) {
		// get query message from Editext
		EditText et = (EditText) findViewById(R.id.etKey);
		String query = et.getText().toString().trim();
		// create intent and start another activity
		Intent intent = new Intent(v.getContext(), SearchListActivity.class);
		intent.putExtra(getString(R.string.extraQuery), query);
		startActivity(intent);
	}

}
