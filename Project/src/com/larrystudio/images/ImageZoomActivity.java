package com.larrystudio.images;

import com.larrystudio.wendys.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polites.android.GestureImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class ImageZoomActivity extends Activity {
	
	private String filePath;
	private ImageLoader imageLoader;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_zoom);
		
		Bundle extras = getIntent().getExtras();
	    filePath = extras.getString(ImagesAdapter.IMAGE_PATH);
	    
	    imageLoader = ImageLoader.getInstance();
	    
	    GestureImageView image = (GestureImageView) findViewById(R.id.image);
	    imageLoader.displayImage(filePath, image);
	}
}