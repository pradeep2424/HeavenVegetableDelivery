package com.heaven.vegetable.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Wolf Soft on 8/3/2017.
 */

public class ProductObject implements Serializable {
    double cgst;
    int categoryID;
    String categoryName;
    String foodType;
    int foodTypeID;
    int dishID;
    String isDiscounted;
    double price;
   String productDescription;
    int productID;
//    Integer listProductImage[] ;
    ArrayList<String> listProductImage;
    String productName;
    double sgst;
    int taxID;
    String taxName;
    int productQuantity;
    int groupID;
    String group;
    int unitID;
    String unit;
    ArrayList<UnitObject> listUnits;

    public ArrayList<UnitObject> getListUnits() {
        return listUnits;
    }

    public void setListUnits(ArrayList<UnitObject> listUnits) {
        this.listUnits = listUnits;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public ArrayList<String> getListProductImage() {
        return listProductImage;
    }

    public void setListProductImage(ArrayList<String> listProductImage) {
        this.listProductImage = listProductImage;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public int getFoodTypeID() {
        return foodTypeID;
    }

    public void setFoodTypeID(int foodTypeID) {
        this.foodTypeID = foodTypeID;
    }

    public int getDishID() {
        return dishID;
    }

    public void setDishID(int dishID) {
        this.dishID = dishID;
    }

    public String getIsDiscounted() {
        return isDiscounted;
    }

    public void setIsDiscounted(String isDiscounted) {
        this.isDiscounted = isDiscounted;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

//    public  Integer[] getProductImage() {
//        return listProductImage;
//    }
//
//    public void setProductImage( Integer[] listProductImage) {
//        this.listProductImage = listProductImage;
//    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    //    String dishID;
//    String dishName;
//    String dishDescription;
//    String dishCategory;
//    String dishImage;
//    String dishAmount;
//    int dishQuantity;
//
//    public int getDishQuantity() {
//        return dishQuantity;
//    }
//
//    public void setDishQuantity(int dishQuantity) {
//        this.dishQuantity = dishQuantity;
//    }
//
//    public String getDishCategory() {
//        return dishCategory;
//    }
//
//    public void setDishCategory(String dishCategory) {
//        this.dishCategory = dishCategory;
//    }
//
//    public String getDishID() {
//        return dishID;
//    }
//
//    public void setDishID(String dishID) {
//        this.dishID = dishID;
//    }
//
//    public String getDishName() {
//        return dishName;
//    }
//
//    public void setDishName(String dishName) {
//        this.dishName = dishName;
//    }
//
//    public String getDishDescription() {
//        return dishDescription;
//    }
//
//    public void setDishDescription(String dishDescription) {
//        this.dishDescription = dishDescription;
//    }
//
//    public String getDishImage() {
//        return dishImage;
//    }
//
//    public void setDishImage(String dishImage) {
//        this.dishImage = dishImage;
//    }
//
//    public String getDishAmount() {
//        return dishAmount;
//    }
//
//    public void setDishAmount(String dishAmount) {
//        this.dishAmount = dishAmount;
//    }
}
