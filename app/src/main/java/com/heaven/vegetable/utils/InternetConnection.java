package com.heaven.vegetable.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;

/**
 * Created by Pradeep Jadhav on 7/27/2017.
 */

public class InternetConnection {

    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    public static boolean checkConnection(@NonNull Context context) {
        return ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
