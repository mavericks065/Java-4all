package com.sss.helparound.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.sss.helparound.R;

public class ForgotPasswordActivity extends Activity{

    // UI references.
    private EditText mEmailView;

    // Values for email and password at the time of the login attempt.
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mEmailView = (EditText) findViewById(R.id.mail);

        findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(view);
            }
        });
    }

    private void reset(View view) {

        View focusView = null;
        boolean cancel = false;

        mEmail = mEmailView.getText().toString();

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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "nicolas065@gmail.com",
                    null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HelpAround: Password reset request");

            final StringBuilder shareMessage = new StringBuilder("Hi, Can you please reset my password? My login is ");
            shareMessage.append(mEmail);
            emailIntent
                    .putExtra(
                            Intent.EXTRA_TEXT,
                            shareMessage.toString());
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }
    }

}
