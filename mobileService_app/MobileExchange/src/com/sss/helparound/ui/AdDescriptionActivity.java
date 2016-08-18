package com.sss.helparound.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import com.sss.helparound.R;
import com.sss.helparound.constant.Extra;
import com.sss.helparound.model.Ad;

public class AdDescriptionActivity extends Activity {
	private TextView textView;
	private Ad mAd;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        mAd = (Ad) b.getSerializable(Extra.RESULT);
        
        setContentView(R.layout.activity_description);
        
        textView = (TextView) findViewById(R.id.title);
        textView.setText(mAd.getTitle());
        
        textView = (TextView) findViewById(R.id.description);
        textView.setText(mAd.getDescription());
        
        textView = (TextView) findViewById(R.id.price);
        textView.setText(mAd.getPrice());
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

		   
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
