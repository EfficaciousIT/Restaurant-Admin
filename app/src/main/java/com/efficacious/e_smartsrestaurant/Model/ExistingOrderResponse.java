package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExistingOrderResponse {

    @SerializedName("GetExistingOrder")
    private List<ExistingOrder> existingOrder;

    public ExistingOrderResponse() {
    }

    public ExistingOrderResponse(List<ExistingOrder> existingOrder) {
        this.existingOrder = existingOrder;
    }

    public List<ExistingOrder> getExistingOrder() {
        return existingOrder;
    }

    public void setExistingOrder(List<ExistingOrder> existingOrder) {
        this.existingOrder = existingOrder;
    }


}
