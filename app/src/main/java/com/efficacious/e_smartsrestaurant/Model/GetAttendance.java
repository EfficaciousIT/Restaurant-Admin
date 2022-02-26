package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class GetAttendance {

    @SerializedName("Employee_Id")
    private Integer employeeId;
    @SerializedName("Intime")
    private String intime;
    @SerializedName("Outtime")
    private String outtime;
    @SerializedName("Status")
    private String status;
    @SerializedName("Res_Id")
    private Integer resId;
    @SerializedName("Created_Date")
    private String createdDate;

    public GetAttendance() {

    }

    public GetAttendance(Integer employeeId, String intime, String outtime, String status, Integer resId, String createdDate) {
        this.employeeId = employeeId;
        this.intime = intime;
        this.outtime = outtime;
        this.status = status;
        this.resId = resId;
        this.createdDate = createdDate;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
