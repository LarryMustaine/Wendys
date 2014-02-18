package com.larrystudio.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class UpdateDataBase extends AsyncTask<Void, Void, Boolean>{

	private Context context;
	private File file;
	private ArrayList<String> parsedFileList;
	private SQLiteDatabase dbAccess;
	private TaskResult callBack;
	private String Table;
	
	public UpdateDataBase(Context context, TaskResult callBack, String Table) {
		this.context = context;
		this.callBack = callBack;
		this.Table = Table;
		dbAccess = DataBaseAccess.getDatabaseConnection(context);
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		OpenFile();
		ParseFile();
		return UpdateDatabase();
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		
		if(result)
			callBack.onSuccess();
		else
			callBack.onFail();
	}

	private void OpenFile() {
		File cacheDir = context.getCacheDir(); 
		file = new File(cacheDir,"DB.txt");
	}
	
	private void ParseFile() {
		parsedFileList = new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));  
	        String line;   
	        while ((line = br.readLine()) != null) {
	        	parsedFileList.add(line);
	        } 
	        br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}

	private boolean UpdateDatabase() {
		if(parsedFileList.size()>0){
			saveIntoDB();
			deleteDBFile();
			return true;
		} else
			return false;
	}

	private void saveIntoDB() {
		ArrayList<String[]> splitedList = splitList();
		ContentValues UPDATE_DB = new ContentValues();
		
		if(dbAccess.isOpen()){
			for(int i=0, length=splitedList.size(); i<length ; i++){
				String[] separated = splitedList.get(i);

				String URL_Parsed =  separated[0].replace("www.dropbox", "dl.dropboxusercontent");
				URL_Parsed += "?dl=1";
				
				UPDATE_DB.put("comment" , separated[1]);
				UPDATE_DB.put("url"     , URL_Parsed);
				
				try{
					dbAccess.insertOrThrow(Table, null, UPDATE_DB);
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
		}
	}

	private ArrayList<String[]> splitList() {
		ArrayList<String[]> splitedList = new ArrayList<String[]>();
		
		for(int i=0, length=parsedFileList.size(); i<length ; i++){
			String[] separated = parsedFileList.get(i).split(" ");
			splitedList.add(separated);
		}

		return splitedList;
	}
	
	private void deleteDBFile() {
		try {
			file.delete();  
	    }
	   catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}