/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
package com.example.lianghw.easyeat;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoreData implements Serializable {

    public List<Food> list_all_food = new ArrayList<>();
    public List<Food> list_order = new ArrayList<>();
    public List<String> list_type = new ArrayList<>();
    public List<BitmapFood> list_bitmap = new ArrayList<>();

    private static StoreData instance;

    public StoreData(){

    }

    /**
     * 获取StoreData单例
     * @return StoreData.instance
     */
    public static StoreData getInstance() {
        if(instance == null){
            instance = new StoreData();
        }
        return instance;
    }
}
