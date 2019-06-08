package com.example.lianghw.easyeat;

import android.util.Pair;

import com.google.gson.Gson;

public class Restaurant {
    private class message{
        int status;
        String msg;
        Restaurant data;
    }
    private class simpleFood{
        int id;
        int number;
    }
    private class order{
        simpleFood[] content;
        String remark;
    }
    String name;
    String description;
    String icon;//商家头像url
    Food[] goods;

    public static Restaurant getRestaurantByUrl(String url){
        String restaurantStr = Network.doGet(url);
        return jsonToClass(restaurantStr);
    }
/*
    public static Pair<String,String> pushOrder(List<Food> foods,){
        String restaurantStr = Network.doGet(url);
        return jsonToClass(restaurantStr);
    }*/

    private static Restaurant jsonToClass(String restaurantStr){
        Restaurant res= new Gson().fromJson(restaurantStr, message.class).data;
        return res;

    }
    public static String getUrlFromQRcode(String url){
        String SECRET = "gg";
        return url+"&secret="+SECRET;
    }
}
