package com.example.lianghw.easyeat;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//主界面
public class MainActivity extends AppCompatActivity {

    private List<Food> foods;
    private List<String> food_type;
    private List<Food> order_list_data;
    private boolean scroll_end = false;
    private MyOrderListViewAdapter myOrderListViewAdapter;
    private PinnedAdapter myPinnedAdapter;
    private MyListViewAdapter myListViewAdapter;
    private Button order_list_btn;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主布居
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //初始化食品列表
        initFood();

        //设置list adapter
        final ListView food_type_list = (ListView) findViewById(R.id.food_type);
        myListViewAdapter = new MyListViewAdapter(MainActivity.this, food_type);
        food_type_list.setAdapter(myListViewAdapter);

        final PinnedListView food_detail_list = (PinnedListView) findViewById(R.id.food_detail);
        myPinnedAdapter = new PinnedAdapter(MainActivity.this, foods);
        //item内部点击事件
        PinnedAdapter.OnButtonClickListener onButtonClickListener = new PinnedAdapter.OnButtonClickListener() {
            @Override
            public void onAddClick(int position) {
                Food order_item = foods.get(position);
                int pre_count = foods.get(position).getCount();
                boolean order_item_exists = false;
                for(int i = 0; i < order_list_data.size(); i++){
                    if(order_item.getFoodName().equals(order_list_data.get(i).getFoodName())){
                        order_item_exists = true;
                        order_list_data.get(i).setCount(pre_count + 1);
                        break;
                    }
                }
                if(!order_item_exists){
                    order_list_data.add(order_item);
                }
                foods.get(position).setCount(pre_count + 1);
                myPinnedAdapter.notifyDataSetChanged();
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
            }

            @Override
            public void onSubClick(int position) {
                Food order_item = foods.get(position);
                int pre_count = foods.get(position).getCount();
                for(int i = 0; i < order_list_data.size(); i++){
                    if(order_item.getFoodName().equals(order_list_data.get(i).getFoodName())){
                        int result = order_list_data.get(i).getCount() - 1;
                        if(result == 0){
                            order_list_data.remove(i);
                        }else{
                            order_list_data.get(i).setCount(result);
                        }
                    }
                }
                foods.get(position).setCount(pre_count - 1);
                myPinnedAdapter.notifyDataSetChanged();
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
            }
        };

        myPinnedAdapter.setOnButtonClickListener(onButtonClickListener);
        food_detail_list.setAdapter(myPinnedAdapter);
        food_detail_list.setPinnedHeaderView(getHeaderView());

        //设置右侧列表滚动事件
        food_detail_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                //获取当前滚动状态确定是否触发滚动事件
                if(scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING){
                    scroll_end = true;
                }else{
                    scroll_end = false;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(!scroll_end){
                    return;
                }
                //滚动确定替换条目
                food_detail_list.configureHeaderView(firstVisibleItem);
                //获取到第一个条目的类型
                String title = foods.get(firstVisibleItem).getFoodType();
                int position = food_type.indexOf(title);
                food_type_list.smoothScrollToPosition(position);
                myListViewAdapter.changeSelected(position);
            }
        });

        //查看餐品详情的点击事件
        food_detail_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food food_item = foods.get(position);
                Intent intent = new Intent(MainActivity.this, FoodDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order_list_data", (Serializable)order_list_data);
                bundle.putSerializable("food_item", food_item);
                intent.putExtras(bundle);
                startActivityForResult(intent,1000);
            }
        });

        //左侧list的点击事件
        food_type_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < foods.size(); i++){
                    if(foods.get(i).getFoodType().equals(food_type.get(position))) {
                        food_detail_list.setSelection(i);
                        break;
                    }
                }
                myListViewAdapter.changeSelected(position);
            }
        });

        final TextView shop_name_text = (TextView)findViewById(R.id.shop_name);
        final TextView shop_info_text =(TextView)findViewById(R.id.shop_description);
        final ImageView shop_img_view = (ImageView)findViewById(R.id.shop_img);

        //查看商店信息的点击事件
        View.OnClickListener shop_click_listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,  ShopDetail.class);
                intent.putExtra("shop_name",shop_name_text.getText().toString());
                intent.putExtra("shop_info", shop_info_text.getText().toString());
                //传递图片src(url)
                //intent.putExtra("shop_info", img_url);
                startActivity(intent);
            }
        };

        shop_name_text.setOnClickListener(shop_click_listener);
        shop_info_text.setOnClickListener(shop_click_listener);
        shop_img_view.setOnClickListener(shop_click_listener);

        order_list_btn = (Button)findViewById(R.id.order_list_btn);
        final ListView order_list = (ListView)findViewById(R.id.order_list);
        myOrderListViewAdapter = new MyOrderListViewAdapter(MainActivity.this, order_list_data);
        //order_item内部点击事件
        MyOrderListViewAdapter.OnOrderButtonClickListener onOrderButtonClickListener = new MyOrderListViewAdapter.OnOrderButtonClickListener() {
            @Override
            public void onAddClick(int position) {
                Food order_item = order_list_data.get(position);
                int pre_count = order_item.getCount();
                for(int i = 0; i < foods.size(); i++){
                    if(foods.get(i).getFoodName().equals(order_item.getFoodName())){
                        foods.get(i).setCount(pre_count + 1);
                        break;
                    }
                }
                order_list_data.get(position).setCount(pre_count + 1);

                myPinnedAdapter.notifyDataSetChanged();
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
            }

            @Override
            public void onSubClick(int position) {
                Food order_item = order_list_data.get(position);
                int result = order_item.getCount() - 1;
                for(int i = 0; i < foods.size(); i++){
                    if(foods.get(i).getFoodName().equals(order_item.getFoodName())){
                        foods.get(i).setCount(result);
                        break;
                    }
                }
                if(result == 0){
                    order_list_data.remove(position);
                }else{
                    order_list_data.get(position).setCount(result);
                }

                myPinnedAdapter.notifyDataSetChanged();
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
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
                        Toast.makeText(MainActivity.this, "订单中没有菜品", Toast.LENGTH_SHORT).show();
                    }else {
                        order_list.setVisibility(View.VISIBLE);
                    }
                } else {
                    order_list.setVisibility(View.GONE);
                }
            }
        });

        //下订单按钮点击事件
        final Button order_make_btn = (Button)findViewById(R.id.order_make_btn);
        order_make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,  PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order_list_data", (Serializable)order_list_data);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        calculate_sum();
    }

    //创建一个显示菜品类别的Header(TextView)
    private View getHeaderView() {
        //头部是个TextView，不能用view.inflate加载布局,会测量不出宽高
        TextView itemView = new TextView(this);
        itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                100));
        itemView.setGravity(Gravity.LEFT);
        itemView.setBackgroundColor(Color.parseColor("#FAFAFA"));
        itemView.setTextSize(20);
        itemView.setTextColor(Color.GRAY);
        itemView.setPadding(30, 0, 0, itemView.getPaddingBottom());
        return itemView;
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

    @Override
//重写了onAcitivityResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000)
        {
            List<Food> new_order_data = (List<Food>) data.getSerializableExtra("order_list_data");
            order_list_data.clear();
            order_list_data.addAll(new_order_data);
            myOrderListViewAdapter.updateData(new_order_data);
            for(int i = 0; i < foods.size(); i++){
                for(int j = 0; j < order_list_data.size(); j++){
                    if(foods.get(i).getFoodName().equals(order_list_data.get(j).getFoodName())){
                        foods.get(i).setCount(order_list_data.get(j).getCount());
                        break;
                    }
                }
            }
            myPinnedAdapter.updateData(foods);
            calculate_sum();
        }
    }

    //for test
    void initFood(){
        //food_list
        final Food chips = new Food("薯条", "小食", "6", "", 0);
        final Food chips1 = new Food("薯条1", "小食", "6", "", 0);
        final Food chips2 = new Food("薯条2", "小食", "6", "", 0);
        final Food chips3 = new Food("薯条3", "小食1", "7", "", 0);
        final Food chips4 = new Food("薯条4", "小食1", "7", "", 0);
        final Food chips5 = new Food("薯条5", "小食1", "7", "", 0);
        final Food chips6 = new Food("薯条6", "小食2", "8", "", 0);
        final Food chips7 = new Food("薯条7", "小食2", "8", "", 0);
        final Food chips8 = new Food("薯条8", "小食2", "8", "", 0);
        final Food chips9 = new Food("薯条9", "小食3", "9", "", 0);
        final Food chips10 = new Food("薯条10", "小食3", "9", "", 0);
        final Food chips11 = new Food("薯条11", "小食3", "9", "", 0);
        final Food chips12 = new Food("薯条12", "小食3", "9", "", 0);
        final Food chips13 = new Food("薯条13", "小食4", "10", "", 0);
        final Food chips14 = new Food("薯条14", "小食4", "10", "", 0);
        final Food chips15 = new Food("薯条15", "小食4", "10", "", 0);
        final Food chips16 = new Food("薯条16", "小食4", "10", "", 0);
        final Food chips17 = new Food("薯条17", "小食5", "11", "", 0);
        final Food chips18 = new Food("薯条18", "小食5", "11", "", 0);
        final Food chips19 = new Food("薯条19", "小食5", "11", "", 0);
        final Food chips20 = new Food("薯条20", "小食5", "11", "", 0);

        foods = new ArrayList<Food>(){{add(chips);add(chips1);add(chips2);add(chips3);add(chips4);add(chips5);add(chips6);add(chips7);add(chips8);add(chips9);add(chips10);
        add(chips11);add(chips12);add(chips13);add(chips14);add(chips15);add(chips16);add(chips17);add(chips18);add(chips19);add(chips20);}};
        food_type = new ArrayList<String>(){{add("小食");add("小食1");add("小食2");add("小食3");add("小食4");add("小食5");}};

        order_list_data = new ArrayList<Food>(){};
    }
}




