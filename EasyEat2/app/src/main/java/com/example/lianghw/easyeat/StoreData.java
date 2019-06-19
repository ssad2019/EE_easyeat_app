/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
package com.example.lianghw.easyeat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoreData implements Serializable {
    public List<Food> all_food_list = new ArrayList<>();
    public List<Food> order_food_list = new ArrayList<>();
    public List<String> food_type_list = new ArrayList<>();

    private static StoreData instance;

    public StoreData(){

    }

    public static StoreData getInstance() {
        if(instance == null){
            instance = new StoreData();
        }
        return instance;
    }
}
