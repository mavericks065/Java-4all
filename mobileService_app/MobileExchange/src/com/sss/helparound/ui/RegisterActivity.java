package com.sss.helparound.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.sss.helparound.R;
import com.sss.helparound.async.UserRegisterTask;
import com.sss.helparound.model.User;

public class RegisterActivity extends Activity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmedPasswordView;
    private EditText mFirstnameView;
    private EditText mLastnameView;

    // Values for email and password at the time of the login attempt.
    private String mEmail;
    private String mPassword;
    private String mFirstName;
    private String mLastName;
    private String mConfirmedPassword;

    private UserRegisterTask mRegisterTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (EditText) findViewById(R.id.mail);
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmedPasswordView = (EditText) findViewById(R.id.confirmPassword);
        mFirstnameView = (EditText) findViewById(R.id.firstName);
        mLastnameView = (EditText) findViewById(R.id.lastName);

        findViewById(R.id.create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister(view);
            }
        });
    }

    private void attemptRegister(View view) {

        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();
        mConfirmedPassword = mConfirmedPasswordView.getText().toString();
        mFirstName = mFirstnameView.getText().toString();
        mLastName = mLastnameView.getText().toString();

        View focusView = null;
        boolean cancel = false;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(mConfirmedPassword)) {
            mConfirmedPasswordView.setError(getString(R.string.error_field_required));
            focusView = mConfirmedPasswordView;
            cancel = true;
        } else if (mConfirmedPassword.length() < 4) {
            mConfirmedPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mConfirmedPasswordView;
            cancel = true;
        } else if (!mConfirmedPassword.equals(mPassword)) {
            mConfirmedPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mConfirmedPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        final User newUser = new User(mEmail, mPassword, mFirstName, mLastName);

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mRegisterTask = new UserRegisterTask(this, newUser);
            mRegisterTask.execute((Void) null);
        }
    }

}