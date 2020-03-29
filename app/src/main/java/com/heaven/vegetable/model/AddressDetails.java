package com.heaven.vegetable.model;

import java.io.Serializable;

public class AddressDetails implements Serializable {
    public int addressID;
    public String address;
    public String addressType;
    public int zipCode;
    public String latitude;
    public String longitude;

//    public String addressTitle;
//
//    public String getAddressTitle() {
//        return addressTitle;
//    }
//
//    public void setAddressTitle(String addressTitle) {
//        this.addressTitle = addressTitle;
//    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
