package com.larrystudio.wendys;
import com.larrystudio.wendys.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
	    super.onCreate();
	
	    DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.loading)
        .showImageOnFail(R.color.black)
        .resetViewBeforeLoading(true)
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .delayBeforeLoading(200)
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
        .build();
	
	    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).
	            defaultDisplayImageOptions(options).build();
	    ImageLoader.getInstance().init(config);
	}
}