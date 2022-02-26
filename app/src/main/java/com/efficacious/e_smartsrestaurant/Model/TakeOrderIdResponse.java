package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TakeOrderIdResponse {
    @SerializedName("GetOrderId")
    private List<GetOrderId> getOrderId = null;

    public List<GetOrderId> getGetOrderId() {
        return getOrderId;
    }

    public void setGetOrderId(List<GetOrderId> getOrderId) {
        this.getOrderId = getOrderId;
    }

}
