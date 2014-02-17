package com.larrystudio.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.larrystudio.wendys.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


public class RefreshDBTask extends AsyncTask<Void, Void, Boolean>
{
	private Context context;
	private UpdateDB updateDB;
	private Dialog dialog;
	private boolean showDialog;
	
	public RefreshDBTask(Context context, UpdateDB updateDB, boolean showDialog){
		this.context = context;
		this.updateDB = updateDB;
		this.showDialog = showDialog;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(showDialog)
			dialog = ProgressDialog.show(context, null, context.getString(R.string.downloading_db));
	}

	@Override
	protected Boolean doInBackground(Void... params){
		
		try {
			URL url = new URL(context.getString(R.string.DB_URL));
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
		if(dialog != null && dialog.isShowing() && showDialog)
			dialog.dismiss();
		
		if(result)
			new UpdateDatabaseTask().execute();
		else{
			updateDB.loadInformation();
			updateDB.populateList();
			updateDB.pull2RefreshComplete();
		}	
	}
	
	private class UpdateDatabaseTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(showDialog)
				dialog = ProgressDialog.show(context, null, context.getString(R.string.saving_db));
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			updateDB.updateDatabase();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			if(dialog != null && dialog.isShowing() && showDialog)
				try{dialog.dismiss();}catch(Exception e){}
			
			updateDB.loadInformation();
			updateDB.populateList();
			updateDB.pull2RefreshComplete();
		}
	}

	public interface UpdateDB{	
		public void updateDatabase();
		public void loadInformation();
		public void populateList();
		public void pull2RefreshComplete();
	}
}
