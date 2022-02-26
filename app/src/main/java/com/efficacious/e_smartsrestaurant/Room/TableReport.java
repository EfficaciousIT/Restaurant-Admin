package com.efficacious.e_smartsrestaurant.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TableReport {

    @PrimaryKey(autoGenerate = true)
    int tableReportId;

    private Long orderId;
    private String tableName;
    private Long total;
    private String status;
    private Long resId;
    private String createdDate;

    public TableReport() {
    }

    public TableReport(Long orderId, String tableName, Long total, String status, Long resId, String createdDate) {
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

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
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
