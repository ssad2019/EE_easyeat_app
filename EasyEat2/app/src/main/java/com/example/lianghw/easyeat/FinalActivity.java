/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
/**
 * FinalActivity
 * 显示整个订单列表和
 * 服务器返回的订单号和时间
 */
package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        List<Food> orderListData = (List<Food>)bundle.getSerializable("order_list_data");
        String orderId = bundle.getString("order_id");
        String orderTime = bundle.getString("order_time");

        final ListView orderList = (ListView)findViewById(R.id.card_list);
        List<TypeListViewItem> cardListData = new ArrayList<>();
        cardListData.add(new TypeListViewItem(0,getHashMapFirstType(orderListData)));
        cardListData.add(new TypeListViewItem(2, getHashMapThirdType(orderId, orderTime)));
        MyTypeListViewAdapter myTypeListViewAdapter = new MyTypeListViewAdapter(FinalActivity.this, cardListData);
        orderList.setAdapter(myTypeListViewAdapter);

    }

    //第一种样式，传输order_list
    private HashMap<String, Object> getHashMapFirstType(List<Food> listData) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("list_data", listData);
        return hashMap;
    }

    //第三种样式，传输order_id order_time
    private HashMap<String, Object> getHashMapThirdType(String orderId, String orderTime) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("order_id", orderId);
        hashMap.put("order_time", orderTime);
        return hashMap;
    }

    //返回键事件
    @Override
    public void onBackPressed() {
        StoreData.getInstance().orderFoodList.clear();
        for(int i = 0; i < StoreData.getInstance().allFoodList.size(); i++){
            if(StoreData.getInstance().allFoodList.get(i).getCount() != 0){
                StoreData.getInstance().allFoodList.get(i).setCount(0);
            }
        }
        Intent intent = new Intent(FinalActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}
