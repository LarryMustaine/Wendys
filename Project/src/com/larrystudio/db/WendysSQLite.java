package com.larrystudio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
 
public class WendysSQLite extends SQLiteOpenHelper {
 
    String sqlCreate  = "CREATE TABLE Images (url TEXT primary key, comment TEXT)";
    String sqlCreate2 = "CREATE TABLE Videos (url TEXT primary key, comment TEXT)";
    
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