package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExistingOrderDetailsResponse {

    @SerializedName("GetExistingOrderDetails")
    private List<GetExistingOrderDetail> getExistingOrderDetails = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ExistingOrderDetailsResponse() {
    }

    /**
     *
     * @param getExistingOrderDetails
     */
    public ExistingOrderDetailsResponse(List<GetExistingOrderDetail> getExistingOrderDetails) {
        super();
        this.getExistingOrderDetails = getExistingOrderDetails;
    }

    public List<GetExistingOrderDetail> getGetExistingOrderDetails() {
        return getExistingOrderDetails;
    }

    public void setGetExistingOrderDetails(List<GetExistingOrderDetail> getExistingOrderDetails) {
        this.getExistingOrderDetails = getExistingOrderDetails;
    }
}
