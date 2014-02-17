package com.larrystudio.downloadedsection;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

public class SetRingtone{

	private String LocalPath;
	private String SoundName;
	private String SetAs;
	private Context context;

	public SetRingtone(Context context, String LocalPath, String SoundName, String SetAs) {
		this.context   = context;
		this.LocalPath = LocalPath;
		this.SoundName = SoundName.replace("_", " ");
		this.SetAs = SetAs;
	}

	public void SetSoundAsRingtone(){
		File ringtoneFile = new File(LocalPath);
		ringtoneFile.setWritable(true, false);
		ringtoneFile.setReadable(true, false);
		ringtoneFile.setExecutable(true, false);

		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, ringtoneFile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, SoundName);
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
		values.put(MediaStore.Audio.Media.ARTIST, "GamesMusic");
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);
		
		if(SetAs.equals("Ringtone")){
			values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
			values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
			values.put(MediaStore.Audio.Media.IS_ALARM, false);
		}else if(SetAs.equals("Notification")){
			values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
			values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
			values.put(MediaStore.Audio.Media.IS_ALARM, false);
		}else if(SetAs.equals("Alarm")){
			values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
			values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
			values.put(MediaStore.Audio.Media.IS_ALARM, true);
		}
		
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(ringtoneFile.getAbsolutePath());
		Uri newUri = context.getContentResolver().insert(uri, values);

		int Type = 0;
		
		if(SetAs.equals("Ringtone")){
			Type = RingtoneManager.TYPE_RINGTONE;
		}else if(SetAs.equals("Notification")){
			Type = RingtoneManager.TYPE_NOTIFICATION;
		}else if(SetAs.equals("Alarm")){
			Type = RingtoneManager.TYPE_ALARM;
		}
		
		RingtoneManager.setActualDefaultRingtoneUri(context,Type,newUri);
	
		// DELETE RINGTONES ALREADY SET BY PATH FILTER
		//context.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + ringtoneFile.getAbsolutePath() + "\"", null);
		
		/*
		Uri contactUri;
		ContentValues values = new ContentValues();
		values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, newRingtoneUri.toString());
		context.getContentResolver().update(contactUri, values, where, args);
		*/
	}
}