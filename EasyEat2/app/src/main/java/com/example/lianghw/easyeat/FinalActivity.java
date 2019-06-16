package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
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
        String time = bundle.getString("order_time");

        final ListView order_list = (ListView)findViewById(R.id.order_list);
        MyFinalOrderAdapter myOrderListViewAdapter = new MyFinalOrderAdapter(FinalActivity.this, order_list_data);
        order_list.setAdapter(myOrderListViewAdapter);



        TextView order_info = findViewById(R.id.order_info);
        order_info.setText(order_id+"\n"+time);


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
