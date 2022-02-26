package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuResponse {
    @SerializedName("MenuDetails")
    private List<MenuDetail> menuDetails;

    public MenuResponse() {
    }

    public MenuResponse(List<MenuDetail> menuDetails) {
        this.menuDetails = menuDetails;
    }

    public List<MenuDetail> getMenuDetails() {
        return menuDetails;
    }

    public void setMenuDetails(List<MenuDetail> menuDetails) {
        this.menuDetails = menuDetails;
    }
}
