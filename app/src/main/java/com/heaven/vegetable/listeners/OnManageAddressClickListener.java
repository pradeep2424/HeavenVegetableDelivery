package com.heaven.vegetable.listeners;

import android.view.View;

/**
 * Created by Pradeep Jadhav on 9/1/2017.
 */

public interface OnManageAddressClickListener {
    void onEditAddress(View view, int position);

    void onDeleteAddress(View view, int position);
}