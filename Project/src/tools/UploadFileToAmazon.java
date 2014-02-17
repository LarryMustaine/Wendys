package tools;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.larrystudio.wendys.R;

import extras.Codes;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class UploadFileToAmazon extends AsyncTask <Void, Void, Integer>{

	private String BUCKET_NAME;
	private String ACCESS_KEY_ID;
	private String SECRET_KEY;
	private int UPLOADING_FILE_STATUS;
	private Context context;
	private TransferManager tx;
	private Upload upload;
	private File finalFileToSend;
	private OnTaskCompleted uploadCallbacks;
	final private String TAG = "UploadFileToAmazon";
	
	ProgressListener progressListener = new ProgressListener() {
        public void progressChanged(ProgressEvent progressEvent) {
            if (upload == null) return;

            switch (progressEvent.getEventCode()) {
            	  case ProgressEvent.STARTED_EVENT_CODE:
            		  Log.d(TAG, "Uploading a File to Amazon WS");
            		  break;
            
	              case ProgressEvent.COMPLETED_EVENT_CODE: 
	            	  Log.d(TAG, "Finished Uploading a File to Amazon WS");
	            	  UPLOADING_FILE_STATUS = Codes.AMAZON_UPLOAD_OK;
	                  break;
	                  
	              case ProgressEvent.FAILED_EVENT_CODE:
	            	  UPLOADING_FILE_STATUS = Codes.AMAZON_UPLOAD_ERROR;
	                  break;
            }
        }
    };
	
	public UploadFileToAmazon(OnTaskCompleted uploadCallbacks, Context context, File finalFileToSend) {
		this.context = context;
		this.finalFileToSend = finalFileToSend;
		this.uploadCallbacks = uploadCallbacks;
		
		UPLOADING_FILE_STATUS = Codes.AMAZON_UPLOAD_IN_PROGRESS;
		setKeys();
	}

	private void setKeys() {
		ACCESS_KEY_ID = context.getString(R.string.amazon_access_key);
		SECRET_KEY    = context.getString(R.string.amazon_secret_key);
		BUCKET_NAME   = context.getString(R.string.amazon_bucket);
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
		try{        
			AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_KEY));
			PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, finalFileToSend.getName(), finalFileToSend).withProgressListener(progressListener); 
			request.setCannedAcl(CannedAccessControlList.PublicRead);
			tx = new TransferManager(s3Client);
	        upload = tx.upload(request);
		}catch (AmazonServiceException ase) {
		    System.out.println("AmazonServiceException, request rejected.");
		    System.out.println("Error Message:    " + ase.getMessage());
		    System.out.println("HTTP Status Code: " + ase.getStatusCode());
		    System.out.println("AWS Error Code:   " + ase.getErrorCode());
		    System.out.println("Error Type:       " + ase.getErrorType());
		    System.out.println("Request ID:       " + ase.getRequestId());
		    return Codes.AMAZON_UPLOAD_ERROR;
		} catch (AmazonClientException ace) {
		    System.out.println("AmazonClientException, internal problem while trying to communicate with S3");
		    System.out.println("Error Message: " + ace.getMessage());
		    return Codes.AMAZON_UPLOAD_ERROR;
		}

		while(UPLOADING_FILE_STATUS == Codes.AMAZON_UPLOAD_IN_PROGRESS){}
			//Need to wait until message is sent to amazon bucket
		
		return UPLOADING_FILE_STATUS;
	}	

	@Override
	protected void onPostExecute(Integer uploadCode) {
		super.onPostExecute(uploadCode);
		
		switch (uploadCode){
			case Codes.AMAZON_UPLOAD_OK:
				uploadCallbacks.onTaskCompleted(finalFileToSend.getAbsolutePath());
				break;
			
			case Codes.AMAZON_UPLOAD_ERROR:
				uploadCallbacks.onTaskError();
				break;
			
			default:
				uploadCallbacks.onTaskError();
		}
	}
}