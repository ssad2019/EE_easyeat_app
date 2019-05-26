package com.example.lianghw.easyeat;

import java.io.Serializable;

public class Food implements Serializable {
    private String foodName;
    private String foodType;
    private String foodPrices;

    public Food(String foodName , String foodType, String foodPrices) {
        this.foodName = foodName;
        this.foodType = foodType;
        this.foodPrices = "$" + foodPrices;
    }

    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodType() {
        return foodType;
    }
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getFoodPrices() {
        return foodPrices;
    }
    public void setFoodPrices(String foodPrices) {
        this.foodName = "$" + foodPrices;
    }
}
