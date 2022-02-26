package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class GetBillDetail {

    @SerializedName("OrderId")
    private Integer orderId;
    @SerializedName("TableName")
    private String tableName;
    @SerializedName("MenuName")
    private String menuName;
    @SerializedName("Price")
    private Float price;
    @SerializedName("Qty")
    private Integer qty;
    @SerializedName("EmployeeId")
    private Integer employeeId;

    public GetBillDetail() {
    }

    public GetBillDetail(Integer orderId, String tableName, String menuName, Float price, Integer qty, Integer employeeId) {
        this.orderId = orderId;
        this.tableName = tableName;
        this.menuName = menuName;
        this.price = price;
        this.qty = qty;
        this.employeeId = employeeId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

}
