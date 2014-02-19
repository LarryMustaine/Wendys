package com.larrystudio.images;

import java.util.ArrayList;

import com.larrystudio.wendys.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import tools.GenericObject;
import tools.SquareImageView;
import android.content.Context;
import android.content.Intent;
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
	
	public ImagesAdapter(Context context, ArrayList<GenericObject> objects){
		this.context = context;
		this.objects = objects;
		this.inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
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
	
	private void setImage(ImageView image, String URL) {
		imageLoader.displayImage(URL, image);
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
		    	}

		        lastPressTime = pressTime; 
			}
		});
	}
	
	@Override public int getCount() { return objects.size(); }
	@Override public Object getItem(int position) { return null; }
	@Override public long getItemId(int position) { return 0; }
}