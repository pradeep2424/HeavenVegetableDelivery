package com.heaven.vegetable.model;

import java.io.Serializable;

public class ReferralDetails implements Serializable {
    int referralID;
    int newUserID;
    double totalAmount;
    String firstName;
    String lastName;

    public int getReferralID() {
        return referralID;
    }

    public void setReferralID(int referralID) {
        this.referralID = referralID;
    }

    public int getNewUserID() {
        return newUserID;
    }

    public void setNewUserID(int newUserID) {
        this.newUserID = newUserID;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
