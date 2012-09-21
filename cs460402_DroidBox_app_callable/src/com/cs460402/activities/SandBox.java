package com.cs460402.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.cs460402.app.async_core.AsyncConstants;
import com.cs460402.app.async_core.ProgressManager;
import com.cs460402.app.async_core.UIHandler;

public class SandBox extends Activity implements OnCancelListener {

	private Activity activity;
	private UIHandler handlerUI;
	
	private Button buttonOn;
	private Button buttonOff;
	private Button buttonIndet;
	private Button buttonIndetNoTitle;

	private ProgressDialog mProgressDialog;
	//private ProgressDialog mProgressDialog2;
    

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.sandbox);
	    
		activity = this;
	    handlerUI = new UIHandler();
        
	    buttonOn = (Button) findViewById(R.id.barOn);
	    buttonOff = (Button) findViewById(R.id.barOff);
	    buttonIndet = (Button) findViewById(R.id.showIndeterminate);
	    buttonIndetNoTitle = (Button) findViewById(R.id.showIndeterminateNoTitle);
	    
	    buttonOn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	
            	Runnable r = new ProgressManager(null, activity, AsyncConstants.PROGRESS_SHOW_BAR, 0, null);
            	handlerUI.post(r);
            }
        });
	    
	    buttonOff.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	Runnable r = new ProgressManager(null, activity, AsyncConstants.PROGRESS_CLOSE_BAR, 0, null);
            	handlerUI.post(r);
            }
        });
	    
	    buttonIndet.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	mProgressDialog = new ProgressDialog(activity);
        		
        		mProgressDialog.setTitle("My Title");
        		mProgressDialog.setMessage("Please wait, working...");		
        		mProgressDialog.setIndeterminate(true);		
        		mProgressDialog.setCancelable(true);
        		mProgressDialog.setOnCancelListener((OnCancelListener) activity);
        		
            	Runnable r = new ProgressManager(mProgressDialog, AsyncConstants.PROGRESS_SHOW_DIALOG, null);
            	handlerUI.post(r);
            	            	
            	r = new ProgressManager(mProgressDialog, AsyncConstants.PROGRESS_CLOSE_DIALOG, null);
            	handlerUI.postDelayed(r, 10000);
            	
            	r = new Runnable() {
					public void run() {
						for (int i = 1; i < 100; i++) {
							Runnable r2 = new ProgressManager(mProgressDialog, AsyncConstants.PROGRESS_UPDATE_DIALOG, "Please wait, working (" + i + ")");
							handlerUI.post(r2);
							try {	
								Thread.sleep(1000);	
							} catch (InterruptedException e) {	
								Log.e("tag", e.getMessage());	
							}
						}
					}
            	};
            	new Thread(r).start();	// this thread will still be running after above dialog closes, or is canceled
            	
            }
        });
	    
	    buttonIndetNoTitle.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	mProgressDialog = new ProgressDialog(activity);
        		
        		//mProgressDialog.setTitle("My Title");
        		mProgressDialog.setMessage("Please wait, working...");		
        		mProgressDialog.setIndeterminate(true);		
        		mProgressDialog.setCancelable(true);
        		mProgressDialog.setOnCancelListener((OnCancelListener) activity);
        		
            	Runnable r = new ProgressManager(mProgressDialog, AsyncConstants.PROGRESS_SHOW_DIALOG, null);
            	handlerUI.post(r);
            	
            	r = new ProgressManager(mProgressDialog, AsyncConstants.PROGRESS_CLOSE_DIALOG, null);
            	handlerUI.postDelayed(r, 10000);
            	
            	r = new Runnable() {
					public void run() {
						for (int i = 1; i < 100; i++) {
							Runnable r2 = new ProgressManager(mProgressDialog, AsyncConstants.PROGRESS_UPDATE_DIALOG, "Please wait, working (" + i + ")");
							handlerUI.post(r2);
							try {	
								Thread.sleep(1000);	
							} catch (InterruptedException e) {	
								Log.e("tag", e.getMessage());	
							}
						}
					}
            	};
            	new Thread(r).start();	// this thread will still be running after above dialog closes, or is canceled
            }
        });
	}


	public void onCancel(DialogInterface dialog) {
		Toast.makeText(this, "task canceled!", Toast.LENGTH_SHORT).show();
	}
	
	

}
