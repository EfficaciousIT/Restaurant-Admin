package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillDetailsResponse {
    @SerializedName("GetBillDetails")
    private List<GetBillDetail> getBillDetails = null;

    public List<GetBillDetail> getGetBillDetails() {
        return getBillDetails;
    }

    public void setGetBillDetails(List<GetBillDetail> getBillDetails) {
        this.getBillDetails = getBillDetails;
    }
}
