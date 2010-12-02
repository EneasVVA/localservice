package com.sample.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

public class RemoteService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
	    return new IRemoteServiceAPI.Stub() {
	    	/**
	    	 * This is implementation of API which we defined in AIDL file
	    	 */
			public String printHello(String name) throws RemoteException {
				return "Wassup, " + name;
				
			}
		};
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(RemoteService.this, "My Local Service started",
				Toast.LENGTH_LONG).show();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(RemoteService.this, "My Local Service ended",
				Toast.LENGTH_LONG).show();
	}
}
