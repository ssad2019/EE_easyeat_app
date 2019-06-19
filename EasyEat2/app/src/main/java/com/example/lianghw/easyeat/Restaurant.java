/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
/**
 * Restaurant
 * Restaurant对象类
 * 记录餐厅数据的结构体
 * 包含餐厅名称：name、描述信息：description、餐厅图标url：icon、食物列表：goods四个数据
 *
 */
package com.example.lianghw.easyeat;

import android.util.Pair;

import com.google.gson.Gson;

import java.util.List;

public class Restaurant {

    String name;
    String description;
    String icon;//商家头像url
    Food[] goods;

    public static Restaurant getRestaurantByUrl(String url){
        String restaurantStr = Network.getInstance().doGet(url);
        return jsonToClass(restaurantStr);
    }

    public Pair<String,String> pushOrder(List<Food> foods,String remark){
        Order order = new Order(foods,remark);
        String  orderJson = new Gson().toJson(order);

        String mes = Network.getInstance().orderFood(orderJson);
        OrderReturn myOrderReturn= new Gson().fromJson(mes, message2.class).data;
        return new Pair<String,String>(myOrderReturn.order_id,myOrderReturn.time);
    }

    private static Restaurant jsonToClass(String restaurantStr){
        Restaurant res= new Gson().fromJson(restaurantStr, message1.class).data;
        return res;

    }
    public static String getUrlFromQRcode(String url){
        String SECRET = "gg";
        return url+"&secret="+SECRET;
    }

    private class message1{
        int status;
        String msg;
        Restaurant data;
    }
    private class message2{
        int status;
        String msg;
        OrderReturn data;
    }
    private class OrderReturn{
        String order_id;
        String time;
    }
    private class simpleFood{
        int id;
        int number;
        public simpleFood(Food food){
            id = food.getId();
            number = food.getCount();
        }
    }
    private class Order{
        simpleFood[] content;
        String remark;
        public Order(List<Food> foods,String remark){
            this.remark = remark;
            content = new simpleFood[foods.size()];
            int i = 0;
            for(Food food:foods){
                content[i] = new simpleFood(food);
                i++;
            }
        }
    }
}
