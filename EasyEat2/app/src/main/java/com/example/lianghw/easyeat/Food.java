package com.example.lianghw.easyeat;

import java.io.Serializable;


//记录菜品数据的结构体
public class Food implements Serializable {
    private class Tag{
        public int id;
        public String name;
        public Tag(int id,String name){
            this.id = id;
            this.name = name;
        }
    }
    private int id;
    private String name;
    private Tag tag;
    private String price;
    private String icon;
    private int count;

    public Food(String foodName , String foodType, String foodPrices, String foodImgUrl, int count) {
        this.name = foodName;
        this.tag = new Tag(0,foodType);
        this.price = "$" + foodPrices;
        this.icon = foodImgUrl;
        this.count = count;
    }

    public String getFoodName() {
        return name;
    }
    public void setFoodName(String foodName) {
        this.name = foodName;
    }

    public String getFoodType() {
        return tag.name;
    }
    public void setFoodType(String foodType) {
        this.tag.name = foodType;
    }

    public String getFoodPrices() {
        return price;
    }
    public void setFoodPrices(String foodPrices) {
        this.price = "$" + foodPrices;
    }

    public String getFoodImgUrl(){ return icon;}
    public void setFoodImgUrl(String foodImgUrl){ this.icon = foodImgUrl; }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
