package com.heaven.vegetable.model;

import java.io.Serializable;

/**
 * Created by Wolf Soft on 8/3/2017.
 */

public class OrderTimelineObject implements Serializable {
    String title;
    String date;
    String message;
    OrderStatusEnum status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }
}
