package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KitchenNewOrderResponse {

    @SerializedName("GetKitchenOrder")
    private List<KitchenNewOrderDetail> getKitchenOrderList;

    public KitchenNewOrderResponse(List<KitchenNewOrderDetail> getKitchenOrderList) {
        this.getKitchenOrderList = getKitchenOrderList;
    }

    public List<KitchenNewOrderDetail> getGetKitchenOrderList() {
        return getKitchenOrderList;
    }

    public void setGetKitchenOrderList(List<KitchenNewOrderDetail> getKitchenOrderList) {
        this.getKitchenOrderList = getKitchenOrderList;
    }


}
