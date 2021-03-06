package com.efficacious.e_smartsrestaurant.Model;

import com.google.gson.annotations.SerializedName;

public class MenuCategoryDetail {
    @SerializedName("Cat_Id")
    private Integer catId;
    @SerializedName("Cat_Name")
    private String catName;
    @SerializedName("Url_Category")
    private Object urlCategory;

    public Integer getCatId() {
        return catId;
    }

    public void setCatId(Integer catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public Object getUrlCategory() {
        return urlCategory;
    }

    public void setUrlCategory(Object urlCategory) {
        this.urlCategory = urlCategory;
    }
}
