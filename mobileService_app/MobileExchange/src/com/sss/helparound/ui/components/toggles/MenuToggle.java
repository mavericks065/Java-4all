package com.sss.helparound.ui.components.toggles;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class MenuToggle extends ActionBarDrawerToggle{
	
	private Activity activity;

	public MenuToggle(Activity activity,
                      DrawerLayout drawerLayout, int drawerImageRes,
                      int openDrawerContentDescRes, int closeDrawerContentDescRes) {
		super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes,
				closeDrawerContentDescRes);
		this.activity = activity;
	}
	
	/** Called when a drawer has settled in a completely closed state. */
	public void onDrawerClosed(View view) {
		super.onDrawerClosed(view);
		setActionBarTitle("Mes résultats");
	}
	
	/** Called when a drawer has settled in a completely open state. */
	public void onDrawerOpened(View drawerView) {
		super.onDrawerOpened(drawerView);
		setActionBarTitle("Réglages");
	}

	private void setActionBarTitle(String title) {
			activity.getActionBar().setTitle(title);
	}
}
