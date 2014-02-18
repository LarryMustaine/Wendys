package com.larrystudio.db;

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
    		Cursor mCount= dbAccess.rawQuery("select count(*) from Images", null);
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
    		Cursor mCount= dbAccess.rawQuery("select count(*) from Videos", null);
			mCount.moveToFirst();
			int count= mCount.getInt(0);
			mCount.close();
			
			if(count > 0)
				return false;
    	}
    	
    	return true;
    }
}