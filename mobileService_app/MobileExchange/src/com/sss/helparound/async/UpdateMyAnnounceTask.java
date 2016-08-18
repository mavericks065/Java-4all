package com.sss.helparound.async;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sss.helparound.R;
import com.sss.helparound.constant.ConstantFields;
import com.sss.helparound.constant.ItemType;
import com.sss.helparound.constant.TechnicalMessage;
import com.sss.helparound.model.Ad;
import com.sss.helparound.ui.LoginActivity;
import com.sss.helparound.ui.components.items.EntryItem;
import com.sss.helparound.utils.HttpUtils;
import com.sss.helparound.utils.URLUtils;
import com.sss.helparound.utils.UserUtil;

public class UpdateMyAnnounceTask extends AsyncTask<Void, Void, String> {

	private Context parent;
	private Ad ad;

	public UpdateMyAnnounceTask(final Context parent, final Ad ad) {
		super();
		this.parent = parent;
		this.ad = ad;
	}

	@Override
	protected String doInBackground(Void... params) {

        LocationManager locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = 0.0;
        double latitude = 0.0;
        if (location != null) { // TODO is used just for the simulation
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

		String message = TechnicalMessage.KO;
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		final String url = URLUtils.getURL(parent, R.string.url_update_my_ad);
		final HttpPost httppost = new HttpPost(url);
		String sessionId = UserUtil.getSessionId(parent);
		httppost.addHeader(new BasicHeader("sessionid", sessionId));
		
        final MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        final StringBody title = new StringBody(ad.getTitle(), ContentType.TEXT_PLAIN);
        builder.addPart(ConstantFields.TITLE, title);
        final StringBody description = new StringBody(ad.getDescription(), ContentType.TEXT_PLAIN);
        builder.addPart(ConstantFields.DESCRIPTION, description);
        final StringBody price = new StringBody(ad.getPrice(), ContentType.TEXT_PLAIN);
        builder.addPart(ConstantFields.PRICE, price);

        final StringBuilder categories = new StringBuilder();
        for (int i = 0; i< ad.getCategories().size(); i++) {
			categories.append(ad.getCategories().get(i).getTitle());
			if (i != ad.getCategories().size() - 1){
				categories.append(";");
			}
		}
        final StringBody category = new StringBody(categories.toString(), ContentType.TEXT_PLAIN);
        builder.addPart(ConstantFields.CATEGORIES, category);
        final StringBody ownerEmail = new StringBody(ad.getOwner().getEmail(), ContentType.TEXT_PLAIN);
        builder.addPart(ConstantFields.OWNEREMAIL, ownerEmail);
        final StringBody ownerFirstname = new StringBody(ad.getOwner().getFirstName(), ContentType.TEXT_PLAIN);
        builder.addPart(ConstantFields.OWNERFIRSTNAME, ownerFirstname);
        final StringBody ownerLastname = new StringBody(ad.getOwner().getLastName(), ContentType.TEXT_PLAIN);
        builder.addPart(ConstantFields.OWNERLASTNAME, ownerLastname);

        builder.addPart(ConstantFields.LON, new StringBody(new Double(longitude).toString(), ContentType.TEXT_PLAIN));
        builder.addPart(ConstantFields.LAT, new StringBody(new Double(latitude).toString(), ContentType.TEXT_PLAIN));

        httppost.setEntity(builder.build());

		try {
			final HttpResponse response = httpClient.execute(httppost);

            message = HttpUtils.getMessageWithStatusCode(response);
			if (TechnicalMessage.OK.equals(message)){
				final JSONObject announceObject = HttpUtils.parseObject(response);

				List<String> categoriesString = Arrays.asList(announceObject.optString(ConstantFields.CATEGORIES).split(";"));
				List<EntryItem> cate = new ArrayList<>();
				for(String s : categoriesString) {
					cate.add(new EntryItem(s, ItemType.CATEGORY));
				}
				ad.setPrice(announceObject.optString(ConstantFields.PRICE));
				ad.setDescription(announceObject.optString(ConstantFields.DESCRIPTION));
				ad.setCategories(cate);
			}
		} catch (IOException e) {
			message = "IOException: " + e.getMessage();
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
		}else {
			// TODO put confirmation message
			Toast.makeText(parent, message, Toast.LENGTH_LONG).show();
		}
	}

}
