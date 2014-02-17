package com.larrystudio.db;

import android.content.Context;
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
		databaseAccess = SQLiteDatabase.openDatabase(context.getDatabasePath("SoundsDB").toString(), null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public static void closeDatabaseConnection() {
		if(databaseAccess != null && databaseAccess.isOpen())
			databaseAccess.close();
	}
	
    private static void createDB() 
    {
    	SoundsSQLite createDBObject = new SoundsSQLite (context, "SoundsDB", null, 1);
		SQLiteDatabase db = createDBObject.getWritableDatabase();
		db.close();	
	}
}