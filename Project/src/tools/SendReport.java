package tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.larrystudio.availablesection.AvailableSounds;
import com.larrystudio.db.DataBaseAccess;
import com.larrystudio.downloadedsection.DownloadedSounds;
import com.larrystudio.wendys.R;

import extras.Codes;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings.Secure;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SendReport{

	private Context context;
	private String  reportHelpText;
	private Dialog CustomDialog;
	private TextView txtMessage;
	private Button btnOK;
	private ProgressBar progressBar;
	private String UDID;
	
	public SendReport(Context context, String reportHelpText) {
		this.context = context;
		this.reportHelpText = reportHelpText;
		
		UDID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	public void sendErrorReport() {		
		showUploadingFileDialog(Codes.ERROR_NOTICE_USER);
	}

	private File createFileWithText() {
		File file = null;
		
		 try{
			 File cacheDir = context.getCacheDir(); 
			 String filePath = cacheDir.getPath() + File.separator + "report_" + System.currentTimeMillis() + ".txt";
			 
			 file = new File(filePath);	 
			 FileWriter writer = new FileWriter(file);
			 
			 String reportFinalText = reportHelpText + "  ID: " + UDID;
			 
			 writer.append(reportFinalText);
		     writer.close();
	    }catch(IOException e){
	         e.printStackTrace();
	    }
		
		 return file;
	}
	
	private void deleteLogFile(String logFilePath) {
		File file = new File(logFilePath);	 
		file.delete();	 
	}
	
	private void uploadFileToAmazonBucket(File finalFileToSend) {
		UploadFileToAmazon uploader = new UploadFileToAmazon(new OnTaskCompleted() {
			
			@Override
			public void onTaskError() {
				if(CustomDialog!=null)
					CustomDialog.dismiss();
				
				showUploadingFileDialog(Codes.AMAZON_UPLOAD_ERROR);
			}
			
			@Override
			public void onTaskCompleted(String logFilePath) {
				updateDB(); //Update entry at DB.
				deleteLogFile(logFilePath);
				resetLists();
				
				if(CustomDialog!=null)
					CustomDialog.dismiss();
				
				showUploadingFileDialog(Codes.AMAZON_UPLOAD_OK);
			}
		},context, finalFileToSend);
		
		uploader.execute();
	}
	
	protected void resetLists() {
		if(AvailableSounds.availableSounds != null)
			AvailableSounds.availableSounds.resetList();
		
		if(DownloadedSounds.downloadedSounds != null)
			DownloadedSounds.downloadedSounds.resetList();
	}

	private void updateDB() {
		SQLiteDatabase dbAccess = DataBaseAccess.getDatabaseConnection(context);
		ContentValues SOUND = new ContentValues();
		
		SOUND.put("LocalPath_Sound"  ,"");
		SOUND.put("Downloaded_Sound" ,"false");

		try{
			if(dbAccess.isOpen()){
				dbAccess.update("Sounds", SOUND, "LocalPath_Sound = \'" + reportHelpText + "\'" , null);
				SOUND.clear();
			}
		} catch(SQLException e){
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void showUploadingFileDialog(int dialogFlag) {
		CustomDialog = new Dialog(context);
		CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		CustomDialog.setCancelable(false);
		CustomDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		
		CustomDialog.setContentView(R.layout.dialog_error); 
		CustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		CustomDialog.setCanceledOnTouchOutside(false);
		
		txtMessage  = (TextView)    CustomDialog.findViewById(R.id.txtDownload);
		btnOK       = (Button)      CustomDialog.findViewById(R.id.btnOK);
		progressBar = (ProgressBar) CustomDialog.findViewById(R.id.progressBar);

		if(dialogFlag == Codes.ERROR_NOTICE_USER){
			txtMessage.setText(context.getString(R.string.amazon_upload_log_notice));
		} else if(dialogFlag == Codes.AMAZON_UPLOAD_IN_PROGRESS){
			txtMessage.setText(context.getString(R.string.amazon_upload_log_uploading));
			btnOK.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		} else if(dialogFlag == Codes.AMAZON_UPLOAD_ERROR){
			txtMessage.setText(context.getString(R.string.amazon_upload_error));
		} else if(dialogFlag == Codes.AMAZON_UPLOAD_OK){
			txtMessage.setText(context.getString(R.string.amazon_upload_ok));
		}
		
		CustomDialog.show();
		setClickListeners(dialogFlag);
	}
	
	private void setClickListeners(final int dialogFlag) {
		btnOK.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	CustomDialog.dismiss();
		    	
		    	if(dialogFlag == Codes.ERROR_NOTICE_USER){
		    		showUploadingFileDialog(Codes.AMAZON_UPLOAD_IN_PROGRESS);
			    	File finalFileToSend = createFileWithText();
					uploadFileToAmazonBucket(finalFileToSend);
				} 
		    }
		});
	}
}