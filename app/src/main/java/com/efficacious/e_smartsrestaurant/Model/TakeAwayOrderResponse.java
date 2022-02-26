package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TakeAwayOrderResponse {
    @SerializedName("GetAllTakeAwayOrder")
    private List<GetAllTakeAwayOrder> getAllTakeAwayOrder = null;

    public List<GetAllTakeAwayOrder> getGetAllTakeAwayOrder() {
        return getAllTakeAwayOrder;
    }

    public void setGetAllTakeAwayOrder(List<GetAllTakeAwayOrder> getAllTakeAwayOrder) {
        this.getAllTakeAwayOrder = getAllTakeAwayOrder;
    }
}
