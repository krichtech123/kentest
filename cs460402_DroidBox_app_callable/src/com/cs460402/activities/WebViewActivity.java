package com.cs460402.activities;

import com.cs460402.app.AppConstants;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewActivity extends Activity implements OnClickListener, SensorEventListener {
	
	private NavWebView webView;
	private LinearLayout progress;
	private TextView textView;
	private ImageButton leftBtn, rightBtn;
	private Button zoomOutBtn, zoomInBtn;
	private String mode, startID, endID, bldgID, floorID;
	private SensorManager sm = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.webview);
        
	    sm = (SensorManager) getSystemService(SENSOR_SERVICE);   //create a sensor manager object
        
	    progress = (LinearLayout)findViewById(R.id.progresslayout);
	    textView = (TextView)findViewById(R.id.stepText);
	    (leftBtn = (ImageButton)findViewById(R.id.leftButton)).setOnClickListener(this);
	    (rightBtn = (ImageButton)findViewById(R.id.rightButton)).setOnClickListener(this);
	    (zoomOutBtn = (Button)findViewById(R.id.zoomOutBtn)).setOnClickListener(this);
	    (zoomInBtn = (Button)findViewById(R.id.zoomInBtn)).setOnClickListener(this);
	    
	    (webView = (NavWebView)findViewById(R.id.webView)).setTextView(textView).setProgressView(progress);
	    //webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		
	    Bundle extras = getIntent().getExtras();
	    if (extras != null)	 {
	    	mode = extras.getString("mode");
	    	AppPrefs.setMapMode(mode, this);
	    	if (mode.equals("route")) {
		    	startID = extras.getString("start");
		    	endID = extras.getString("end");
		    	String verbose = extras.getString("verbose");
		    	String soe = extras.getString("soe");
		    	
		    	if (startID != null && endID != null && startID.length() != 0 && endID.length() != 0) {
		    		webView.loadUrl(AppConstants.GLOBAL_WEB_SITE + "?mode=" + mode + "&start=" + startID + "&end=" + endID + "&" + verbose + "&" + soe);
		    	} else {
		    		webView.loadUrl(AppConstants.GLOBAL_WEB_SITE);
		    	}
	    	} else if (mode.equals("browse")) {
	    		bldgID = extras.getString("bldg");
	    		
	    		webView.loadUrl(AppConstants.GLOBAL_WEB_SITE + "?mode=" + mode + "&bldg=" + bldgID);		
	    	}
	    } else {
	    	webView.loadUrl(AppConstants.GLOBAL_WEB_SITE);
	    }
	    
	    
	}

	public void onClick(View button) {
		switch (button.getId()) {
		case R.id.leftButton:
			webView.previousStep();
			break;
		case R.id.rightButton:
			webView.nextStep();
			break;
		case R.id.zoomInBtn:
			webView.zoomView(true);
			break;
		case R.id.zoomOutBtn:
			webView.zoomView(false);
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		super.onCreateOptionsMenu(menu);
		AppOptionsMenu.buildOptionMenu(menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		return AppOptionsMenu.handleMenuSelection(item, this, startID, endID);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		int type = event.sensor.getType();
		
        if (type == Sensor.TYPE_ORIENTATION) {
        	// x == event.values[0]
        	// y == event.values[1]
        	// z == event.values[2]
        	
        	webView.rotateDot((int)event.values[0]);
        }
        if (type == Sensor.TYPE_ACCELEROMETER) {
        	// x == event.values[0]
        	// y == event.values[1]
        	// z == event.values[2]
        	
        }  
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
	
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		//Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
		if (mode.equals("route")) {
			//register listeners
	        sm.registerListener(this, 
	                sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
	                SensorManager.SENSOR_DELAY_NORMAL);
	        
	        sm.registerListener(this, 
	                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	                SensorManager.SENSOR_DELAY_NORMAL);
	        
	        sm.registerListener(this, 
	                sm.getDefaultSensor(Sensor.TYPE_TEMPERATURE),
	                SensorManager.SENSOR_DELAY_NORMAL);
		}
		
		webView.getHandler().sendMessage(webView.getHandler().obtainMessage(webView.HANDLER_UPDATE_MAP_PREFS));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onStop() {
		//Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
		if (mode.equals("route")) sm.unregisterListener(this);
        super.onStop();
	}

}
