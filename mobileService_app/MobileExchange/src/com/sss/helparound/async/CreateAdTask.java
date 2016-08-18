package com.sss.helparound.async;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sss.helparound.R;
import com.sss.helparound.constant.ConstantFields;
import com.sss.helparound.constant.TechnicalMessage;
import com.sss.helparound.model.Ad;
import com.sss.helparound.ui.LoginActivity;
import com.sss.helparound.utils.HttpUtils;
import com.sss.helparound.utils.URLUtils;
import com.sss.helparound.utils.UserUtil;

public class CreateAdTask extends AsyncTask<Void, Void, String> {

	private Context parent;
	private Ad ad;

	public CreateAdTask(final Context parent, final Ad ad) {
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
		final String url = URLUtils.getURL(parent, R.string.url_create_ad);
		final HttpPut httpPut = new HttpPut(url);
		String sessionId = UserUtil.getSessionId(parent);
		httpPut.addHeader(new BasicHeader("sessionid", sessionId));

		try {
            List<NameValuePair> attributes = new ArrayList<>();
            attributes.add(new BasicNameValuePair(ConstantFields.TITLE, ad.getTitle()));
            attributes.add(new BasicNameValuePair(ConstantFields.DESCRIPTION, ad.getDescription()));
            attributes.add(new BasicNameValuePair(ConstantFields.PRICE, ad.getPrice()));
            attributes.add(new BasicNameValuePair("operatingPlaceCity", ad.getPlace().getCity()));
            attributes.add(new BasicNameValuePair("operatingPlaceZip", ad.getPlace().getZip()));
            attributes.add(new BasicNameValuePair("operatingPlaceAddress", ad.getPlace().getAddress()));
            attributes.add(new BasicNameValuePair("operatingPlacePerimeter", ad.getPlace().getPerimeter()));
            attributes.add(new BasicNameValuePair("operatingPlaceRemoteService", ad.getPlace().isRemoteService() ? "true" : "false"));

            final StringBuilder categories = new StringBuilder();
            for (int i = 0; i< ad.getCategories().size(); i++) {
                categories.append(ad.getCategories().get(i).getTitle());
                if (i != ad.getCategories().size() - 1){
                    categories.append(";");
                }
            }

            attributes.add(new BasicNameValuePair(ConstantFields.CATEGORIES, categories.toString()));
            attributes.add(new BasicNameValuePair(ConstantFields.OWNEREMAIL, ad.getOwner().getEmail()));
            attributes.add(new BasicNameValuePair(ConstantFields.OWNERFIRSTNAME, ad.getOwner().getFirstName()));
            attributes.add(new BasicNameValuePair(ConstantFields.OWNERLASTNAME, ad.getOwner().getLastName()));
            attributes.add(new BasicNameValuePair(ConstantFields.LON, new Double(longitude).toString()));
            attributes.add(new BasicNameValuePair(ConstantFields.LAT, new Double(latitude).toString()));

            httpPut.setEntity(new UrlEncodedFormEntity(attributes));
			final HttpResponse response = httpClient.execute(httpPut);
			message = HttpUtils.getMessageWithStatusCode(response);
			
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
		} else {
			//TODO change message => confirmation message
			Toast.makeText(parent, message, Toast.LENGTH_LONG).show();
		}
	}

}
