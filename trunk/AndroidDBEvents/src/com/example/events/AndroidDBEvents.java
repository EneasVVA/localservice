package com.example.events;

import static com.example.events.Constants.TABLE_NAME;
import static com.example.events.Constants.TIME;
import static com.example.events.Constants.TITLE;
import static android.provider.BaseColumns._ID;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class AndroidDBEvents extends ListActivity {
	private Events events;
	private static String[] FROM = { _ID, TIME, TITLE, };
	private static String ORDER_BY = TIME + " DESC";
	private static int[] TO = { R.id.rowid, R.id.time, R.id.title, };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		events = new Events(this);
		try {
			addEvent("Hello, Android!");
			Cursor cursor = getEvents();
			showEvents(cursor);
		} finally {
			events.close();
		}
	}

	private Cursor getEvents() {
		// Perform a managed query. The Activity will handle closing
		// and re-querying the cursor when needed.
		SQLiteDatabase db = events.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null,
				ORDER_BY);
		
		startManagingCursor(cursor);
		return cursor;
	}

	private void addEvent(String string) {
		// Insert a new record into the Events data source.
		// You would do something similar for delete and update.
		SQLiteDatabase db = events.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TIME, System.currentTimeMillis());
		values.put(TITLE, string);
		db.insertOrThrow(TABLE_NAME, null, values);
	}

	private void showEvents(Cursor cursor) {
		// Set up data binding
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.item, cursor, FROM, TO);
		setListAdapter(adapter);
	}
}