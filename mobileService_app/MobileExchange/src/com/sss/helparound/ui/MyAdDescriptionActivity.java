package com.sss.helparound.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.sss.helparound.R;
import com.sss.helparound.async.DeleteMyAdTask;
import com.sss.helparound.async.UpdateMyAnnounceTask;
import com.sss.helparound.constant.Extra;
import com.sss.helparound.constant.ItemType;
import com.sss.helparound.model.Ad;
import com.sss.helparound.model.User;
import com.sss.helparound.ui.components.items.EntryItem;
import com.sss.helparound.ui.components.items.Item;
import com.sss.helparound.utils.UserUtil;

public class MyAdDescriptionActivity extends Activity {

    private TextView mAdTitle;
    private EditText mAdDescription;
    private EditText mAdPrice;
    private List<Spinner> mSpinners;

    private User mUser;
    private Ad mAd;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        setContentView(R.layout.activity_my_ad_description);
        final LinearLayout linear = (LinearLayout) findViewById(R.id.display_my_ad_spinners);

        context = this;
        mSpinners = new ArrayList<>();
        mAd = (Ad) b.getSerializable(Extra.RESULT);

        mAdTitle = (TextView) findViewById(R.id.textview_title_my_ad);
        mAdTitle.setText(mAd.getTitle());

        mAdDescription = (EditText) findViewById(R.id.description);
        mAdDescription.setText(mAd.getDescription());

        mAdPrice = (EditText) findViewById(R.id.price);
        mAdPrice.setText(mAd.getPrice());

    	for (EntryItem ci : mAd.getCategories()){
    		createSpinnerLine(ci, linear);
    	}

        final Button modificationButton = (Button) findViewById(R.id.update_my_ad_information);

        modificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAdInformationAction();
            }
        });

        final Button deleteButton = (Button) findViewById(R.id.delete_my_ad_information);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAdAction();
            }
        });
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Is called when delete button has been clicked on
     */
    private void deleteAdAction() {
        mUser = UserUtil.getUserConnected((Activity) context);
        final Ad ad = new Ad(mAd.getTitle(),
                mAd.getDescription(), mAd.getPrice(), mAd.getCategories(), mUser);

        new DeleteMyAdTask(context, ad).execute();

        for (int i = 0; i< ListAdActivity.mAdapterAd.getCount(); i++){
            if (ListAdActivity.mAdapterAd.getItem(i).equals(mAd)){
                ListAdActivity.mAdapterAd.remove(mAd);
            }
        }
        ListAdActivity.mAdapterAd.notifyDataSetChanged();
        finish();
    }

    /**
     * Is called when update button has been clicked on
     */
    private void updateAdInformationAction() {
        final List<EntryItem> categories = new ArrayList<>();
        mUser = UserUtil.getUserConnected((Activity) context);

        for (Spinner s : mSpinners) {
            categories
                    .add(new EntryItem(s.getSelectedItem().toString(), ItemType.CATEGORY));
        }

        final Ad ad = new Ad(mAdTitle.getText().toString(),
                mAdDescription.getText().toString(), mAdPrice.getText().toString(), categories, mUser);

        new UpdateMyAnnounceTask(context, ad).execute();

        final Intent intent = new Intent(context, ListAdActivity.class);
        final Bundle b = new Bundle();
        b.putSerializable(Extra.RESULT, ad);
        intent.putExtras(b);
        setResult(RESULT_OK, intent);

        finish();

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

    private void createSpinnerLine(final EntryItem ci, final LinearLayout linear) {
        final LinearLayout horizontal = new LinearLayout(this);
        horizontal.setOrientation(LinearLayout.HORIZONTAL);

        final List<Item> allCategoryItems = ListAdActivity.mItems;
        final Spinner spinner = createSpinner(ci, allCategoryItems);
        
        mSpinners.add(spinner);
        horizontal.addView(spinner);

        final Button plus = new Button(this);

        plus.setBackgroundResource(android.R.drawable.arrow_down_float);
        plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createSpinnerLine(ci, linear);
            }
        });
        horizontal.addView(plus);

        if (mSpinners.size() > 1) {
            final Button minus = new Button(this);
            minus.setBackgroundResource(android.R.drawable.arrow_up_float);
            minus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    linear.removeView(horizontal);
                }
            });
            horizontal.addView(minus);
        }
        linear.addView(horizontal);
    }

    /**
     * Method for creating one spinner and selecting the good information
     * corresponding to what it had in database
     * @param ci
     * @param items
     * @return
     */
    private Spinner createSpinner(EntryItem ci, final List<Item> items) {
    	
    	Spinner spinner = new Spinner(this);

        List<String> categoryList = new ArrayList<>();
        for (Item item : items) {
            if ((item instanceof EntryItem) &&
                    (ItemType.CATEGORY.equals((((EntryItem)item).getItemType())))) {
                categoryList.add(((EntryItem) item).getTitle());
            }
        }
        

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categoryList);

        spinner.setAdapter(adapter);
        
        int i = 0 ;
        for (String category : categoryList) {
        	if (category.equals(ci.getTitle())){
        		spinner.setSelection(i);
        	}
        	i++;
        }
        return spinner;
    }
}