package com.project.weather.guide.shared;

import java.io.Serializable;

public class GPSCoords implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9089625887874273630L;
	private double mLat, mLon;

	GPSCoords(double lat, double lon) {
		mLat = lat;
		mLon = lon;
	}

	public double getmLat() {
		return mLat;
	}

	public double getmLon() {
		return mLon;
	}
}