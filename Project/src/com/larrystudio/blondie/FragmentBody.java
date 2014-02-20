package com.larrystudio.blondie;

import java.util.Locale;

import com.larrystudio.images.ImagesFragment;
import com.larrystudio.menu.MenuFragment;
import com.larrystudio.videos.VideosFragment;
import com.larrystudio.blondie.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBody extends Fragment {

	private final int PAGER_SECTIONS = 3;
	public static final int MENU_SECTION = 0;
	public static final int IMAGES_SECTION = 1;
	public static final int VIDEOS_SECTION = 2;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private int pageSelected = MENU_SECTION;
	private Fragment fragment[] = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_body,container, false);
		
		fragment = new Fragment[PAGER_SECTIONS];
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				pageSelected = position;
				
				if(position != MENU_SECTION)
					((MenuFragment) fragment[MENU_SECTION]).hideImages();
				else
					((MenuFragment) fragment[MENU_SECTION]).animateImages();
			}
			
			@Override public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override public void onPageScrollStateChanged(int arg0) {}
		});
		
		return rootView;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if(fragment[position] == null){
				switch (position) {
				case 0:
					fragment[position] = new MenuFragment(mViewPager);
					break;
				case 1:
					fragment[position] = new ImagesFragment();
					break;
				case 2:
					fragment[position] = new VideosFragment();
					break;
				}
			}
			
			return fragment[position];
		}

		@Override
		public int getCount() {
			return PAGER_SECTIONS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_menu_fragment).toUpperCase(l);
			case 1:
				return getString(R.string.title_images_fragment).toUpperCase(l);
			case 2:
				return getString(R.string.title_videos_fragment).toUpperCase(l);
			}
			return null;
		}	
	}

	public boolean handleBackButton() {
		switch(pageSelected){
		case MENU_SECTION: return false;
		case IMAGES_SECTION:
			((MenuFragment) fragment[MENU_SECTION]).animateImages();
		case VIDEOS_SECTION:
			mViewPager.setCurrentItem(MENU_SECTION);
		}

		return true;
	}
}