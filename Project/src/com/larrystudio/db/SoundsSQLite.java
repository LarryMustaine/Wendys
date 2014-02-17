package com.larrystudio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
public class SoundsSQLite extends SQLiteOpenHelper {
 
    String sqlCreate  = "CREATE TABLE Sounds ( Category         TEXT, " +
    										 " Section     		TEXT, " +
    								     	 " Name        		TEXT, " +
    										 " Downloaded  		TEXT, " +
    									     " URL         		TEXT, " +
    									     " Status      		TEXT, " +
    									     " URL_Sound        TEXT primary key, " +
    									     " Downloaded_Sound TEXT, " +
    									     " LocalPath_Sound  TEXT, " +
    										 " LocalPath 		TEXT)";
    
    String sqlCreate2 = "CREATE TABLE Categories ( Section     TEXT, " +
			                                     " Name        TEXT, " +
			                                     " Downloaded  TEXT, " +
			                                     " URL         TEXT primary key, " +
			                                     " Status      TEXT, " +
			                                     " LocalPath   TEXT)";
    
    public SoundsSQLite(Context context, String nombre, CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate2);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {			
	}
}