package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class GetBillForTable {
    @SerializedName("OrderId")
    private Integer orderId;
    @SerializedName("Register_Id")
    private Integer registerId;
    @SerializedName("TableName")
    private String tableName;
    @SerializedName("EmployeeId")
    private Integer employeeId;
    @SerializedName("vchSplit_status")
    private String vchSplitStatus;
    @SerializedName("vchSplitTableName")
    private Object vchSplitTableName;
    @SerializedName("First_Name")
    private String firstName;
    @SerializedName("Last_Name")
    private String lastName;
    @SerializedName("Mobile_No")
    private String mobileNo;

    public GetBillForTable() {
    }

    public GetBillForTable(Integer orderId, Integer registerId, String tableName, Integer employeeId, String vchSplitStatus, Object vchSplitTableName, String firstName, String lastName, String mobileNo) {
        this.orderId = orderId;
        this.registerId = registerId;
        this.tableName = tableName;
        this.employeeId = employeeId;
        this.vchSplitStatus = vchSplitStatus;
        this.vchSplitTableName = vchSplitTableName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getRegisterId() {
        return registerId;
    }

    public void setRegisterId(Integer registerId) {
        this.registerId = registerId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getVchSplitStatus() {
        return vchSplitStatus;
    }

    public void setVchSplitStatus(String vchSplitStatus) {
        this.vchSplitStatus = vchSplitStatus;
    }

    public Object getVchSplitTableName() {
        return vchSplitTableName;
    }

    public void setVchSplitTableName(Object vchSplitTableName) {
        this.vchSplitTableName = vchSplitTableName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
