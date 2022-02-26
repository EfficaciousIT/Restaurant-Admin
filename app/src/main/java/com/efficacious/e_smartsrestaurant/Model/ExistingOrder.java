package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class ExistingOrder {

    @SerializedName("OrderId")
    private Integer orderId;
    @SerializedName("TableName")
    private String tableName;
    @SerializedName("RegisterId")
    private Integer registerId;
    @SerializedName("EmployeeId")
    private Integer employeeId;
    @SerializedName("ResId")
    private Integer resId;
    @SerializedName("personCont")
    private Integer personCont;

    public ExistingOrder() {
    }

    public ExistingOrder(Integer orderId, String tableName, Integer registerId, Integer employeeId, Integer resId, Integer personCont) {
        this.orderId = orderId;
        this.tableName = tableName;
        this.registerId = registerId;
        this.employeeId = employeeId;
        this.resId = resId;
        this.personCont = personCont;
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

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public Integer getPersonCont() {
        return personCont;
    }

    public void setPersonCont(Integer personCont) {
        this.personCont = personCont;
    }
}
