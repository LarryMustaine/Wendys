package com.larrystudio.gcm;

import com.larrystudio.wendys.MainActivity;
import com.larrystudio.wendys.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMBroadcastReceiver extends BroadcastReceiver {

	private static final String logTag = GCMBroadcastReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(logTag, "Received intent: " + intent.toString());
		showNotification(context, intent);
	}

	private void showNotification(Context context, Intent intent) {
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

		Bundle e = intent.getExtras();
		String msg = e.getString("message");
		
		Log.i(logTag, "Message: " + msg);
		
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_action_good)
		.setContentTitle("My notification")
		.setContentText("Hello World!");
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}  
}