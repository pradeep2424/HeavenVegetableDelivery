package com.heaven.vegetable.listeners;

/**
 * Created by Pradeep Jadhav on 9/1/2017.
 */

public interface OnItemAddedToCart {
    void onItemChangedInCart(int quantity, int position, String incrementOrDecrement);
}