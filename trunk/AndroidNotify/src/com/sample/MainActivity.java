package com.sample;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private NotificationManager mNotificationManager;
	private int SIMPLE_NOTFICATION_ID;
	final Notification notifyDetails = new Notification(R.drawable.arrow,
			"Alert Message", System.currentTimeMillis());

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setUpNotification();
	
		Button notifyMe = (Button) findViewById(R.id.btn_notify);
		Button clearNotification = (Button) findViewById(R.id.btn_notify_clear);

		notifyMe.setOnClickListener(listener);
		clearNotification.setOnClickListener(listener);

	}

	/** Helper function to set style of notification. */
	private void setUpNotification() {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		long[] vibrate = { 100, 100, 200, 300 };
		notifyDetails.vibrate = vibrate;
		notifyDetails.defaults = Notification.DEFAULT_ALL;
	}

	/** Helper to handle button clicks */
	private View.OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_notify:
				Context context = getApplicationContext();
				CharSequence contentTitle = "You have new message";
				CharSequence contentText = "Check new message";

				Intent notifyIntent = new Intent(context, MainActivity.class);

				PendingIntent intent = PendingIntent.getActivity(
						MainActivity.this, 0, notifyIntent,
						android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

				notifyDetails.setLatestEventInfo(context, contentTitle,
						contentText, intent);

				mNotificationManager.notify(SIMPLE_NOTFICATION_ID,
						notifyDetails);

				break;
			case R.id.btn_notify_clear:
				mNotificationManager.cancel(SIMPLE_NOTFICATION_ID);
				break;

			}
		}
	};
}
