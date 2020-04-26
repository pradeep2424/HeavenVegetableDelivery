package com.heaven.vegetable.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.heaven.vegetable.R;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.main.GetStartedMobileNumberActivity;
import com.heaven.vegetable.main.GetStartedVerifyOTPActivity;
import com.heaven.vegetable.model.UnitObject;
import com.heaven.vegetable.model.UserDetails;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.ConstantValues;
import com.heaven.vegetable.utils.InternetConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    DialogLoadingIndicator progressIndicator;

    RelativeLayout rlRootLayout;

    TextView tvFullName;
    EditText etFName;
    EditText etLName;
    EditText etEmailID;
    TextView tvMobile;
    TextView tvSave;

    String fname;
    String lname;
    String fullName;
    String email;

    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        init();
        events();
        setUserInformation();
    }

    private void init() {
        userDetails = Application.userDetails;
        progressIndicator = DialogLoadingIndicator.getInstance();

        rlRootLayout = findViewById(R.id.rl_rootLayout);
        tvFullName = findViewById(R.id.tv_fullName);
        etFName = findViewById(R.id.et_fname);
        etLName = findViewById(R.id.et_lname);
        etEmailID = findViewById(R.id.et_emailID);
        tvMobile = findViewById(R.id.tv_mobile);
        tvSave = findViewById(R.id.tv_save);

    }

    private void events() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = etFName.getText().toString().trim();
                lname = etLName.getText().toString().trim();
                email = etEmailID.getText().toString().trim();
                fullName = fname.concat(" ").concat(lname);

                updateUserProfile();
            }
        });

        tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSnackbarErrorMsg(getResources().getString(R.string.can_edit_mobile_no));
            }
        });
    }

    private void setUserInformation() {
        if (Application.userDetails != null) {

            if (Application.userDetails.getFullName() != null) {
                tvFullName.setText(Application.userDetails.getFullName());
            }

            if (Application.userDetails.getFirstName() != null) {
                etFName.setText(Application.userDetails.getFirstName());
            }

            if (Application.userDetails.getLastName() != null) {
                etLName.setText(Application.userDetails.getLastName());
            }

            if (Application.userDetails.getEmail() != null) {
                etEmailID.setText(Application.userDetails.getEmail());
            }

            if (Application.userDetails.getMobile() != null) {
                tvMobile.setText(Application.userDetails.getMobile());
//                tvMobile.setText("9594******");
            }
        }
    }

    private void saveUserData() {
        String fname = etFName.getText().toString().trim();
        String lname = etLName.getText().toString().trim();
        String fullName = fname.concat(" ").concat(lname);
        String email = etEmailID.getText().toString().trim();
        String mobileNo = tvMobile.getText().toString().trim();

        Application.userDetails.setFirstName(fname);
        Application.userDetails.setLastName(lname);
        Application.userDetails.setFullName(fullName);
        Application.userDetails.setEmail(email);
        Application.userDetails.setMobile(mobileNo);
    }

    private void updateUserProfile() {
        if (InternetConnection.checkConnection(this)) {
            showDialog();

            int userID = userDetails.getUserID();

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.updateUserProfile(userID, email, fname, lname);
//            Call<ResponseBody> call = apiService.getProductDetailsData(userTypeID, restaurantID, foodTypeID, categoryID);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();

                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            saveUserData();
                            showSnackbarErrorMsg(getResources().getString(R.string.profile_updated));

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dismissDialog();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        dismissDialog();
                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
                        Log.e("Error onFailure : ", t.toString());
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
//            signOutFirebaseAccounts();

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            updateUserProfile();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    public void showDialog() {
        progressIndicator.showProgress(EditProfileActivity.this);
    }

    public void dismissDialog() {
        if (progressIndicator != null) {
            progressIndicator.hideProgress();
        }
    }

    public void showSnackbarErrorMsg(String erroMsg) {
//        Snackbar.make(fragmentRootView, erroMsg, Snackbar.LENGTH_LONG).show();

        Snackbar snackbar = Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
