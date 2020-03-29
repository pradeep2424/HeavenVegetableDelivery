package com.heaven.vegetable.model;

import java.io.Serializable;

public class CartObject implements Serializable {
    double cgst;
    int restaurantID;
    double deliveryCharge;
    String restaurantName;
    boolean isIncludeTax;
    boolean isTaxApplicable;
    double productAmount;
    int productID;
    String productName;
    int productQuantity;
    double productRate;
    String productSize;
    double sgst;
    int taxID;
    double taxableVal;
    double totalAmount;
    int userID;
    int cartID;

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public int getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public boolean getIsIncludeTax() {
        return isIncludeTax;
    }

    public void setIsIncludeTax(boolean includeTax) {
        isIncludeTax = includeTax;
    }

    public boolean getIsTaxApplicable() {
        return isTaxApplicable;
    }

    public void setIsTaxApplicable(boolean taxApplicable) {
        isTaxApplicable = taxApplicable;
    }

    public double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(double productAmount) {
        this.productAmount = productAmount;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getProductRate() {
        return productRate;
    }

    public void setProductRate(double productRate) {
        this.productRate = productRate;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public int getTaxID() {
        return taxID;
    }

    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

    public double getTaxableVal() {
        return taxableVal;
    }

    public void setTaxableVal(double taxableVal) {
        this.taxableVal = taxableVal;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }
}
