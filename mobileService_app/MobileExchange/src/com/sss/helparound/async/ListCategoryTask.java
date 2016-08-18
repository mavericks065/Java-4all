package com.sss.helparound.async;

import android.os.AsyncTask;
import android.widget.ListView;
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
import com.sss.helparound.constant.ItemType;
import com.sss.helparound.constant.TechnicalMessage;
import com.sss.helparound.listener.MenuItemListener;
import com.sss.helparound.ui.EntryAdapter;
import com.sss.helparound.ui.ListAdActivity;
import com.sss.helparound.ui.LoginActivity;
import com.sss.helparound.ui.components.items.EntryItem;
import com.sss.helparound.ui.components.items.Item;
import com.sss.helparound.utils.HttpUtils;
import com.sss.helparound.utils.URLUtils;
import com.sss.helparound.utils.UserUtil;

public class ListCategoryTask extends AsyncTask<Void, Void, String> {

	private ListAdActivity parent;
    private EntryAdapter mAdapter;
    public List<EntryItem> mCategories = new ArrayList<>();
    private ListView mListView;

    private final static int MAX_NUMBER_SECTIONS = 8;

    public ListCategoryTask(ListAdActivity parent, EntryAdapter adapter, ListView listView) {
        super();
        this.parent = parent;
        mAdapter = adapter;
        mListView = listView;
    }

    @Override
    protected String doInBackground(Void... params) {
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        final String url = URLUtils.getURL(parent, R.string.url_categories);
        final HttpGet httpget = new HttpGet(url);
        httpget.addHeader(new BasicHeader("sessionid", UserUtil.getSessionId(parent)));
        String message = TechnicalMessage.KO;
        try {

            final HttpResponse response = httpClient.execute(httpget);
            message = HttpUtils.getMessageWithStatusCode(response);
			
			if (TechnicalMessage.OK.equals(message)) {
				final JSONArray categories = HttpUtils.parseArray(response);
				for (int i = 0; i < categories.length(); i++) {
					final JSONObject row = categories.getJSONObject(i);

					final EntryItem category = new EntryItem(row.getString("name"), ItemType.CATEGORY);
					mCategories.add(category);
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

			if (mAdapter.getItems().size() < MAX_NUMBER_SECTIONS) {
				for (Item item : mCategories) {
					mAdapter.add(item);
				}
			}

			mListView.setOnItemClickListener(new MenuItemListener(mAdapter,
					parent));
			//TODO change message => confirmation message
			Toast.makeText(parent, message, Toast.LENGTH_LONG).show();
		}
	}

}
