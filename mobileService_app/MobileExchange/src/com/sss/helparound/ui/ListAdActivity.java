package com.sss.helparound.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;

import com.sss.helparound.R;
import com.sss.helparound.async.ListAdTask;
import com.sss.helparound.async.ListCategoryTask;
import com.sss.helparound.constant.Extra;
import com.sss.helparound.constant.ItemType;
import com.sss.helparound.listener.ResultsItemListener;
import com.sss.helparound.model.Ad;
import com.sss.helparound.model.Filter;
import com.sss.helparound.model.User;
import com.sss.helparound.ui.components.items.EntryItem;
import com.sss.helparound.ui.components.items.Item;
import com.sss.helparound.ui.components.items.SectionItem;
import com.sss.helparound.ui.components.toggles.MenuToggle;

public class ListAdActivity extends Activity {

    public static ArrayAdapter<Ad> mAdapterAd;
    public static ArrayList<Ad> mResultList = new ArrayList<>();

    private ListView mListViewAd;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;

    private Filter mFilter;

    public static boolean mIsMyAd;
    public EntryAdapter mDrawerAdapter;
    public static ArrayList<Item> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mItems == null) {
            mItems = new ArrayList<>();
            mItems.addAll(Arrays.asList(new EntryItem(this.getString(R.string.menu_home),
                    ItemType.HOME_PAGE), new EntryItem(this.getString(R.string.menu_disconnection),
                    ItemType.LOG_OUT), new SectionItem(this.getString(R.string.menu_profile)),
                    new EntryItem(this.getString(R.string.menu_my_account),
                    ItemType.MY_ACCOUNT), new EntryItem(this.getString(R.string.menu_my_ads),
                    ItemType.MY_ADS), new EntryItem(this.getString(R.string.menu_create_ad),
                    ItemType.CREATE_AD), new SectionItem(this.getString(R.string.menu_categories))));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mFilter = new Filter();
        createListView();
        createDrawerView();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            refreshAds(query);
            mDrawerLayout.closeDrawers();
        }
    }

    private void createListView() {
        mListViewAd = (ListView) findViewById(R.id.content_frame);

        mListViewAd.setOnItemClickListener(new ResultsItemListener(
                mResultList, this));

        mAdapterAd = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        refreshAds();

        mListViewAd.setAdapter(mAdapterAd);
    }

    private void createDrawerView() {
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);

        mDrawerAdapter = new EntryAdapter(this, mItems);

        new ListCategoryTask(this, mDrawerAdapter, mDrawerListView).execute();

        mDrawerListView.setAdapter(mDrawerAdapter);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        mDrawerToggle = new MenuToggle(this,
                mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.result, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * every time we come back on ListAdActivity this method is triggered
     * to find if we there is result data passed by the children activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) if (resultCode == RESULT_OK) {
            Ad ad = (Ad) data.getExtras().getSerializable(Extra.RESULT);

            for (int i = 0; i < mAdapterAd.getCount(); i++) {
                if (ad.equals(mAdapterAd.getItem(i))) {
                    mAdapterAd.getItem(i).setCategories(ad.getCategories());
                    mAdapterAd.getItem(i).setDescription(ad.getDescription());
                    mAdapterAd.getItem(i).setPrice(ad.getPrice());
                }
            }
            mAdapterAd.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshAds(mFilter);
                break;

            default:
                // do nothing
                break;
        }
        return false;
    }



    public void refreshAds() {
        mFilter.setCategory(null);
        mFilter.setQuery(null);
        mFilter.setUser(null);
        mIsMyAd = false;
        refreshAds(mFilter);
    }

    public void refreshAds(final EntryItem category) {
        mFilter.setCategory(category);
        mFilter.setQuery(null);
        mFilter.setUser(null);
        mIsMyAd = false;
        refreshAds(mFilter);
    }

    public void refreshAds(final User user) {
        mFilter.setCategory(null);
        mFilter.setQuery(null);
        mFilter.setUser(user);
        mIsMyAd = true;
        refreshAds(mFilter);
    }

    public void refreshAds(final String query) {
        mFilter.setCategory(null);
        mFilter.setQuery(query);
        mFilter.setUser(null);
        mIsMyAd = false;
        refreshAds(mFilter);
    }


    private void refreshAds(final Filter filter){
        mResultList.clear();
        mAdapterAd.clear();
        new ListAdTask(this, mAdapterAd, mResultList, filter).execute();
    }
}
