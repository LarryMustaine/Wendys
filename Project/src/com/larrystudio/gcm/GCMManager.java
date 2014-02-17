package com.larrystudio.gcm;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.larrystudio.wendys.R;

public class GCMManager {

	private int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private String PROPERTY_REG_ID = "PROPERTY_REG_ID";
	private String PROPERTY_APP_VERSION = "PROPERTY_APP_VERSION";
	private Activity activity;
	private GoogleCloudMessaging gcm;
	private String regid;
	private Context context;

	public GCMManager(Activity activity) {
		this.activity = activity;
		this.context = (Context) activity;
	}

	public void startGCMService() {
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(context);
			regid = getRegistrationId(context);

			if (regid.isEmpty()) {
				registerInBackground();
			}
		}
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} 

			return false;
		}
		return true;
	}

	private String getRegistrationId(Context context) {
		SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		
		if (registrationId.isEmpty())
			return "";

		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		
		if (registeredVersion != currentVersion)
			return "";

		return registrationId;
	}

	private SharedPreferences getGCMPreferences(Context context) {
		return context.getSharedPreferences(GCMManager.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}
	
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	private void registerInBackground() {
	    new GCMRegistration().execute();
	}
	
	private class GCMRegistration extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
            try {
                if (gcm == null)
                    gcm = GoogleCloudMessaging.getInstance(context);

                regid = gcm.register(context.getString(R.string.gcm_project_id));

                sendRegistrationIdToBackend();
                storeRegistrationId(context, regid);
            } catch (IOException ex) {
                // Failed to Register
            	Log.d("", "Error :" + ex.getMessage());
            }
			return null;
		}
		
	}
	
	private void sendRegistrationIdToBackend() {
	    // Your implementation here. Send Data to Server
	}
	
	private void storeRegistrationId(Context context, String regId) {
	    SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
}