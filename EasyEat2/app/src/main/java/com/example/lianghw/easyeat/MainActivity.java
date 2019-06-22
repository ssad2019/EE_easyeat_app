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


import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.lianghw.easyeat.Restaurant.getRestaurantByUrl;

//主界面
public class MainActivity extends AppCompatActivity {

    private StoreData data_instance = StoreData.getInstance();

    private boolean bln_is_scroll_end = false;
    private OrderListViewAdapter orderListViewAdapter;
    private PinnedListViewAdapter pinnedListViewAdapter;
    private SimpleListViewAdapter simpleListViewAdapter;
    private Button btn_order_make;
    private Button btn_order_list;
    private Restaurant restaurant;

    private TextView txt_restaurant_name;
    private TextView txt_restaurant_detail;
    private ImageView img_restaurant;
    private Bitmap bitmap;

    private static final int HANDLER_MESSAGE = 0x01;
    public static final int REQUEST_FOOD_DETAIL = 1000;
    public static final int RESULT_FOOD_DETAIL = 1000;
    public static final int REQUEST_PAY = 1001;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_MESSAGE:
                    if(!restaurant.name.equals("")){
                        txt_restaurant_name.setText(restaurant.name);
                    }
                    if(!restaurant.description.equals("")){
                        txt_restaurant_detail.setText(restaurant.description);
                    }
                    if(!restaurant.icon.equals("")){
                        img_restaurant.setImageBitmap(bitmap);
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
        final String str_data_url = intent.getStringExtra("data_url");
        new Thread() {
            @Override
            public void run() {
                restaurant = getRestaurantByUrl(str_data_url);
                if(!restaurant.icon.equals("")) {
                    bitmap = Network.getInstance().getBitmap(restaurant.icon);
                }
                List<Food> list_foods_copy = new ArrayList<>(Arrays.asList(restaurant.goods));
                data_instance.list_all_food = list_foods_copy;
                for(int i = 0; i < data_instance.list_all_food.size(); i++){
                    if(data_instance.list_type.indexOf(data_instance.list_all_food.get(i).getType()) == -1){
                        data_instance.list_type.add(data_instance.list_all_food.get(i).getType());
                    }
                }
                handler.sendEmptyMessage(HANDLER_MESSAGE);
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

        txt_restaurant_name = (TextView) findViewById(R.id.txt_name);
        txt_restaurant_detail = (TextView) findViewById(R.id.txt_description);
        img_restaurant = (ImageView) findViewById(R.id.img_shop);

        //初始化食品列表
        Intent intent = getIntent();
        if(intent.getStringExtra("data_url") != null) {
            initData(intent);
        }

        while(data_instance.list_all_food.size() == 0 || data_instance.list_type.size() == 0){

        }

        //设置list adapter
        final ListView lv_food_type = (ListView) findViewById(R.id.lv_type);

        simpleListViewAdapter = new SimpleListViewAdapter(MainActivity.this, data_instance.list_type);
        lv_food_type.setAdapter(simpleListViewAdapter);

        final PinnedListView lv_food_detail = (PinnedListView) findViewById(R.id.lv_food);

        pinnedListViewAdapter = new PinnedListViewAdapter(MainActivity.this, data_instance.list_all_food);
        //item内部点击事件
        PinnedListViewAdapter.OnButtonClickListener onButtonClickListener = new PinnedListViewAdapter.OnButtonClickListener() {
            @Override
            public void onAddClick(int position) {
                Food order_item = data_instance.list_all_food.get(position);
                int int_pre_count = data_instance.list_all_food.get(position).getCount();
                boolean bln_is_exists = false;
                for(int i = 0; i < data_instance.list_order.size(); i++){
                    if(order_item.getName().equals(data_instance.list_order.get(i).getName())){
                        bln_is_exists = true;
                        data_instance.list_order.get(i).setCount(int_pre_count + 1);
                        break;
                    }
                }
                if(!bln_is_exists){
                    data_instance.list_order.add(order_item);
                }
                data_instance.list_all_food.get(position).setCount(int_pre_count + 1);
                pinnedListViewAdapter.notifyDataSetChanged();
                orderListViewAdapter.notifyDataSetChanged();
                calculateSum();
                checkOrderStatus();
            }

            @Override
            public void onSubClick(int position) {
                Food order_item = data_instance.list_all_food.get(position);
                int int_pre_count = data_instance.list_all_food.get(position).getCount();
                for(int i = 0; i < data_instance.list_order.size(); i++){
                    if(order_item.getName().equals(data_instance.list_order.get(i).getName())){
                        int result = data_instance.list_order.get(i).getCount() - 1;
                        if(result == 0){
                            data_instance.list_order.remove(i);
                        }else{
                            data_instance.list_order.get(i).setCount(result);
                        }
                    }
                }
                data_instance.list_all_food.get(position).setCount(int_pre_count - 1);
                pinnedListViewAdapter.notifyDataSetChanged();
                orderListViewAdapter.notifyDataSetChanged();
                calculateSum();
                checkOrderStatus();
            }
        };

        pinnedListViewAdapter.setOnButtonClickListener(onButtonClickListener);
        lv_food_detail.setAdapter(pinnedListViewAdapter);
        lv_food_detail.setPinnedHeaderView(getHeaderView());

        //设置右侧列表滚动事件
        lv_food_detail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                //获取当前滚动状态确定是否触发滚动事件
                if(scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING){
                    bln_is_scroll_end = true;
                }else{
                    bln_is_scroll_end = false;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(!bln_is_scroll_end){
                    return;
                }
                //滚动确定替换条目
                lv_food_detail.configureHeaderView(firstVisibleItem);
                //获取到第一个条目的类型
                String str_title = data_instance.list_all_food.get(firstVisibleItem).getType();
                int int_position = data_instance.list_type.indexOf(str_title);
                lv_food_type.smoothScrollToPosition(int_position);
                simpleListViewAdapter.changeSelected(int_position);
            }
        });

        //查看餐品详情的点击事件
        lv_food_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food food_item = data_instance.list_all_food.get(position);
                Intent intent = new Intent(MainActivity.this, FoodDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("food_item", food_item);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_FOOD_DETAIL);
            }
        });

        //左侧list的点击事件
        lv_food_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < data_instance.list_all_food.size(); i++){
                    if(data_instance.list_all_food.get(i).getType().equals(data_instance.list_type.get(position))) {
                        lv_food_detail.setSelection(i);
                        break;
                    }
                }
                simpleListViewAdapter.changeSelected(position);
            }
        });

        final TextView shop_name_text = (TextView)findViewById(R.id.txt_name);
        final TextView shop_info_text =(TextView)findViewById(R.id.txt_description);
        final ImageView shop_img_view = (ImageView)findViewById(R.id.img_shop);

        //查看商店信息的点击事件
        View.OnClickListener shop_click_listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,  ShopDetailActivity.class);
                intent.putExtra("shop_name",shop_name_text.getText().toString());
                intent.putExtra("shop_info", shop_info_text.getText().toString());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte [] bitmapByte = byteArrayOutputStream.toByteArray();
                intent.putExtra("shop_img", bitmapByte);
                startActivity(intent);
            }
        };

        shop_name_text.setOnClickListener(shop_click_listener);
        shop_info_text.setOnClickListener(shop_click_listener);
        shop_img_view.setOnClickListener(shop_click_listener);

        btn_order_list = (Button)findViewById(R.id.btn_list);
        final ListView lv_order = (ListView)findViewById(R.id.lv_order);
        orderListViewAdapter = new OrderListViewAdapter(MainActivity.this, data_instance.list_order);
        //order_item内部点击事件
        OrderListViewAdapter.OnOrderButtonClickListener onOrderButtonClickListener = new OrderListViewAdapter.OnOrderButtonClickListener() {
            @Override
            public void onAddClick(int position) {
                Food order_item = data_instance.list_order.get(position);
                int int_pre_count = order_item.getCount();
                for(int i = 0; i < data_instance.list_all_food.size(); i++){
                    if(data_instance.list_all_food.get(i).getName().equals(order_item.getName())){
                        data_instance.list_all_food.get(i).setCount(int_pre_count + 1);
                        break;
                    }
                }
                data_instance.list_order.get(position).setCount(int_pre_count + 1);

                pinnedListViewAdapter.notifyDataSetChanged();
                orderListViewAdapter.notifyDataSetChanged();
                calculateSum();
                checkOrderStatus();
            }

            @Override
            public void onSubClick(int position) {
                Food order_item = data_instance.list_order.get(position);
                int int_result = order_item.getCount() - 1;
                for(int i = 0; i < data_instance.list_all_food.size(); i++){
                    if(data_instance.list_all_food.get(i).getName().equals(order_item.getName())){
                        data_instance.list_all_food.get(i).setCount(int_result);
                        break;
                    }
                }
                if(int_result == 0){
                    data_instance.list_order.remove(position);
                }else{
                    data_instance.list_order.get(position).setCount(int_result);
                }

                pinnedListViewAdapter.notifyDataSetChanged();
                orderListViewAdapter.notifyDataSetChanged();
                calculateSum();
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
                        Toast.makeText(MainActivity.this, "订单中没有菜品", Toast.LENGTH_SHORT).show();
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

        //下订单按钮点击事件
        btn_order_make = (Button)findViewById(R.id.btn_make);
        btn_order_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data_instance.list_order.size() == 0){
                    return;
                }
                Intent intent = new Intent(MainActivity.this,  PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("order_list_data", (Serializable)data_instance.list_order);
                bundle.putString("total", btn_order_list.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 1001);
            }
        });

        calculateSum();
    }

    /**
     * 创建一个显示菜品类别的Header(TextView)
     */
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

    /**
     * 计算当前订单总价
     */
    private void calculateSum(){
        double sum = 0;
        for(int i = 0; i < data_instance.list_order.size(); i++){
            double price = Double.valueOf(data_instance.list_order.get(i).getPrice());
            sum += data_instance.list_order.get(i).getCount() * price;
        }
        btn_order_list.setText("总价:     ￥" + Double.toString(sum));
    }

    /**
     * 检测订单的列表是否为空，并改变button的样式
     */
    private void checkOrderStatus(){
        if(data_instance.list_order.size() != 0){
            btn_order_make.setText("去结算");
            btn_order_make.setBackgroundResource(R.drawable.btn_right_selected_style);
        }else{
            btn_order_make.setText("");
            btn_order_make.setBackgroundResource(R.drawable.btn_right_style);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_FOOD_DETAIL)
        {
            pinnedListViewAdapter.notifyDataSetChanged();
            orderListViewAdapter.notifyDataSetChanged();
            calculateSum();
            checkOrderStatus();
        }else if(requestCode == REQUEST_PAY){

        }
    }
}




