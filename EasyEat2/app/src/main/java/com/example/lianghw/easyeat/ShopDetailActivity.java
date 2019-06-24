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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

//商店详情界面
public class ShopDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主布居
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        StoreData data_instance = StoreData.getInstance();

        final TextView txt_shop_name = (TextView)findViewById(R.id.txt_name);
        final TextView txt_shop_description =(TextView)findViewById(R.id.txt_description);
        final ImageView img_shop = (ImageView)findViewById(R.id.img_shop);

        Intent intent = getIntent();
        txt_shop_name.setText(intent.getStringExtra("shop_name"));
        txt_shop_description.setText(intent.getStringExtra("shop_info"));
        txt_shop_description.setMovementMethod(ScrollingMovementMethod.getInstance());

        if(data_instance.bitmap_shop != null)
        {
            img_shop.setImageBitmap(data_instance.bitmap_shop);
        }
    }
}
