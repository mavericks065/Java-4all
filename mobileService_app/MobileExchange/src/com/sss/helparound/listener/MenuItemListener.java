package com.sss.helparound.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.sss.helparound.R;
import com.sss.helparound.constant.Extra;
import com.sss.helparound.constant.ItemType;
import com.sss.helparound.model.User;
import com.sss.helparound.ui.CreateAdActivity;
import com.sss.helparound.ui.EntryAdapter;
import com.sss.helparound.ui.ListAdActivity;
import com.sss.helparound.ui.LoginActivity;
import com.sss.helparound.ui.MyAccountActivity;
import com.sss.helparound.ui.SettingActivity;
import com.sss.helparound.ui.components.items.EntryItem;
import com.sss.helparound.utils.UserUtil;

public class MenuItemListener implements ListView.OnItemClickListener {

	private EntryAdapter entryAdapter;
	private ListAdActivity mParent;
    private User mUser;

    public MenuItemListener(final EntryAdapter settings, final ListAdActivity parent) {
		super();
		this.entryAdapter = settings;
		this.mParent = parent;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
			Intent intent = null;
			EntryItem entryItem = (EntryItem) entryAdapter.getItem(position);
			ItemType itemType = entryItem.getItemType();
			switch (itemType) {
				case CREATE_AD:
					intent = new Intent(mParent, CreateAdActivity.class);
					Bundle b = new Bundle();
		            b.putSerializable(Extra.SETTING, itemType);
		            intent.putExtras(b);
					break;

				case MY_ACCOUNT:
					intent = new Intent(mParent, MyAccountActivity.class);
					break;

				case HOME_PAGE :
                    mParent.refreshAds();
					((DrawerLayout) mParent.findViewById(R.id.drawer_layout)).closeDrawers();
		         break;

                case MY_ADS:
                    mUser = UserUtil.getUserConnected(mParent);
                    mParent.refreshAds(mUser);
                    ((DrawerLayout) mParent.findViewById(R.id.drawer_layout)).closeDrawers();
                    break;

				case LOG_OUT:
	                // Perform logout : forget rememberMe and display login
	                // store rememberMe value
	                SharedPreferences settings = mParent.getSharedPreferences(
	                		mParent.getString(R.string.prefs), Context.MODE_PRIVATE);
	                SharedPreferences.Editor editor = settings.edit();
	                editor.putBoolean("rememberMe", false);
	                editor.commit();
	                ((Activity) mParent).finish();
	                // display login
	                intent = new Intent(mParent, LoginActivity.class);
					break;
				case CATEGORY:
					mParent.refreshAds((EntryItem) (entryAdapter.getItem(position)));
					((DrawerLayout) mParent.findViewById(R.id.drawer_layout)).closeDrawers();
					break;
				default:
					intent = new Intent(mParent, SettingActivity.class);
					Bundle bundleSetting = new Bundle();
		            bundleSetting.putSerializable(Extra.SETTING, itemType);
		            intent.putExtras(bundleSetting);
					break;
			}
			
            if (intent!=null){
            	mParent.startActivity(intent);
            }
			
		}

}
