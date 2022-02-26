package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
    @SerializedName("MenuCategoryDetails")
    List<MenuCategoryDetail> menuCategoryDetails;

    public CategoryResponse(List<MenuCategoryDetail> menuCategoryDetails) {
        this.menuCategoryDetails = menuCategoryDetails;
    }

    public List<MenuCategoryDetail> getMenuCategoryDetails() {
        return menuCategoryDetails;
    }

    public void setMenuCategoryDetails(List<MenuCategoryDetail> menuCategoryDetails) {
        this.menuCategoryDetails = menuCategoryDetails;
    }
}
