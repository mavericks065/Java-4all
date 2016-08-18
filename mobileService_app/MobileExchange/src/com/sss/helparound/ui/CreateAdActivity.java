package com.sss.helparound.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import com.sss.helparound.R;
import com.sss.helparound.async.CreateAdTask;
import com.sss.helparound.constant.ItemType;
import com.sss.helparound.model.Ad;
import com.sss.helparound.model.OperatingPlace;
import com.sss.helparound.model.User;
import com.sss.helparound.ui.components.items.EntryItem;
import com.sss.helparound.ui.components.items.Item;
import com.sss.helparound.utils.UserUtil;

public class CreateAdActivity extends Activity {

    private Context context;
    private List<Spinner> mCategorySpinner;
    private Spinner mPerimeterSpinner;
    private User mUser;

    private static final List<String> mPerimeterList = new ArrayList<>(8);
    static {
        mPerimeterList.add("0 km");
        mPerimeterList.add("1 km");
        mPerimeterList.add("5 km");
        mPerimeterList.add("10 km");
        mPerimeterList.add("20 km");
        mPerimeterList.add("50 km");
        mPerimeterList.add("100 km");
        mPerimeterList.add("+ 100 km");
    }

    public CreateAdActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_create_ad);
        final LinearLayout linear = (LinearLayout) findViewById(R.id.create_ad_spinners);
        mCategorySpinner = new ArrayList<>();

        createPerimeterSpinner();
        createSpinnerLine(linear);

        final Button button = (Button) findViewById(R.id.create_ad_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final EditText title = (EditText) findViewById(R.id.create_ad_title);
                final EditText description = (EditText) findViewById(R.id.create_ad_description);
                final EditText price = (EditText) findViewById(R.id.create_ad_price);

                mUser = UserUtil.getUserConnected((Activity) context);

                final ArrayList<EntryItem> categories = new ArrayList<>();
                for (Spinner s : mCategorySpinner) {
                    categories
                            .add(new EntryItem(s.getSelectedItem().toString(), ItemType.CATEGORY));
                }

                final EditText address = (EditText) findViewById(R.id.create_ad_address);
                final EditText cityAddress = (EditText) findViewById(R.id.create_ad_address_city);
                final EditText zipAddress = (EditText) findViewById(R.id.create_ad_address_zip_code);
                final CheckBox isRemoteWorking = (CheckBox) findViewById(R.id.create_ad_remote_checkbox);
                //final Spinner perimeterSpinner = (Spinner) findViewById(R.id.create_ad__spinner_address_perimeter);

                final OperatingPlace operatingPlace = new OperatingPlace(address.getText().toString(), cityAddress.getText().toString(),
                                                                         zipAddress.getText().toString(), mPerimeterSpinner.getSelectedItem().toString(),
                                                                         isRemoteWorking.isChecked());
                final Ad ad = new Ad(title.getText().toString(), description.getText().toString(),
                                        price.getText().toString(), categories, mUser, operatingPlace);

                new CreateAdTask(context, ad).execute();

                ListAdActivity.mAdapterAd.add(ad);
                ListAdActivity.mAdapterAd.notifyDataSetChanged();
                finish();
                }
        });
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

    private void createPerimeterSpinner() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, mPerimeterList);
        mPerimeterSpinner = (Spinner) findViewById(R.id.create_ad__spinner_address_perimeter);
        mPerimeterSpinner.setAdapter(adapter);
    }

    private void createSpinnerLine(final LinearLayout linear) {
        final LinearLayout horizontal = new LinearLayout(this);
        horizontal.setOrientation(LinearLayout.HORIZONTAL);
        final Spinner spinner = createSpinner(ListAdActivity.mItems);
        mCategorySpinner.add(spinner);
        horizontal.addView(spinner);
        final Button plus = new Button(this);
        plus.setBackgroundResource(android.R.drawable.arrow_down_float);
        plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createSpinnerLine(linear);
            }
        });
        horizontal.addView(plus);

        if (mCategorySpinner.size() > 1) {
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

    private Spinner createSpinner(List<Item> items) {
        Spinner spinner = new Spinner(this);
        List<String> categoryList = new ArrayList<>();

        for (Item item : items) {
            if ((item instanceof EntryItem) &&
                    (ItemType.CATEGORY.equals((((EntryItem)item).getItemType())))) {
                categoryList.add((((EntryItem)item).getTitle()));
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categoryList);

        spinner.setAdapter(adapter);
        return spinner;
    }
}
