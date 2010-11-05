package com.project.weather.guide;

import com.project.weather.guide.services.WeatherGuideService;
import com.project.weather.guide.services.WeatherGuideServiceMonitor;
import com.project.weather.guide.shared.GPSCoords;
import com.project.weather.guide.shared.IConstants;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class WeatherGuide extends Activity implements IConstants {
	private static final int CLOSE_ID = Menu.FIRST+1;
	private static final int ABOUT_US = Menu.FIRST+2;
	private WeatherGuideServiceMonitor service = null;
	BroadcastReceiver forcastReceiver;
	private WebView browser;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		doBindService();

		// Register a broad cast receiver
		forcastReceiver = new ForecastReceiver();
		registerReceiver(forcastReceiver, new IntentFilter(WEATHER_UPDATE));
		
		setup();
	}
	
	private void setup() {
		browser=(WebView)findViewById(R.id.webkit);
	}

	@Override
	protected void onPause() {
		super.onPause();
		cancleNotification();
	}
	
	protected void onResume() {
		super.onResume();
		cancleNotification();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(forcastReceiver);
		doUnbindService();
	}

	private void doBindService() {
		bindService(new Intent(this, WeatherGuideService.class), onService,
				BIND_AUTO_CREATE);
	}

	private void doUnbindService() {
		unbindService(onService);
	}

	private ServiceConnection onService = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = (WeatherGuideServiceMonitor) binder;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null;
		}
	};

	private void cancleNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}

	class ForecastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context c, Intent i) {
			Bundle extras = i.getExtras();
			if (extras != null) {
				String toLoad = extras.getString(PAGE);
				browser.loadDataWithBaseURL(null, toLoad, "text/html","UTF-8", null);
/*				GPSCoords loc = (GPSCoords) extras
						.getSerializable(GPS_CORDINATES);*/
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, CLOSE_ID, Menu.NONE, "Close")
				.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		menu.add(Menu.NONE, ABOUT_US, Menu.NONE, "About")
		.setIcon(android.R.drawable.ic_menu_help);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case CLOSE_ID:
				finish();
				return(true);
		}
		return(super.onOptionsItemSelected(item));
	}
}