package com.edbert.library.navigationdrawer;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.edbert.library.R;
import com.edbert.library.navigationdrawer.NavDrawerItemInterface.Type;
import com.edbert.library.navigationdrawer.viewadapter.NavDrawerListAdapter;

public abstract class NavDrawerActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	public AbstractNavDrawerItemManager navDrawerManager;
	// used to store app title
	private CharSequence mTitle;

	private ArrayList<NavDrawerItemInterface> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		setNavDrawerManager();
		navDrawerItems = navDrawerManager.getNavDrawerItems();

		// Set up
		setUpNavDrawerTiles();
		setUpNavDrawerHeader();
		setUpNavDrawerFooter();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
		
		
		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav
				// menu
				// toggle
				// icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				// invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				// invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		
			return super.onOptionsItemSelected(item);
		
	}

	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	

	protected abstract void setUpNavDrawerHeader();

	protected abstract void setUpNavDrawerFooter();

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		NavDrawerItem navDrawerSelected = (NavDrawerItem) navDrawerManager.
				getNavDrawerItems().get(position);
		Fragment fragment = null;

		// if we want to log out, we will terminate it right here
		if (navDrawerSelected.getNavDrawerType().equals(Type.LOGOUT_TYPE)) {

			navDrawerSelected.doAction(this);
			return;
		}

		navDrawerSelected.doAction(this);

		fragment = navDrawerSelected.getFragment();
		// replace the fragment once found
		if (fragment != null) {
			// save instances and pop stacks to save previous instances
			FragmentManager fragmentManager = getSupportFragmentManager();

			String backStateName = fragment.getClass().getName();
			boolean fragmentPopped = fragmentManager.popBackStackImmediate(
					backStateName, 0);

			if (!fragmentPopped
					&& fragmentManager.findFragmentByTag(backStateName) == null) {

				FragmentTransaction ft = fragmentManager.beginTransaction();

				ft.replace(R.id.frame_container, fragment, backStateName);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

				ft.addToBackStack(backStateName);
				ft.commit();

			}
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			if (navDrawerSelected.updateActionBarTitle())
				setTitle(navDrawerSelected.getActionBarTitle());

			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (navDrawerItems.get(position).getNavDrawerType().equals(Type.PROFILE_TYPE)) {
				return;
			}
			displayView(position);
		}
	}
	protected abstract void setNavDrawerManager();
	
	protected abstract void setUpNavDrawerTiles();
}
