package com.sss.helparound.async;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import com.sss.helparound.R;
import com.sss.helparound.constant.TechnicalMessage;
import com.sss.helparound.constant.ConstantFields;
import com.sss.helparound.model.User;
import com.sss.helparound.ui.LoginActivity;
import com.sss.helparound.utils.HttpUtils;
import com.sss.helparound.utils.URLUtils;
import com.sss.helparound.utils.UserUtil;

public class UpdateMyAccountTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private User user;

    public UpdateMyAccountTask(Context context, User user) {
        super();
        this.context = context;
        this.user = user;
    }

    @Override
    protected String doInBackground(Void... params) {

        String message = TechnicalMessage.KO;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        final String url = URLUtils.getURL(context, R.string.url_update_user);

        final HttpPost httppost = new HttpPost(url);
		String sessionId = UserUtil.getSessionId(context);
		httppost.addHeader(new BasicHeader("sessionid", sessionId));

        try {
            final MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.addPart(ConstantFields.EMAIL, new StringBody(user.getEmail(), ContentType.TEXT_PLAIN));
            builder.addPart(ConstantFields.PWD, new StringBody(user.getPassword(), ContentType.TEXT_PLAIN));

            if (user.getFirstName() != null && !user.getFirstName().equals("")) {
                builder.addPart(ConstantFields.FIRST_NAME, new StringBody(user.getFirstName(), ContentType.TEXT_PLAIN));
            }
            if (user.getLastName() != null && !user.getLastName().equals("")) {
                builder.addPart(ConstantFields.LAST_NAME, new StringBody(user.getLastName(), ContentType.TEXT_PLAIN));
            }
            if (user.getAddress() != null && !user.getAddress().equals("")) {
                builder.addPart(ConstantFields.ADDRESS, new StringBody(user.getAddress(), ContentType.TEXT_PLAIN));
            }
            if (user.getSexe() != null && !user.getSexe().equals("")) {
                builder.addPart(ConstantFields.SEXE, new StringBody(user.getSexe(), ContentType.TEXT_PLAIN));
            }

            httppost.setEntity(builder.build());
            final HttpResponse response = httpClient.execute(httppost);
            message = HttpUtils.getMessageWithStatusCode(response);
			
			if(TechnicalMessage.OK.equals(message)){
            final JSONArray users = HttpUtils.parseArray(response);
				if (users.length() != 0) {
					JSONObject userObject = users.getJSONObject(0);
					user.setFirstName(userObject.optString(ConstantFields.FIRST_NAME));
					user.setLastName(userObject.optString(ConstantFields.LAST_NAME));
					user.setSexe(userObject.optString(ConstantFields.SEXE));
					user.setAddress(userObject.optString(ConstantFields.ADDRESS));
					user.setPassword(userObject.optString(ConstantFields.PWD));

					SharedPreferences sharedPrefsMyAccount = context.getSharedPreferences(
							context.getString(R.string.prefs), Context.MODE_PRIVATE);
					SharedPreferences.Editor prefsEditor = sharedPrefsMyAccount.edit();
					Gson gson = new Gson();
					String json = gson.toJson(user);
					prefsEditor.putString("user", json);
					prefsEditor.commit();
				}
			}
        } catch (JSONException | IOException e) {
            Log.e(UpdateMyAccountTask.class.toString(), "Exception: " + e.getMessage());
        }
        return message;
    }


	@Override
	protected void onPostExecute(final String message) {
		if (TechnicalMessage.UNAUTHORIZED.equals(message)) {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			LoginActivity.logout();
		} else if (TechnicalMessage.KO.equals(message)) {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		} else {
			//Why launch the activity again ?
			//Intent intent = new Intent(context, MyAccountActivity.class);
			//context.startActivity(intent);
			((Activity) context).finish();
			//TODO change message => confirmation message
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}
	}
}
