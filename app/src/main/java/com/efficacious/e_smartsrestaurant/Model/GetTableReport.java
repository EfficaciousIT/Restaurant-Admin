package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class GetTableReport {
    @SerializedName("Order_id")
    private Long orderId;
    @SerializedName("Table_Name")
    private String tableName;
    @SerializedName("Total")
    private int total;
    @SerializedName("Status")
    private String status;
    @SerializedName("Res_Id")
    private Long resId;
    @SerializedName("Created_Date")
    private String createdDate;

    public GetTableReport() {
    }

    public GetTableReport(Long orderId, String tableName, int total, String status, Long resId, String createdDate) {
        this.orderId = orderId;
        this.tableName = tableName;
        this.total = total;
        this.status = status;
        this.resId = resId;
        this.createdDate = createdDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getResId() {
        return resId;
    }

    public void setResId(Long resId) {
        this.resId = resId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
