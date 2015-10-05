package com.ng.activityresult;

import com.ng.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ParentActivity extends Activity {

	private TextView helloWorldViewMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parent);

		helloWorldViewMessage = (TextView) findViewById(R.id.helloWorldViewMessage);
	}

	// Method to handle the Click Event on GetMessage Button
	public void getMessage(View V) {
		// Create The Intent and Start The Activity to get The message
		Intent intentGetMessage = new Intent(this, ChildActivity.class);
		startActivityForResult(intentGetMessage, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			if (null != data) {
				String message = data.getStringExtra("MESSAGE");
				// Set the message string in textView
				helloWorldViewMessage.setText("Message from child Activity: "
						+ message);
			}
		}
	}

}
