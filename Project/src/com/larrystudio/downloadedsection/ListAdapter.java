package com.larrystudio.downloadedsection;

import java.util.ArrayList;

import tools.ClickPlaySound;
import tools.SoundObject;
import tools.ClickCategories;

import com.larrystudio.wendys.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import extras.ListViewHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter{
	
    private LayoutInflater inflater;	  
    private Context context;
    private ArrayList<SoundObject> infoList;
    private String Level;
    private DownloadedSounds downloadedSounds;
    private ImageLoader imageLoader;

	public ListAdapter(Context context, ArrayList<SoundObject> infoList, String Level, DownloadedSounds downloadedSounds) {
		this.context  = context; 
		this.infoList = infoList;
		this.Level    = Level;
		this.downloadedSounds = downloadedSounds;
		this.inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public View getView( final int position, View convertView, ViewGroup parent){	
		ListViewHolder viewHolder = null;
		
		if(convertView == null){
			viewHolder = new ListViewHolder();
    		convertView = this.inflater.inflate(R.layout.list_row, null);
    		initializeObjects(convertView, viewHolder);
    	}
    	else{
    		viewHolder = (ListViewHolder) convertView.getTag();
    	}			
		
		convertView.setTag(viewHolder);

		setPlayButton     (position, viewHolder);
    	setImage          (position, viewHolder);
    	setClickListeners (position, viewHolder);
        	
	    return convertView;     
	}

	private void initializeObjects(View convertView, ListViewHolder viewHolder) {
		viewHolder.imgLogo       = (ImageView) convertView.findViewById(R.id.imgLogo);
		viewHolder.imgPlaySound  = (ImageView) convertView.findViewById(R.id.imgPlaySound);
		viewHolder.txtSoundText  = (TextView)  convertView.findViewById(R.id.txtSoundText);
		viewHolder.txtSoundText.setVisibility(View.INVISIBLE);
	}
	
	private void setPlayButton(int position, ListViewHolder viewHolder) {
		if(Level.equals("Categories")){
			viewHolder.imgPlaySound.setVisibility(View.INVISIBLE);
		} else if(Level.equals("Sounds")){
			viewHolder.imgPlaySound.setVisibility(View.VISIBLE);
		}
	}
	
	private void setClickListeners(final int position, final ListViewHolder viewHolder)  { 
		viewHolder.imgLogo.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	SoundObject soundObject = infoList.get(position);
		    	
		    	if(Level.equals("Categories")){
					new ClickCategories(downloadedSounds, "Sounds", soundObject.getName()).doClick();
				} else if(Level.equals("Sounds")){
					new ClickDownloadedSounds(context, soundObject.getLocalPathSound(), soundObject.getName(), downloadedSounds).doClick();
				}
		    }
		});
		
		viewHolder.imgPlaySound.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	SoundObject soundObject = infoList.get(position);
		    	new ClickPlaySound(context, soundObject.getLocalPathSound()).doClick();
		    }
		});
	}
	
	private void setImage(final int position, final ListViewHolder viewHolder) {
		SoundObject soundObject = infoList.get(position);
		
		imageLoader.displayImage(soundObject.getURL(), viewHolder.imgLogo, new ImageLoadingListener() {
		    @Override
		    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		        setSoundText(position, viewHolder.txtSoundText);
		    }
		    @Override
		    public void onLoadingCancelled(String imageUri, View view) {}
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}
			@Override
			public void onLoadingStarted(String arg0, View arg1) {}
		});
	}

	public void setSoundText(int position, TextView txtSoundText) {
		if(Level.equals("Categories")){
			txtSoundText.setVisibility(View.INVISIBLE);
		} else if(Level.equals("Sounds")){
			SoundObject soundObject = infoList.get(position);
			
			txtSoundText.setVisibility(View.VISIBLE);
			txtSoundText.setText(soundObject.getName().replace("_", " "));
		}
	}

	@Override
	public int getCount() {
		return infoList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}