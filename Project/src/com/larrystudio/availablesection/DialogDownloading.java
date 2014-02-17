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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DialogDownloading implements SoundDownloadingCallback{

	private String URL_Sounds;
	private Dialog CustomDialog;
	private Context context;
	private Button btnCancel;
	private ProgressBar pgrLoading;
	private TextView txtProgress;
	private DownloadSounds downloadingSounds;
	private AvailableSounds availableSounds;
	private ImageView imgPlaySound;
	private ListAdapter listAdapter;

	public DialogDownloading(Context context, String URL_Sounds, AvailableSounds availableSounds, ImageView imgPlaySound, ListAdapter listAdapter) {
		this.context = context;
		this.URL_Sounds = URL_Sounds;
		this.availableSounds = availableSounds;
		this.imgPlaySound = imgPlaySound;
		this.listAdapter = listAdapter;
	}
	
	public void ShowCustomDialog(){
		CustomDialog = new Dialog(context);
		CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		CustomDialog.setCancelable(true);
		CustomDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		
		CustomDialog.setContentView(R.layout.dialog_downloading); 
		CustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		CustomDialog.setCanceledOnTouchOutside(false);

		btnCancel   = (Button) CustomDialog.findViewById(R.id.btnCancel);
		pgrLoading  = (ProgressBar) CustomDialog.findViewById(R.id.prgPercentage);
		txtProgress = (TextView) CustomDialog.findViewById(R.id.txtPercentage);
		
		CustomDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				cancelDownloadingTask();
			}
		});
		
		CustomDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				cancelDownloadingTask();
			}
		});
		
		setClickListeners();
		CustomDialog.show();
		startDownloadingTask();
	}

	
	protected void cancelDownloadingTask() {
		if(downloadingSounds != null)
			downloadingSounds.cancel(true);
	}

	private void startDownloadingTask() {
		downloadingSounds = new DownloadSounds(context);
		downloadingSounds.setProgressBarAndText(pgrLoading, txtProgress, this);
		downloadingSounds.execute(URL_Sounds);
	}

	private void setClickListeners() {
		btnCancel.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	dismissDialog();
		    	cancelDownloadingTask();
		    }
		});
	}

	@Override
	public void onDownloadingTaskFinished(boolean downloadedComplete) {
		if(downloadedComplete){
			dismissDialog();
			listAdapter.updateArray();
			availableSounds.updatePlayButtonIconVisibility(imgPlaySound);
		} else {
			new DialogError(context, this).ShowCustomDialog();
		}
	}

	@Override
	public void dismissDialog() {
		if(CustomDialog != null)
			CustomDialog.dismiss();
	}
}