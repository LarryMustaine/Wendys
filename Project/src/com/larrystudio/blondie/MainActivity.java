package com.larrystudio.blondie;

import tools.RateApp;

import com.bugsense.trace.BugSenseHandler;
import com.larrystudio.about.About;
import com.larrystudio.actionbar.ActionBarAccess;
import com.larrystudio.drawer.ListMenuDrawerAdapter;
import com.larrystudio.blondie.R;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MainActivity extends FragmentActivity {

	private Fragment fragment;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	
    	ActionBarAccess.instantiateActionBar(this);
    	
        fragment = new FragmentBody();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, fragment)
                       .commit();
        
        setDrawerMenu();
        setDrawerToggle();
    }

	private void setDrawerMenu() {
    	mDrawerList = (ListView) findViewById(R.id.left_drawer);
    	mDrawerList.setAdapter(new ListMenuDrawerAdapter(this));
    	
    	mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View viewChild, int position, long arg3) {
				mDrawerLayout.closeDrawer(mDrawerList);
				String selection = ListMenuDrawerAdapter.menuTitle[position];
				
				if(selection.equals(ListMenuDrawerAdapter.RATE_APP)){
					new RateApp(MainActivity.this);
				}else if(selection.equals(ListMenuDrawerAdapter.ABOUT)){
					Intent myIntent = new Intent(MainActivity.this, About.class);
					MainActivity.this.startActivity(myIntent);
				}
			}
		});
	}
	
    private void setDrawerToggle() {
    	mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    	
    	mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name
        );
      
      mDrawerLayout.setDrawerListener(mDrawerToggle);
      
      if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2){
    	  getActionBar().setDisplayHomeAsUpEnabled(true);
    	  getActionBar().setHomeButtonEnabled(true);
      }
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		
		switch (item.getItemId()){
		case R.id.menu_rateApp:
			new RateApp(this);
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
         if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	 if(!((FragmentBody) fragment).handleBackButton()){
        		 finish();
        		 return false; 
        	 }
         }else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) 
        	 return false;
         
         return true;
     }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BugSenseHandler.closeSession(this);
	}
}