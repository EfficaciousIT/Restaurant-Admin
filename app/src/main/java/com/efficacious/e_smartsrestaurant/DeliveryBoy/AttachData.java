package com.efficacious.e_smartsrestaurant.DeliveryBoy;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class AttachData {
    @Exclude
    public String AttachOrderId;
    public String TotalBill;
    public String RegisterId;
    public String UserName;
    public String MobileNo;


    public <T extends AttachData> T withId(@NonNull final String id, String totalBill,String registerId, String userName, String mobileNumber){
        this.AttachOrderId = id;
        this.TotalBill = totalBill;
        this.RegisterId = registerId;
        this.UserName = userName;
        this.MobileNo = mobileNumber;
        return (T)this;
    }
}
