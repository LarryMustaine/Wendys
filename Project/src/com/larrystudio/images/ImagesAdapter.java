package com.larrystudio.images;

import java.util.ArrayList;

import com.larrystudio.wendys.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import tools.GenericObject;
import tools.SquareImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImagesAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<GenericObject> objects;
	private ImageLoader imageLoader;
	
	public ImagesAdapter(Context context, ArrayList<GenericObject> objects){
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
	
	@Override public int getCount() { return objects.size(); }
	@Override public Object getItem(int position) { return null; }
	@Override public long getItemId(int position) { return 0; }
}