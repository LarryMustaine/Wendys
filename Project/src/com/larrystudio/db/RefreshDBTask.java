package com.larrystudio.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;


public class RefreshDBTask extends AsyncTask<Void, Void, Boolean>
{
	private Context context;
	private UpdateDB updateDB;
	private String DB_URL;
	
	public RefreshDBTask(Context context, String DB_URL, UpdateDB updateDB){
		this.context = context;
		this.DB_URL = DB_URL;
		this.updateDB = updateDB;
	}

	@Override
	protected Boolean doInBackground(Void... params){
		try {
			URL url = new URL(DB_URL);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(3000);
			urlConnection.connect();

			File cacheDir = context.getCacheDir(); 
			File file = new File(cacheDir,"DB.txt");
			FileOutputStream fileOutput = new FileOutputStream(file);

			InputStream inputStream = urlConnection.getInputStream();

			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
				fileOutput.write(buffer, 0, bufferLength);
			}
			fileOutput.close();
			
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		
		if(result)
			updateDB.onSuccess();
		else
			updateDB.onFail();
	}
	
	public interface UpdateDB{	
		public void onSuccess();
		public void onFail();
	}
}