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

public class BitmapFood{
    private int id;
    private Bitmap bitmap;

    public BitmapFood(int id, Bitmap bitmap){
        this.id = id;
        this.bitmap = bitmap;
    }

    /**
     * 获取图片id
     * @return BitmapFood.id
     */
    public int getId(){return id;};

    /**
     * 获取图片bitmap
     * @return BitmapFood.bitmap
     */
    public Bitmap getBitmap(){return bitmap;}
}
