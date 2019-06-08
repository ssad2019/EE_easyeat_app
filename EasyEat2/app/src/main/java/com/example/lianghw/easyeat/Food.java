package com.example.lianghw.easyeat;

import java.io.Serializable;

//记录菜品数据的结构体
public class Food implements Serializable {
    private String foodName;
    private String foodType;
    private String foodPrices;
    private String foodImgUrl;
    private int count;

    public Food(String foodName , String foodType, String foodPrices, String foodImgUrl, int count) {
        this.foodName = foodName;
        this.foodType = foodType;
        this.foodPrices = "$" + foodPrices;
        this.foodImgUrl = foodImgUrl;
        this.count = count;
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
        this.foodPrices = "$" + foodPrices;
    }

    public String getFoodImgUrl(){ return foodImgUrl;}
    public void setFoodImgUrl(String foodImgUrl){ this.foodImgUrl = foodImgUrl; }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
