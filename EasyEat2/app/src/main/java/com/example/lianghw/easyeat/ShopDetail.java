package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

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
