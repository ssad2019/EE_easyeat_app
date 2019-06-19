/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */

/**
 *
 *
 */
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
    public String getName() {
        return name;
    }
    public String getType() {
        return tag.name;
    }
    public String getPrice() {
        return price;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
}
