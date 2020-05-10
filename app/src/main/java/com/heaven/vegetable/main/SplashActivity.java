package com.heaven.vegetable.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.heaven.vegetable.BuildConfig;
import com.heaven.vegetable.R;
import com.heaven.vegetable.model.AppSetting;
import com.heaven.vegetable.model.SMSGatewayObject;
import com.heaven.vegetable.model.UserDetails;
import com.heaven.vegetable.service.retrofit.ApiInterface;
import com.heaven.vegetable.service.retrofit.RetroClient;
import com.heaven.vegetable.sharedPreference.PrefManagerConfig;
import com.heaven.vegetable.utils.Application;
import com.heaven.vegetable.utils.ConstantValues;
import com.heaven.vegetable.utils.GPSTracker;
import com.heaven.vegetable.utils.InternetConnection;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;
import com.sucho.placepicker.AddressData;

import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    RelativeLayout rlRootLayout;

    private PrefManagerConfig prefManagerConfig;

    boolean isUserLoggedIn;
    String mobileNumber;
    BlurPopupWindow blurPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        printHashKey();

        init();
        getCurrentLocation();

        getSmsDetails();
//        getAppSettings();

//        loadNextPage();

    }

//    public void printHashKey() {
//        try {
//            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(
//                    getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String hashKey = new String(Base64.encode(md.digest(), 0));
//                Log.i("*****", "printHashKey() Hash Key : " + hashKey);
//            }
//        } catch (NoSuchAlgorithmException e) {
//            Log.e("*****", "printHashKey()", e);
//        } catch (Exception e) {
//            Log.e("*****", "printHashKey()", e);
//        }
//    }

    private void init() {
        prefManagerConfig = new PrefManagerConfig(this);
        rlRootLayout = findViewById(R.id.rl_rootLayout);
    }

    private void loadNextPage() {
        isUserLoggedIn = prefManagerConfig.getIsUserLoggedIn();
        mobileNumber = prefManagerConfig.getMobileNo();

        if (isUserLoggedIn && !mobileNumber.equalsIgnoreCase(prefManagerConfig.SP_DEFAULT_VALUE)) {
//            autoLogin();

            getUserDetails();
//            getAreaDetails();

//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }, 2000);

        } else {
//            callSignUpPage();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, GetStartedMobileNumberActivity.class);
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }

//    private JSONObject createJsonUserDetails() {
//        JSONObject postParam = new JSONObject();
//
//        try {
//            postParam.put("Username", "test");
//            postParam.put("Password", "test");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return postParam;
//    }

    private void getCurrentLocation() {
        if (requestLocationPermission()) {
            getLatitudeLongitude();
        }
    }

    private boolean requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                return false;
            }
        }
        return true;

    }

    private void getLatitudeLongitude() {
        try {
            double latitude;
            double longitude;

            GPSTracker tracker = new GPSTracker(this);
            if (!tracker.canGetLocation()) {
                tracker.showSettingsAlert();
            } else {
                latitude = tracker.getLatitude();
                longitude = tracker.getLongitude();

                getAddressFromMap(latitude, longitude);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAddressFromMap(double latitude, double longitude) throws IOException {
        Geocoder gCoder = new Geocoder(this);
        List<Address> addresses = gCoder.getFromLocation(latitude, longitude, 1);
        if (addresses != null && addresses.size() > 0) {

            AddressData addressData = new AddressData(latitude, longitude, addresses);
            Application.locationAddressData = addressData;

            Address address = addressData.getAddressList().get(0);
            String fullAddress = address.getAddressLine(0);
            String zipCodeStr = address.getPostalCode();
            String cityName = address.getLocality();
            String subLocality = address.getSubLocality();
            String area = address.getFeatureName();


            int zipCode = 0;
//        checking if zipCodeStr has integer values
            if (zipCodeStr != null && zipCodeStr.matches("[0-9]+")) {
                zipCode = Integer.parseInt(zipCodeStr);
            }

            Application.userDetails.setAddress(fullAddress);
            Application.userDetails.setZipCode(zipCode);
            Application.userDetails.setCityName(cityName);
            Application.userDetails.setSubLocality(subLocality);
            Application.userDetails.setAddressType("Home");

//            Toast.makeText(myContext, "country: " + addresses.get(0).getCountryName(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isAppUpdateAvailable(String newAppVersion) {
        int serverAppVersion = 0;
        int userAppVersion = 0;

        if (TextUtils.isDigitsOnly(newAppVersion)) {
            serverAppVersion = Integer.parseInt(newAppVersion);
        }

        String strUserAppVersion = BuildConfig.VERSION_NAME.replaceAll("\\.", "");
        if (TextUtils.isDigitsOnly(strUserAppVersion)) {
            userAppVersion = Integer.parseInt(strUserAppVersion);
        }

        if (userAppVersion < serverAppVersion) {
            return true;
        } else {
            return false;
        }

    }

    private void showDialogAppForceUpdate() {
        blurPopupWindow = new BlurPopupWindow.Builder(SplashActivity.this)
                .setContentView(R.layout.dialog_layout_force_app_update)
                .bindClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                                ("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                        blurPopupWindow.dismiss();
                    }
                }, R.id.ll_update)
                .setGravity(Gravity.CENTER)
                .setScaleRatio(0.2f)
                .setDismissOnClickBack(false)
                .setDismissOnTouchBackground(false)
                .setBlurRadius(10)
                .setTintColor(0x30000000)
                .build();

        blurPopupWindow.show();
    }

    private void getUserDetails() {
        if (InternetConnection.checkConnection(this)) {

            ApiInterface apiService = RetroClient.getApiService(this);
//            Call<ResponseBody> call = apiService.getUserDetails(mobileNumber, mobileNumber);
            Call<ResponseBody> call = apiService.getUserDetails(mobileNumber, mobileNumber);
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

                            double totalPoints = jsonObj.optDouble("TotalPoints");
                            if (Double.isNaN(totalPoints)) {
                                totalPoints = 0;
                            }

                            String url = jsonObj.optString("URL");
                            String smsUsername = jsonObj.optString("SMSUsername");
                            String smsPass = jsonObj.optString("SMSPass");
                            String channel = jsonObj.optString("channel");
                            String sendSMS = jsonObj.optString("SendSMS");
                            String senderID = jsonObj.optString("SenderID");

                            String fullName = fname.concat(" ").concat(lname);

//                            UserDetails userDetails = new UserDetails();
                            Application.userDetails.setFirstName(fname);
                            Application.userDetails.setLastName(lname);
                            Application.userDetails.setFullName(fullName);
                            Application.userDetails.setUserType(userType);
                            Application.userDetails.setEmail(email);
                            Application.userDetails.setMobile(mobile);
                            Application.userDetails.setGender(gender);
                            Application.userDetails.setTelephone(telephone);
                            Application.userDetails.setFacebookId(facebookId);
                            Application.userDetails.setUserID(userID);
                            Application.userDetails.setUsername(username);
                            Application.userDetails.setPassword(password);
                            Application.userDetails.setUserPhoto(userPhoto);
                            Application.userDetails.setUserRole(userRole);
//                            userDetails.setAddress(address);
//                            userDetails.setArea(area);
//                            userDetails.setCityName(cityName);
//                            userDetails.setStateName(stateName);
//                            userDetails.setZipCode(zipCode);
//                            userDetails.setTotalReferralPoints(totalPoints);
//                            Application.userDetails = userDetails;

//                            SMSGatewayObject smsGatewayObject = new SMSGatewayObject();
//                            smsGatewayObject.setUrl(url);
//                            smsGatewayObject.setSmsUsername(smsUsername);
//                            smsGatewayObject.setSmsPass(smsPass);
//                            smsGatewayObject.setChannel(channel);
//                            smsGatewayObject.setSendSMS(sendSMS);
//                            smsGatewayObject.setSenderID(senderID);
//                            Application.smsGatewayObject = smsGatewayObject;

                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

//                            if (status.equalsIgnoreCase("Success")) {
//                                FirebaseUser user = mAuth.getCurrentUser();
//
//                                String accessToken = jsonObj.getString("AccessToken");
//                                int accountType = jsonObj.getInt("AccountType");
//                                String mainCorpNo = jsonObj.getString("MainCorpNo");
//                                String corpID = jsonObj.getString("CorpID");
//                                String emailID = jsonObj.getString("EmailID");
//                                String firstName = jsonObj.getString("FirstName");
//                                String lastName = jsonObj.getString("LastName");
//
//                                String hibernate = jsonObj.getString("Hibernate");
//                                boolean isGracePeriod = jsonObj.getBoolean("IsGracePeriod");
//                                boolean isTrial = jsonObj.getBoolean("IsTrial");
//                                msgHeader = jsonObj.getString("MessageHeader");
//                                message = jsonObj.getString("Message");
//
//                                int maxQuestionsAllowed = jsonObj.getInt("MaxQuestionsAllowed");
//                                int maxSurveysAllowed = jsonObj.getInt("MaxSurveysAllowed");
//
//                            } else if (status.equalsIgnoreCase("LoginFailed")) {
//
//                                String msg = jsonObj.getString("Message");
//                                String msgHeader = jsonObj.getString("MessageHeader");
//
//                                prefs.edit().clear().apply();
//                                signOutFirebaseAccounts();
//
//
//                                if (msgHeader.trim().equalsIgnoreCase("")) {
//                                    showSnackbarErrorMsg(msg);
//                                    callSignUpPage();
//                                } else {
//                                    accountBlockedDialog(msgHeader, msg);
//                                }
//
//                            } else if (status.equalsIgnoreCase("Invalid AccessToken")) {
//                                showSnackbarErrorMsg(getResources().getString(R.string.invalid_access_token));
//
//                            } else if (status.equalsIgnoreCase("Error")) {
//                                String msg = jsonObj.getString("Message");
//                                showSnackbarErrorMsg(msg);
//
//                            } else {
//                                showSnackbarErrorMsg("Unmatched response, Please try again.");
//                            }

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

    private void getSmsDetails() {
        if (InternetConnection.checkConnection(this)) {
            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getSMSDetails();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();

                            JSONObject jsonObj = new JSONObject(responseString);
                            String status = jsonObj.optString("Status");

                            String url = jsonObj.optString("URL");
                            String smsUsername = jsonObj.optString("SMSUsername");
                            String smsPass = jsonObj.optString("SMSPass");
                            String channel = jsonObj.optString("channel");
                            String sendSMS = jsonObj.optString("SendSMS");
                            String senderID = jsonObj.optString("SenderID");

                            SMSGatewayObject smsGatewayObject = new SMSGatewayObject();
                            smsGatewayObject.setUrl(url);
                            smsGatewayObject.setSmsUsername(smsUsername);
                            smsGatewayObject.setSmsPass(smsPass);
                            smsGatewayObject.setChannel(channel);
                            smsGatewayObject.setSendSMS(sendSMS);
                            smsGatewayObject.setSenderID(senderID);
                            Application.smsGatewayObject = smsGatewayObject;

                            getAppSettings();

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
//            signOutFirebaseAccounts();

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getSmsDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }

    private void getAppSettings() {
        if (InternetConnection.checkConnection(this)) {
            ApiInterface apiService = RetroClient.getApiService(this);
            Call<ResponseBody> call = apiService.getAppSetting();
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        int statusCode = response.code();
                        if (response.isSuccessful()) {

                            String responseString = response.body().string();

                            JSONObject jsonObj = new JSONObject(responseString);

                            int id = jsonObj.optInt("ID");
                            int maxDiscount = jsonObj.optInt("MaxDiscount");
                            int minimumAmountForFreeDelivery = jsonObj.optInt("MinAmtForFreeDel");
                            int referralPercent = jsonObj.optInt("ReferrelPercent");
                            String contactEmail = jsonObj.optString("ContactEmail");
                            String contactNo = jsonObj.optString("ContactNo");
                            String smsTemplate = jsonObj.optString("SMSTemplate");
                            String appVersion = jsonObj.optString("version");

                            AppSetting appSetting = new AppSetting();
                            appSetting.setMaxDiscount(maxDiscount);
                            appSetting.setContactEmail(contactEmail);
                            appSetting.setContactNo(contactNo);
                            appSetting.setMinimumAmountForFreeDelivery(minimumAmountForFreeDelivery);
                            appSetting.setOrderSuccessSMSTemplate(smsTemplate);
                            Application.appSetting = appSetting;

                            if (isAppUpdateAvailable(appVersion)) {
                                showDialogAppForceUpdate();

                            } else {
                                loadNextPage();
                            }
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
//            signOutFirebaseAccounts();

            Snackbar.make(rlRootLayout, getResources().getString(R.string.no_internet),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getSmsDetails();
                        }
                    })
//                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
                    .show();
        }
    }


//    private void getAreaDetails() {
//        if (InternetConnection.checkConnection(this)) {
//
//            ApiInterface apiService = RetroClient.getApiService(this);
////            Call<ResponseBody> call = apiService.getUserDetails(createJsonUserDetails());
//            Call<ResponseBody> call = apiService.getAreaDetails();
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//
//                        int statusCode = response.code();
//                        if (response.isSuccessful()) {
//
//                            String responseString = response.body().string();
//                            JSONArray jsonArray = new JSONArray(responseString);
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObj = jsonArray.getJSONObject(i);
//
//                                String area = jsonObj.optString("Area");
//                                String areaId = jsonObj.optString("AreaId");
//                                String cityId = jsonObj.optString("CityId");
//                                String cityName = jsonObj.optString("CityName");
//                                String country = jsonObj.optString("Country");
//                                String countryId = jsonObj.optString("CountryId");
//                                String stateId = jsonObj.optString("StateId");
//                                String stateName = jsonObj.optString("StateName");
//                            }
//
//
////                            if (status.equalsIgnoreCase("Success")) {
////                                FirebaseUser user = mAuth.getCurrentUser();
////
////                                String accessToken = jsonObj.getString("AccessToken");
////                                int accountType = jsonObj.getInt("AccountType");
////                                String mainCorpNo = jsonObj.getString("MainCorpNo");
////                                String corpID = jsonObj.getString("CorpID");
////                                String emailID = jsonObj.getString("EmailID");
////                                String firstName = jsonObj.getString("FirstName");
////                                String lastName = jsonObj.getString("LastName");
////
////                                String hibernate = jsonObj.getString("Hibernate");
////                                boolean isGracePeriod = jsonObj.getBoolean("IsGracePeriod");
////                                boolean isTrial = jsonObj.getBoolean("IsTrial");
////                                msgHeader = jsonObj.getString("MessageHeader");
////                                message = jsonObj.getString("Message");
////
////                                int maxQuestionsAllowed = jsonObj.getInt("MaxQuestionsAllowed");
////                                int maxSurveysAllowed = jsonObj.getInt("MaxSurveysAllowed");
////
////                            } else if (status.equalsIgnoreCase("LoginFailed")) {
////
////                                String msg = jsonObj.getString("Message");
////                                String msgHeader = jsonObj.getString("MessageHeader");
////
////                                prefs.edit().clear().apply();
////                                signOutFirebaseAccounts();
////
////
////                                if (msgHeader.trim().equalsIgnoreCase("")) {
////                                    showSnackbarErrorMsg(msg);
////                                    callSignUpPage();
////                                } else {
////                                    accountBlockedDialog(msgHeader, msg);
////                                }
////
////                            } else if (status.equalsIgnoreCase("Invalid AccessToken")) {
////                                showSnackbarErrorMsg(getResources().getString(R.string.invalid_access_token));
////
////                            } else if (status.equalsIgnoreCase("Error")) {
////                                String msg = jsonObj.getString("Message");
////                                showSnackbarErrorMsg(msg);
////
////                            } else {
////                                showSnackbarErrorMsg("Unmatched response, Please try again.");
////                            }
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
//                            getAreaDetails();
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }


//    private void insertUserDetails() {
//        if (InternetConnection.checkConnection(this)) {
//
//            UserDetails userDetails = new UserDetails();
//            userDetails.setUsername("Pradeep");
//            userDetails.setPassword("PradeepPass");
//
//            ApiInterface apiService = RetroClient.getApiService(this);
////            Call<ResponseBody> call = apiService.getUserDetails(createJsonUserDetails());
//            Call<ResponseBody> call = apiService.insertUserDetails(userDetails);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//
//                        int statusCode = response.code();
//                        if (response.isSuccessful()) {
//
//                            String responseString = response.body().string();
//                            JSONObject jsonObject = new JSONObject(responseString);
//
//
////                            if (status.equalsIgnoreCase("Success")) {
////                                FirebaseUser user = mAuth.getCurrentUser();
////
////                                String accessToken = jsonObj.getString("AccessToken");
////                                int accountType = jsonObj.getInt("AccountType");
////                                String mainCorpNo = jsonObj.getString("MainCorpNo");
////                                String corpID = jsonObj.getString("CorpID");
////                                String emailID = jsonObj.getString("EmailID");
////                                String firstName = jsonObj.getString("FirstName");
////                                String lastName = jsonObj.getString("LastName");
////
////                                String hibernate = jsonObj.getString("Hibernate");
////                                boolean isGracePeriod = jsonObj.getBoolean("IsGracePeriod");
////                                boolean isTrial = jsonObj.getBoolean("IsTrial");
////                                msgHeader = jsonObj.getString("MessageHeader");
////                                message = jsonObj.getString("Message");
////
////                                int maxQuestionsAllowed = jsonObj.getInt("MaxQuestionsAllowed");
////                                int maxSurveysAllowed = jsonObj.getInt("MaxSurveysAllowed");
////
////                            } else if (status.equalsIgnoreCase("LoginFailed")) {
////
////                                String msg = jsonObj.getString("Message");
////                                String msgHeader = jsonObj.getString("MessageHeader");
////
////                                prefs.edit().clear().apply();
////                                signOutFirebaseAccounts();
////
////
////                                if (msgHeader.trim().equalsIgnoreCase("")) {
////                                    showSnackbarErrorMsg(msg);
////                                    callSignUpPage();
////                                } else {
////                                    accountBlockedDialog(msgHeader, msg);
////                                }
////
////                            } else if (status.equalsIgnoreCase("Invalid AccessToken")) {
////                                showSnackbarErrorMsg(getResources().getString(R.string.invalid_access_token));
////
////                            } else if (status.equalsIgnoreCase("Error")) {
////                                String msg = jsonObj.getString("Message");
////                                showSnackbarErrorMsg(msg);
////
////                            } else {
////                                showSnackbarErrorMsg("Unmatched response, Please try again.");
////                            }
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
//                            getUserDetails();
//                        }
//                    })
////                    .setActionTextColor(getResources().getColor(R.color.colorSnackbarButtonText))
//                    .show();
//        }
//    }

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
    protected void onDestroy() {
        super.onDestroy();

//        if(blurPopupWindow.)
    }
}
