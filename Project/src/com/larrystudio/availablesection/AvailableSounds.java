package com.larrystudio.availablesection;

import java.util.ArrayList;

import tools.SoundObject;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.larrystudio.db.RefreshDBTask;
import com.larrystudio.db.UpdateDataBase;
import com.larrystudio.db.RefreshDBTask.UpdateDB;
import com.larrystudio.downloadedsection.SetRingtoneToContact;
import com.larrystudio.wendys.FragmentBody;
import com.larrystudio.wendys.R;
import extras.ContactPicker;
import extras.ErrorMessages;
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
import android.widget.ImageView;
import android.widget.ListView;

public class AvailableSounds extends Fragment implements UpdateDB, ErrorMessages, Subsection, PlayIconCallback, ContactPicker{

	public static String currentLevel = FragmentBody.LEVEL_CATEGORIES;
	private ListView lvAvailableSounds;
	private ImageView imgNoInfo;
	private Activity activity;
	private ListManager listManager;
	private ArrayList<SoundObject> subCategoryArrayList;
	private ArrayList<SoundObject> categoryArrayList;
	private ArrayList<RefreshDBTask> tasksArrayList;
	private RefreshDBTask refreshDBTask;
	private ListAdapter adapter;
	public static AvailableSounds availableSounds;
	private String LocalPath_Sound;
	private PullToRefreshLayout pull2refresh;
	
	public AvailableSounds() {
		availableSounds = this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sounds,container, false);
		
		initializeObjects(rootView);
		setClickListener();
		configurePull2Refresh();
		
		startPopulationProcess();
		
		return rootView;
	}

	private void initializeObjects(View rootView) {
		lvAvailableSounds = (ListView)  rootView.findViewById(R.id.lvAvailableSounds);
		imgNoInfo         = (ImageView) rootView.findViewById(R.id.imgNoInfo);
		pull2refresh      = (PullToRefreshLayout) rootView.findViewById(R.id.pull2refresh);
	}
	
	private void setClickListener() {
		imgNoInfo.setOnClickListener(new View.OnClickListener() {
			
		    @Override
		    public void onClick(View view) {
		    	imgNoInfo.setVisibility(View.GONE);
		    	lvAvailableSounds.setVisibility(View.GONE);
		    	refreshDB(true);
		    }
		});
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
				refreshDB(false);
			}
		})
        .setup(pull2refresh);
	}
	
	private void startPopulationProcess() {
		loadInformation();
		
		if(categoryArrayList.size()>0){
			populateList();
		}
		else{
			refreshDB(true);
		}
	}

	public void loadInformation() {
		listManager = null;
		listManager = new ListManager(activity);
		listManager.loadInformation();
		
		categoryArrayList = listManager.getCategoryInformation();
	}

	private void refreshDB(boolean showDialog) {
		if(tasksArrayList == null){
			tasksArrayList = new ArrayList<RefreshDBTask>();
		}
		
		refreshDBTask = new RefreshDBTask(activity, this, showDialog);
		refreshDBTask.execute();
		
		tasksArrayList.add(refreshDBTask);
	}
	
	public void populateList() {
		if(categoryArrayList.size()>0){
			subCategoryArrayList = listManager.getsubCategoriesInformation();
			setInformationToList("Categories", null);
			setNoInfoImage(false);
		} else {
			setNoInfoImage(true);
		}		
	}

	public void setInformationToList(String Level, String SectionName) {
		adapter = null;
		
		if(Level.equals("Categories")){
			currentLevel = FragmentBody.LEVEL_CATEGORIES;
			adapter = new ListAdapter(activity, SectionName, categoryArrayList, Level, this);
		} else if(Level.equals("Sounds")){
			currentLevel = FragmentBody.LEVEL_SUBSECTION;
			ArrayList<SoundObject> filteredSubCategoryList = filterSubCategoryList(SectionName);
			adapter = new ListAdapter(activity, SectionName, filteredSubCategoryList, Level, this);
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
	
	private void setNoInfoImage(boolean setNoInfoImageFlag) {
		if(setNoInfoImageFlag){
			lvAvailableSounds.setVisibility(View.GONE);
			imgNoInfo.setVisibility(View.VISIBLE);
		} else {
			lvAvailableSounds.setVisibility(View.VISIBLE);
			imgNoInfo.setVisibility(View.GONE);
		}
	}
	
	public void updateDatabase() {
		UpdateDataBase updateDataBase = new UpdateDataBase(activity);
		updateDataBase.OpenFile();
		updateDataBase.ParseFile();
		updateDataBase.UpdateDatabase();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		
		if(tasksArrayList != null){
			for(int i=0, length=tasksArrayList.size(); i<length ;i++){
				RefreshDBTask refreshDBTask = tasksArrayList.get(i);
				refreshDBTask.cancel(true);
			}
		}
	}

	@Override
	public void ErrorOnDB() {
		setNoInfoImage(true);
	}
	
	public void updatePlayButtonIconVisibility(ImageView imgPlaySound){
		imgPlaySound.setVisibility(View.VISIBLE);
	}

	@Override
	public void LaunchContactPicker(String LocalPath_Sound) {
		this.LocalPath_Sound = LocalPath_Sound;
		
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 
    	startActivityForResult(intent, 1);
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	  super.onActivityResult(reqCode, resultCode, data);

	  if(data != null){
      	Uri contactData = data.getData();
      	new SetRingtoneToContact(getActivity(), LocalPath_Sound, contactData).SetSoundAsRingtone();
      }
	}
	
	public void resetList() {
		startPopulationProcess();
	}

	@Override
	public void pull2RefreshComplete() {
		pull2refresh.setRefreshComplete();
	}
}