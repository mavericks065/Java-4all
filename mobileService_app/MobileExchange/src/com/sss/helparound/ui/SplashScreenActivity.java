package com.sss.helparound.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.sss.helparound.R;
import com.sss.helparound.utils.UserUtil;

public class SplashScreenActivity extends Activity {

	public static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.layout_splash_screen);
        boolean rememberMe = UserUtil.isRememberMe(context);
        
        Intent intent;
        if (rememberMe) {
        	//TODO [PSI] Maybe check if sessionid is not empty
			intent = new Intent(context,  ListAdActivity.class);
        }else {
        	intent = new Intent(context,  LoginActivity.class);
        }
        
    	context.startActivity(intent);
        ((Activity) context).finish();
        
	}
}
