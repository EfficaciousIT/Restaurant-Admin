package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetails {

    List<OrderDetails> orderDetails;

    @SerializedName("OrderId")
    private Integer orderId;
    @SerializedName("CategoryName")
    private String categoryName;
    @SerializedName("MenuName")
    private String menuName;
    @SerializedName("TableName")
    private String tableName;
    @SerializedName("RegisterId")
    private Integer registerId;
    @SerializedName("EmployeeId")
    private Integer employeeId;
    @SerializedName("Price")
    private Integer price;
    @SerializedName("Qty")
    private Integer qty;
    @SerializedName("vchFoodDescription")
    private String vchFoodDescription;
    @SerializedName("Kitchen_status")
    private String kitchenStatus;
    @SerializedName("status")
    private String status;

    @SerializedName("TimeStamp")
    private String timeStamp;

    public OrderDetails() {
    }

    public OrderDetails(Integer orderId, String categoryName, String menuName, String tableName, Integer registerId, Integer employeeId, Integer price, Integer qty, String vchFoodDescription, String kitchenStatus, String status, String timeStamp) {
        this.orderId = orderId;
        this.categoryName = categoryName;
        this.menuName = menuName;
        this.tableName = tableName;
        this.registerId = registerId;
        this.employeeId = employeeId;
        this.price = price;
        this.qty = qty;
        this.vchFoodDescription = vchFoodDescription;
        this.kitchenStatus = kitchenStatus;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Integer registerId) {
        this.registerId = registerId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getVchFoodDescription() {
        return vchFoodDescription;
    }

    public void setVchFoodDescription(String vchFoodDescription) {
        this.vchFoodDescription = vchFoodDescription;
    }

    public String getKitchenStatus() {
        return kitchenStatus;
    }

    public void setKitchenStatus(String kitchenStatus) {
        this.kitchenStatus = kitchenStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
