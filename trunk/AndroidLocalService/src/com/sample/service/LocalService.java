package com.sample.service;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class LocalService extends Service {
	private Timer localServiceTimer = new Timer();
	private static long UPDATE_INTERVAL = 60000;
	private static long DELAY_INTERVAL = 0;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(LocalService.this, "My Local Service started",
				Toast.LENGTH_LONG).show();
		startLocalService();
	}

	private void startLocalService() {
		localServiceTimer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				try {
					// do your service work
					Thread.sleep(UPDATE_INTERVAL);
				} catch (InterruptedException ie) {
					// You can catch it and show appropriate message
				}
			}
		}, DELAY_INTERVAL, UPDATE_INTERVAL);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		endLocalService();
		Toast.makeText(LocalService.this, "My Local Service ended",
				Toast.LENGTH_LONG).show();
	}

	private void endLocalService() {
		if (localServiceTimer != null)
			localServiceTimer.cancel();
	}

}
