package com.project.weather.guide.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.project.weather.guide.WeatherGuide;
import com.project.weather.guide.shared.IConstants;
import com.project.weather.guide.shared.Utils;
import com.project.weather.guide.shared.WeatherInfo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class WeatherGuideService extends Service implements IConstants {
	private final Binder binder = new LocalBinder();
	Intent i = new Intent(WEATHER_UPDATE);
	static LocationManager locManager;
	WeatherDataFetcher fetcher;

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "WeatherGuide Service started");
		super.onCreate();
		locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,
				10000.0f, listener);
		initSetup();
	}

	private void initSetup() {
		if (locManager != null) {
			GPSCoords loc = calculateCurrentCoordinates();
			updateSetup(loc.mLat, loc.mLon);
		}
	}

	private void updateSetup(double latitude, double longitude) {
		if (latitude != 0 && longitude != 0) {
			String url = getFormattedString(getParams(latitude, longitude));
			if (checkTaskStatus()) {
				fetcher = new WeatherDataFetcher();
				fetcher.execute(url);
			}
		}
	}

	private String getFormattedString(Map<String, String> params) {
		StringBuilder builder = new StringBuilder();
		builder
				.append("http://www.weather.gov/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php?");
		Set<Entry<String, String>> entries = params.entrySet();
		Iterator itr = entries.iterator();
		while (itr.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) itr
					.next();
			builder.append(entry.getKey());
			builder.append("=" + entry.getValue());
		}
		return builder.toString();
		/*
		 * String url =
		 * "http://www.weather.gov/forecasts/xml/sample_products/browser_interface/ndfdXMLclient.php?lat="
		 * + latitude + "&lon=" + longitude +
		 * "&product=time-series&begin=2004-01-01T00:00:00&end=2013-04-20T00:00:00&maxt=maxt&mint=mint;"
		 * ;
		 */
	}

	private Map<String, String> getParams(double latitude, double longitude) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("lat", Double.toString(latitude));
		params.put("&lon", Double.toString(longitude));
		params.put("&product", "time-series");
		params.put("&begin", ""); // glance
		params.put("&end", ""); // glance
		params.put("&temp", "temp");
		params.put("&icons", "icons");
		// params.put("&maxt", "maxt");
		// params.put("&mint", "mint");
		// params.put("&sky", "sky");
		return params;
	}

	// Helper method to check task status
	private Boolean checkTaskStatus() {
		Boolean result = false;
		// make sure we don't collide with another pending location update
		if (fetcher == null || fetcher.getStatus() == AsyncTask.Status.FINISHED
				|| fetcher.isCancelled()) {
			result = true;
		} else {
			Log.w(TAG, "Warning: upload task already going");
		}
		return result;
	}

	// Helper method to cancle task status
	private void cancleTask() {
		if (fetcher != null && fetcher.getStatus() != AsyncTask.Status.FINISHED) {
			fetcher.cancel(true);
			fetcher = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		cancleTask();
		Log.d(TAG, "WeatherGuide Service ended");
	}

	public class LocalBinder extends Binder implements
			WeatherGuideServiceMonitor {

	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		CharSequence contentTitle = "Weather Guide";
		CharSequence contentText = "Forecast Update";

		final NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification note = new Notification(
				android.R.drawable.ic_menu_mylocation, "New Weather update!",
				System.currentTimeMillis());
		long[] vibrate = { 100, 100, 200, 300 };
		note.vibrate = vibrate;
		note.defaults = Notification.DEFAULT_ALL;
		Intent i = new Intent(this, WeatherGuide.class);

		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		note.setLatestEventInfo(this, contentTitle, contentText, pi);
		mgr.notify(NOTIFICATION_ID, note);
	}

	public class WeatherDataFetcher extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			if (url != null) {
				return downloadData(url);
			} else
				return null;
		}

		private String downloadData(String url) {
			final HttpClient client = new DefaultHttpClient();
			final HttpGet getRequest = new HttpGet(url);

			try {
				HttpResponse response = client.execute(getRequest);
				final int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					Log.w(TAG, "Error " + statusCode
							+ " Error downloading data " + url);
					return null;
				}
				final HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = null;
					try {
						inputStream = entity.getContent();
						return Utils.read(inputStream);
					} finally {
						if (inputStream != null) {
							inputStream.close();
						}
						entity.consumeContent();
					}
				}
			} catch (IOException e) {
				getRequest.abort();
				cancleTask();
				Log.w(TAG, "error while retrieving data from " + url, e);
			} catch (IllegalStateException e) {
				getRequest.abort();
				cancleTask();
				Log.w(TAG, "Incorrect URL: " + url);
			} catch (Exception e) {
				getRequest.abort();
				cancleTask();
				Log.w(TAG, "Error while retrieving from " + url, e);
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				try {
					List<WeatherInfo> data = Utils.parseData(result);
					String res = Utils.generatePage(data);
					sendResult(res);
				} catch (Exception e) {
					Log.d("TAG", "Invalid data ,errr while parsing");
				}
			} else {
				Log.d(TAG, "error in downloading information");
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
	}

	public void sendResult(String result) {
		showNotification();
		i.putExtra(PAGE, result);
		sendBroadcast(i);
	}

	LocationListener listener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			updateSetup(location.getLatitude(), location.getLongitude());
		}
	};

	/**
	 * Attempt to get the last known location of the device. Usually this is the
	 * last value that a location provider set
	 */
	private static GPSCoords calculateCurrentCoordinates() {
		GPSCoords mPlaceCoords = null;
		double lat = 0, lon = 0;
		try {
			Criteria criteria = new Criteria();
			String best = locManager.getBestProvider(criteria, true);
			Location recentLoc = locManager.getLastKnownLocation(best);
			lat = (double) recentLoc.getLatitude();
			lon = (double) recentLoc.getLongitude();
			mPlaceCoords = new GPSCoords(lat, lon);
		} catch (Exception e) {
			Log.e(TAG, "Location failed", e);
		}
		return mPlaceCoords;
	}

	public static class GPSCoords {
		public double mLat, mLon;

		GPSCoords(double lat, double lon) {
			mLat = lat;
			mLon = lon;
		}
	}

}