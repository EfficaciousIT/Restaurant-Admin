package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBillForTableResponse {
    @SerializedName("GetBillForTable")
    private List<GetBillForTable> getBillForTable = null;

    public List<GetBillForTable> getGetBillForTable() {
        return getBillForTable;
    }

    public void setGetBillForTable(List<GetBillForTable> getBillForTable) {
        this.getBillForTable = getBillForTable;
    }
}
