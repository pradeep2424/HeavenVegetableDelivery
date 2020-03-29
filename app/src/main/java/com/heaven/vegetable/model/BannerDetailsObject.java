package com.heaven.vegetable.model;

import java.io.Serializable;


public class BannerDetailsObject implements Serializable {

    String title;
    String photoURL;

//    public BannerDetailsObject(String title, String photoURL) {
//        this.title = title;
//        this.photoURL = photoURL;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}


