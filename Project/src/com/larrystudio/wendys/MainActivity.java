package com.larrystudio.wendys;

import tools.RateApp;
import tools.SendEmail;

import com.bugsense.trace.BugSenseHandler;
import com.larrystudio.about.About;
import com.larrystudio.actionbar.ActionBarAccess;
import com.larrystudio.drawer.ListMenuDrawerAdapter;
import com.larrystudio.gcm.GCMManager;
import com.larrystudio.wendys.R;

import extras.Codes;

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
        setContentView(R.layout.main);
    	
		//BugSenseHandler.I_WANT_TO_DEBUG = true;
		//BugSenseHandler.initAndStartSession(this, this.getString(R.string.bugsense_api_key));
    	ActionBarAccess.instantiateActionBar(this);
    	
        fragment = new FragmentBody();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, fragment)
                       .commit();
        
        setDrawerMenu();
        setDrawerToggle();
        
        new GCMManager(this).startGCMService();
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
				}else if(selection.equals(ListMenuDrawerAdapter.MAKE_REQUEST)){
					new SendEmail(MainActivity.this);
				}else if(selection.equals(ListMenuDrawerAdapter.BUY_APP)){
					new RateApp(MainActivity.this, getString(R.string.app_pay_package_name));
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
                R.drawable.ic_action_gamepad,
                R.string.action_bar_subtitle,
                R.string.action_bar_subtitle
        );
      
      mDrawerLayout.setDrawerListener(mDrawerToggle);
      
      if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2){
    	  getActionBar().setDisplayHomeAsUpEnabled(true);
    	  getActionBar().setHomeButtonEnabled(true);
      }
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		if(getString(R.string.app_type).equals(Codes.APP_TYPE_FREE))
			getMenuInflater().inflate(R.menu.action_bar_free, menu);
		else if(getString(R.string.app_type).equals(Codes.APP_TYPE_PAY))
			getMenuInflater().inflate(R.menu.action_bar_pay, menu);
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
			
		case R.id.menu_buyApp:
			new RateApp(this, getString(R.string.app_pay_package_name));
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
         }else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) { 
        	 return false;
  	     }
         
         return true;
     }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		BugSenseHandler.closeSession(this);
	}
}