package com.sss.helparound.async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sss.helparound.R;
import com.sss.helparound.constant.ConstantFields;
import com.sss.helparound.constant.TechnicalMessage;
import com.sss.helparound.model.User;
import com.sss.helparound.ui.LoginActivity;
import com.sss.helparound.utils.URLUtils;

public class UserRegisterTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private User user;

    public UserRegisterTask(final Context context, final User user) {
        super();
        this.context = context;
        this.user = user;

    }

    @Override
    protected String doInBackground(Void... params) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = 0.0;
        double latitude = 0.0;
        if (location != null) { // TODO is used just for the simulation
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        final String url = URLUtils.getURL(context, R.string.url_create_user);

        HttpPut httpput = new HttpPut(url);

        try {

            List<NameValuePair> attributes = new ArrayList<>();
            attributes.add(new BasicNameValuePair(ConstantFields.EMAIL, user.getEmail()));
            attributes.add(new BasicNameValuePair(ConstantFields.PWD, user.getPassword()));
            attributes.add(new BasicNameValuePair(ConstantFields.FIRST_NAME, user.getFirstName()));
            attributes.add(new BasicNameValuePair(ConstantFields.LAST_NAME, user.getLastName()));
            attributes.add(new BasicNameValuePair(ConstantFields.LON, new Double(longitude).toString()));
            attributes.add(new BasicNameValuePair(ConstantFields.LAT, new Double(latitude).toString()));

            httpput.setEntity(new UrlEncodedFormEntity(attributes));

            final HttpResponse response = httpClient.execute(httpput);
            result = String.valueOf(response.getStatusLine().getStatusCode());

        } catch (IOException e) {
            Log.e(UserRegisterTask.class.toString(), "Exception: " + e.getMessage());
            result = "Probléme " + e.getMessage() + " pendant l'enregistrement.";
        }
        return result;
    }

    @Override
    protected void onPostExecute(final String message) {
        if (!TechnicalMessage.OK.equals(message)) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

}
