package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FoodDetail extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主布居
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail_info);
        Intent intent = getIntent();
        final TextView food_name = (TextView)findViewById(R.id.food_name);
        final ImageView food_img = (ImageView)findViewById(R.id.food_img);
        final TextView food_prices = (TextView)findViewById(R.id.food_prices);
        final TextView food_detail = (TextView)findViewById(R.id.food_info);
        food_name.setText(intent.getStringExtra("food_name"));
        food_prices.setText(intent.getStringExtra("food_price"));
        food_detail.setMovementMethod(ScrollingMovementMethod.getInstance());
        //设置图片
    }
}
