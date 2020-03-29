package com.heaven.vegetable.main;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.heaven.vegetable.R;
import com.heaven.vegetable.model.UserDetails;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.ConstantValues;

public class GetStartedMobileNumberActivity extends AppCompatActivity {
    //    View view;
//    View viewToolbar;
//    ImageView ivBack;

    LinearLayout llConfirm;
    EditText etMobileNumber;
    EditText etFName;
    EditText etLName;

    final int RESOLVE_PHONE_HINT = 100;
    private final int REQUEST_CODE_MOBILE_OTP_ACTIVITY = 101;
    String flagCalledFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_mobile_number);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flagCalledFrom = bundle.getString("CalledFrom");
        }

        init();
        componentEvents();
        setNameData();
        getHintPhoneNumber();
    }

    private void init() {
//        viewToolbar = findViewById(R.id.view_toolbar);
//        ivBack = viewToolbar.findViewById(R.id.iv_back);

        llConfirm = findViewById(R.id.ll_confirm);
        etMobileNumber = findViewById(R.id.et_mobileNumber);
        etFName = findViewById(R.id.et_fname);
        etLName = findViewById(R.id.et_lname);

//        tvTitle = (TextView) findViewById(R.id.tv_title);
//        tvLogin = (TextView) findViewById(R.id.tv_login);
//        tvSignUp = (TextView) findViewById(R.id.tv_signUp);

//        Typeface custom_fonts = Typeface.createFromAsset(getAssets(), "fonts/ArgonPERSONAL-Regular.otf");
//        tvTitle.setTypeface(custom_fonts);
    }

    private void componentEvents() {
        llConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();

                Intent intent = new Intent(GetStartedMobileNumberActivity.this, GetStartedVerifyOTPActivity.class);
                intent.putExtra("CalledFrom", flagCalledFrom);

                if (flagCalledFrom != null && flagCalledFrom.equalsIgnoreCase(ConstantValues.ACTIVITY_CART_ACTION_PLACE_ORDER)) {
                    startActivityForResult(intent, REQUEST_CODE_MOBILE_OTP_ACTIVITY);

                } else {
                    startActivity(intent);
                    finish();
                }

            }
        });
    }


    // Construct a request for phone numbers and show the picker
    private void getHintPhoneNumber() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(hintRequest);

        try {
            startIntentSenderForResult(intent.getIntentSender(), RESOLVE_PHONE_HINT, null, 0, 0, 0);

        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setNameData() {
        UserDetails userDetails = Application.userDetails;
        if (userDetails.getFirstName() != null) {
            etFName.setText(userDetails.getFirstName());
        }

        if (userDetails.getLastName() != null) {
            etLName.setText(userDetails.getLastName());
        }
    }

    private void saveUserData() {
        String fname = etFName.getText().toString().trim();
        String lname = etLName.getText().toString().trim();
        String mobileNo = etMobileNumber.getText().toString().trim();
        String fullName = fname.concat(" ").concat(lname);

        Application.userDetails.setFirstName(fname);
        Application.userDetails.setLastName(lname);
        Application.userDetails.setFullName(fullName);
        Application.userDetails.setMobile(mobileNo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_PHONE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                String phoneNumber = cred.getId().substring(3);
                etMobileNumber.setText(phoneNumber);
            }
        } else if (requestCode == REQUEST_CODE_MOBILE_OTP_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

                String flag = data.getExtras().getString("MESSAGE");
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", flag);
                setResult(RESULT_OK, intent);
                finish();
            }

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (flagCalledFrom != null && flagCalledFrom.equalsIgnoreCase(ConstantValues.ACTIVITY_CART_ACTION_PLACE_ORDER)) {
            Intent intent = new Intent();
            intent.putExtra("MESSAGE", "MOBILE_NOT_VERIFIED");
            setResult(RESULT_CANCELED, intent);
            finish();
        } else {
//            Intent it = new Intent(this, GetStartedActivity.class);
//            startActivity(it);
            finish();
        }


    }
}
