package com.larrystudio.availablesection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import com.larrystudio.db.DataBaseAccess;
import com.larrystudio.downloadedsection.DownloadedSounds;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;

public class DownloadSounds extends AsyncTask <String, Integer, String>{

	private SQLiteDatabase dbAccess;
	private HttpURLConnection urlConnection;
	private ProgressBar pgrLoading;
	private TextView txtProgress;
	private SoundDownloadingCallback soundDownloadingCallback;
	private Context context;
	
	public DownloadSounds(Context context) {
		this.context = context;
		dbAccess = DataBaseAccess.getDatabaseConnection(context);
	}
	
	public void setProgressBarAndText(ProgressBar pgrLoading, TextView txtProgress, SoundDownloadingCallback soundDownloadingCallback) {
		this.pgrLoading = pgrLoading;
		this.txtProgress = txtProgress;
		this.soundDownloadingCallback = soundDownloadingCallback;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String URL   = params[0];
		
		try {			
			URL url = new URL(URL);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(10000);
			urlConnection.connect();
			
			String[] separatedURL = URL.split("/");
			int length = separatedURL.length - 1;
			String[] separatedFileName = separatedURL[length].split(Pattern.quote("."));
			
			String fileName = separatedFileName[0] + ".mp3";
			
			String root = context.getExternalFilesDir(null).toString();
	        File myDir = new File(root + "/VideoGamesMusic");
	        myDir.mkdirs();
	        
	        String filePath = myDir.getPath() + "/" + fileName;
	        
	        File file = new File (filePath);
			file.setWritable(true, false);
			file.setReadable(true, false);
			file.setExecutable(true, false);
			
			FileOutputStream fileOutput = new FileOutputStream(file);

			InputStream inputStream = urlConnection.getInputStream();
			int totalSize = urlConnection.getContentLength();
			int downloadedSize = 0;

			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
				fileOutput.write(buffer, 0, bufferLength);
				downloadedSize += bufferLength;
				publishProgress(totalSize, downloadedSize);
			}
			fileOutput.close();
			
			if(verifyFileIsNotCorrupted(filePath)){
				updateBD(filePath, URL);
				return filePath;
			}
			else
				return null;			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			BugSenseHandler.sendException(e);
		} catch (IOException e) {
			e.printStackTrace();
			BugSenseHandler.sendException(e);
		} catch (Exception e) {
			e.printStackTrace();
			BugSenseHandler.sendException(e);
		}		
		
		return null;
	}	

	@Override
	protected void onProgressUpdate(Integer... loadingValues) {
		super.onProgressUpdate(loadingValues);
		
		Integer totalSize = loadingValues[0];
		Integer downloadedSize = loadingValues[1];
		Integer percentage = (downloadedSize * 100) / totalSize;
		pgrLoading.setProgress(percentage);
		txtProgress.setText(percentage + "%");
	}
	
	private boolean verifyFileIsNotCorrupted(String filePath) {
		File file = new File(filePath);
		
		if(file.exists())
			return true;
		else
			return false;
	}

	private void updateBD(String filePath, String URL_Sound) {
		ContentValues SOUND = new ContentValues();
		
		SOUND.put("LocalPath_Sound"  ,filePath);
		SOUND.put("Downloaded_Sound" ,"true");

		try{
			if(dbAccess.isOpen()){
				dbAccess.update("Sounds", SOUND, "URL_Sound = \'" + URL_Sound + "\'" , null);
				SOUND.clear();
			}
		} catch(SQLException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onPostExecute(String SoundPath) {
		super.onPostExecute(SoundPath);
		
		boolean downloadedComplete;
		
		if(SoundPath == null){
			downloadedComplete = false;
		} else {
			downloadedComplete = true;
			DownloadedSounds.downloadedSounds.resetList();
		}
		
		soundDownloadingCallback.onDownloadingTaskFinished(downloadedComplete);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if(urlConnection != null){
			urlConnection.disconnect();
		}
	}	
}