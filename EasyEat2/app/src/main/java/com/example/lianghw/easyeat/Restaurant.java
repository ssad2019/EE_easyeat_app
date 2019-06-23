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

    /**
     * 通过url获取餐馆信息
     * @param url String 餐馆url
     * @return 餐馆 Restaurant
     */
    public static Restaurant getRestaurantByUrl(String url){
        String str_restaurant = Network.getInstance().doGet(url);
        Restaurant r = tranJsonToClass(str_restaurant);
        return r;
    }

    /**
     * 提交订单
     * @param foods List<Food> 订单列表
     * @param remark String 备注
     * @return <订单id，订单时间> Pair<String,String>
     */
    public Pair<String,String> pushOrder(List<Food> foods,String remark){
        Order order = new Order(foods,remark);
        String  str_orderJson = new Gson().toJson(order);

        String str_mes = Network.getInstance().orderFood(str_orderJson);
        OrderReturn myOrderReturn= new Gson().fromJson(str_mes, message2.class).data;
        return new Pair<String,String>(myOrderReturn.order_id,myOrderReturn.time);
    }

    /**
     * 餐馆json转为结构体
     * @param restaurant_str String 餐馆json
     * @return  餐馆结构体 Restaurant
     */
    private static Restaurant tranJsonToClass(String restaurant_str){
        Restaurant res= new Gson().fromJson(restaurant_str, message1.class).data;
        return res;

    }

    /**
     * 获取餐厅url
     * @param url String
     * @return  url加上app识别码 String
     */
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
