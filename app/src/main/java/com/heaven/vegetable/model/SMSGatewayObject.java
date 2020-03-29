package com.heaven.vegetable.model;

import java.io.Serializable;

public class SMSGatewayObject implements Serializable {
    String url;
    String smsUsername;
    String smsPass;
    String channel;
    String sendSMS;
    String senderID;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSmsUsername() {
        return smsUsername;
    }

    public void setSmsUsername(String smsUsername) {
        this.smsUsername = smsUsername;
    }

    public String getSmsPass() {
        return smsPass;
    }

    public void setSmsPass(String smsPass) {
        this.smsPass = smsPass;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSendSMS() {
        return sendSMS;
    }

    public void setSendSMS(String sendSMS) {
        this.sendSMS = sendSMS;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
