package com.heaven.vegetable.listeners;

import android.view.View;

/**
 * Created by Pradeep Jadhav on 9/1/2017.
 */

public interface OnPastOrderOptionsClickListener {
    void onClickReceipt(View view, int position);

    void onClickReorder(View view, int position);
}