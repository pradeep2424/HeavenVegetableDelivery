package com.heaven.vegetable.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pradeep Jadhav on 2/15/2018.
 */

public class PrefManagerConfig {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    public final String SP_DEFAULT_VALUE = "Default";

    private final String PREF_NAME_LOGIN_DETAILS = "SPLoginDetails";
    private final String SP_KEY_IS_USER_LOGGED_IN = "IsUserLoggedIn";
    private final String SP_KEY_MOBILE_NO = "MobileNo";
    private final String SP_KEY_REFERRAL_CODE = "ReferralCode";

    public PrefManagerConfig(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME_LOGIN_DETAILS, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsUserLoggedIn(boolean isUserLoggedIn) {
        editor.putBoolean(SP_KEY_IS_USER_LOGGED_IN, isUserLoggedIn);
        editor.commit();
    }

    public boolean getIsUserLoggedIn() {
        return pref.getBoolean(SP_KEY_IS_USER_LOGGED_IN, false);
    }

    public String getMobileNo() {
        return pref.getString(SP_KEY_MOBILE_NO, SP_DEFAULT_VALUE);
    }

    public void setMobileNo(String appLang) {
        editor.putString(SP_KEY_MOBILE_NO, appLang);
        editor.commit();
    }

    public String getReferralCode() {
        return pref.getString(SP_KEY_REFERRAL_CODE, SP_DEFAULT_VALUE);
    }

    public void setReferralCode(String referralCode) {
        editor.putString(SP_KEY_REFERRAL_CODE, referralCode);
        editor.commit();
    }


    public void clearPrefOnLogout() {
        editor.clear();
        editor.commit();
    }

}