package com.larrystudio.blondie;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import com.larrystudio.blondie.R;

public class LoginActivity extends Activity {

	private int PASSWORD_REQUEST_CODE = 0;
	public static int PASSWORD_RESULT_OK = 200;
	public static int PASSWORD_RESULT_FAIL = 300;
	private ScheduledExecutorService worker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		
		findViewById(R.id.image_face).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(LoginActivity.this, PasswordActivity.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				LoginActivity.this.startActivityForResult(myIntent, PASSWORD_REQUEST_CODE);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == PASSWORD_REQUEST_CODE && resultCode == PASSWORD_RESULT_OK){
			delayTask();
		}
	}
	
	private void delayTask() {
	  Runnable task = new Runnable() {
	    public void run() {
	    	Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			LoginActivity.this.startActivity(myIntent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			finish();
	    }
	  };
	  worker = Executors.newSingleThreadScheduledExecutor();
	  worker.schedule(task, 1, TimeUnit.SECONDS);
	}
}