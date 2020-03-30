package com.heaven.vegetable.loader;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.heaven.vegetable.R;

/**
 * Created by Pradeep Jadhav on 12/4/2017.
 */

public class DialogLoadingIndicator {
    public static DialogLoadingIndicator mCShowProgress;
    public Dialog mDialog;

    public DialogLoadingIndicator() {
    }

    public static DialogLoadingIndicator getInstance() {
        if (mCShowProgress== null) {
            mCShowProgress= new DialogLoadingIndicator();
        }
        return mCShowProgress;
    }

    public void showProgress(Context mContext) {
        mDialog= new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.loading_indicator);
        mDialog.findViewById(R.id.avi_loading).setVisibility(View.VISIBLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog!= null) {
            mDialog.dismiss();
            mDialog= null;
        }
    }
}