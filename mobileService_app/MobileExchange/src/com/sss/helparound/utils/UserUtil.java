package com.sss.helparound.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import com.sss.helparound.R;
import com.sss.helparound.model.User;

public class UserUtil {
    public static final String USER = "user";
    public static final String SESSION_ID = "sessionid";
    public static final String REMEMBER_ME = "rememberMe";

    public static User getUserConnected(final Activity activity) {
        final SharedPreferences sharedPrefsMyAccount = activity.getSharedPreferences(
                activity.getString(R.string.prefs), Context.MODE_PRIVATE);
        final Gson gson = new Gson();
        final String userJson = sharedPrefsMyAccount.getString(USER, "");
        return gson.fromJson(userJson, User.class);
    }
    
    
    public static String getSessionId(final Context context) {
    	final SharedPreferences prefs = context.getSharedPreferences(
    			context.getString(R.string.prefs), Context.MODE_PRIVATE);
    	final String sessionId = prefs.getString(SESSION_ID, "");
    	return sessionId;
    }
    
	public static boolean isRememberMe(final Context context) {
		final SharedPreferences prefs = context.getSharedPreferences(
				context.getString(R.string.prefs), Context.MODE_PRIVATE);
		boolean rememberMe = prefs.getBoolean(REMEMBER_ME, false);
		return rememberMe;
	}

	public static void clearSession(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(ctx.getString(R.string.prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(REMEMBER_ME, false);
        editor.putString(SESSION_ID, "");
        editor.commit();
		
	}
}
