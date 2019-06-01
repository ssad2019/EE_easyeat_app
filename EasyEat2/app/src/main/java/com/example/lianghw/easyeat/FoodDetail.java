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
        Food food_item = (Food) intent.getExtras().getSerializable("food_item");
        final TextView food_name = (TextView)findViewById(R.id.food_name);
        final ImageView food_img = (ImageView)findViewById(R.id.food_img);
        final TextView food_prices = (TextView)findViewById(R.id.food_prices);
        final TextView food_detail = (TextView)findViewById(R.id.food_info);
        food_name.setText(food_item.getFoodName());
        food_prices.setText(food_item.getFoodPrices());
        food_detail.setMovementMethod(ScrollingMovementMethod.getInstance());
        //设置图片
    }
}
