package com.larrystudio.menu;

import com.larrystudio.wendys.FragmentBody;
import com.larrystudio.wendys.R;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
public class MenuFragment extends Fragment{

	private ViewPager mViewPager;
	private ImageView imgImages;
	private ImageView imgVideos;
	
	public MenuFragment(){}
	
	public MenuFragment(ViewPager viewPager){
		this.mViewPager = viewPager;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_menu,container, false);
		
		imgImages = (ImageView) rootView.findViewById(R.id.image_images);
		imgVideos = (ImageView) rootView.findViewById(R.id.image_videos);
		
		imgImages.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(FragmentBody.IMAGES_SECTION);
			}
		});
		
		imgVideos.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(FragmentBody.VIDEOS_SECTION);
			}
		});
		
		return rootView;
	}
}