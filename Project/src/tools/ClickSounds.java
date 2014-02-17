package tools;

import com.larrystudio.availablesection.AvailableSounds;
import com.larrystudio.availablesection.DialogMenu;
import com.larrystudio.availablesection.ListAdapter;
import com.larrystudio.downloadedsection.ClickDownloadedSounds;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class ClickSounds{

	private String URL_Sounds;
	private String Downloaded;
	private ImageView imgPlaySound;
	private Context context;
	private AvailableSounds availableSounds;
	private ListAdapter listAdapter;
	private SoundObject soundObject;
	
	public ClickSounds(Context context, String URL_Sounds, String Downloaded, ImageView imgPlaySound, AvailableSounds availableSounds, ListAdapter listAdapter, SoundObject soundObject) {
		this.context = context;
		this.URL_Sounds = URL_Sounds;
		this.Downloaded = Downloaded;
		this.imgPlaySound = imgPlaySound;
		this.availableSounds = availableSounds;
		this.listAdapter = listAdapter;
		this.soundObject = soundObject;
	}
	
	public void doClick(){
		if(Downloaded.equals("false")){
			setCheckImage(false);
			showDialog(false);
		} else if(Downloaded.equals("true")){
			setCheckImage(true);
			showDialog(true);
		}
	}

	private void setCheckImage(boolean isSoundDownloaded) {
		if(isSoundDownloaded){
			imgPlaySound.setVisibility(View.VISIBLE);
		}else{
			imgPlaySound.setVisibility(View.INVISIBLE);
		}
	}

	private void showDialog(boolean isSoundDownloaded) {
		if(!isSoundDownloaded)
			new DialogMenu(context, URL_Sounds, availableSounds, imgPlaySound, listAdapter).ShowCustomDialog();
		else
			new ClickDownloadedSounds(context, soundObject.getLocalPathSound(), soundObject.getName(), availableSounds).doClick();
	}
}