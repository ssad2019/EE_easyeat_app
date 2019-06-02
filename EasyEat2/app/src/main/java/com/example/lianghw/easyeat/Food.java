package com.example.lianghw.easyeat;

import java.io.Serializable;

public class Food implements Serializable {
    private String foodName;
    private String foodType;
    private String foodPrices;
    private String foodImgUrl;

    public Food(String foodName , String foodType, String foodPrices, String foodImgUrl) {
        this.foodName = foodName;
        this.foodType = foodType;
        this.foodPrices = "$" + foodPrices;
        this.foodImgUrl = foodImgUrl;
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

    public String getFoodImgUrl(){ return foodImgUrl;}
    public void setFoodImgUrl(String foodImgUrl){ this.foodImgUrl = foodImgUrl; }
}
