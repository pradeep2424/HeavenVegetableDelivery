package com.heaven.vegetable.model;

/**
 * Created by Wolf Soft on 8/3/2017.
 */

public class CuisineObject {

    Integer image;
    String price;
    String cuisineName;
    String city;

    public CuisineObject(Integer image, String price, String cuisineName, String city) {
        this.image = image;
        this.price = price;
        this.cuisineName = cuisineName;
        this.city = city;
    }


    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCuisineName() {
        return cuisineName;
    }

    public void setCuisineName(String cuisineName) {
        this.cuisineName = cuisineName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
