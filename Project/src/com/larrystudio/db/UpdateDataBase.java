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

public class UpdateDataBase{

	private Context context;
	private File file;
	private ArrayList<String> parsedFileList;
	private SQLiteDatabase dbAccess;
	
	public UpdateDataBase(Context context) {
		this.context = context;
		dbAccess = getDatabaseWriteConnection(context);
	}
	
	private SQLiteDatabase getDatabaseWriteConnection(Context context) {
		return DataBaseAccess.getDatabaseConnection(context);
	}

	public void OpenFile() {
		File cacheDir = context.getCacheDir(); 
		file = new File(cacheDir,"DB.txt");
	}
	
	public void ParseFile() {
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

	public void UpdateDatabase() {
		if(parsedFileList.size()>0){
			saveIntoDB();
			deleteDBFile();
		}
	}

	private void saveIntoDB() {
		String Table = null;
		ArrayList<String[]> splitedList = splitList();
		ContentValues UPDATE_DB = new ContentValues();
		
		if(dbAccess.isOpen()){
			for(int i=0, length=splitedList.size(); i<length ; i++){
				String[] separated = splitedList.get(i);
				
				if(separated[0].equals("Categories")){
					Table = "Categories";
				} else if(separated[0].equals("Sounds")){
					Table = "Sounds";
				} else{
					
					String Url_Parsed =  separated[3].replace("www.dropbox", "dl.dropboxusercontent");
					Url_Parsed += "?dl=1";
					
					UPDATE_DB.put("Section"   , separated[0]);
					UPDATE_DB.put("Name"      , separated[1]);
					UPDATE_DB.put("Downloaded", separated[2]);
					UPDATE_DB.put("URL"       , Url_Parsed);
					UPDATE_DB.put("Status"    , separated[4]);
					
					if(Table.equals("Sounds")){
						String Url_Sound_Parsed =  separated[6].replace("www.dropbox", "dl.dropboxusercontent");
						Url_Sound_Parsed += "?dl=1";
								
						UPDATE_DB.put("Category"        , separated[5]);
						UPDATE_DB.put("URL_Sound"       , Url_Sound_Parsed);
						UPDATE_DB.put("Downloaded_Sound", separated[7]);
					}
					
					try{
						dbAccess.insertOrThrow(Table, null, UPDATE_DB);
					} catch (SQLException e){
						String CONDITION = null;
						UPDATE_DB.clear();
						UPDATE_DB.put("Status", separated[4]);
						
						if(Table.equals("Categories")){
							CONDITION = "URL = '" + separated[3] + "'";
						} else if(Table.equals("Sounds")){
							CONDITION = "URL_Sound = '" + separated[6] + "'";
						}
						
						dbAccess.update(Table, UPDATE_DB, CONDITION, null);
					}
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