/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
/**
 * FinalActivity页面
 * 显示：整个订单列表和
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

        List<Food> list_order_data = (List<Food>)bundle.getSerializable("order_list_data");
        String str_order_id = bundle.getString("order_id");
        String str_order_time = bundle.getString("order_time");

        final ListView lv_card = (ListView)findViewById(R.id.lv_card);
        List<TypeListViewItem> list_card_data = new ArrayList<>();
        list_card_data.add(new TypeListViewItem(TypeListViewItem.TYPELISTITEMVIEW_TYPE_1,getHashMapFirstType(list_order_data)));
        list_card_data.add(new TypeListViewItem(TypeListViewItem.TYPELISTVIEWITEM_TYPE_3, getHashMapThirdType(str_order_id, str_order_time)));
        TypeListViewAdapter typeListViewAdapter = new TypeListViewAdapter(FinalActivity.this, list_card_data);
        lv_card.setAdapter(typeListViewAdapter);

    }

    /**
     * 创建用于构造第一类型TypeListViewItem的HashMap
     * @param list_data List<Food> 传入第一类型需要使用的列表数据
     * @return HashMap<String, Object> 创建好的HashMap
     */
    private HashMap<String, Object> getHashMapFirstType(List<Food> list_data) {
        HashMap<String, Object> hash_map = new HashMap<String, Object>();
        hash_map.put("list_data", list_data);
        return hash_map;
    }

    /**
     * 创建用于构造第三类型TypeListViewItem的HashMap
     * @param str_order_id String 传入第三类型需要使用的订单号
     * @param str_order_time String 传入第三类型需要使用的订单时间
     * @return HashMap<String, Object> 创建好的HashMap
     */
    private HashMap<String, Object> getHashMapThirdType(String str_order_id, String str_order_time) {
        HashMap<String, Object> hash_map = new HashMap<String, Object>();
        hash_map.put("order_id", str_order_id);
        hash_map.put("order_time", str_order_time);
        return hash_map;
    }

    //返回键事件
    @Override
    public void onBackPressed() {
        StoreData.getInstance().list_order.clear();
        for(int i = 0; i < StoreData.getInstance().list_all_food.size(); i++){
            if(StoreData.getInstance().list_all_food.get(i).getCount() != 0){
                StoreData.getInstance().list_all_food.get(i).setCount(0);
            }
        }
        Intent intent = new Intent(FinalActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}
