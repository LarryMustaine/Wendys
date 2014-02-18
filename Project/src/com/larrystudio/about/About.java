package com.larrystudio.about;

import tools.SendEmail;

import com.larrystudio.wendys.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

public class About extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_about);
		
		findViewById(R.id.txtContact).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SendEmail(About.this);
			}
		});
	}
}