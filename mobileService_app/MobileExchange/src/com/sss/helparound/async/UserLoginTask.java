package com.sss.helparound.async;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;

import com.sss.helparound.R;
import com.sss.helparound.constant.ConstantFields;
import com.sss.helparound.model.User;
import com.sss.helparound.ui.ListAdActivity;
import com.sss.helparound.ui.LoginActivity;
import com.sss.helparound.utils.HttpUtils;
import com.sss.helparound.utils.URLUtils;
import com.sss.helparound.utils.UserUtil;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

	private LoginActivity loginActivity;
	private User user; 
	private Boolean mRememberMe;
	private EditText mPasswordView;

	public UserLoginTask(LoginActivity loginActivity, User user, Boolean rememberMe, EditText passwordView) {
		super();
		this.loginActivity = loginActivity;
		this.user = user;
		mRememberMe = rememberMe;
		mPasswordView = passwordView;

	}

	@Override
	protected Boolean doInBackground(Void... params) {

        LocationManager locationManager = (LocationManager) loginActivity.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = 0.0;
        double latitude = 0.0;
        if (location != null) { // TODO is used just for the simulation
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        DefaultHttpClient httpClient = new DefaultHttpClient();
        final String url = URLUtils.getURL(loginActivity, R.string.url_login, user.getEmail(), user.getPassword(), longitude, latitude);
        HttpGet httpget = new HttpGet(url);

        boolean result = false;
		try {
            final HttpResponse response = httpClient.execute(httpget);
            List<Cookie> cookies = httpClient.getCookieStore().getCookies();
            String sessionId = "";
            for (Cookie cookie : cookies) {
            	if ("sessionid".equals(cookie.getName())){
            		sessionId = cookie.getValue();
            	}
			}
            JSONObject userObject = HttpUtils.parseObject(response);

            if (userObject.get("toLog").equals("false")) {
                return result;
            }
            result = true;
            user.setFirstName(userObject.optString(ConstantFields.FIRST_NAME));
            user.setLastName(userObject.optString(ConstantFields.LAST_NAME));
            user.setSexe(userObject.optString(ConstantFields.SEXE));
            user.setAddress(userObject.optString(ConstantFields.ADDRESS));

            SharedPreferences prefs = loginActivity.getSharedPreferences(
                    loginActivity.getString(R.string.prefs), Context.MODE_PRIVATE);
            Editor editor = prefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(user);
            editor.putString(UserUtil.USER, json);
            editor.putString(UserUtil.SESSION_ID, sessionId);
            editor.putBoolean(UserUtil.REMEMBER_ME, mRememberMe);
            editor.commit();
                
		} catch (JSONException | IOException e) {
			Log.e(UserLoginTask.class.toString(), "IOException: " + e.getMessage());
			result = false;
		} 
		return result;
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		loginActivity.showProgress(false);
        loginActivity.setmAuthTask(null);

		if (success) {
            loginActivity.finish();
			Intent intent = new Intent(loginActivity, ListAdActivity.class);
			loginActivity.startActivity(intent);
		} else {
			mPasswordView
					.setError(loginActivity.getString(R.string.error_incorrect_password));
			mPasswordView.requestFocus();
		}
	}

	@Override
	protected void onCancelled() {
		loginActivity.showProgress(false);
	}
}
