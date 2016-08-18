package com.sss.helparound.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.sss.helparound.R;
import com.sss.helparound.async.UpdateMyAccountTask;
import com.sss.helparound.model.User;
import com.sss.helparound.utils.UserUtil;

public class MyAccountActivity extends Activity {

    private UpdateMyAccountTask mMyAccountTask = null;
    private Context context;

    // Values for updating / upgrading the account
	private User mUser;
    private String mEmail;
    private String mFirstName;
    private String mLastName;
    private String mOldPasswordFromDatabase;
    private String mOldPassword;
    private String mNewPassword;
    private String mNewPasswordConfirmation;
    private String mAddress;
    private String mSexAccount;

    // constants
    private static final String USER = "user";

    // UI references
    private TextView mEmailView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mAddressView;
    private EditText mOldPasswordView;
    private EditText mNewPasswordView;
    private EditText mNewPasswordConfirmationView;
    private Spinner mSexeSpinner;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        context = this;
		mUser = UserUtil.getUserConnected((Activity) context);

        mOldPasswordFromDatabase = mUser.getPassword();

        mEmailView = (TextView) findViewById(R.id.my_account_mail);
        mEmailView.setText(mUser.getEmail());

        mFirstNameView = (EditText) findViewById(R.id.my_account_first_name);
        mFirstNameView.setText(mUser.getFirstName());

        mLastNameView = (EditText) findViewById(R.id.my_account_last_name);
        mLastNameView.setText(mUser.getLastName());

        mAddressView = (EditText) findViewById(R.id.my_account_address);
        mAddressView.setText(mUser.getAddress());

        mOldPasswordView = (EditText) findViewById(R.id.my_old_account_password);
        mNewPasswordView = (EditText) findViewById(R.id.my_new_password_account);
        mNewPasswordConfirmationView = (EditText) findViewById(R.id.my_new_account_password_confirmation);

        // TODO must move the genre of the people in a configuration file
        List<String> sexAccount = new ArrayList<>();
        sexAccount.add("F");
        sexAccount.add("M");

        mSexeSpinner = (Spinner) findViewById(R.id.my_sexe_account);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1, sexAccount);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSexeSpinner.setAdapter(adapter);
        if (mUser.getSexe().equals("F")) {
            mSexeSpinner.setSelection(0);
        } else {
            mSexeSpinner.setSelection(1);
        }


        findViewById(R.id.update_my_account_information).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateUserInformation(view);
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

    private void updateUserInformation(final View view){

        // Store the values at the time the user wants to update his account
        mEmail = mEmailView.getText().toString();
        mFirstName = mFirstNameView.getText().toString();
        mLastName = mLastNameView.getText().toString();
        mAddress = mAddressView.getText().toString();
        mOldPassword = mOldPasswordView.getText().toString();
        mNewPassword = mNewPasswordView.getText().toString();
        mNewPasswordConfirmation = mNewPasswordConfirmationView.getText().toString();
        mSexAccount = mSexeSpinner.getSelectedItem().toString();

        boolean cancel = false;
        boolean changePWD = !TextUtils.isEmpty(mOldPassword)
                && !TextUtils.isEmpty(mNewPassword)
                && !TextUtils.isEmpty(mNewPasswordConfirmation)
                && mNewPasswordConfirmation.equals(mNewPassword);

        if (!TextUtils.isEmpty(mOldPassword) && !mOldPassword.equals(mOldPasswordFromDatabase)) {
            cancel = true;
        }

        if (cancel) {
            return;
        }
        if (changePWD) {
            if (mSexAccount == null || mSexAccount.equals("")){
                mUser = new User(mEmail, mNewPassword, mFirstName, mLastName, mAddress, "");
            } else {
                mUser = new User(mEmail, mNewPassword, mFirstName, mLastName, mAddress, mSexAccount);
            }
        } else {
            if (mSexAccount == null || mSexAccount.equals("")){
                mUser = new User(mEmail, mOldPasswordFromDatabase, mFirstName, mLastName, mAddress, "");
            } else {
                mUser = new User(mEmail, mOldPasswordFromDatabase, mFirstName, mLastName, mAddress, mSexAccount);
            }
        }
        mMyAccountTask = new UpdateMyAccountTask(this, mUser);
        mMyAccountTask.execute((Void) null);
    }

}
