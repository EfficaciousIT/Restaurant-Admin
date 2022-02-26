package com.efficacious.e_smartsrestaurant.Model;

import com.efficacious.e_smartsrestaurant.DeliveryBoy.AttachData;

public class DeliveryBoyData extends AttachData {

    String MobileNumber;
    String Name;
    String isVerify;
    String TimeStamp;
    String Status;

    public DeliveryBoyData() {
    }

    public DeliveryBoyData(String mobileNumber, String name, String isVerify, String timeStamp, String status) {
        MobileNumber = mobileNumber;
        Name = name;
        this.isVerify = isVerify;
        TimeStamp = timeStamp;
        Status = status;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(String isVerify) {
        this.isVerify = isVerify;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
