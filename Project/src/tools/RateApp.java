package tools;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class RateApp {

	public RateApp(Context context){
		Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
	    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
	    try {
	    	context.startActivity(goToMarket);
	    } catch (ActivityNotFoundException e) {
	    	e.printStackTrace();
	    }
	}
	
	public RateApp(Context context, String packageName){
		Uri uri = Uri.parse("market://details?id=" + packageName);
	    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
	    try {
	    	context.startActivity(goToMarket);
	    } catch (ActivityNotFoundException e) {
	    	e.printStackTrace();
	    }
	}
}