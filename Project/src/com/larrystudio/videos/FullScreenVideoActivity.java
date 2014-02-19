package com.larrystudio.videos;

import com.larrystudio.wendys.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class FullScreenVideoActivity extends Activity{
	
	private VideoView videoPlayer;
	private ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		String URL = bundle.getString(VideosAdapter.VIDEO_PATH);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_fullscreen_video);
		initUI();
		configureVideoPlayer();
		setVideo(URL.replace("?dl=1", ""));
	}

	public void initUI() {
		videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}
	
	private void videoVisibility(int visibility){
		videoPlayer.setVisibility(visibility);
	}
	
	private void progressBarVisibility(int visibility){
		progressBar.setVisibility(visibility);
	}
	
	private void setVideo(String url) {
		Log.d("", url);
		
		Uri myUri = Uri.parse(url);
		videoPlayer.setVideoURI(myUri);
		videoPlayer.start();
	}
	
	private void configureVideoPlayer() {
		MediaController controller = new MediaController(this);
		videoPlayer.setMediaController(controller);
		
		videoPlayer.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				videoPlayer.stopPlayback();
				videoVisibility(View.INVISIBLE);
				progressBarVisibility(View.INVISIBLE);
				return true;
			}
		});

		videoPlayer.setOnPreparedListener(new OnPreparedListener() {

               public void onPrepared(MediaPlayer mp) {
            	   if(videoPlayer != null){
            		   progressBarVisibility(View.INVISIBLE);
            	   }
            	   
            	   mp.setOnInfoListener(new OnInfoListener() {
					
					@Override
					public boolean onInfo(MediaPlayer mp, int what, int extra) {
						switch(what){
							case 701: //Started to Buffering
								progressBarVisibility(View.VISIBLE);
							break;
							
							case 702: //Started to play, doesn't mean is 100% completed
								progressBarVisibility(View.INVISIBLE);
							break;
						}
						
						return false;
					}
				});
               }
		});	

		videoPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				videoPlayer.start();
			}
		});	
	}
	
	@Override
	public void onBackPressed() {
		finish();
		FullScreenVideoActivity.this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
}