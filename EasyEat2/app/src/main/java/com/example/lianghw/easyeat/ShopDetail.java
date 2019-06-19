/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

//商店详情界面
public class ShopDetail extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主布居
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_info);
        final TextView shop_name_text = (TextView)findViewById(R.id.shop_name);
        final TextView shop_info_text =(TextView)findViewById(R.id.shop_info);
        final ImageView shop_img_view = (ImageView)findViewById(R.id.shop_img);

        Intent intent = getIntent();
        shop_name_text.setText(intent.getStringExtra("shop_name"));
        shop_info_text.setText(intent.getStringExtra("shop_info"));
        shop_info_text.setMovementMethod(ScrollingMovementMethod.getInstance());
        //设置图片url
        //shop_img_view.setImageURI("");
    }
}
