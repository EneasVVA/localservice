package com.example.events;

import android.net.Uri;
import android.provider.BaseColumns;

public interface Constants extends BaseColumns{
	public static final String TABLE_NAME = "events" ;
	// Columns in the Events database
	public static final String TIME = "time" ;
	public static final String TITLE = "title" ;
	public static final String AUTHORITY = "org.example.events";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE_NAME);

}
