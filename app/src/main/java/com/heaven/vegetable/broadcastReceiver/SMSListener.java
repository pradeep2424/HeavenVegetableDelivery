package com.heaven.vegetable.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.heaven.vegetable.interfaces.OTPListener;

public class SMSListener extends BroadcastReceiver {

    private static OTPListener mListener; // this listener will do the magic of throwing the extracted OTP to all the bound views.

    @Override
    public void onReceive(Context context, Intent intent) {

        // this function is trigged when each time a new SMS is received on device.

        Bundle data = intent.getExtras();

        Object[] pdus = new Object[0];
        if (data != null) {
            pdus = (Object[]) data.get("pdus"); // the pdus key will contain the newly received SMS
        }

        if (pdus != null) {
            for (Object pdu : pdus) { // loop through and pick up the SMS of interest
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

//                String phoneNumber = smsMessage.getDisplayOriginatingAddress();
//                String senderNum = phoneNumber;
                String message = smsMessage.getDisplayMessageBody().replaceAll("\\D", "");

//                Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                if (mListener!=null)
                    mListener.onOtpReceived(message);
                break;
            }
        }
    }

    public static void bindListener(OTPListener listener) {
        mListener = listener;
    }

    public static void unbindListener() {
        mListener = null;
    }
}