package com.efficacious.e_smartsrestaurant.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class TableBook {

    @PrimaryKey(autoGenerate = true)
    int tableBookId;

    private String tableName;
    private Integer registerId;
    private Integer employeeId;
    private String deviceId;
    private Integer isActive;
    private Integer intPersonCount;
    private Integer resId;
    private String vchSplitTableName;
    private String vchSplitStatus;
    private String status;



    public TableBook() {
    }

    public TableBook(String tableName, Integer registerId, Integer employeeId, String deviceId, Integer isActive, Integer intPersonCount, Integer resId, String vchSplitTableName, String vchSplitStatus, String status) {
        this.tableName = tableName;
        this.registerId = registerId;
        this.employeeId = employeeId;
        this.deviceId = deviceId;
        this.isActive = isActive;
        this.intPersonCount = intPersonCount;
        this.resId = resId;
        this.vchSplitTableName = vchSplitTableName;
        this.vchSplitStatus = vchSplitStatus;
        this.status = status;
    }

    public int getTableBookId() {
        return tableBookId;
    }

    public void setTableBookId(int tableBookId) {
        this.tableBookId = tableBookId;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIntPersonCount() {
        return intPersonCount;
    }

    public void setIntPersonCount(Integer intPersonCount) {
        this.intPersonCount = intPersonCount;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getVchSplitTableName() {
        return vchSplitTableName;
    }

    public void setVchSplitTableName(String vchSplitTableName) {
        this.vchSplitTableName = vchSplitTableName;
    }

    public String getVchSplitStatus() {
        return vchSplitStatus;
    }

    public void setVchSplitStatus(String vchSplitStatus) {
        this.vchSplitStatus = vchSplitStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
