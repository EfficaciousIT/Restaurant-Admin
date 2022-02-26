package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantDetailsResponse {
    @SerializedName("GetRestaurantDetails")
    private List<GetRestaurantDetail> getRestaurantDetails = null;

    public List<GetRestaurantDetail> getGetRestaurantDetails() {
        return getRestaurantDetails;
    }

    public void setGetRestaurantDetails(List<GetRestaurantDetail> getRestaurantDetails) {
        this.getRestaurantDetails = getRestaurantDetails;
    }
}
