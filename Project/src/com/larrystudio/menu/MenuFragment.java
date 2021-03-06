package com.larrystudio.menu;

import com.larrystudio.blondie.FragmentBody;
import com.larrystudio.blondie.R;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
public class MenuFragment extends Fragment{

	private ViewPager mViewPager;
	private ImageView imgImages;
	private ImageView imgVideos;
	private ImageView imgFrameImages;
	private ImageView imgFrameVideos;
	
	public MenuFragment(){}
	
	public MenuFragment(ViewPager viewPager){
		this.mViewPager = viewPager;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_menu,container, false);
		
		imgImages = (ImageView) rootView.findViewById(R.id.image_images);
		imgVideos = (ImageView) rootView.findViewById(R.id.image_videos);
		imgFrameImages = (ImageView) rootView.findViewById(R.id.image_frame_images);
		imgFrameVideos = (ImageView) rootView.findViewById(R.id.image_frame_videos);
		
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

	@Override
	public void onResume() {
		super.onResume();
		
		animateImages();
	}
	
	public void animateImages() {
		setAnimationEntrance(imgImages, imgFrameImages);
		setAnimationEntrance(imgVideos, imgFrameVideos);
	}

	public void hideImages(){
		setAnimationGone(imgImages, imgFrameImages);
		setAnimationGone(imgVideos, imgFrameVideos);
	}
	
	private void setAnimationGone(final ImageView imgImage, final ImageView imgFrame){
		imgImage.setVisibility(View.VISIBLE);
		imgFrame.setVisibility(View.VISIBLE);
		
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
	    animation.setDuration(500);
	    
	    animation.setAnimationListener(new AnimationListener() {
	        @Override public void onAnimationStart(Animation animation) {}
	        @Override public void onAnimationRepeat(Animation animation) {}

	        @Override
	        public void onAnimationEnd(Animation animation) {
	        	imgImage.setVisibility(View.INVISIBLE);
	        	imgFrame.setVisibility(View.INVISIBLE);
	        }
	    });

	    imgFrame.startAnimation(animation);
	}

	public void setAnimationEntrance(final ImageView imgImage, final ImageView imgFrame){
		imgImage.setVisibility(View.INVISIBLE);
		imgFrame.setVisibility(View.INVISIBLE);
		
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
	    animation.setDuration(500);
	    
	    animation.setAnimationListener(new AnimationListener() {
	        @Override public void onAnimationStart(Animation animation) {}
	        @Override public void onAnimationRepeat(Animation animation) {}

	        @Override
	        public void onAnimationEnd(Animation animation) {
	        	imgImage.setVisibility(View.VISIBLE);
	        	imgFrame.setVisibility(View.VISIBLE);
	        }
	    });

	    imgFrame.startAnimation(animation);
	}
}