/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
package com.example.lianghw.easyeat;

import java.util.HashMap;

public class TypeListViewItem {
    public int type;
    public HashMap<String, Object> map;

    public static final int TYPELISTITEMVIEW_TYPE_1 = 0;
    public static final int TYPELISTVIEWITEM_TYPE_2 = 1;
    public static final int TYPELISTVIEWITEM_TYPE_3 = 2;

    public TypeListViewItem(int type, HashMap<String, Object> map) {
        this.type = type;
        this.map = map;
    }
}
