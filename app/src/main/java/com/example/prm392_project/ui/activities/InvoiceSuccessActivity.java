package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;

public class InvoiceSuccessActivity extends AppCompatActivity {

    private TextView tvInvoiceId;
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_success);

        // Initialize views
        tvInvoiceId = findViewById(R.id.tvInvoiceId);
        btnHome = findViewById(R.id.btn_home);

        // Get invoice ID from intent
        String invoiceId = getIntent().getStringExtra("INVOICE_ID");
        if (invoiceId != null) {
            tvInvoiceId.setText("Invoice ID: " + invoiceId);
        }

        // Home button listener
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(InvoiceSuccessActivity.this, MainActivity.class); // Replace with your home activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}