package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class TableStatusDetail {
    @SerializedName("Table_Status")
    private String tableStatus;
    @SerializedName("Table_Id")
    private Integer tableId;
    @SerializedName("Table_Name")
    private String tableName;
    @SerializedName("Res_Id")
    private Integer resId;

    public TableStatusDetail() {
    }

    public TableStatusDetail(String tableStatus, Integer tableId, String tableName, Integer resId) {
        this.tableStatus = tableStatus;
        this.tableId = tableId;
        this.tableName = tableName;
        this.resId = resId;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }
}
