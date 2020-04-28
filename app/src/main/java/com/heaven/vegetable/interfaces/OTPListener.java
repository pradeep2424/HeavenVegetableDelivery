package com.heaven.vegetable.interfaces;

public interface OTPListener {
    void onOtpReceived(String otp);
    void onOtpTimeout();

//    void onOTPReceived(String otp);
}
