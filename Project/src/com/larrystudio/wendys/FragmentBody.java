package com.larrystudio.wendys;

import java.util.Locale;

import com.larrystudio.availablesection.AvailableSounds;
import com.larrystudio.downloadedsection.DownloadedSounds;
import com.larrystudio.wendys.R;
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

	public static final String LEVEL_CATEGORIES = "level_categories";
	public static final String LEVEL_SUBSECTION = "level_subsection";
	private final int AVAILABLE_SECTION = 0;
	private final int DOWNLAODED_SECTION = 1;
	private final int PAGER_SECTIONS = 2;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private int pageSelected = 0;
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
					fragment[position] = new AvailableSounds();
					break;
				case 1:
					fragment[position] = new DownloadedSounds();
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
				return getString(R.string.title_available_sounds).toUpperCase(l);
			case 1:
				return getString(R.string.title_downloaded_sounds).toUpperCase(l);
			}
			return null;
		}	
	}

	public boolean handleBackButton() {
       	 if(pageSelected == AVAILABLE_SECTION){
       		 if(AvailableSounds.currentLevel.equals(LEVEL_SUBSECTION))
       			 ((AvailableSounds) fragment[pageSelected]).setInformationToList("Categories", null);
       		 else if (AvailableSounds.currentLevel.equals(LEVEL_CATEGORIES)){
       			return false;
       		 }
       	 }else if(pageSelected == DOWNLAODED_SECTION){
       		 if(DownloadedSounds.currentLevel.equals(LEVEL_SUBSECTION))
       			 ((DownloadedSounds) fragment[pageSelected]).setInformationToList("Categories", null);
       		 else if (DownloadedSounds.currentLevel.equals(LEVEL_CATEGORIES)){
       			return false;
       		 }
       	 }
        
        return true;
	}
}