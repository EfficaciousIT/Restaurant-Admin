package com.efficacious.e_smartsrestaurant.Model;

public class TakeAwayOrderData {

    String DeliveryBoyId;
    String MobileNumber;
    String OrderId;
    String RegisterId;
    String Status;
    String TotalBill;
    Long TimeStamp;
    String UserName;

    public TakeAwayOrderData() {
    }

    public TakeAwayOrderData(String deliveryBoyId, String mobileNumber, String orderId, String registerId, String status, String totalBill, Long timeStamp, String userName) {
        DeliveryBoyId = deliveryBoyId;
        MobileNumber = mobileNumber;
        OrderId = orderId;
        RegisterId = registerId;
        Status = status;
        TotalBill = totalBill;
        TimeStamp = timeStamp;
        UserName = userName;
    }

    public String getDeliveryBoyId() {
        return DeliveryBoyId;
    }

    public void setDeliveryBoyId(String deliveryBoyId) {
        DeliveryBoyId = deliveryBoyId;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getRegisterId() {
        return RegisterId;
    }

    public void setRegisterId(String registerId) {
        RegisterId = registerId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTotalBill() {
        return TotalBill;
    }

    public void setTotalBill(String totalBill) {
        TotalBill = totalBill;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
