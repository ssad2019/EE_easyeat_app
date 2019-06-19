package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//支付确认界面
public class PayActivity extends Activity {

    private List<Food> order_list_data;
    private Pair<String, String> order_data;
    private String remark_string = "";

    private Handler pay_handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x01:
                    Intent intent = new Intent(PayActivity.this, FinalActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order_list_data", (Serializable) order_list_data);
                    bundle.putString("order_id", order_data.first);
                    bundle.putString("order_time", order_data.second);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        order_list_data = (List<Food>) intent.getExtras().getSerializable("order_list_data");
        String total = (String)intent.getExtras().getString("total");
        Button btn_total = (Button)findViewById(R.id.order_list_btn);
        btn_total.setText(total);

        final ListView order_list = (ListView)findViewById(R.id.card_list);
        List<TypeListViewItem> card_list_data = new ArrayList<>();
        card_list_data.add(new TypeListViewItem(0,getHashMapFirstType(order_list_data)));
        card_list_data.add(new TypeListViewItem(1, new HashMap<String, Object>()));
        MyTypeListViewAdapter myTypeListviewAdapter = new MyTypeListViewAdapter(PayActivity.this, card_list_data);
        myTypeListviewAdapter.setTextChangeListener(new MyTypeListViewAdapter.TextChangeListener() {
            @Override
            public void remark_change(String remark) {
                remark_string = remark;
            }
        });
        order_list.setAdapter(myTypeListviewAdapter);

        final Button confirm_btn = findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //访问网站提交获取订单id和time
                new Thread() {
                    @Override
                    public void run() {
                        Restaurant restaurant_instance = new Restaurant();
                        order_data = restaurant_instance.pushOrder(order_list_data, remark_string);
                        pay_handler.sendEmptyMessage(0x01);
                    }
                }.start();
            }
        });
    }

    //返回键事件
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PayActivity.this, MainActivity.class);
        setResult(1001, intent);
        finish();
        super.onBackPressed();
    }

    //第一种样式，传输order_list
    private HashMap<String, Object> getHashMapFirstType(List<Food> list_data) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("list_data", list_data);
        return hashMap;
    }
}
