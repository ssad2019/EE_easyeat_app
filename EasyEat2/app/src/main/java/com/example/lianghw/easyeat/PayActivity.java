package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

//支付确认界面
public class PayActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        Intent intent = getIntent();
        List<Food> order_list_data = (List<Food>) intent.getExtras().getSerializable("order_list_data");

        ListView order_list = (ListView)findViewById(R.id.order_list);
        MyOrderListViewAdapter myOrderListViewAdapter = new MyOrderListViewAdapter(PayActivity.this, order_list_data);
        order_list.setAdapter(myOrderListViewAdapter);

        Button order_confirm_btn = findViewById(R.id.order_confirm_btn);
        order_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
