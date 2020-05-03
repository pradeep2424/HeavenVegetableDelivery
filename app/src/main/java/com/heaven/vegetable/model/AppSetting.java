package com.heaven.vegetable.model;

import java.io.Serializable;

public class AppSetting implements Serializable {
    int maxDiscount;
    int minimumAmountForFreeDelivery;
    String contactEmail;
    String contactNo;
    String orderSuccessSMSTemplate;

    public int getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(int maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public int getMinimumAmountForFreeDelivery() {
        return minimumAmountForFreeDelivery;
    }

    public void setMinimumAmountForFreeDelivery(int minimumAmountForFreeDelivery) {
        this.minimumAmountForFreeDelivery = minimumAmountForFreeDelivery;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getOrderSuccessSMSTemplate() {
        return orderSuccessSMSTemplate;
    }

    public void setOrderSuccessSMSTemplate(String orderSuccessSMSTemplate) {
        this.orderSuccessSMSTemplate = orderSuccessSMSTemplate;
    }
}
