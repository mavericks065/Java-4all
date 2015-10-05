package com.ng.activityresult;

import com.ng.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ChildActivity extends Activity {
	
	private EditText  editTextMessage;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        // Get the reference of Edit Text
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
    }
    public void submitMessage(View V)
    {
        // get the Entered  message
        String message = editTextMessage.getText().toString();
        Intent intentMessage = new Intent();
 
        // put the message in Intent
        intentMessage.putExtra("MESSAGE",message);
        setResult(2,intentMessage);
        // finish The activity 
        finish();
    }

}
