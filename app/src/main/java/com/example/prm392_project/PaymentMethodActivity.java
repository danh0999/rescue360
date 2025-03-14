package com.example.prm392_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PaymentMethodActivity extends AppCompatActivity {

    private LinearLayout layoutCash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        // Toolbar back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(PaymentMethodActivity.this, RequestListActivity.class);
            startActivity(intent);
            finish();
        });


        layoutCash = findViewById(R.id.layoutCash);
        layoutCash.setOnClickListener(v -> {
            Toast.makeText(this, "Đã chọn thanh toán bằng tiền mặt", Toast.LENGTH_SHORT).show();
            // Xử lý chuyển sang màn hình thanh toán tiền mặt tại đây
            Intent intent = new Intent(PaymentMethodActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
