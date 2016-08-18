package com.sss.helparound.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import com.sss.helparound.R;
import com.sss.helparound.constant.Extra;
import com.sss.helparound.ui.components.items.EntryItem;

public class SettingActivity extends Activity {

	private TextView textView;
	private EntryItem mSettingItem;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        mSettingItem = (EntryItem) b.getSerializable(Extra.SETTING);
        
        setContentView(R.layout.activity_description);
        
        textView = (TextView) findViewById(R.id.description);
        textView.setText(mSettingItem.getTitle());
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
