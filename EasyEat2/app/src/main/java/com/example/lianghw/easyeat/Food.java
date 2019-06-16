package com.example.lianghw.easyeat;

import java.io.Serializable;


//记录菜品数据的结构体
public class Food implements Serializable {
    private class Tag implements Serializable{
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

    public int getId() {
        return id;
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
        this.price = foodPrices;
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
