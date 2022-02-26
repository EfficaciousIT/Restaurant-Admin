package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillUpdateDetails {
    List<BillUpdateDetails> billUpdateDetails;

    @SerializedName("OrderId")
    private Integer orderId;
    @SerializedName("status")
    private String status;
    @SerializedName("Total")
    private Integer total;

    public BillUpdateDetails(Integer orderId, String status, Integer total) {
        this.orderId = orderId;
        this.status = status;
        this.total = total;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
