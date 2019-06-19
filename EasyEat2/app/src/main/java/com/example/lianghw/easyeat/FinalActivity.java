package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FinalActivity extends Activity {
    private List<Food> order_list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        order_list_data = (List<Food>)bundle.getSerializable("order_list_data");
        String order_id = bundle.getString("order_id");
        String order_time = bundle.getString("order_time");

        final ListView order_list = (ListView)findViewById(R.id.card_list);
        List<TypeListViewItem> card_list_data = new ArrayList<>();
        card_list_data.add(new TypeListViewItem(0,getHashMapFirstType(order_list_data)));
        card_list_data.add(new TypeListViewItem(2, getHashMapThirdType(order_id, order_time)));
        MyTypeListViewAdapter myTypeListViewAdapter = new MyTypeListViewAdapter(FinalActivity.this, card_list_data);
        order_list.setAdapter(myTypeListViewAdapter);

    }

    //第一种样式，传输order_list
    private HashMap<String, Object> getHashMapFirstType(List<Food> list_data) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("list_data", list_data);
        return hashMap;
    }

    //第三种样式，传输order_id order_time
    private HashMap<String, Object> getHashMapThirdType(String order_id, String order_time) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("order_id", order_id);
        hashMap.put("order_time", order_time);
        return hashMap;
    }

    //返回键事件
    @Override
    public void onBackPressed() {
        StoreData.getInstance().order_food_list.clear();
        for(int i = 0; i < StoreData.getInstance().all_food_list.size(); i++){
            if(StoreData.getInstance().all_food_list.get(i).getCount() != 0){
                StoreData.getInstance().all_food_list.get(i).setCount(0);
            }
        }
        Intent intent = new Intent(FinalActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}
