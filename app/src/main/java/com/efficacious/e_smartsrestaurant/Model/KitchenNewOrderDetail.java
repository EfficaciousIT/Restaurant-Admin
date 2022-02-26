package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class KitchenNewOrderDetail {

    @SerializedName("OrderId")
    private Integer orderId;
    @SerializedName("TableName")
    private String tableName;
    @SerializedName("RegisterId")
    private Integer registerId;
    @SerializedName("Total")
    private Object total;
    @SerializedName("Flag")
    private String flag;
    @SerializedName("vchSplitStatus")
    private String vchSplitStatus;
    @SerializedName("vchSplitTableName")
    private String vchSplitTableName;

    public KitchenNewOrderDetail() {
    }

    public KitchenNewOrderDetail(Integer orderId, String tableName, Integer registerId, Object total, String flag, String vchSplitStatus, String vchSplitTableName) {
        this.orderId = orderId;
        this.tableName = tableName;
        this.registerId = registerId;
        this.total = total;
        this.flag = flag;
        this.vchSplitStatus = vchSplitStatus;
        this.vchSplitTableName = vchSplitTableName;
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

    public Object getTotal() {
        return total;
    }

    public void setTotal(Object total) {
        this.total = total;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getVchSplitStatus() {
        return vchSplitStatus;
    }

    public void setVchSplitStatus(String vchSplitStatus) {
        this.vchSplitStatus = vchSplitStatus;
    }

    public String getVchSplitTableName() {
        return vchSplitTableName;
    }

    public void setVchSplitTableName(String vchSplitTableName) {
        this.vchSplitTableName = vchSplitTableName;
    }


}
