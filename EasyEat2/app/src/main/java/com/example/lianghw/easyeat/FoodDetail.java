package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

import static android.support.constraint.ConstraintLayout.LayoutParams.PARENT_ID;

//菜品详情界面
public class FoodDetail extends Activity {

    private List<Food> order_list_data;

    private Button order_list_btn;
    private Food food_item;
    private Button add_btn;
    private Button sub_button;
    private TextView food_count;

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
        order_list_data = (List<Food>) intent.getExtras().getSerializable("order_list_data");

        food_name.setText(food_item.getFoodName());
        food_prices.setText(food_item.getFoodPrices());
        food_detail.setMovementMethod(ScrollingMovementMethod.getInstance());
        food_count.setText(food_item.getCount() + "");
        check_count();
        //设置图片

        final MyOrderListViewAdapter myOrderListViewAdapter = new MyOrderListViewAdapter(FoodDetail.this, order_list_data);
        //order_item内部点击事件
        MyOrderListViewAdapter.OnOrderButtonClickListener onOrderButtonClickListener = new MyOrderListViewAdapter.OnOrderButtonClickListener() {
            @Override
            public void onAddClick(int position) {
                Food order_item = order_list_data.get(position);
                int pre_count = order_item.getCount();
                order_list_data.get(position).setCount(pre_count + 1);
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
                check_count();
            }

            @Override
            public void onSubClick(int position) {
                Food order_item = order_list_data.get(position);
                int result = order_item.getCount() - 1;
                if(result == 0){
                    order_list_data.remove(position);
                }else{
                    order_list_data.get(position).setCount(result);
                }
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
                check_count();
            }
        };

        myOrderListViewAdapter.setOnOrderButtonClickListener(onOrderButtonClickListener);
        order_list.setAdapter(myOrderListViewAdapter);

        //查看订单列表按钮点击事件
        order_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order_list.getVisibility() == View.GONE) {
                    if(order_list_data.size() == 0){
                        Toast.makeText(FoodDetail.this, "订单中没有菜品", Toast.LENGTH_SHORT).show();
                    }else {
                        order_list.setVisibility(View.VISIBLE);
                    }
                } else {
                    order_list.setVisibility(View.GONE);
                }
            }
        });
        calculate_sum();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_item.setCount(food_item.getCount() + 1);
                if(order_list_data.size() != 0) {
                    for (int i = 0; i < order_list_data.size(); i++){
                        if(order_list_data.get(i).getFoodName().equals(food_item.getFoodName())){
                            order_list_data.get(i).setCount(food_item.getCount());
                            break;
                        }
                    }
                }else{
                    order_list_data.add(food_item);
                }
                myOrderListViewAdapter.notifyDataSetChanged();
                check_count();
                calculate_sum();
            }
        });

        sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_item.setCount(food_item.getCount() - 1);
                for (int i = 0; i < order_list_data.size(); i++){
                    if(order_list_data.get(i).getFoodName().equals(food_item.getFoodName())){
                        if(food_item.getCount() == 0){
                            order_list_data.remove(i);
                        }else {
                            order_list_data.get(i).setCount(food_item.getCount());
                        }
                        break;
                    }
                }
                myOrderListViewAdapter.notifyDataSetChanged();
                check_count();
                calculate_sum();
            }
        });

    }

    //计算总价
    private void calculate_sum(){
        int sum = 0;
        for(int i = 0; i < order_list_data.size(); i++){
            String price_string = order_list_data.get(i).getFoodPrices();
            int price = Integer.parseInt(price_string.substring(1, price_string.length()));
            sum += order_list_data.get(i).getCount() * price;
        }
        order_list_btn.setText("总价:     $" + sum);
    }

    //检验food count
    private void check_count(){
        int count = 0;
        if(order_list_data != null) {
            for (int i = 0; i < order_list_data.size(); i++) {
                if (order_list_data.get(i).getFoodName().equals(food_item.getFoodName())) {
                    count = order_list_data.get(i).getCount();
                }
            }
        }
        if(count == 0){
            sub_button.setVisibility(View.GONE);
            food_count.setVisibility(View.GONE);
            add_btn.setText(" + 添加到购物车");
            ConstraintLayout.LayoutParams add_btn_layout = new ConstraintLayout.LayoutParams(DensityUtil.dp_to_px(FoodDetail.this, 120), DensityUtil.dp_to_px(FoodDetail.this, 30));
            add_btn_layout.bottomToBottom = R.id.food_prices;
            add_btn_layout.rightToRight = PARENT_ID;
            add_btn_layout.setMargins(0,0, DensityUtil.dp_to_px(FoodDetail.this, 25), DensityUtil.dp_to_px(FoodDetail.this, 5));
            add_btn.setLayoutParams(add_btn_layout);
        }else{
            sub_button.setVisibility(View.VISIBLE);
            food_count.setVisibility(View.VISIBLE);
            food_count.setText(count + "");
            add_btn.setText("+");
            ConstraintLayout.LayoutParams add_btn_layout = new ConstraintLayout.LayoutParams(DensityUtil.dp_to_px(FoodDetail.this, 30), DensityUtil.dp_to_px(FoodDetail.this, 30));
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("order_list_data", (Serializable) order_list_data);
        intent.putExtras(bundle);
        setResult(1000, intent);
        finish();
        super.onBackPressed();
    }

}
