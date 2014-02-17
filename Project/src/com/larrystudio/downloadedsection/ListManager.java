package com.larrystudio.downloadedsection;

import java.util.ArrayList;

import tools.SoundObject;

import com.larrystudio.db.DataBaseAccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ListManager{

	private SQLiteDatabase dbAccess;
	private Context context;
	private ArrayList<SoundObject> soundsList;
	private ArrayList<SoundObject> categoryList;

	public ListManager(Context context) {
		this.context = context;
		
		categoryList = new ArrayList<SoundObject>();
		soundsList = new ArrayList<SoundObject>();
		
		getAccessToDB();
	}
	
	private void getAccessToDB(){
		dbAccess = DataBaseAccess.getDatabaseConnection(context);
	}
	
	public void loadInformation(){
		
		getInfoFromDB("Sounds", null);
		
		if(soundsList.size()>0){
			ArrayList<String> categories = getArrayOfCategories();
			
			for(int i=0, length=categories.size(); i<length; i++){
				getInfoFromDB("Categories", categories.get(i));
			}
		}
	}

	private void getInfoFromDB(String Table, String Category_Name) {	
		if (dbAccess.isOpen()){
			
			Cursor SOUNDS = null;
			StringBuilder query = new StringBuilder();
			
			query.append("SELECT  Section, " +
					            " Name, " +
					            " Downloaded, " +
					            " URL, " +
                     			" LocalPath ");
			
			if(Table.equals("Sounds")){
				query.append(",Category, " +
							 " URL_Sound, " +
							 " Downloaded_Sound, " +
							 " LocalPath_Sound ");
			}
			
			query.append("FROM '" + Table + "' " +
	    				 "WHERE Status = 'Active'");
			
			if(Table.equals("Sounds")){
				query.append(" AND Downloaded_Sound = 'true'");
				query.append(" ORDER BY Name ASC");
			} else if(Table.equals("Categories")){
				query.append(" AND Name = '" + Category_Name + "'");
			}
			
			SOUNDS = dbAccess.rawQuery(query.toString(), null);
    		
    		SOUNDS.moveToFirst();
    		
    		for(int i=0, length=SOUNDS.getCount(); i<length; i++){
    			SoundObject soundObject = new SoundObject();
    			
    			soundObject.setSection   (SOUNDS.getString(0));
    			soundObject.setName      (SOUNDS.getString(1));
    			soundObject.setDownloaded(SOUNDS.getString(2));
    			soundObject.setURL       (SOUNDS.getString(3));
    			soundObject.setLocalPath (SOUNDS.getString(4));
    			
    			if(Table.equals("Sounds")){
    				soundObject.setCategory  (SOUNDS.getString(5));
	    			soundObject.setURLSound  (SOUNDS.getString(6));
	    			soundObject.setDownloadedSound (SOUNDS.getString(7));
	    			soundObject.setLocalPathSound  (SOUNDS.getString(8));
    			}
    			
    			if(Table.equals("Categories")){
    				categoryList.add(soundObject);
    			} else if(Table.equals("Sounds")){
    				soundsList.add(soundObject);
    			}
    			
    			soundObject = null;
    			
    			SOUNDS.moveToNext();
    		}
		}
	}
	
	private ArrayList<String> getArrayOfCategories() {
		ArrayList<String> tempArray = new ArrayList<String>();
		
		for(int i=0, length=soundsList.size(); i<length ; i++){
			SoundObject tempSoundObject = soundsList.get(i);
			String category = tempSoundObject.getCategory();
			
			if(!tempArray.contains(category)){
				tempArray.add(category);
			}
		}
		
		return tempArray;
	}
	
	public ArrayList<SoundObject> getsubCategoriesInformation(){
		return soundsList;
	}
	
	public ArrayList<SoundObject> getCategoryInformation(){
		return categoryList;
	}
}