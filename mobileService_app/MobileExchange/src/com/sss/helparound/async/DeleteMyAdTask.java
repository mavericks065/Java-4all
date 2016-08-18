package com.sss.helparound.async;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.IOException;

import com.sss.helparound.R;
import com.sss.helparound.constant.ConstantFields;
import com.sss.helparound.constant.TechnicalMessage;
import com.sss.helparound.model.Ad;
import com.sss.helparound.ui.LoginActivity;
import com.sss.helparound.utils.HttpUtils;
import com.sss.helparound.utils.URLUtils;
import com.sss.helparound.utils.UserUtil;

public class DeleteMyAdTask extends AsyncTask<Void, Void, String> {

	private Context parent;
	private Ad ad;

	public DeleteMyAdTask(final Context parent, final Ad ad) {
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
		final String url = URLUtils.getURL(parent, R.string.url_delete_my_ad);
		final HttpDelete httpDelete = new HttpDelete(url);
		String sessionId = UserUtil.getSessionId(parent);
		httpDelete.addHeader(new BasicHeader("sessionid", sessionId));
        httpDelete.setHeader(ConstantFields.TITLE, ad.getTitle());
        httpDelete.setHeader(ConstantFields.DESCRIPTION, ad.getDescription());
        httpDelete.setHeader(ConstantFields.OWNEREMAIL, ad.getOwner().getEmail());
        httpDelete.setHeader(ConstantFields.LON, new Double(longitude).toString());
        httpDelete.setHeader(ConstantFields.LAT, new Double(latitude).toString());

        try {
			final HttpResponse response = httpClient.execute(httpDelete);
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