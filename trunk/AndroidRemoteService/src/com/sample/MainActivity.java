package com.sample;

import com.sample.service.IRemoteServiceAPI;
import com.sample.service.RemoteService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	IRemoteServiceAPI api;
	connection remote_service ;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		doBindService();
		setUp();
	}

	private void setUp() {
		// Setup on click listeners
		Button btn_start_service = (Button) findViewById(R.id.Btn_start);
		Button btn_end_service = (Button) findViewById(R.id.Btn_end);
		final TextView text = (TextView)findViewById(R.id.TextView01);

		// Start the service when user clicks on start
		btn_start_service.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				try {
					String msg = api.printHello("Welcome Android User");
					text.setText(msg);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Stop the service when user clicks on end
		btn_end_service.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				doUnBindService();
			}
		});
	}

	/**
	 * This method bind the remote service and creates connection
	 */
	private void doBindService() {
		// Creating a intent to launch a LocalService
		final Intent remote_service_intent = new Intent(this, RemoteService.class);
		remote_service = new connection();
		boolean ret = bindService(remote_service_intent, remote_service, Context.BIND_AUTO_CREATE);
		Log.d("REMOTE SERVICE", "Binding service" + ret);
	}

	/**
	 * This method unbinds the remote service and release connection
	 */
	private void doUnBindService() {
		unbindService(remote_service);
		remote_service = null;
		Log.d("REMOTE SERVICE", "Unbinding ervice");
	}

	class connection implements ServiceConnection{
		public void onServiceDisconnected(ComponentName name) {
			Log.d("REMOTE SERVICE", "onServiceConnected() disconnected");
			api = null;
		}

		public void onServiceConnected(ComponentName name, IBinder boundService) {
			api = IRemoteServiceAPI.Stub.asInterface((IBinder) boundService);
			Log.d("REMOTE SERVICE", "onServiceConnected() connected");
		}
	};
}