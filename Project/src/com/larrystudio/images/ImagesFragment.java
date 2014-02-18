package com.larrystudio.images;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.larrystudio.db.DataBaseAccess;
import com.larrystudio.db.RefreshDBTask;
import com.larrystudio.db.RefreshDBTask.UpdateDB;
import com.larrystudio.wendys.R;

import android.support.v4.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ImagesFragment extends Fragment{

	private PullToRefreshLayout pull2refresh;
	private ListView lvImages;
	private SQLiteDatabase dbAccess;
	private String DB_URL = getActivity().getString(R.string.DB_IMAGES_URL);
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_images,container, false);
		
		dbAccess = DataBaseAccess.getDatabaseConnection(getActivity());
		
		initializeObjects(rootView);
		configurePull2Refresh();
		populationProcess();
		return rootView;
	}
	
	private void initializeObjects(View rootView) {
		lvImages = (ListView) rootView.findViewById(R.id.lvImages);
		pull2refresh = (PullToRefreshLayout) rootView.findViewById(R.id.pull2refresh);
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
				getInfoFromServer();
			}
		})
        .setup(pull2refresh);
	}
	
	private void populationProcess() {
		if(!DataBaseAccess.ifImagesIsEmpty(dbAccess))
			populateList();
		else
			getInfoFromServer();
	}

	private void populateList() {
		
	}
	
	private void getInfoFromServer() {
		new RefreshDBTask(getActivity(), DB_URL, new UpdateDB() {
			
			@Override
			public void onSuccess() {
				
			}

			@Override
			public void onFail() {
				Toast.makeText(getActivity(), getActivity().getString(R.string.dialog_error) , Toast.LENGTH_SHORT).show();
			}
			
		}).execute();
	}
}