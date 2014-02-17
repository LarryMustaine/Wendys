package com.larrystudio.wendys;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class PasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.password_activity);
	}

	@Override
	public void onBackPressed() {
		finish();
		PasswordActivity.this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
}