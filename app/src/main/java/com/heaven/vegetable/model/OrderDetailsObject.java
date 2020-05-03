package com.heaven.vegetable.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDetailsObject implements Serializable {
    String restaurantName;
    int orderID;
    int orderNumber;
    String orderDate;
    int orderType;
    int orderStatus;
    int orderMode;
    int paymentID;
    int productID;
    String productName;
    double productRate;
    int productQuantity;
    double taxableVal;
    double cgst;
    double sgst;
    String userAddress;
    int userID;
    int clientID;
//    int restaurantID;
    double totalAmount;
    int taxID;
    boolean orderPaid;
    String rejectReason;
    boolean isIncludeTax;
    boolean isTaxApplicable;
    int unitID;
    ArrayList<ProductObject> listProducts;
    ArrayList<OrderTimelineObject> listOrderStatusTimeline;

    public ArrayList<OrderTimelineObject> getListOrderStatusTimeline() {
        return listOrderStatusTimeline;
    }

    public void setListOrderStatusTimeline(ArrayList<OrderTimelineObject> listOrderStatusTimeline) {
        this.listOrderStatusTimeline = listOrderStatusTimeline;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public ArrayList<ProductObject> getListProducts() {
        return listProducts;
    }

    public void setListProducts(ArrayList<ProductObject> listProducts) {
        this.listProducts = listProducts;
    }
//    double deliveryCharge;
//    boolean isIncludeTax;
//    boolean isTaxApplicable;

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


    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderMode() {
        return orderMode;
    }

    public void setOrderMode(int orderMode) {
        this.orderMode = orderMode;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
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

    public double getProductRate() {
        return productRate;
    }

    public void setProductRate(double productRate) {
        this.productRate = productRate;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getTaxableVal() {
        return taxableVal;
    }

    public void setTaxableVal(double taxableVal) {
        this.taxableVal = taxableVal;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    //    public int getRestaurantID() {
//        return restaurantID;
//    }
//
//    public void setRestaurantID(int restaurantID) {
//        this.restaurantID = restaurantID;
//    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTaxID() {
        return taxID;
    }

    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

    public boolean getOrderPaid() {
        return orderPaid;
    }

    public void setOrderPaid(boolean orderPaid) {
        this.orderPaid = orderPaid;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

}
