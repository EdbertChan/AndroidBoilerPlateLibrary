package com.edbert.library.navigationdrawer.viewadapter;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;


import com.edbert.library.R;
import com.edbert.library.navigationdrawer.NavDrawerItem;
import com.edbert.library.navigationdrawer.NavDrawerItemInterface;
import com.edbert.library.navigationdrawer.NavDrawerItemInterface.Type;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.viewbadger.BadgeView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItemInterface> navDrawerItems;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItemInterface> navDrawerItems) {
		this.context = context;
		//this is a refrence
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			// populate the holder
			if (navDrawerItems.get(position).getNavDrawerType()
					.equals(Type.PROFILE_TYPE)) {

				convertView = mInflater.inflate(R.layout.profile_list_item, parent,
						false);

				holder.title = (TextView) convertView.findViewById(R.id.profile_title);
				holder.image = (ImageView) convertView
						.findViewById(R.id.profile_image);

				holder.profile_settings_icon = (ImageView) convertView
						.findViewById(R.id.profile_settings_icon);
				convertView.setTag(holder);
			} else {
				convertView = mInflater.inflate(R.layout.drawer_list_item, parent,
						false);

				holder.title = (TextView) convertView.findViewById(R.id.drawer_title);
				holder.image = (ImageView) convertView.findViewById(R.id.drawer_icon);

				holder.badge_icon = (BadgeView) convertView
						.findViewById(R.id.drawer_counter);
				convertView.setTag(holder);
			}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// decide how to display this
		if (navDrawerItems.get(position).getNavDrawerType()
				.equals(Type.PROFILE_TYPE)) {
			setUpProfileItem(holder, position);
		} else {
			setUpNonProfileItem(holder, position);
		}
		return convertView;
	}

	private void setUpNonProfileItem(ViewHolder holder, int position) {

		holder.title.setText(navDrawerItems.get(position).getTitle());

		// if we have an img for the thing, we will display it.
		if (navDrawerItems.get(position).getIcon() == NavDrawerItem.NO_ICON) {
			holder.image.setVisibility(View.INVISIBLE);
		} else {
			holder.image.setVisibility(View.VISIBLE);
			holder.image.setImageResource(navDrawerItems.get(position)
					.getIcon());
		}

		if (navDrawerItems.get(position).getCount() != 0) {
			holder.badge_icon.setText(String.valueOf(navDrawerItems.get(
					position).getCount()));
			holder.badge_icon.setVisibility(View.VISIBLE);
		} else {
			holder.badge_icon.setVisibility(View.GONE);
		}

	}

	public void setBadgeNumber(int position, int count) {
		navDrawerItems.get(position).setCount(count);
	}

	private void setUpProfileItem(ViewHolder holder, int position) {
		//set author text
		holder.title.setText("Test User");
	
		holder.profile_settings_icon.setImageResource(R.drawable.settings);
		// if we have an img for the thing, we will display it.

		String imageURL = "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg";
		ImageLoader.getInstance().displayImage(imageURL, holder.image);
		
		// displaying count
		// check whether it set visible or not

	}

	private static class ViewHolder {

		TextView title;
		ImageView image;

		// profile
		ImageView profile_settings_icon;

		// other
		BadgeView badge_icon;
	}
}