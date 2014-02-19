package com.larrystudio.images;

import java.util.ArrayList;

import tools.GenericObject;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.larrystudio.db.DataBaseAccess;
import com.larrystudio.db.RefreshDBTask;
import com.larrystudio.db.TaskResult;
import com.larrystudio.db.UpdateDataBase;
import com.larrystudio.db.WendysSQLite;
import com.larrystudio.wendys.R;

import android.support.v4.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ImagesFragment extends Fragment{

	private PullToRefreshLayout pull2refresh;
	private ListView lvImages;
	private ProgressBar progressBar;
	private SQLiteDatabase dbAccess;
	private String DB_URL;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list,container, false);
		
		dbAccess = DataBaseAccess.getDatabaseConnection(getActivity());
		DB_URL = getActivity().getString(R.string.DB_IMAGES_URL);
		
		initializeObjects(rootView);
		configurePull2Refresh();
		populationProcess();
		return rootView;
	}
	
	private void initializeObjects(View rootView) {
		lvImages = (ListView) rootView.findViewById(R.id.lvGeneric);
		pull2refresh = (PullToRefreshLayout) rootView.findViewById(R.id.pull2refresh);
		progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
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
				if(DataBaseAccess.ifImagesIsEmpty(dbAccess))
					progressBar.setVisibility(View.VISIBLE);
				
				getInfoFromServer();
			}
		})
        .setup(pull2refresh);
	}
	
	private void populationProcess() {
		if(!DataBaseAccess.ifImagesIsEmpty(dbAccess))
			populateList();
		else{
			progressBar.setVisibility(View.VISIBLE);
			getInfoFromServer();
		}
	}

	private void populateList() {
		ArrayList<GenericObject> objects = DataBaseAccess.getInfo(dbAccess, WendysSQLite.TABLE_IMAGES);
		ImagesAdapter adapter = new ImagesAdapter(getActivity(), objects);
		lvImages.setAdapter(adapter);
	}
	
	private void getInfoFromServer() {
		new RefreshDBTask(getActivity(), DB_URL, new TaskResult() {
			
			@Override
			public void onSuccess() {
				updateDB();
			}

			@Override
			public void onFail() {
				finishTasks();
				Toast.makeText(getActivity(), getActivity().getString(R.string.dialog_error) , Toast.LENGTH_SHORT).show();
			}
			
		}).execute();
	}

	protected void updateDB() {
		new UpdateDataBase(getActivity(), new TaskResult() {
			
			@Override
			public void onSuccess() {
				finishTasks();
				populationProcess();
			}
			
			@Override
			public void onFail() {
				finishTasks();
			}
		}, WendysSQLite.TABLE_IMAGES).execute();
	}

	protected void finishTasks() {
		progressBar.setVisibility(View.GONE);
		pull2refresh.setRefreshComplete();
	}
}