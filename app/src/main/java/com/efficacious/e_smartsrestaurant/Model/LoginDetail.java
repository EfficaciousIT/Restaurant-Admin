package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginDetail {
    @SerializedName("Res_Id")
    @Expose
    private Integer resId;
    @SerializedName("UserType_Id")
    @Expose
    private Integer userTypeId;
    @SerializedName("Employee_Id")
    @Expose
    private Integer employeeId;
    @SerializedName("First_Name")
    @Expose
    private String firstName;
    @SerializedName("Last_Name")
    @Expose
    private String lastName;
    @SerializedName("User_Type")
    @Expose
    private String userType;
    @SerializedName("User_Name")
    @Expose
    private String userName;
    @SerializedName("Password")
    @Expose
    private String password;

    public LoginDetail() {
    }

    public LoginDetail(Integer resId, Integer userTypeId, Integer employeeId, String firstName, String lastName, String userType, String userName, String password) {
        this.resId = resId;
        this.userTypeId = userTypeId;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.userName = userName;
        this.password = password;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
