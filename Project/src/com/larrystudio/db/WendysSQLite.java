package com.larrystudio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
public class WendysSQLite extends SQLiteOpenHelper {
 
	public static String TABLE_IMAGES = "Images";
	public static String TABLE_VIDEOS = "Videos";
	
    String sqlCreate  = "CREATE TABLE " + TABLE_IMAGES + " (url TEXT primary key, comment TEXT)";
    String sqlCreate2 = "CREATE TABLE " + TABLE_VIDEOS + " (url TEXT primary key, comment TEXT)";
    
    public WendysSQLite(Context context, String nombre, CursorFactory factory, int version) {
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