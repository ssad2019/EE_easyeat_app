package com.example.lianghw.easyeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PayActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
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
