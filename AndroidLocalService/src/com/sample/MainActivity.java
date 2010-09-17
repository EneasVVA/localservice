package com.sample;

import com.sample.service.LocalService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUp();
    }

	private void setUp() {
		//Creating a intent to launch a LocalService 
        final Intent local_service = new Intent(this, LocalService.class);
        //Setup on click listeners
        Button btn_start_service = (Button) findViewById(R.id.Btn_start);
        Button btn_end_service = (Button) findViewById(R.id.Btn_end);

        // Start the service when user clicks on start 
        btn_start_service.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
            	startService(local_service);
            }
        });
        
        // Stop the service when user clicks on end 
        btn_end_service.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
            	stopService(local_service);
            }
        });
		
	}
}