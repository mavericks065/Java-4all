package com.sss.helparound.async;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sss.helparound.R;
import com.sss.helparound.constant.ConstantFields;
import com.sss.helparound.constant.ItemType;
import com.sss.helparound.constant.TechnicalMessage;
import com.sss.helparound.model.Ad;
import com.sss.helparound.model.Filter;
import com.sss.helparound.model.User;
import com.sss.helparound.ui.ListAdActivity;
import com.sss.helparound.ui.LoginActivity;
import com.sss.helparound.ui.components.items.EntryItem;
import com.sss.helparound.utils.HttpUtils;
import com.sss.helparound.utils.URLUtils;
import com.sss.helparound.utils.UserUtil;

public class ListAdTask extends AsyncTask<Void, Void, String> {

    private ListAdActivity parent;
    private ArrayAdapter<Ad> mAdapter;
    private List<Ad> mAdsList;
    private Filter mFilter;

    public ListAdTask(final ListAdActivity parent, final ArrayAdapter<Ad> adapter, final ArrayList<Ad> resultList,
                      final Filter filter) {
        super();
        this.parent = parent;
        mAdapter = adapter;
        mAdsList = resultList;
        mFilter = filter;
    }

    @Override
    protected String doInBackground(Void... params) {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "";

        if (mFilter.getCategory() != null) {
            final String category = URLUtils.encode(mFilter.getCategory().getTitle());
            url = URLUtils.getURL(parent, R.string.url_ads_by_categories, category);
        } else if (mFilter.getUser() != null) {
            final String user = URLUtils.encode(mFilter.getUser().getEmail());
            url = URLUtils.getURL(parent, R.string.url_ads_by_user, user);
        } else if (mFilter.getQuery() != null) {
            final String query = URLUtils.encode(mFilter.getQuery());
            url = URLUtils.getURL(parent, R.string.url_ads_by_string, query);
        } else {
            url = URLUtils.getURL(parent, R.string.url_ads);
        }

        HttpGet httpget = new HttpGet(url);
        String sessionId = UserUtil.getSessionId(parent);
        httpget.addHeader(new BasicHeader("sessionid", sessionId));
        String message = TechnicalMessage.KO;
        try {
            final HttpResponse response = httpClient.execute(httpget);
            message = HttpUtils.getMessageWithStatusCode(response);

            if (TechnicalMessage.OK.equals(message)) {
                JSONArray announces = HttpUtils.parseArray(response);
                for (int i = 0; i < announces.length(); i++) {
                    JSONObject row = announces.getJSONObject(i);
                    Ad ad = new Ad();
                    final String title = row.getString(ConstantFields.TITLE);
                    ad.setTitle(title);
                    final String description = row.getString(ConstantFields.DESCRIPTION);
                    ad.setDescription(description);
                    final String price = row.getString(ConstantFields.PRICE);
                    ad.setPrice(price);
                    final JSONArray categoriesTab = row.getJSONArray(ConstantFields.CATEGORIES);
                    List<EntryItem> categories = new ArrayList<>();

                    for (int j = 0; j < categoriesTab.length(); j++) {
                        categories.add(new EntryItem(categoriesTab.getString(j), ItemType.CATEGORY));
                    }
                    ad.setCategories(categories);

                    final JSONObject ownerObject = (JSONObject) row.get("owner");
                    final User owner = new User();

                    owner.setEmail(ownerObject.getString(ConstantFields.EMAIL));
                    owner.setFirstName(ownerObject.getString(ConstantFields.FIRST_NAME));
                    owner.setLastName(ownerObject.getString(ConstantFields.LAST_NAME));

                    ad.setOwner(owner);

                    mAdsList.add(ad);
                }
            }
        } catch (IOException e) {
            message = "IOException: " + e.getMessage();
        } catch (JSONException e) {
            message = "JSONException: " + e.getMessage();
        } catch (Exception e) {
            message = "Unknown exception: " + e.getMessage();
        }
        return message;
    }

    @Override
    protected void onPostExecute(String message) {
        if (TechnicalMessage.UNAUTHORIZED.equals(message)) {
            Toast.makeText(parent, message, Toast.LENGTH_LONG).show();
            LoginActivity.logout();
        } else if (TechnicalMessage.KO.equals(message)) {
            Toast.makeText(parent, message, Toast.LENGTH_LONG).show();
        } else {
            // mAdapter.addAll(mAdsList); on API >= 11
            for (Ad ad : mAdsList) {
                mAdapter.add(ad);
            }

            mAdapter.notifyDataSetChanged();

            //TODO change message => confirmation message
            Toast.makeText(parent, message, Toast.LENGTH_LONG).show();
        }
    }

}
