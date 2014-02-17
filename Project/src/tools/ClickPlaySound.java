package tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class ClickPlaySound{

	private String LocalPath_Sound;
	private Context context;
	
	public ClickPlaySound(Context context, String LocalPath_Sound) {
		this.context = context;
		this.LocalPath_Sound = LocalPath_Sound;
	}
	
	public void doClick(){
		MediaPlayer mPlayer = MediaPlayer.create(context, Uri.parse(LocalPath_Sound));
		
		try{
			if(mPlayer != null){
				mPlayer.start();
			}else{
				new SendReport(context, LocalPath_Sound).sendErrorReport();
			}
		} catch(Exception e){
			new SendReport(context, LocalPath_Sound).sendErrorReport();
		}
	}
}