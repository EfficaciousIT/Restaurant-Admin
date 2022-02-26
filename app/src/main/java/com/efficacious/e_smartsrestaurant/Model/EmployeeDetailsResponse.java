package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeDetailsResponse {

    @SerializedName("GetEmployeeDetails")
    private List<GetEmployeeDetail> getEmployeeDetails = null;

    public List<GetEmployeeDetail> getGetEmployeeDetails() {
        return getEmployeeDetails;
    }

    public void setGetEmployeeDetails(List<GetEmployeeDetail> getEmployeeDetails) {
        this.getEmployeeDetails = getEmployeeDetails;
    }
}
