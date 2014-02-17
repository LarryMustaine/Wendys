package com.larrystudio.availablesection;


import com.larrystudio.wendys.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DialogMenu{

	private String URL_Sounds;
	private Dialog CustomDialog;
	private Context context;
	private RelativeLayout layoutDownload;
	private AvailableSounds availableSounds;
	private ImageView imgPlaySound;
	private ListAdapter listAdapter;
	
	public DialogMenu(Context context, String URL_Sounds, AvailableSounds availableSounds, ImageView imgPlaySound, ListAdapter listAdapter) {
		this.context = context;
		this.URL_Sounds = URL_Sounds;
		this.availableSounds = availableSounds;
		this.imgPlaySound = imgPlaySound;
		this.listAdapter = listAdapter;
	}
	
	public void ShowCustomDialog(){
		CustomDialog = new Dialog(context);
		CustomDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		CustomDialog.setCancelable(true);
		CustomDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		
		CustomDialog.setContentView(R.layout.dialog_click_sound); 
		CustomDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		CustomDialog.setCanceledOnTouchOutside(true);
		
		layoutDownload = (RelativeLayout) CustomDialog.findViewById(R.id.layoutDownload);

		CustomDialog.show();
		setClickListeners();
	}
	
	private void setClickListeners() {
		layoutDownload.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
		    	CustomDialog.dismiss();
		    	new DialogDownloading(context, URL_Sounds, availableSounds, imgPlaySound, listAdapter).ShowCustomDialog();
		    }
		});
	}
}