package com.efficacious.e_smartsrestaurant.Model;

public class CartData {
    int foodImg;
    String foodName;
    int foodPrice;

    public CartData(int foodImg, String foodName, int foodPrice) {
        this.foodImg = foodImg;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
    }

    public CartData(CartData[] menuData) {
    }

    public int getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(int foodImg) {
        this.foodImg = foodImg;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }
}
