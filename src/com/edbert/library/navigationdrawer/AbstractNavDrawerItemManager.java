package com.edbert.library.navigationdrawer;

import java.util.ArrayList;

import android.content.Context;

public abstract class AbstractNavDrawerItemManager {
	protected ArrayList<NavDrawerItemInterface> navDrawerButtonArrayList = new ArrayList<NavDrawerItemInterface>();

	public void register(NavDrawerItemInterface stratFact) {
		navDrawerButtonArrayList.add(stratFact);
	}

	public void clear() {
		navDrawerButtonArrayList.clear();
	}

	public void setPosition(int position, int value) {
		NavDrawerItem i = (NavDrawerItem) navDrawerButtonArrayList
				.get(position);
		i.setCount(value);
	}

	public ArrayList<NavDrawerItemInterface> getNavDrawerItems() {
		return navDrawerButtonArrayList;
	}

	public abstract void setDefaultLayout();
}