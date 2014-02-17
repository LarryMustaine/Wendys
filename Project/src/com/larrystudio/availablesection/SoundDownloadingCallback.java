package com.larrystudio.availablesection;

public interface SoundDownloadingCallback{	
	public void onDownloadingTaskFinished(boolean downloadedComplete);
	public void dismissDialog();
}