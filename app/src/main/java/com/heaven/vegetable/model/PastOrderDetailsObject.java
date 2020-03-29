package com.heaven.vegetable.model;//package com.miracle.dronam.model;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//
//public class PastOrderDetailsObject implements Serializable {
//    String restaurantName;
//    int orderID;
//    int orderNumber;
//    String orderDate;
//    int orderType;
//    int orderStatus;
//    int orderMode;
//    int paymentID;
//    double taxableVal;
//    double cgst;
//    double sgst;
//    String userAddress;
//    int userID;
//    int clientID;
////    int restaurantID;
//    double totalAmount;
//    int taxID;
//    boolean orderPaid;
//    String rejectReason;
//    ArrayList<DishObject> listProducts;
//
//    public ArrayList<DishObject> getListProducts() {
//        return listProducts;
//    }
//
//    public void setListProducts(ArrayList<DishObject> listProducts) {
//        this.listProducts = listProducts;
//    }
//
////    double deliveryCharge;
////    boolean isIncludeTax;
////    boolean isTaxApplicable;
//
//    public String getRestaurantName() {
//        return restaurantName;
//    }
//
//    public void setRestaurantName(String restaurantName) {
//        this.restaurantName = restaurantName;
//    }
//
//    public int getOrderID() {
//        return orderID;
//    }
//
//    public void setOrderID(int orderID) {
//        this.orderID = orderID;
//    }
//
//    public int getOrderNumber() {
//        return orderNumber;
//    }
//
//    public void setOrderNumber(int orderNumber) {
//        this.orderNumber = orderNumber;
//    }
//
//    public String getOrderDate() {
//        return orderDate;
//    }
//
//    public void setOrderDate(String orderDate) {
//        this.orderDate = orderDate;
//    }
//
//    public int getOrderType() {
//        return orderType;
//    }
//
//    public void setOrderType(int orderType) {
//        this.orderType = orderType;
//    }
//
//    public int getOrderStatus() {
//        return orderStatus;
//    }
//
//    public void setOrderStatus(int orderStatus) {
//        this.orderStatus = orderStatus;
//    }
//
//    public int getOrderMode() {
//        return orderMode;
//    }
//
//    public void setOrderMode(int orderMode) {
//        this.orderMode = orderMode;
//    }
//
//    public int getPaymentID() {
//        return paymentID;
//    }
//
//    public double getTaxableVal() {
//        return taxableVal;
//    }
//
//    public void setTaxableVal(double taxableVal) {
//        this.taxableVal = taxableVal;
//    }
//
//    public double getCgst() {
//        return cgst;
//    }
//
//    public void setCgst(double cgst) {
//        this.cgst = cgst;
//    }
//
//    public double getSgst() {
//        return sgst;
//    }
//
//    public void setSgst(double sgst) {
//        this.sgst = sgst;
//    }
//
//    public String getUserAddress() {
//        return userAddress;
//    }
//
//    public void setUserAddress(String userAddress) {
//        this.userAddress = userAddress;
//    }
//
//    public int getUserID() {
//        return userID;
//    }
//
//    public void setUserID(int userID) {
//        this.userID = userID;
//    }
//
//    public int getClientID() {
//        return clientID;
//    }
//
//    public void setClientID(int clientID) {
//        this.clientID = clientID;
//    }
//
//    //    public int getRestaurantID() {
////        return restaurantID;
////    }
////
////    public void setRestaurantID(int restaurantID) {
////        this.restaurantID = restaurantID;
////    }
//
//    public double getTotalAmount() {
//        return totalAmount;
//    }
//
//    public void setTotalAmount(double totalAmount) {
//        this.totalAmount = totalAmount;
//    }
//
//    public int getTaxID() {
//        return taxID;
//    }
//
//    public void setTaxID(int taxID) {
//        this.taxID = taxID;
//    }
//
//    public boolean getOrderPaid() {
//        return orderPaid;
//    }
//
//    public void setOrderPaid(boolean orderPaid) {
//        this.orderPaid = orderPaid;
//    }
//
//    public String getRejectReason() {
//        return rejectReason;
//    }
//
//    public void setRejectReason(String rejectReason) {
//        this.rejectReason = rejectReason;
//    }
//
//
//    //     String orderID;
////     String orderNumber;
////     String orderDate;
////     String orderType;
////     String orderStatus;
////     String orderMode;
////     String paymentID;
////     String dishID;
////     String dishName;
////     String dishRate;
////     String dishAmount;
////     String dishSize;
////     String dishImage;
////     String dishQuantity;
////     String taxableVal;
////     String CGST;
////     String SGST;
////     String CGSTVal;
////     String SGSTVal;
////     String subtotal;
////     String userAddress;
////     String voucher;
////     String userID;
////     String restaurantID;
////     String restaurantName;
////     String restaurantReviews;
////     String totalAmount;
////     String taxable;
////     String includeTax;
////     String foodType;
////     String taxID;
////     String orderPaid;
////     String userTypeID;
////     String discount;
////     String userRole;
////     String rejectReason;
////
////     public String getOrderID() {
////          return orderID;
////     }
////
////     public void setOrderID(String orderID) {
////          this.orderID = orderID;
////     }
////
////     public String getOrderNumber() {
////          return orderNumber;
////     }
////
////     public void setOrderNumber(String orderNumber) {
////          this.orderNumber = orderNumber;
////     }
////
////     public String getOrderDate() {
////          return orderDate;
////     }
////
////     public void setOrderDate(String orderDate) {
////          this.orderDate = orderDate;
////     }
////
////     public String getOrderType() {
////          return orderType;
////     }
////
////     public void setOrderType(String orderType) {
////          this.orderType = orderType;
////     }
////
////     public String getOrderStatus() {
////          return orderStatus;
////     }
////
////     public void setOrderStatus(String orderStatus) {
////          this.orderStatus = orderStatus;
////     }
////
////     public String getOrderMode() {
////          return orderMode;
////     }
////
////     public void setOrderMode(String orderMode) {
////          this.orderMode = orderMode;
////     }
////
////     public String getPaymentID() {
////          return paymentID;
////     }
////
////     public void setPaymentID(String paymentID) {
////          this.paymentID = paymentID;
////     }
////
////     public String getDishID() {
////          return dishID;
////     }
////
////     public void setDishID(String dishID) {
////          this.dishID = dishID;
////     }
////
////     public String getDishName() {
////          return dishName;
////     }
////
////     public void setDishName(String dishName) {
////          this.dishName = dishName;
////     }
////
////     public String getDishRate() {
////          return dishRate;
////     }
////
////     public void setDishRate(String dishRate) {
////          this.dishRate = dishRate;
////     }
////
////     public String getDishAmount() {
////          return dishAmount;
////     }
////
////     public void setDishAmount(String dishAmount) {
////          this.dishAmount = dishAmount;
////     }
////
////     public String getDishSize() {
////          return dishSize;
////     }
////
////     public void setDishSize(String dishSize) {
////          this.dishSize = dishSize;
////     }
////
////     public String getDishImage() {
////          return dishImage;
////     }
////
////     public void setDishImage(String dishImage) {
////          this.dishImage = dishImage;
////     }
////
////     public String getDishQuantity() {
////          return dishQuantity;
////     }
////
////     public void setDishQuantity(String dishQuantity) {
////          this.dishQuantity = dishQuantity;
////     }
////
////     public String getTaxableVal() {
////          return taxableVal;
////     }
////
////     public void setTaxableVal(String taxableVal) {
////          this.taxableVal = taxableVal;
////     }
////
////     public String getCGST() {
////          return CGST;
////     }
////
////     public void setCGST(String CGST) {
////          this.CGST = CGST;
////     }
////
////     public String getSGST() {
////          return SGST;
////     }
////
////     public void setSGST(String SGST) {
////          this.SGST = SGST;
////     }
////
////     public String getCGSTVal() {
////          return CGSTVal;
////     }
////
////     public void setCGSTVal(String CGSTVal) {
////          this.CGSTVal = CGSTVal;
////     }
////
////     public String getSGSTVal() {
////          return SGSTVal;
////     }
////
////     public void setSGSTVal(String SGSTVal) {
////          this.SGSTVal = SGSTVal;
////     }
////
////     public String getSubtotal() {
////          return subtotal;
////     }
////
////     public void setSubtotal(String subtotal) {
////          this.subtotal = subtotal;
////     }
////
////     public String getUserAddress() {
////          return userAddress;
////     }
////
////     public void setUserAddress(String userAddress) {
////          this.userAddress = userAddress;
////     }
////
////     public String getVoucher() {
////          return voucher;
////     }
////
////     public void setVoucher(String voucher) {
////          this.voucher = voucher;
////     }
////
////     public String getUserID() {
////          return userID;
////     }
////
////     public void setUserID(String userID) {
////          this.userID = userID;
////     }
////
////     public String getRestaurantID() {
////          return restaurantID;
////     }
////
////     public void setRestaurantID(String restaurantID) {
////          this.restaurantID = restaurantID;
////     }
////
////     public String getRestaurantName() {
////          return restaurantName;
////     }
////
////     public void setRestaurantName(String restaurantName) {
////          this.restaurantName = restaurantName;
////     }
////
////     public String getRestaurantReviews() {
////          return restaurantReviews;
////     }
////
////     public void setRestaurantReviews(String restaurantReviews) {
////          this.restaurantReviews = restaurantReviews;
////     }
////
////     public String getTotalAmount() {
////          return totalAmount;
////     }
////
////     public void setTotalAmount(String totalAmount) {
////          this.totalAmount = totalAmount;
////     }
////
////     public String getTaxable() {
////          return taxable;
////     }
////
////     public void setTaxable(String taxable) {
////          this.taxable = taxable;
////     }
////
////     public String getIncludeTax() {
////          return includeTax;
////     }
////
////     public void setIncludeTax(String includeTax) {
////          this.includeTax = includeTax;
////     }
////
////     public String getFoodType() {
////          return foodType;
////     }
////
////     public void setFoodType(String foodType) {
////          this.foodType = foodType;
////     }
////
////     public String getTaxID() {
////          return taxID;
////     }
////
////     public void setTaxID(String taxID) {
////          this.taxID = taxID;
////     }
////
////     public String getOrderPaid() {
////          return orderPaid;
////     }
////
////     public void setOrderPaid(String orderPaid) {
////          this.orderPaid = orderPaid;
////     }
////
////     public String getUserTypeID() {
////          return userTypeID;
////     }
////
////     public void setUserTypeID(String userTypeID) {
////          this.userTypeID = userTypeID;
////     }
////
////     public String getDiscount() {
////          return discount;
////     }
////
////     public void setDiscount(String discount) {
////          this.discount = discount;
////     }
////
////     public String getUserRole() {
////          return userRole;
////     }
////
////     public void setUserRole(String userRole) {
////          this.userRole = userRole;
////     }
////
////     public String getRejectReason() {
////          return rejectReason;
////     }
////
////     public void setRejectReason(String rejectReason) {
////          this.rejectReason = rejectReason;
////     }
////
//}
