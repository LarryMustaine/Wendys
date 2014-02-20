package com.larrystudio.drawer;

import com.larrystudio.blondie.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListMenuDrawerAdapter extends BaseAdapter{
	
	public final static String RATE_APP = "Rate App";
	public final static String ABOUT = "About";
    public static String[] menuTitle;
    private int[] menuIcon;
    private LayoutInflater inflater;
    
	public ListMenuDrawerAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
		generateMenuItems();
	}

	private void generateMenuItems() {
		menuTitle = new String[]{RATE_APP, ABOUT};
		menuIcon = new int[]{R.drawable.ic_action_important, R.drawable.ic_action_help};
	}

	@Override
	public View getView( final int position, View convertView, ViewGroup parent){	
		ListViewHolder viewHolder = null;
		
		if(convertView == null){
			viewHolder = new ListViewHolder();
    		convertView = this.inflater.inflate(R.layout.drawer_list_item, null);
    		initializeObjects(convertView, viewHolder);
    	}
    	else{
    		viewHolder = (ListViewHolder) convertView.getTag();
    	}			
		
		convertView.setTag(viewHolder);

    	setImage(viewHolder.imgIcon, menuIcon[position]);
    	setMenuText(viewHolder.txtMenuItem, menuTitle[position]);
        	
	    return convertView;     
	}
	
	public class ListViewHolder {
		public ImageView imgIcon;
		public TextView  txtMenuItem;
	}

	private void initializeObjects(View convertView, ListViewHolder viewHolder) {
		viewHolder.imgIcon      = (ImageView) convertView.findViewById(R.id.imgIcon);
		viewHolder.txtMenuItem  = (TextView)  convertView.findViewById(R.id.txtMenuItem);
	}
	
	private void setImage(ImageView imgIcon, int menuIcon) {
		imgIcon.setImageResource(menuIcon);
	}

	public void setMenuText(TextView txtMenuText, String menuTitle) {
		txtMenuText.setText(menuTitle);
	}

	@Override
	public int getCount() {
		return menuTitle.length;
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