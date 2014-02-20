package com.larrystudio.blondie;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import com.larrystudio.blondie.R;

public class PasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_password);
		
		final EditText edtPassword = (EditText) findViewById(R.id.edit_password);
		
		findViewById(R.id.button_enter).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(edtPassword.getText().toString().equals(PasswordActivity.this.getString(R.string.wendys_password)))
					setCloseAndAnim(LoginActivity.PASSWORD_RESULT_OK);
				else
					edtPassword.setError(PasswordActivity.this.getString(R.string.password_wrong_password));
			}
		});
	}

	@Override
	public void onBackPressed() {
		setCloseAndAnim(LoginActivity.PASSWORD_RESULT_FAIL);
	}

	private void setCloseAndAnim(int CODE_RESULT) {
		setResult(CODE_RESULT);
		finish();
		PasswordActivity.this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
}