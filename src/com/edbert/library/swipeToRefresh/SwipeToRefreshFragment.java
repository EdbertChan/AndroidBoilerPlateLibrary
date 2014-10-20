package com.edbert.library.swipeToRefresh;

import org.michenux.drodrolib.network.sync.AbstractSyncHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public abstract class SwipeToRefreshFragment extends Fragment implements
		AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>,
		SwipeRefreshLayout.OnRefreshListener {
	protected CursorAdapter mAdapter;
	protected SwipeRefreshLayout swipeView;
	CursorLoader cursorLoader;
	private BroadcastReceiver onFinishSyncReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateRefresh(false);
			reloadData();
		}
	};

	private BroadcastReceiver onStartSyncReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateRefresh(true);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (isSyncing()) {
			updateRefresh(true);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(
				onFinishSyncReceiver,
				new IntentFilter(syncFinishedFlag()));
		LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(
				onStartSyncReceiver,
				new IntentFilter(syncStartedFlag()));

	/*	if (!mHasSync) {
			requestSync(); // mTutorialSyncHelper.performSync();
		}*/
	}

	@Override
	public void onPause() {
		super.onPause();

		LocalBroadcastManager.getInstance(this.getActivity())
				.unregisterReceiver(onFinishSyncReceiver);
		LocalBroadcastManager.getInstance(this.getActivity())
				.unregisterReceiver(onStartSyncReceiver);

	}

	public abstract CursorLoader generateCursorLoader();

	protected Cursor generateCursor() {
		return generateCursorLoader().loadInBackground();
	}

	@Override
	public void onRefresh() {
		// mTutorialSyncHelper.performSync();
		requestSync();
	}

	private void updateRefresh(boolean isSyncing) {
		if(swipeView == null){
			return;
		}
		if (!isSyncing) {
			swipeView.setRefreshing(false);
		} else {
			swipeView.setRefreshing(true);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {

		return generateCursorLoader();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {

		this.mAdapter.changeCursor(cursor);
		mAdapter.notifyDataSetChanged();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		this.mAdapter.changeCursor(null);
	}

	// what do we do when we click the item?
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		return;
	}

	public abstract void requestSync();
	/**
	 * This function is for how you want to reload the data visually
	 */
	public abstract void reloadData();

	public abstract String syncStartedFlag();

	public abstract String syncFinishedFlag();
	
	public abstract boolean isSyncing();

}