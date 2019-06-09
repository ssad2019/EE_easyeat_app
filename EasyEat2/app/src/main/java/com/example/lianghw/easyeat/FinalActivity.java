package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class FinalActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        List<Food> order_list_data = (List<Food>)bundle.getSerializable("order_list_data");
        String order_id = bundle.getString("order_id");
        String time = bundle.getString("order_time");

        ListView order_list = (ListView)findViewById(R.id.order_list);
        MyOrderListViewAdapter myOrderListViewAdapter = new MyOrderListViewAdapter(FinalActivity.this, order_list_data);
        order_list.setAdapter(myOrderListViewAdapter);

        //返回键事件
        TextView order_info = findViewById(R.id.order_info);
        order_info.setText(order_id+"\n"+time);



    }
}
