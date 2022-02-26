package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TableStatusResponse {
    @SerializedName("TableStatusDetails")
    private List<TableStatusDetail> tableStatusDetails = null;

    public List<TableStatusDetail> getTableStatusDetails() {
        return tableStatusDetails;
    }

    public void setTableStatusDetails(List<TableStatusDetail> tableStatusDetails) {
        this.tableStatusDetails = tableStatusDetails;
    }

}
