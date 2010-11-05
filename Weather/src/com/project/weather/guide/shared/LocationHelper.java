package com.project.weather.guide.shared;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationHelper implements IConstants {
	private LocationManager manager;
	
	public LocationHelper(LocationManager mgr){
		this.manager = mgr;
	}
	
	public GPSCoords getCurrentLocation(){
		return calculateCurrentCoordinates();
	}

	/**
	 * Attempt to get the last known location of the device. Usually this is the
	 * last value that a location provider set
	 */
	private GPSCoords calculateCurrentCoordinates() {
		GPSCoords mPlaceCoords = null;
		double lat = 0, lon = 0;
		try {
			Criteria criteria = new Criteria();
			String best = manager.getBestProvider(criteria, true);
			Location recentLoc = manager.getLastKnownLocation(best);
			lat = (double) recentLoc.getLatitude();
			lon = (double) recentLoc.getLongitude();
			mPlaceCoords = new GPSCoords(lat, lon);
		} catch (Exception e) {
			Log.e(TAG, "Location failed", e);
		}
		return mPlaceCoords;
	}
}
