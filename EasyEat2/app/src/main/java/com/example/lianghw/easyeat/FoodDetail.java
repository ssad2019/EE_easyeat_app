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

//菜品详情界面
public class FoodDetail extends Activity {

    private StoreData data_instance = StoreData.getInstance();

    private Button order_list_btn;
    private Food food_item;
    private Button add_btn;
    private Button sub_button;
    private TextView food_count;
    private Button order_make_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主布居
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail_info);

        final TextView food_name = (TextView)findViewById(R.id.food_name);
        final ImageView food_img = (ImageView)findViewById(R.id.food_img);
        final TextView food_prices = (TextView)findViewById(R.id.food_prices);
        final TextView food_detail = (TextView)findViewById(R.id.food_info);
        food_count = (TextView)findViewById(R.id.food_count);
        add_btn = (Button)findViewById(R.id.add_btn);
        sub_button = (Button)findViewById(R.id.sub_btn);
        order_list_btn = (Button)findViewById(R.id.order_list_btn);
        final ListView order_list = (ListView)findViewById(R.id.order_list);

        Intent intent = getIntent();
        food_item = (Food) intent.getExtras().getSerializable("food_item");

        food_name.setText(food_item.getName());
        food_prices.setText(food_item.getPrice());
        food_detail.setMovementMethod(ScrollingMovementMethod.getInstance());
        food_count.setText(food_item.getCount() + "");
        check_count();
        //设置图片

        final MyOrderListViewAdapter myOrderListViewAdapter = new MyOrderListViewAdapter(FoodDetail.this, data_instance.order_food_list);
        //order_item内部点击事件
        MyOrderListViewAdapter.OnOrderButtonClickListener onOrderButtonClickListener = new MyOrderListViewAdapter.OnOrderButtonClickListener() {
            @Override
            public void onAddClick(int position) {
                Food order_item = data_instance.order_food_list.get(position);
                int pre_count = order_item.getCount();
                for(int i = 0; i < data_instance.all_food_list.size(); i++){
                    if(data_instance.all_food_list.get(i).getName().equals(order_item.getName())){
                        data_instance.all_food_list.get(i).setCount(pre_count + 1);
                        break;
                    }
                }
                data_instance.order_food_list.get(position).setCount(pre_count + 1);
                if(order_item.getName().equals(food_item.getName())){
                    food_item.setCount(pre_count + 1);
                }
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
                check_count();
                check_order_status();
            }

            @Override
            public void onSubClick(int position) {
                Food order_item = data_instance.order_food_list.get(position);
                int result = order_item.getCount() - 1;
                for(int i = 0; i < data_instance.all_food_list.size(); i++){
                    if(data_instance.all_food_list.get(i).getName().equals(order_item.getName())){
                        data_instance.all_food_list.get(i).setCount(result);
                        break;
                    }
                }
                if(result == 0){
                    data_instance.order_food_list.remove(position);
                }else{
                    data_instance.order_food_list.get(position).setCount(result);
                }
                if(order_item.getName().equals(food_item.getName())){
                    food_item.setCount(result);
                }
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
                check_count();
                check_order_status();
            }
        };

        myOrderListViewAdapter.setOnOrderButtonClickListener(onOrderButtonClickListener);
        order_list.setAdapter(myOrderListViewAdapter);

        //查看订单列表按钮点击事件
        final View shadow_view = (View)findViewById(R.id.shadow);
        order_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order_list.getVisibility() == View.GONE) {
                    if(data_instance.order_food_list.size() == 0){
                        Toast.makeText(FoodDetail.this, "订单中没有菜品", Toast.LENGTH_SHORT).show();
                    }else {
                        order_list.setVisibility(View.VISIBLE);
                        shadow_view.setVisibility(View.VISIBLE);
                    }
                } else {
                    order_list.setVisibility(View.GONE);
                    shadow_view.setVisibility(View.GONE);
                }
            }
        });
        shadow_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_list.setVisibility(View.GONE);
                shadow_view.setVisibility(View.GONE);
            }
        });

        order_make_btn = (Button)findViewById(R.id.order_make_btn);
        order_make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data_instance.order_food_list.size() == 0){
                    return;
                }
                Intent intent = new Intent(FoodDetail.this,  PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order_list_data", (Serializable)data_instance.order_food_list);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        calculate_sum();
        check_order_status();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_item.setCount(food_item.getCount() + 1);
                if(data_instance.order_food_list.size() != 0) {
                    boolean food_exists = false;
                    for (int i = 0; i < data_instance.order_food_list.size(); i++){
                        if(data_instance.order_food_list.get(i).getName().equals(food_item.getName())){
                            data_instance.order_food_list.get(i).setCount(food_item.getCount());
                            food_exists = true;
                            break;
                        }
                    }
                    if(!food_exists){
                        data_instance.order_food_list.add(food_item);
                    }
                }else{
                    data_instance.order_food_list.add(food_item);
                }

                for (int i = 0; i < data_instance.all_food_list.size(); i++){
                    if(data_instance.all_food_list.get(i).getName().equals(food_item.getName())){
                        data_instance.all_food_list.get(i).setCount(food_item.getCount());
                        break;
                    }
                }

                myOrderListViewAdapter.notifyDataSetChanged();
                check_count();
                calculate_sum();
                check_order_status();
            }
        });

        sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_item.setCount(food_item.getCount() - 1);
                for (int i = 0; i < data_instance.order_food_list.size(); i++){
                    if(data_instance.order_food_list.get(i).getName().equals(food_item.getName())){
                        if(food_item.getCount() == 0){
                            data_instance.order_food_list.remove(i);
                        }else {
                            data_instance.order_food_list.get(i).setCount(food_item.getCount());
                        }
                        break;
                    }
                }

                for (int i = 0; i < data_instance.all_food_list.size(); i++){
                    if(data_instance.all_food_list.get(i).getName().equals(food_item.getName())){
                        data_instance.all_food_list.get(i).setCount(food_item.getCount());
                        break;
                    }
                }

                myOrderListViewAdapter.notifyDataSetChanged();
                check_count();
                calculate_sum();
                check_order_status();
            }
        });

    }

    //计算总价
    private void calculate_sum(){
        double sum = 0;
        for(int i = 0; i < data_instance.order_food_list.size(); i++){
            double price = Double.valueOf(data_instance.order_food_list.get(i).getPrice());
            sum += data_instance.order_food_list.get(i).getCount() * price;
        }
        order_list_btn.setText("总价:     $" + Double.toString(sum));
    }

    //下订单按钮UI变化
    private void check_order_status(){
        if(data_instance.order_food_list.size() != 0){
            order_make_btn.setText("去结算");
            order_make_btn.setBackgroundResource(R.drawable.btn_right_selected_style);
        }else{
            order_make_btn.setText("");
            order_make_btn.setBackgroundResource(R.drawable.btn_right_style);
        }
    }

    //检验food count
    private void check_count(){
        int count = 0;
        if(data_instance.order_food_list != null) {
            for (int i = 0; i < data_instance.order_food_list.size(); i++) {
                if (data_instance.order_food_list.get(i).getName().equals(food_item.getName())) {
                    count = data_instance.order_food_list.get(i).getCount();
                }
            }
        }
        if(count == 0){
            sub_button.setVisibility(View.GONE);
            food_count.setVisibility(View.GONE);
            add_btn.setText(" + 添加到购物车");
            ConstraintLayout.LayoutParams add_btn_layout = new ConstraintLayout.LayoutParams(DensityUtil.dp_to_px(FoodDetail.this, 120), DensityUtil.dp_to_px(FoodDetail.this, 25));
            add_btn_layout.bottomToBottom = R.id.food_prices;
            add_btn_layout.rightToRight = PARENT_ID;
            add_btn_layout.setMargins(0,0, DensityUtil.dp_to_px(FoodDetail.this, 25), DensityUtil.dp_to_px(FoodDetail.this, 5));
            add_btn.setLayoutParams(add_btn_layout);
        }else{
            sub_button.setVisibility(View.VISIBLE);
            food_count.setVisibility(View.VISIBLE);
            food_count.setText(count + "");
            add_btn.setText("+");
            ConstraintLayout.LayoutParams add_btn_layout = new ConstraintLayout.LayoutParams(DensityUtil.dp_to_px(FoodDetail.this, 25), DensityUtil.dp_to_px(FoodDetail.this, 25));
            add_btn_layout.bottomToBottom = R.id.food_prices;
            add_btn_layout.rightToRight = PARENT_ID;
            add_btn_layout.setMargins(0,0, DensityUtil.dp_to_px(FoodDetail.this, 25), DensityUtil.dp_to_px(FoodDetail.this, 5));
            add_btn.setLayoutParams(add_btn_layout);
        }
    }

    //返回键事件
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(1000, intent);
        finish();
        super.onBackPressed();
    }

}
