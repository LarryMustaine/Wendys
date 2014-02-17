package tools;

import com.larrystudio.downloadedsection.CopyFile;
import com.larrystudio.downloadedsection.DeleteFile;
import com.larrystudio.downloadedsection.SetRingtone;
import com.larrystudio.wendys.R;

import extras.ContactPicker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class DialogSetSoundAs{

	private String LocalPath_Sound;
	private String SoundName;
	private Dialog CustomDialog;
	private Context context;
	private RelativeLayout layoutSetAsRingtone;
	private RelativeLayout layoutSetAsNotification;
	private RelativeLayout layoutSetAsAlarm;
	private RelativeLayout layoutSetAsContact;
	private RelativeLayout layoutCopyFile;
	private RelativeLayout layoutDeleteFile;
	private ContactPicker contactPicker;
	
	public DialogSetSoundAs(Context context, String LocalPath_Sound, String SoundName, Object referenceObject) {
		this.context = context;
		this.LocalPath_Sound = LocalPath_Sound;
		this.SoundName = SoundName;
		this.contactPicker = (ContactPicker) referenceObject;
	}
	
	public void ShowCustomDialog(){
		CustomDialog = new Dialog(context);
		CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		CustomDialog.setCancelable(true);
		CustomDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		
		CustomDialog.setContentView(R.layout.dialog_set_sound); 
		CustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		CustomDialog.setCanceledOnTouchOutside(true);
		
		layoutSetAsRingtone     = (RelativeLayout) CustomDialog.findViewById(R.id.layoutSetAsRingtone);
		layoutSetAsNotification = (RelativeLayout) CustomDialog.findViewById(R.id.layoutSetAsNotification);
		layoutSetAsAlarm        = (RelativeLayout) CustomDialog.findViewById(R.id.layoutSetAsAlarm);
		layoutSetAsContact      = (RelativeLayout) CustomDialog.findViewById(R.id.layoutSetAsContact);
		layoutCopyFile			= (RelativeLayout) CustomDialog.findViewById(R.id.layoutCopyFile);
		layoutDeleteFile		= (RelativeLayout) CustomDialog.findViewById(R.id.layoutDeleteFile);

		CustomDialog.show();
		setClickListeners();
	}
	
	private void setClickListeners() {
		layoutSetAsRingtone.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	CustomDialog.dismiss();
		    	setSoundAs("Ringtone");
		    }
		});
		
		layoutSetAsNotification.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	CustomDialog.dismiss();
		    	setSoundAs("Notification");
		    }
		});
		
		layoutSetAsAlarm.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	CustomDialog.dismiss();
		    	setSoundAs("Alarm");
		    }
		});
		
		layoutSetAsContact.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	CustomDialog.dismiss();
		    	contactPicker.LaunchContactPicker(LocalPath_Sound);
		    }
		});
		
		layoutCopyFile.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	CustomDialog.dismiss();
		    	new CopyFile(context, LocalPath_Sound, SoundName).CopyFileToDestination();
		    }
		});
		
		layoutDeleteFile.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	CustomDialog.dismiss();
		    	new DeleteFile(context, LocalPath_Sound).DeleteFileFromSandbox();
		    }
		});
	}
	
	private void setSoundAs(String Type){
		new SetRingtone(context, LocalPath_Sound, SoundName, Type).SetSoundAsRingtone();
	}
}