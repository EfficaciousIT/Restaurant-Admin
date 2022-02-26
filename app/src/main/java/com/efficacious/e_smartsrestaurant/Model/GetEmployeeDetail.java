package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class GetEmployeeDetail {

    @SerializedName("Employee_Id")
    private Long employeeId;

    @SerializedName("First_Name")
    private String firstName;

    @SerializedName("Middle_Name")
    private String middleName;

    @SerializedName("Last_Name")
    private String lastName;

    @SerializedName("Gender")
    private String gender;

    @SerializedName("Mobile_No")
    private String mobileNo;

    @SerializedName("Email_Id")
    private String emailId;

    @SerializedName("Address_1")
    private String address1;

    @SerializedName("Imei_No")
    private String imeiNo;

    @SerializedName("vchFcmToken")
    private String vchFcmToken;

    @SerializedName("vchProfile")
    private String vchProfile;

    public GetEmployeeDetail() {
    }

    public GetEmployeeDetail(Long employeeId, String firstName, String middleName, String lastName, String gender, String mobileNo, String emailId, String address1, String imeiNo, String vchFcmToken, String vchProfile) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.address1 = address1;
        this.imeiNo = imeiNo;
        this.vchFcmToken = vchFcmToken;
        this.vchProfile = vchProfile;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getImeiNo() {
        return imeiNo;
    }

    public void setImeiNo(String imeiNo) {
        this.imeiNo = imeiNo;
    }

    public String getVchFcmToken() {
        return vchFcmToken;
    }

    public void setVchFcmToken(String vchFcmToken) {
        this.vchFcmToken = vchFcmToken;
    }

    public String getVchProfile() {
        return vchProfile;
    }

    public void setVchProfile(String vchProfile) {
        this.vchProfile = vchProfile;
    }

}
