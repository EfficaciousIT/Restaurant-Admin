package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class InsertAttendance {
    @SerializedName("Employee_Id")
    private int employeeId;
    @SerializedName("Status")
    private String status;
    @SerializedName("Res_Id")
    private int resId;

    public InsertAttendance(int employeeId, String status, int resId) {
        this.employeeId = employeeId;
        this.status = status;
        this.resId = resId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
