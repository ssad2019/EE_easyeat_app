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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import static android.support.constraint.ConstraintLayout.LayoutParams.PARENT_ID;
import static com.example.lianghw.easyeat.MainActivity.RESULT_FOOD_DETAIL;

//菜品详情界面
public class FoodDetailActivity extends Activity {

    private StoreData data_instance = StoreData.getInstance();

    private Button btn_order_list;
    private Food food_item;
    private Button btn_add;
    private Button btn_sub;
    private TextView txt_count;
    private Button btn_make;
    private String str_data_url;
    private static final int HANDLER_MESSAGE = 0x01;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE:
                    img_food.setImageBitmap(food_item.getBitmap());
                    break;
                default:
                    break;
            }
        }
    };
    private ImageView img_food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主布居
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        final TextView txt_name = (TextView)findViewById(R.id.txt_name);
        img_food = (ImageView)findViewById(R.id.img_food);
        final TextView txt_price = (TextView)findViewById(R.id.txt_price);
        final TextView txt_description = (TextView)findViewById(R.id.txt_description);
        txt_count = (TextView)findViewById(R.id.txt_count);
        btn_add = (Button)findViewById(R.id.btn_add);
        btn_sub = (Button)findViewById(R.id.btn_sub);
        btn_order_list = (Button)findViewById(R.id.btn_list);
        final ListView lv_order = (ListView)findViewById(R.id.lv_order);

        Intent intent = getIntent();
        str_data_url = intent.getStringExtra("str_data_url");
        int int_id = intent.getIntExtra("food_id", 0);
        for(int i = 0; i < data_instance.list_all_food.size(); i++){
            if(int_id == data_instance.list_all_food.get(i).getId()){
                food_item = data_instance.list_all_food.get(i);
                break;
            }
        }

        txt_name.setText(food_item.getName());
        txt_price.setText(food_item.getPrice());
        txt_description.setMovementMethod(ScrollingMovementMethod.getInstance());
        txt_count.setText(food_item.getCount() + "");
        if(!food_item.getIcon().equals("")) {
            if(food_item.getBitmap() == null){
                new Thread() {
                    @Override
                    public void run() {
                        food_item.getBitmapbyUrl();
                        handler.sendEmptyMessage(HANDLER_MESSAGE);
                    }
                }.start();
            }else{
                img_food.setImageBitmap(food_item.getBitmap());
            }
        }else{
            img_food.setImageResource(R.mipmap.sample_food);
        }
        checkCount();


        final OrderListViewAdapter orderListViewAdapter = new OrderListViewAdapter(FoodDetailActivity.this, data_instance.list_order);
        //order_item内部点击事件
        OrderListViewAdapter.OnOrderButtonClickListener onOrderButtonClickListener = new OrderListViewAdapter.OnOrderButtonClickListener() {
            @Override
            public void onAddClick(int position) {
                Food order_item = data_instance.list_order.get(position);
                int int_pre_count = order_item.getCount();
                for(int i = 0; i < data_instance.list_all_food.size(); i++){
                    if(data_instance.list_all_food.get(i).getId() == order_item.getId()){
                        data_instance.list_all_food.get(i).setCount(int_pre_count + 1);
                        break;
                    }
                }
                data_instance.list_order.get(position).setCount(int_pre_count + 1);
                if(order_item.getId() == food_item.getId()){
                    food_item.setCount(int_pre_count + 1);
                }
                orderListViewAdapter.notifyDataSetChanged();
                calculateSum();
                checkCount();
                checkOrderStatus();
            }

            @Override
            public void onSubClick(int position) {
                Food order_item = data_instance.list_order.get(position);
                int int_result = order_item.getCount() - 1;
                for(int i = 0; i < data_instance.list_all_food.size(); i++){
                    if(data_instance.list_all_food.get(i).getId() == order_item.getId()){
                        data_instance.list_all_food.get(i).setCount(int_result);
                        break;
                    }
                }
                if(int_result == 0){
                    data_instance.list_order.remove(position);
                }else{
                    data_instance.list_order.get(position).setCount(int_result);
                }
                if(order_item.getId() == food_item.getId()){
                    food_item.setCount(int_result);
                }
                orderListViewAdapter.notifyDataSetChanged();
                calculateSum();
                checkCount();
                checkOrderStatus();
            }
        };

        orderListViewAdapter.setOnOrderButtonClickListener(onOrderButtonClickListener);
        lv_order.setAdapter(orderListViewAdapter);

        //查看订单列表按钮点击事件
        final View view_shadow = (View)findViewById(R.id.view_shadow);
        btn_order_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_order.getVisibility() == View.GONE) {
                    if(data_instance.list_order.size() == 0){
                        Toast.makeText(FoodDetailActivity.this, "订单中没有菜品", Toast.LENGTH_SHORT).show();
                    }else {
                        lv_order.setVisibility(View.VISIBLE);
                        view_shadow.setVisibility(View.VISIBLE);
                    }
                } else {
                    lv_order.setVisibility(View.GONE);
                    view_shadow.setVisibility(View.GONE);
                }
            }
        });
        view_shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_order.setVisibility(View.GONE);
                view_shadow.setVisibility(View.GONE);
            }
        });

        btn_make = (Button)findViewById(R.id.btn_make);
        btn_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data_instance.list_order.size() == 0){
                    return;
                }
                Intent intent = new Intent(FoodDetailActivity.this,  PayActivity.class);
                intent.putExtra("total", btn_order_list.getText().toString());
                intent.putExtra("str_data_url", str_data_url);
                startActivity(intent);
            }
        });

        calculateSum();
        checkOrderStatus();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_item.setCount(food_item.getCount() + 1);
                if(data_instance.list_order.size() != 0) {
                    boolean bln_is_exist = false;
                    for (int i = 0; i < data_instance.list_order.size(); i++){
                        if(data_instance.list_order.get(i).getId() == food_item.getId()){
                            data_instance.list_order.get(i).setCount(food_item.getCount());
                            bln_is_exist = true;
                            break;
                        }
                    }
                    if(!bln_is_exist){
                        data_instance.list_order.add(food_item);
                    }
                }else{
                    data_instance.list_order.add(food_item);
                }

                for (int i = 0; i < data_instance.list_all_food.size(); i++){
                    if(data_instance.list_all_food.get(i).getId() == food_item.getId()){
                        data_instance.list_all_food.get(i).setCount(food_item.getCount());
                        break;
                    }
                }

                orderListViewAdapter.notifyDataSetChanged();
                checkCount();
                calculateSum();
                checkOrderStatus();
            }
        });

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_item.setCount(food_item.getCount() - 1);
                for (int i = 0; i < data_instance.list_order.size(); i++){
                    if(data_instance.list_order.get(i).getId() == food_item.getId()){
                        if(food_item.getCount() == 0){
                            data_instance.list_order.remove(i);
                        }else {
                            data_instance.list_order.get(i).setCount(food_item.getCount());
                        }
                        break;
                    }
                }

                for (int i = 0; i < data_instance.list_all_food.size(); i++){
                    if(data_instance.list_all_food.get(i).getId() == food_item.getId()){
                        data_instance.list_all_food.get(i).setCount(food_item.getCount());
                        break;
                    }
                }

                orderListViewAdapter.notifyDataSetChanged();
                checkCount();
                calculateSum();
                checkOrderStatus();
            }
        });

    }

    /**
     * 计算当前订单总价
     */
    private void calculateSum(){
        double sum = 0;
        for(int i = 0; i < data_instance.list_order.size(); i++){
            double price = Double.valueOf(data_instance.list_order.get(i).getPrice());
            sum += data_instance.list_order.get(i).getCount() * price;
        }
        btn_order_list.setText("总价:     $" + Double.toString(sum));
    }

    /**
     * 检测订单的列表是否为空，并改变button的样式
     */
    private void checkOrderStatus(){
        if(data_instance.list_order.size() != 0){
            btn_make.setText("去结算");
            btn_make.setBackgroundResource(R.drawable.btn_right_selected_style);
        }else{
            btn_make.setText("");
            btn_make.setBackgroundResource(R.drawable.btn_right_style);
        }
    }

    /**
     * 检测当前food在订单中的数量，并依此改变button的样式
     */
    private void checkCount(){
        int count = 0;
        if(data_instance.list_order != null) {
            for (int i = 0; i < data_instance.list_order.size(); i++) {
                if (data_instance.list_order.get(i).getId() == food_item.getId()) {
                    count = data_instance.list_order.get(i).getCount();
                }
            }
        }
        if(count == 0){
            btn_sub.setVisibility(View.GONE);
            txt_count.setVisibility(View.GONE);
            btn_add.setText(" + 添加到购物车");
            ConstraintLayout.LayoutParams add_btn_layout = new ConstraintLayout.LayoutParams(DensityUtil.dpToPx(FoodDetailActivity.this, 120), DensityUtil.dpToPx(FoodDetailActivity.this, 25));
            add_btn_layout.bottomToBottom = R.id.txt_price;
            add_btn_layout.rightToRight = PARENT_ID;
            add_btn_layout.setMargins(0,0, DensityUtil.dpToPx(FoodDetailActivity.this, 25), DensityUtil.dpToPx(FoodDetailActivity.this, 5));
            btn_add.setLayoutParams(add_btn_layout);
        }else{
            btn_sub.setVisibility(View.VISIBLE);
            txt_count.setVisibility(View.VISIBLE);
            txt_count.setText(count + "");
            btn_add.setText("+");
            ConstraintLayout.LayoutParams add_btn_layout = new ConstraintLayout.LayoutParams(DensityUtil.dpToPx(FoodDetailActivity.this, 25), DensityUtil.dpToPx(FoodDetailActivity.this, 25));
            add_btn_layout.bottomToBottom = R.id.txt_price;
            add_btn_layout.rightToRight = PARENT_ID;
            add_btn_layout.setMargins(0,0, DensityUtil.dpToPx(FoodDetailActivity.this, 25), DensityUtil.dpToPx(FoodDetailActivity.this, 5));
            btn_add.setLayoutParams(add_btn_layout);
        }
    }

    //返回键事件
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_FOOD_DETAIL, intent);
        finish();
        super.onBackPressed();
    }

}
