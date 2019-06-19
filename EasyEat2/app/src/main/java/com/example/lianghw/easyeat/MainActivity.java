/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
package com.example.lianghw.easyeat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.lianghw.easyeat.Restaurant.getRestaurantByUrl;

//主界面
public class MainActivity extends AppCompatActivity {

    private StoreData data_instance = StoreData.getInstance();

    private boolean scroll_end = false;
    private MyOrderListViewAdapter myOrderListViewAdapter;
    private PinnedAdapter myPinnedAdapter;
    private MyListViewAdapter myListViewAdapter;
    private Button order_list_btn;
    private Restaurant restaurant;

    private TextView restaurant_name;
    private TextView restaurant_detail;
    private ImageView restaurant_img;
    private Bitmap bitmap;
    private Button order_make_btn;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x01:
                    if(!restaurant.name.equals("")){
                        restaurant_name.setText(restaurant.name);
                    }
                    if(!restaurant.description.equals("")){
                        restaurant_detail.setText(restaurant.description);
                    }
                    if(!restaurant.icon.equals("")){
                        restaurant_img.setImageBitmap(bitmap);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    //for test
    void initData(Intent intent){
        final String data_url = intent.getStringExtra("data_url");
        new Thread() {
            @Override
            public void run() {
                restaurant = getRestaurantByUrl(data_url);
                if(!restaurant.icon.equals("")) {
                    bitmap = Network.getInstance().getBitmap(restaurant.icon);
                }
                List<Food> foods_copy = new ArrayList<>(Arrays.asList(restaurant.goods));
                data_instance.allFoodList = foods_copy;
                for(int i = 0; i < data_instance.allFoodList.size(); i++){
                    if(data_instance.food_type_list.indexOf(data_instance.allFoodList.get(i).getType()) == -1){
                        data_instance.food_type_list.add(data_instance.allFoodList.get(i).getType());
                    }
                }
                handler.sendEmptyMessage(0x01);
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载主布居
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        restaurant_name = (TextView) findViewById(R.id.shop_name);
        restaurant_detail = (TextView) findViewById(R.id.shop_description);
        restaurant_img = (ImageView) findViewById(R.id.shop_img);

        //初始化食品列表
        Intent intent = getIntent();
        if(intent.getStringExtra("data_url") != null) {
            initData(intent);
        }

        while(data_instance.allFoodList.size() == 0 || data_instance.food_type_list.size() == 0){

        }

        //设置list adapter
        final ListView food_type_list = (ListView) findViewById(R.id.food_type);

        myListViewAdapter = new MyListViewAdapter(MainActivity.this, data_instance.food_type_list);
        food_type_list.setAdapter(myListViewAdapter);

        final PinnedListView food_detail_list = (PinnedListView) findViewById(R.id.food_detail);

        myPinnedAdapter = new PinnedAdapter(MainActivity.this, data_instance.allFoodList);
        //item内部点击事件
        PinnedAdapter.OnButtonClickListener onButtonClickListener = new PinnedAdapter.OnButtonClickListener() {
            @Override
            public void onAddClick(int position) {
                Food order_item = data_instance.allFoodList.get(position);
                int pre_count = data_instance.allFoodList.get(position).getCount();
                boolean order_item_exists = false;
                for(int i = 0; i < data_instance.orderFoodList.size(); i++){
                    if(order_item.getName().equals(data_instance.orderFoodList.get(i).getName())){
                        order_item_exists = true;
                        data_instance.orderFoodList.get(i).setCount(pre_count + 1);
                        break;
                    }
                }
                if(!order_item_exists){
                    data_instance.orderFoodList.add(order_item);
                }
                data_instance.allFoodList.get(position).setCount(pre_count + 1);
                myPinnedAdapter.notifyDataSetChanged();
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
                check_order_status();
            }

            @Override
            public void onSubClick(int position) {
                Food order_item = data_instance.allFoodList.get(position);
                int pre_count = data_instance.allFoodList.get(position).getCount();
                for(int i = 0; i < data_instance.orderFoodList.size(); i++){
                    if(order_item.getName().equals(data_instance.orderFoodList.get(i).getName())){
                        int result = data_instance.orderFoodList.get(i).getCount() - 1;
                        if(result == 0){
                            data_instance.orderFoodList.remove(i);
                        }else{
                            data_instance.orderFoodList.get(i).setCount(result);
                        }
                    }
                }
                data_instance.allFoodList.get(position).setCount(pre_count - 1);
                myPinnedAdapter.notifyDataSetChanged();
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
                check_order_status();
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
                String title = data_instance.allFoodList.get(firstVisibleItem).getType();
                int position = data_instance.food_type_list.indexOf(title);
                food_type_list.smoothScrollToPosition(position);
                myListViewAdapter.changeSelected(position);
            }
        });

        //查看餐品详情的点击事件
        food_detail_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food food_item = data_instance.allFoodList.get(position);
                Intent intent = new Intent(MainActivity.this, FoodDetail.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("food_item", food_item);
                intent.putExtras(bundle);
                startActivityForResult(intent,1000);
            }
        });

        //左侧list的点击事件
        food_type_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < data_instance.allFoodList.size(); i++){
                    if(data_instance.allFoodList.get(i).getType().equals(data_instance.food_type_list.get(position))) {
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
        myOrderListViewAdapter = new MyOrderListViewAdapter(MainActivity.this, data_instance.orderFoodList);
        //order_item内部点击事件
        MyOrderListViewAdapter.OnOrderButtonClickListener onOrderButtonClickListener = new MyOrderListViewAdapter.OnOrderButtonClickListener() {
            @Override
            public void onAddClick(int position) {
                Food order_item = data_instance.orderFoodList.get(position);
                int pre_count = order_item.getCount();
                for(int i = 0; i < data_instance.allFoodList.size(); i++){
                    if(data_instance.allFoodList.get(i).getName().equals(order_item.getName())){
                        data_instance.allFoodList.get(i).setCount(pre_count + 1);
                        break;
                    }
                }
                data_instance.orderFoodList.get(position).setCount(pre_count + 1);

                myPinnedAdapter.notifyDataSetChanged();
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
                check_order_status();
            }

            @Override
            public void onSubClick(int position) {
                Food order_item = data_instance.orderFoodList.get(position);
                int result = order_item.getCount() - 1;
                for(int i = 0; i < data_instance.allFoodList.size(); i++){
                    if(data_instance.allFoodList.get(i).getName().equals(order_item.getName())){
                        data_instance.allFoodList.get(i).setCount(result);
                        break;
                    }
                }
                if(result == 0){
                    data_instance.orderFoodList.remove(position);
                }else{
                    data_instance.orderFoodList.get(position).setCount(result);
                }

                myPinnedAdapter.notifyDataSetChanged();
                myOrderListViewAdapter.notifyDataSetChanged();
                calculate_sum();
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
                    if(data_instance.orderFoodList.size() == 0){
                        Toast.makeText(MainActivity.this, "订单中没有菜品", Toast.LENGTH_SHORT).show();
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

        //下订单按钮点击事件
        order_make_btn = (Button)findViewById(R.id.order_make_btn);
        order_make_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data_instance.orderFoodList.size() == 0){
                    return;
                }
                Intent intent = new Intent(MainActivity.this,  PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order_list_data", (Serializable)data_instance.orderFoodList);
                bundle.putString("total", order_list_btn.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 1001);
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
        itemView.setTextColor(Color.parseColor("#000000"));
        itemView.setPadding(30, 0, 0, itemView.getPaddingBottom());
        return itemView;
    }

    //计算总价
    private void calculate_sum(){
        double sum = 0;
        for(int i = 0; i < data_instance.orderFoodList.size(); i++){
            double price = Double.valueOf(data_instance.orderFoodList.get(i).getPrice());
            sum += data_instance.orderFoodList.get(i).getCount() * price;
        }
        order_list_btn.setText("总价:     ￥" + Double.toString(sum));
    }

    private void check_order_status(){
        if(data_instance.orderFoodList.size() != 0){
            order_make_btn.setText("去结算");
            order_make_btn.setBackgroundResource(R.drawable.btn_right_selected_style);
        }else{
            order_make_btn.setText("");
            order_make_btn.setBackgroundResource(R.drawable.btn_right_style);
        }
    }

    @Override
//重写了onAcitivityResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000)
        {
            myPinnedAdapter.notifyDataSetChanged();
            myOrderListViewAdapter.notifyDataSetChanged();
            calculate_sum();
            check_order_status();
        }else if(requestCode == 1001){

        }else if(resultCode == 1001){

        }
    }
}




