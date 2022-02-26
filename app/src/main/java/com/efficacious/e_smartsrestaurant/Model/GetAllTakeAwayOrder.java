package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class GetAllTakeAwayOrder {
    @SerializedName("OrderId")
    private Long orderId;
    @SerializedName("Register_Id")
    private Long registerId;
    @SerializedName("Res_Id")
    private Long resId;

    public GetAllTakeAwayOrder() {
    }

    public GetAllTakeAwayOrder(Long orderId, Long registerId, Long resId) {
        this.orderId = orderId;
        this.registerId = registerId;
        this.resId = resId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Long registerId) {
        this.registerId = registerId;
    }

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

}
