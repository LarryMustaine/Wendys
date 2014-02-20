package com.larrystudio.videos;

import java.util.ArrayList;

import com.larrystudio.blondie.R;

import tools.GenericObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideosAdapter extends BaseAdapter {

	public static final String VIDEO_PATH = "VIDEO_PATH";
	private LayoutInflater inflater;
	private ArrayList<GenericObject> objects;
	private Context context;
	private Activity activity;
	
	public VideosAdapter(Context context, ArrayList<GenericObject> objects){
		this.context = context;
		this.objects = objects;
		this.inflater = LayoutInflater.from(context);
		activity = (Activity) context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListViewHolder viewHolder = null;
		
		if(convertView == null){
			viewHolder = new ListViewHolder();
    		convertView = this.inflater.inflate(R.layout.video_list_row, null);
    		initializeObjects(viewHolder, convertView);
    	}
    	else
    		viewHolder = (ListViewHolder) convertView.getTag();
		
		convertView.setTag(viewHolder);

    	setComment(viewHolder.info, viewHolder.mb, objects.get(position).getComment());
        setClick(viewHolder, objects.get(position).getURL());
    	
	    return convertView; 
	}

	public class ListViewHolder {
		public TextView info;
		public TextView mb;
		public ImageView play;
	}
	
	private void initializeObjects(ListViewHolder viewHolder, View convertView) {
		viewHolder.info = (TextView) convertView.findViewById(R.id.txtInfo);
		viewHolder.mb = (TextView) convertView.findViewById(R.id.txtMB);
		viewHolder.play = (ImageView) convertView.findViewById(R.id.img_play);
	}
	
	private void setComment(TextView comment, TextView mb, String Comment) {
		String[] comments = Comment.split("/");
		comment.setText(comments[0].replace("_", " "));
		mb.setText(comments[1]);
	}
	
	private void setClick(ListViewHolder viewHolder, final String URL) {
		viewHolder.play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent fullscreenVideo = new Intent(context, FullScreenVideoActivity.class);
				fullscreenVideo.putExtra(VIDEO_PATH, URL);
				context.startActivity(fullscreenVideo);
				activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
	}
	
	@Override public int getCount() { return objects.size(); }
	@Override public Object getItem(int position) { return null; }
	@Override public long getItemId(int position) { return 0; }
}