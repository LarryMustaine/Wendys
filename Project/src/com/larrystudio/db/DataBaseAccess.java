package com.larrystudio.db;

import java.util.ArrayList;

import tools.GenericObject;

import com.larrystudio.wendys.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


public final class DataBaseAccess {
	
	private static SQLiteDatabase databaseAccess;
	private static Context context;
	
	public static SQLiteDatabase getDatabaseConnection(Context context) {
		DataBaseAccess.context = context;
		
		if(databaseAccess == null || !databaseAccess.isOpen()) {
			try{
				openDatabaseConnection();				
			} catch(SQLiteException e) {
				createDB();
				openDatabaseConnection();
			}
		}
		return databaseAccess;
	}
	
	private static void openDatabaseConnection() {
		databaseAccess = SQLiteDatabase.openDatabase(context.getDatabasePath(context.getString(R.string.db_name)).toString(), null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public static void closeDatabaseConnection() {
		if(databaseAccess != null && databaseAccess.isOpen())
			databaseAccess.close();
	}
	
    private static void createDB() {
    	WendysSQLite createDBObject = new WendysSQLite (context, context.getString(R.string.db_name), null, 1);
		SQLiteDatabase db = createDBObject.getWritableDatabase();
		db.close();	
	}
    
    public static boolean ifImagesIsEmpty(SQLiteDatabase dbAccess){
    	if(dbAccess.isOpen()){
    		Cursor mCount= dbAccess.rawQuery("select count(*) from " + WendysSQLite.TABLE_IMAGES, null);
			mCount.moveToFirst();
			int count= mCount.getInt(0);
			mCount.close();
			
			if(count > 0)
				return false;
    	}
    	
    	return true;
    }
    
    public static boolean ifVideosIsEmpty(SQLiteDatabase dbAccess){
    	if(dbAccess.isOpen()){
    		Cursor mCount= dbAccess.rawQuery("select count(*) from " + WendysSQLite.TABLE_VIDEOS, null);
			mCount.moveToFirst();
			int count= mCount.getInt(0);
			mCount.close();
			
			if(count > 0)
				return false;
    	}
    	
    	return true;
    }

	public static ArrayList<GenericObject> getInfo(SQLiteDatabase dbAccess, String TABLE) {
		ArrayList<GenericObject> objects = new ArrayList<GenericObject>();
		
		if(dbAccess.isOpen()){
    		Cursor INFO = dbAccess.rawQuery("select url, comment from " + TABLE, null);
    		INFO.moveToFirst();
    		
    		for(int i=0, length=INFO.getCount(); i<length; i++){
    			GenericObject object = new GenericObject();
    			object.setURL(INFO.getString(0));
    			object.setComment(INFO.getString(1));
    			
    			objects.add(object);
    			INFO.moveToNext();
    		}
    		
    		INFO.close();
    	}
		
		return objects;
	}
}