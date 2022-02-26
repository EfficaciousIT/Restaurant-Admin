package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTableReportResponse {
    @SerializedName("GetTableReport")
    private List<GetTableReport> getTableReport = null;

    public List<GetTableReport> getGetTableReport() {
        return getTableReport;
    }

    public void setGetTableReport(List<GetTableReport> getTableReport) {
        this.getTableReport = getTableReport;
    }
}
