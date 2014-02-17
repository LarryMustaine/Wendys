package com.larrystudio.downloadedsection;

import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;

public class SetRingtoneToContact{

	private String LocalPath;
	private Context context;
	private Uri contactData;

	public SetRingtoneToContact(Activity activity, String LocalPath, Uri contactData) {
		this.context     = activity;
		this.LocalPath   = LocalPath;
		this.contactData = contactData;
	}

	public void SetSoundAsRingtone(){
		File ringtoneFile = new File(LocalPath);
		ringtoneFile.setWritable(true, false);
		ringtoneFile.setReadable(true, false);
		ringtoneFile.setExecutable(true, false);

		ContentValues values = new ContentValues();

        values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, ringtoneFile.getAbsolutePath());
        context.getContentResolver().update(contactData, values, null, null);
	}
}