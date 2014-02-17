package com.larrystudio.downloadedsection;

import tools.DialogSetSoundAs;
import android.content.Context;

public class ClickDownloadedSounds{

	private String LocalPath_Sound;
	private String SoundName;
	private Context context;
	private Object referenceObject;
	
	public ClickDownloadedSounds(Context context, String LocalPath_Sound, String SoundName, Object referenceObject) {
		this.context = context;
		this.LocalPath_Sound = LocalPath_Sound;
		this.SoundName = SoundName;
		this.referenceObject = referenceObject;
	}
	
	public void doClick(){
		showDialog();
	}

	private void showDialog() {
			new DialogSetSoundAs(context, LocalPath_Sound, SoundName, referenceObject).ShowCustomDialog();
	}
}