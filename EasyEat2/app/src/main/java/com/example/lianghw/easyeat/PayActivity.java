package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
            Intent intent = new Intent(PayActivity.this, FinalActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("order_list_data", (Serializable) order_list_data);

            //访问网站提交获取订单id和time
            Restaurant restaurant_instasnce = new Restaurant();
            Pair<String, String> order_data = restaurant_instasnce.pushOrder(order_list_data, remark);

            bundle.putString("order_id", order_data.first);
            bundle.putString("order_time", order_data.second);
            intent.putExtras(bundle);
            startActivity(intent);
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
