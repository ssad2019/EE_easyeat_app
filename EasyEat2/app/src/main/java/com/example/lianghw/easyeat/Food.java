/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */

/**
 * 记录菜品数据的结构体
 *
 */
package com.example.lianghw.easyeat;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.lianghw.easyeat.Restaurant.getRestaurantByUrl;

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

    /**
     * 获取Food的id
     * @return Food.id
     */
    public int getId() {
        return id;
    }

    /**
     * 获取Food的Name
     * @return Food.Name
     */
    public String getName() {
        return name;
    }

    /**
     * 获取Food的Type
     * @return Food.Type
     */
    public String getType() {
        return tag.name;
    }

    /**
     * 获取Food的Price
     * @return Food.Price
     */
    public String getPrice() {
        return price;
    }

    /**
     * 获取Food的Count
     * @return Food.Count
     */
    public int getCount() {
        return count;
    }

    /**
     * 设置Food的Count
     * @param count int 设置的count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 设获取Food的Icon (img url)
     * @return Food.icon
     */
    public String getIcon(){return icon;}
}
