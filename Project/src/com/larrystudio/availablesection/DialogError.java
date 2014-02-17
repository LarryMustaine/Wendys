package com.larrystudio.availablesection;

import com.larrystudio.wendys.R;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class DialogError{

	private Dialog CustomDialog;
	private Context context;
	private Button btnOK;
	private SoundDownloadingCallback soundDownloadingCallback;

	public DialogError(Context context, SoundDownloadingCallback soundDownloadingCallback) {
		this.context = context;
		this.soundDownloadingCallback = soundDownloadingCallback;
	}
	
	public void ShowCustomDialog(){
		CustomDialog = new Dialog(context);
		CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		CustomDialog.setCancelable(true);
		CustomDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		
		CustomDialog.setContentView(R.layout.dialog_error); 
		CustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		CustomDialog.setCanceledOnTouchOutside(true);
		
		btnOK = (Button) CustomDialog.findViewById(R.id.btnOK);
		
		CustomDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dismissDownloadingDialog();
			}
		});
		
		CustomDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				dismissDownloadingDialog();
			}
		});
		
		setClickListeners();
		CustomDialog.show();
	}
	
	private void setClickListeners() {
		btnOK.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	dismissDownloadingDialog();
		    }
		});
	}
	
	protected void dismissDownloadingDialog() {
		CustomDialog.dismiss();
		soundDownloadingCallback.dismissDialog();
	}
}