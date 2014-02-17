package com.larrystudio.downloadedsection;

import java.io.File;

import com.larrystudio.availablesection.AvailableSounds;
import com.larrystudio.db.DataBaseAccess;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DeleteFile{

	private String LocalPath;
	private File soundFile;
	private SQLiteDatabase dbAccess;

	public DeleteFile(Context context, String LocalPath) {
		this.LocalPath = LocalPath;
		dbAccess = getDatabaseWriteConnection(context);
	}
	
	private SQLiteDatabase getDatabaseWriteConnection(Context context) {
		return DataBaseAccess.getDatabaseConnection(context);
	}

	public void DeleteFileFromSandbox(){
		soundFile = new File(LocalPath);
		deleteRecursive(soundFile);
		updateEntryFromDB();
		resetLists();
	}

	protected void resetLists() {
		if(AvailableSounds.availableSounds != null)
			AvailableSounds.availableSounds.resetList();
		
		if(DownloadedSounds.downloadedSounds != null)
			DownloadedSounds.downloadedSounds.resetList();
	}
	
	private void deleteRecursive(File fileOrDirectory) {
		 if (fileOrDirectory.isDirectory()){
		    for (File child : fileOrDirectory.listFiles()){
		        deleteRecursive(child);
		    }
		 }
		    fileOrDirectory.delete();
	}
	
	
	private void updateEntryFromDB() {
		ContentValues UPDATE_DB = new ContentValues();
		
		UPDATE_DB.put("LocalPath_Sound" , "");
		UPDATE_DB.put("Downloaded_Sound", "false");
		
		String CONDITION = "LocalPath_Sound = '" + LocalPath + "'";
		
		if(dbAccess.isOpen()){
			dbAccess.update("Sounds", UPDATE_DB, CONDITION, null);	
		}
	}	
}