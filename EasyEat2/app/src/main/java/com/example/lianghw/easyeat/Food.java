package com.example.lianghw.easyeat;

import java.io.Serializable;

public class Food implements Serializable {
    private String foodName;
    private String foodType;
    private String foodType_short;
    private String foodNutrition;
    private int backgroindColor;



    public Food(String foodName , String foodType_short) {
        this.foodName = foodName;
        this.foodType = "种类";
        this.foodType_short = foodType_short;
        this.foodNutrition = "营养物质";
        this.backgroindColor = 1;
    }
    public Food(String foodName , String foodType, String foodType_short, String foodNutrition,int backgroindColor) {
        this.foodName = foodName;
        this.foodType = foodType;
        this.foodType_short = foodType_short;
        this.foodNutrition = foodNutrition;
        this.backgroindColor = backgroindColor;
    }

    public int getBackgroindColor() {
        return backgroindColor;
    }

    public void setBackgroindColor(int backgroindColor) {
        this.backgroindColor = backgroindColor;
    }

    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodNutrition() {
        return foodNutrition;
    }

    public void setFoodNutrition(String foodNutrition) {
        this.foodNutrition = foodNutrition;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType_short(String foodType_short) {
        this.foodType_short = foodType_short;
    }

    public String getFoodType_short() {
        return foodType_short;
    }
    // get & set 方法
}
