package com.larrystudio.images;

import java.util.ArrayList;

import com.larrystudio.wendys.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import tools.GenericObject;
import tools.SquareImageView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImagesAdapter extends BaseAdapter {

	public static final String IMAGE_PATH = "IMAGE_PATH";
	private LayoutInflater inflater;
	private Context context;
	private ArrayList<GenericObject> objects;
	private ImageLoader imageLoader;
	private long DOUBLE_PRESS_INTERVAL = 250;
	private long lastPressTime;
	private Activity activity;
	
	public ImagesAdapter(Context context, ArrayList<GenericObject> objects){
		this.context = context;
		this.objects = objects;
		this.inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		activity = (Activity) context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListViewHolder viewHolder = null;
		
		if(convertView == null){
			viewHolder = new ListViewHolder();
    		convertView = this.inflater.inflate(R.layout.image_list_row, null);
    		initializeObjects(viewHolder, convertView);
    	}
    	else
    		viewHolder = (ListViewHolder) convertView.getTag();
		
		convertView.setTag(viewHolder);

		viewHolder.image.setVisibility(View.INVISIBLE);
		
    	setImage(viewHolder.image, objects.get(position).getURL());
    	setComment(viewHolder.comment, objects.get(position).getComment());
    	setClick(viewHolder.image, objects.get(position).getURL());
        	
	    return convertView; 
	}

	public class ListViewHolder {
		public SquareImageView image;
		public TextView comment;
	}
	
	private void initializeObjects(ListViewHolder viewHolder, View convertView) {
		viewHolder.image   = (SquareImageView) convertView.findViewById(R.id.imgPicture);
		viewHolder.comment = (TextView)  convertView.findViewById(R.id.txtComment);
	}
	
	private void setImage(final ImageView image, String URL) {
		imageLoader.displayImage(URL, image, new ImageLoadingListener() {
			
			@Override
			public void onLoadingComplete(String URL, View view, Bitmap bitmap) {
				image.setVisibility(View.VISIBLE);
			}
			
			@Override public void onLoadingStarted(String arg0, View arg1) {}
			@Override public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}
			@Override public void onLoadingCancelled(String arg0, View arg1) {}
		});
	}
	
	private void setComment(TextView comment, String Comment) {
		Comment = Comment.replace("_", " ");
		comment.setText(Comment);
	}
	
	private void setClick(ImageView image, final String URL){
		image.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				long pressTime = System.currentTimeMillis();
				
		    	if(pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {		
		    		Intent showZoom = new Intent(context, ImageZoomActivity.class);
					showZoom.putExtra(IMAGE_PATH, URL);
					context.startActivity(showZoom);
					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		    	}

		        lastPressTime = pressTime; 
			}
		});
	}
	
	@Override public int getCount() { return objects.size(); }
	@Override public Object getItem(int position) { return null; }
	@Override public long getItemId(int position) { return 0; }
}