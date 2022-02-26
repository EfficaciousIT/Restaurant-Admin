package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class UpdateToken {
    @SerializedName("Employee_Id")
    private Integer employeeId;
    @SerializedName("vchFcmToken")
    private String vchFcmToken;

    public UpdateToken(Integer employeeId, String vchFcmToken) {
        this.employeeId = employeeId;
        this.vchFcmToken = vchFcmToken;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getVchFcmToken() {
        return vchFcmToken;
    }

    public void setVchFcmToken(String vchFcmToken) {
        this.vchFcmToken = vchFcmToken;
    }
}
