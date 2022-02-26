package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class GetRestaurantDetail {
    @SerializedName("ResId")
    private Integer resId;
    @SerializedName("ResName")
    private String resName;
    @SerializedName("Address")
    private String address;
    @SerializedName("ResType")
    private String resType;
    @SerializedName("Email")
    private String email;
    @SerializedName("Mobile")
    private String mobile;
    @SerializedName("GSTIN")
    private String gstin;

    public GetRestaurantDetail() {
    }

    public GetRestaurantDetail(Integer resId, String resName, String address, String resType, String email, String mobile, String gstin) {
        this.resId = resId;
        this.resName = resName;
        this.address = address;
        this.resType = resType;
        this.email = email;
        this.mobile = mobile;
        this.gstin = gstin;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }
}
