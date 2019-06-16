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
import java.util.List;

//支付确认界面
public class PayActivity extends Activity {

    private List<Food> order_list_data;
    private TextView total_count;
    private Pair<String, String> order_data;

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
        setContentView(R.layout.payment);

        Intent intent = getIntent();
        order_list_data = (List<Food>) intent.getExtras().getSerializable("order_list_data");

        final ListView order_list = (ListView)findViewById(R.id.order_list);
        MyFinalOrderAdapter myOrderListViewAdapter = new MyFinalOrderAdapter(PayActivity.this, order_list_data);
        order_list.setAdapter(myOrderListViewAdapter);

        total_count = findViewById(R.id.total_count);
        calculate_sum();

        final TextView remark_text = (TextView)findViewById(R.id.remark);
        final String remark = remark_text.getText().toString();

        final Button order_confirm_btn = findViewById(R.id.order_confirm_btn);
        order_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //访问网站提交获取订单id和time
                new Thread() {
                    @Override
                    public void run() {
                        Restaurant restaurant_instance = new Restaurant();
                        order_data = restaurant_instance.pushOrder(order_list_data, remark);
                        pay_handler.sendEmptyMessage(0x01);
                    }
                }.start();
            }
        });
    }
    //计算总价
    private void calculate_sum(){
        double sum = 0;
        for(int i = 0; i < order_list_data.size(); i++){
            double price = Double.valueOf(order_list_data.get(i).getFoodPrices());
            sum += order_list_data.get(i).getCount() * price;
        }
        total_count.setText("$" + sum);
    }

    //返回键事件
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PayActivity.this, MainActivity.class);
        setResult(1001, intent);
        finish();
        super.onBackPressed();
    }
}
