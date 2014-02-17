package com.larrystudio.downloadedsection;

import java.util.ArrayList;
import tools.SoundObject;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.larrystudio.wendys.FragmentBody;
import com.larrystudio.wendys.R;
import extras.ContactPicker;
import extras.Subsection;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DownloadedSounds extends Fragment implements Subsection, ContactPicker {

	public static String currentLevel = FragmentBody.LEVEL_CATEGORIES;
	private ListView lvAvailableSounds;
	private Activity activity;
	private ArrayList<SoundObject> subCategoryArrayList;
	private ArrayList<SoundObject> categoryArrayList;
	private ListManager listManager;
	private ListAdapter adapter;
	private String LocalPath_Sound;
	public static DownloadedSounds downloadedSounds;
	private PullToRefreshLayout pull2refresh;
	
	public DownloadedSounds() {
		downloadedSounds = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sounds,container, false);
		
		initializeObjects(rootView);
		configurePull2Refresh();
		
		startPopulationProcess();
		
		return rootView;
	}

	private void initializeObjects(View rootView) {
		lvAvailableSounds   = (ListView)  rootView.findViewById(R.id.lvAvailableSounds);
		pull2refresh      = (PullToRefreshLayout) rootView.findViewById(R.id.pull2refresh);
	}
	
	
	private void configurePull2Refresh() {				
		ActionBarPullToRefresh.from(getActivity())
        .allChildrenArePullable()
        .options(Options.create()
        		        .noMinimize()
        		        .build())
        .listener(new OnRefreshListener() {
			
			@Override
			public void onRefreshStarted(View view) {
				startPopulationProcess();
			}
		})
        .setup(pull2refresh);
	}
	
	private void startPopulationProcess() {
		loadInformation();
		
		if(categoryArrayList.size()>0){
			populateList();
		}else{
			lvAvailableSounds.setAdapter(null);
		}
		
		pull2refresh.setRefreshComplete();
	}
	
	public void loadInformation() {
		listManager = null;
		listManager = new ListManager(activity);
		listManager.loadInformation();
		
		categoryArrayList = listManager.getCategoryInformation();
	}
	
	public void populateList() {
		if(categoryArrayList.size()>0){
			subCategoryArrayList = listManager.getsubCategoriesInformation();
			setInformationToList("Categories", null);
		}		
	}
	
	public void setInformationToList(String Level, String SectionName) {
		adapter = null;
		
		if(Level.equals("Categories")){
			currentLevel = FragmentBody.LEVEL_CATEGORIES;
			adapter = new ListAdapter(activity, categoryArrayList, Level, this);
		} else if(Level.equals("Sounds")){
			currentLevel = FragmentBody.LEVEL_SUBSECTION;
			ArrayList<SoundObject> filteredSubCategoryList = filterSubCategoryList(SectionName);
			
			adapter = new ListAdapter(activity, filteredSubCategoryList, Level, this);
		}
		
		lvAvailableSounds.setAdapter(adapter);
	}
	
	private ArrayList<SoundObject> filterSubCategoryList(String sectionName) {
		ArrayList<SoundObject> filteredSubCategoryList = new ArrayList<SoundObject>();
		
		for(int i=0, length=subCategoryArrayList.size(); i<length; i++){
			SoundObject tempSoundObject = subCategoryArrayList.get(i);
			
			if(tempSoundObject.getCategory().equals(sectionName)){
				filteredSubCategoryList.add(tempSoundObject);
			}
		}
		
		return filteredSubCategoryList;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	  super.onActivityResult(reqCode, resultCode, data);

	    if(data != null){
		    Uri contactData = data.getData();
		    new SetRingtoneToContact(getActivity(), LocalPath_Sound, contactData).SetSoundAsRingtone();
	    }
	}

	@Override
	public void LaunchContactPicker(String LocalPath_Sound) {
		this.LocalPath_Sound = LocalPath_Sound;
		
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 
    	startActivityForResult(intent, 1);
	}

	public void resetList() {
		startPopulationProcess();
	}
}