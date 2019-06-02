package com.example.lianghw.easyeat;

import java.io.Serializable;

public class Order_Food implements Serializable {
    private String foodName;
    private int count;
    private String foodPrices;
    private String foodImgUrl;

    public Order_Food(String foodName , int count, String foodPrices, String foodImgUrl) {
        this.foodName = foodName;
        this.count = count;
        this.foodPrices = "$" + foodPrices;
        this.foodImgUrl = foodImgUrl;
    }

    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
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
