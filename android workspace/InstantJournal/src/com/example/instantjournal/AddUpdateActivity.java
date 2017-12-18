package com.example.instantjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddUpdateActivity extends ActionBarActivity {
	private Long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_update);
		// get intent
		Intent intent = getIntent();
		String idStr = intent.getStringExtra(DatabaseHelper.KEY_ID);
		if (idStr != null)
			id = Long.parseLong(idStr);
		else
			idStr = null;
		String timestamp = intent.getStringExtra(DatabaseHelper.KEY_TIMESTAMP);
		if (timestamp == null)
			timestamp = Util.getCurrentTimestamp();
		String key = intent.getStringExtra(DatabaseHelper.KEY_KEY);
		String value = intent.getStringExtra(DatabaseHelper.KEY_VALUE);
		EditText et = (EditText) findViewById(R.id.etTimestamp);
		et.setText(timestamp);
		et = (EditText) findViewById(R.id.editTextKey);
		et.setText(key);
		et = (EditText) findViewById(R.id.editTextValue);
		et.setText(value);

		// as per the sender act accordingly
		Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
		Button btnDelete = (Button) findViewById(R.id.btnDelete);
		Button btnAdd = (Button) findViewById(R.id.btnDetailAdd);
		if (id == null) {
			// from the main launcher activity
			btnUpdate.setEnabled(false);
			btnDelete.setEnabled(false);
			btnAdd.setEnabled(true);
		} else {
			// from list view search list activity
			btnUpdate.setEnabled(true);
			btnDelete.setEnabled(true);
			btnAdd.setEnabled(false);
		}

	}

	public void btnAddResponse(View v) {
		TextView tv = (TextView) findViewById(R.id.etTimestamp);
		String timestamp = tv.getText().toString();
		tv = (TextView) findViewById(R.id.editTextKey);
		String key = tv.getText().toString();
		tv = (TextView) findViewById(R.id.editTextValue);
		String value = tv.getText().toString();
		try {
			DatabaseHelper db = new DatabaseHelper(getApplicationContext());
			id = db.createJournalEntry(timestamp, key, value);
			Button btnAdd = (Button) findViewById(R.id.btnDetailAdd);
			btnAdd.setEnabled(false);
			Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT)
					.show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Error writing to database", Toast.LENGTH_SHORT).show();
		}
	}

	public void btnUpdateResponse(View v) {
		TextView tv = (TextView) findViewById(R.id.etTimestamp);
		String timestamp = tv.getText().toString();
		tv = (TextView) findViewById(R.id.editTextKey);
		String key = tv.getText().toString();
		tv = (TextView) findViewById(R.id.editTextValue);
		String value = tv.getText().toString();
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		if (db.updateEntry(new JnlEntry(id, timestamp, key, value)) > 0)
			Toast.makeText(getApplicationContext(), "Updated",
					Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getApplicationContext(), "Error updating",
					Toast.LENGTH_SHORT).show();
	}

	public void btnDeleteResponse(View v) {

		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		if (db.deleteEntry(id) > 0) {
			Toast.makeText(getApplicationContext(), "Deleted",
					Toast.LENGTH_SHORT).show();
			finish();
		} else
			Toast.makeText(getApplicationContext(), "Error Deleting",
					Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_update, menu);
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
}
