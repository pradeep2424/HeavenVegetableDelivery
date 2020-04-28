package com.heaven.vegetable.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.heaven.vegetable.R;
import com.heaven.vegetable.activity.LocationGoogleMapActivity;
import com.heaven.vegetable.broadcastReceiver.SMSListener;
import com.heaven.vegetable.interfaces.OTPListener;
import com.heaven.vegetable.loader.DialogLoadingIndicator;
import com.heaven.vegetable.model.UserDetails;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.sharedPreference.PrefManagerConfig;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.ConstantValues;
import com.heaven.vegetable.utils.InternetConnection;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetStartedVerifyOTPActivity extends AppCompatActivity implements OTPListener {
    DialogLoadingIndicator progressIndicator;
    RelativeLayout rlRootLayout;
    View viewToolbar;
    ImageView ivBack;

    private LinearLayout llConfirm;
    private TextView tvTitleText;
    private TextView tvTimerOTP;
    private TextView tvResendOTP;
    private OtpView otpView;

    private String mVerificationId;
    private String mobileNumber;
    private String generatedOTP = "";
    private String enteredOTP = "";

    String flagCalledFrom;

    private FirebaseAuth mAuth;

    private PrefManagerConfig prefManagerConfig;
//    private final int REQUEST_PERMISSION_READ_SMS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_verify_otp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flagCalledFrom = bundle.getString("CalledFrom");
        }

        mAuth = FirebaseAuth.getInstance();
        SMSListener.bindListener(this);
        mobileNumber = Application.userDetails.getMobile();

        init();
        events();
        sendVerificationCode();
    }

    private void init() {
        progressIndicator = DialogLoadingIndicator.getInstance();
        prefManagerConfig = new PrefManagerConfig(this);

        rlRootLayout = findViewById(R.id.rl_rootLayout);
        viewToolbar = findViewById(R.id.view_toolbarOTP);
        ivBack = (ImageView) findViewById(R.id.iv_back);

        llConfirm = (LinearLayout) findViewById(R.id.ll_confirm);
        tvTitleText = (TextView) findViewById(R.id.tv_otpText);
        tvTimerOTP = (TextView) findViewById(R.id.tv_otpTimer);
        tvResendOTP = (TextView) findViewById(R.id.tv_otpResend);
        otpView = (OtpView) findViewById(R.id.otp_view);
//        btnGetStarted = (Button) findViewById(R.id.btn_getStartedNow);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            mobileNumber = extras.getString("Mobile");
//        }


        String titleText = getResources().getString(R.string.login_verify_otp_text)
                .concat(" ").concat(mobileNumber);
        tvTitleText.setText(titleText);
    }

    private void events() {
        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
//                sendOTP();
            }
        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                enteredOTP = otp;
            }
        });

        llConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpCode = otpView.getText().toString();

                if (validate(otpCode)) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }


//                if (generatedOTP.equalsIgnoreCase(enteredOTP)
//                        || enteredOTP.equalsIgnoreCase("242424")) {
//
//                    insertUserDetails();
////                    Intent intent = new Intent(GetStartedVerifyOTPActivity.this, LocationGoogleMapActivity.class);
////                    startActivity(intent);
////                    finish();
//
//                } else {
//                    showSnackbarErrorMsg(getString(R.string.incorrect_otp));
//                }
            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean validate(String otpCode) {
        if (otpCode.length() < 1) {
            showSnackbarErrorMsg(getString(R.string.please_enter_otp));
            return false;
        }
        return true;
    }

    private void startOTPTimer() {
        showTimerCount();

        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                String strTimeText = getString(R.string.otp_wait_for);
                tvTimerOTP.setText(strTimeText + " (00:" + millisUntilFinished / 1000 + ")");
            }

            public void onFinish() {
//                mTextField.setText("done!");
                discardTimerCount();
            }

        }.start();
    }

    private void showTimerCount() {
//        otpView.setEnabled(false);
        tvTimerOTP.setVisibility(View.VISIBLE);
        tvResendOTP.setVisibility(View.GONE);
    }

    private void discardTimerCount() {
//        otpView.setEnabled(true);
        tvTimerOTP.setVisibility(View.GONE);
        tvResendOTP.setVisibility(View.VISIBLE);
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode() {
        startOTPTimer();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNumber, 60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, otpCode);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(GetStartedVerifyOTPActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            insertUserDetails();

//                            Intent intent = new Intent(GetStartedVerifyOTPActivity.this, LocationGoogleMapActivity.class);
//                            startActivity(intent);
//                            finish();

                        } else {
                            //verification unsuccessful.. display an error message
                            String message = "Somthing is wrong, we will fix it soon...";
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = getResources().getString(R.string.incorrect_otp);
                            }

                            showSnackbarErrorMsg(message);
                        }
                    }
                });
    }

//    private void sendOTP() {
//        if (InternetConnection.checkConnection(this)) {
//            startOTPTimer();
//
//            generatedOTP = generateRandomOTP();
//
//            SMSGatewayObject smsGatewayObject = Application.smsGatewayObject;
//            String smsURL = smsGatewayObject.getUrl();
//            String smsUsername = smsGatewayObject.getSmsUsername();
//            String smsPass = smsGatewayObject.getSmsPass();
//            String channel = smsGatewayObject.getChannel();
//            String senderID = smsGatewayObject.getSenderID();
//            String otpMessage = generatedOTP.concat(" ").concat(getString(R.string.otp_message));
//
//            String url = smsURL + "user=" + smsUsername + "&pass=" + smsPass
//                    + "&channel=" + channel + "&number=" + mobileNumber + "&message=" + otpMessage
//                    + "&SenderID=" + senderID + "&Campaign=";
//
//            ApiInterface apiService = RetroClient.getApiService(this);
//            Call<ResponseBody> call = apiService.getOtpSMS(url);
//
////            Call<ResponseBody> call = apiService.getOtpSMS(smsUsername, smsPass, channel,
////                    mobileNumber, senderID, otpAndMessage, "" );
//
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    try {
//                        int statusCode = response.code();
//                        if (response.isSuccessful()) {
//
//                        } else {
//                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    try {
//                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
//                        Log.e("Error onFailure : ", t.toString());
//                        t.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } else {
////            signOutFirebaseAccounts();
//
//            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction("RETRY", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            sendOTP();
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }

    private JsonObject createJsonUserDetails() {
        JsonObject postParam = new JsonObject();

        try {
            postParam.addProperty("Mobile", Application.userDetails.getMobile());
            postParam.addProperty("FName", Application.userDetails.getFirstName());
            postParam.addProperty("LName", Application.userDetails.getLastName());
            postParam.addProperty("Email", Application.userDetails.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postParam;
    }

    private void insertUserDetails() {
        if (InternetConnection.checkConnection(this)) {
            showDialog();

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.insertUserDetails(createJsonUserDetails());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();
//                            JSONObject jsonObject = new JSONObject(responseString);

                            prefManagerConfig.setIsUserLoggedIn(true);
                            prefManagerConfig.setMobileNo(mobileNumber);

//                            String referralCode = prefManagerConfig.getReferralCode();
//                            if (referralCode != null && !referralCode.equalsIgnoreCase(prefManagerConfig.SP_DEFAULT_VALUE)) {
//                                addReferral(referralCode);
//                            }

                            getUserDetails();

//                            Intent intent = new Intent(GetStartedVerifyOTPActivity.this, LocationGoogleMapActivity.class);
//                            startActivity(intent);
//                            finish();

                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                            insertUserDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void addReferral(final String referralCode) {
        if (InternetConnection.checkConnection(this)) {

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.addReferral(mobileNumber, referralCode);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {

                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();


                        } else {
                            showSnackbarErrorMsg(getResources().getString(R.string.something_went_wrong));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        showSnackbarErrorMsg(getResources().getString(R.string.server_conn_lost));
                        Log.e("Error onFailure : ", t.toString());
                        t.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addReferral(referralCode);
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


    private void getUserDetails() {
        if (InternetConnection.checkConnection(this)) {

            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getUserDetails(mobileNumber, mobileNumber);
//            Call<ResponseBody> call = apiService.getUserDetails("9665175415", "9665175415");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();

                            JSONObject jsonObj = new JSONObject(responseString);
                            String status = jsonObj.optString("Status");

                            String fname = jsonObj.optString("FName");
                            String lname = jsonObj.optString("LName");
                            String gender = jsonObj.optString("Gender");
                            String email = jsonObj.optString("Email");
                            String telephone = jsonObj.optString("Telephone");
                            String mobile = jsonObj.optString("Mobile");
                            String facebookId = jsonObj.optString("FacebookId");

                            int userID = jsonObj.optInt("UserID");
                            String username = jsonObj.optString("Username");
                            String password = jsonObj.optString("Pass");
                            String userPhoto = jsonObj.optString("UserPhoto");
                            String userRole = jsonObj.optString("UserRole");
                            String userType = jsonObj.optString("UserType");

                            String address = jsonObj.optString("Address");
                            String area = jsonObj.optString("Area");
                            String cityName = jsonObj.optString("CityName");
                            String stateName = jsonObj.optString("StateName");
                            int zipCode = jsonObj.optInt("ZipCode");
//
//                            double totalPoints = jsonObj.optDouble("TotalPoints");
//                            if (Double.isNaN(totalPoints)) {
//                                totalPoints = 0;
//                            }

                            String url = jsonObj.optString("URL");
                            String smsUsername = jsonObj.optString("SMSUsername");
                            String smsPass = jsonObj.optString("SMSPass");
                            String channel = jsonObj.optString("channel");
                            String sendSMS = jsonObj.optString("SendSMS");
                            String senderID = jsonObj.optString("SenderID");

                            String fullName = fname.concat(" ").concat(lname);

                            UserDetails userDetails = new UserDetails();
                            userDetails.setFirstName(fname);
                            userDetails.setLastName(lname);
                            userDetails.setFullName(fullName);
                            userDetails.setUserType(userType);
                            userDetails.setEmail(email);
                            userDetails.setMobile(mobile);
                            userDetails.setGender(gender);
                            userDetails.setTelephone(telephone);
                            userDetails.setFacebookId(facebookId);
                            userDetails.setUserID(userID);
                            userDetails.setUsername(username);
                            userDetails.setPassword(password);
                            userDetails.setUserPhoto(userPhoto);
                            userDetails.setUserRole(userRole);
                            userDetails.setAddress(address);
                            userDetails.setArea(area);
                            userDetails.setCityName(cityName);
                            userDetails.setStateName(stateName);
                            userDetails.setZipCode(zipCode);
//                            userDetails.setTotalReferralPoints(totalPoints);
                            Application.userDetails = userDetails;

                            if (flagCalledFrom != null && flagCalledFrom.equalsIgnoreCase(ConstantValues.ACTIVITY_CART_ACTION_PLACE_ORDER)) {
                                Intent intent = new Intent();
                                intent.putExtra("MESSAGE", "MOBILE_VERIFIED");
                                setResult(RESULT_OK, intent);
                                finish();

                            } else {
                                Intent intent = new Intent(GetStartedVerifyOTPActivity.this, LocationGoogleMapActivity.class);
                                intent.putExtra("CalledFrom", ConstantValues.ACTIVITY_ACTION_OTP);
                                startActivity(intent);
                                finish();
                            }

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
                            getUserDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


    public void showSnackbarErrorMsgWithButton(String erroMsg) {
        Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
    }

    public void showSnackbarErrorMsg(String erroMsg) {
        Snackbar snackbar = Snackbar.make(rlRootLayout, erroMsg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView
                .findViewById(R.id.snackbar_text);
        snackTextView.setMaxLines(4);
        snackbar.show();
    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.location_permission_title))
                .setMessage(getResources().getString(R.string.location_permission_text))
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .setCancelable(false)
                .create()
                .show();
    }

    private String generateRandomOTP() {
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(999999));
        return otp;
    }

    public void showDialog() {
        progressIndicator.showProgress(GetStartedVerifyOTPActivity.this);
    }

    public void dismissDialog() {
        if (progressIndicator != null) {
            progressIndicator.hideProgress();
        }
    }

//    private boolean requestSMSPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            int permissionReadSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
//            int permissionReceiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
//
//            List<String> listPermissionsNeeded = new ArrayList<>();
//            if (permissionReadSMS != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(Manifest.permission.READ_SMS);
//            }
//
//            if (permissionReceiveSMS != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
//            }
//
//            if (!listPermissionsNeeded.isEmpty()) {
//                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSION_READ_SMS);
//                return false;
//            }
//        }
//        return true;
//
//    }


//    private void verifyVerificationCode(String code) {
//        //creating the credential
//        phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, code);
//    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otpView.setText(code);
                //verifying the code
//                verifyVerificationCode(code);
            } else {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(GetStartedVerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_PERMISSION_READ_SMS:
//
//                Map<String, Integer> perms1 = new HashMap<>();
//                perms1.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
//                perms1.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);
//
//                if (grantResults.length > 0) {
//                    for (int i = 0; i < permissions.length; i++) {
//                        perms1.put(permissions[i], grantResults[i]);
//                    }
//
//                    // Check for both permissions
////                    if (perms1.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//////                            && perms1.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//////                            && perms1.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//
//                    if (perms1.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
//                            && perms1.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
//                        SMSListener.bindListener(this);
//
//                    } else {
//
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)
//                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
//
//                            showDialogOK(new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    switch (which) {
//                                        case DialogInterface.BUTTON_POSITIVE:
//                                            requestSMSPermission();
//
//                                            break;
//                                        case DialogInterface.BUTTON_NEGATIVE:
//
//                                            break;
//                                    }
//                                }
//                            });
//                        }
//                        //permission is denied (and never ask again is  checked)
//                        //shouldShowRequestPermissionRationale will return false
//                        else {
//                            Toast.makeText(this, "Go to settings and enable app permissions",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//
//                break;
//        }
//    }


    @Override
    public void onOtpReceived(String otp) {
        otpView.setText(otp);

//        Toast.makeText(this,"Got : "+otp,Toast.LENGTH_LONG).show();
//        Log.d("Otp",otp);
    }

    @Override
    public void onOtpTimeout() {

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        Intent it = new Intent(this, GetStartedMobileNumberActivity.class);
        it.putExtra("CalledFrom", flagCalledFrom);
        startActivity(it);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SMSListener.unbindListener();
    }
}
