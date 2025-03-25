package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.RescueInvoice;
import com.example.prm392_project.data.external.response.RescueInvoiceMetadata;
import com.example.prm392_project.data.external.response.RescueInvoiceReq;
import com.example.prm392_project.data.external.services.RescueSvc;


public class CreateInvoiceActivity extends AppCompatActivity {

    private TextView tvRescueReqId;
    private EditText editAmount, editDescription;
    private Button btnSubmit, btnBack;
    private ProgressBar progressBar;
    private String rescueReqId;
    private RescueSvc rescueSvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);

        // Initialize views
        tvRescueReqId = findViewById(R.id.tv_request_id);
        editAmount = findViewById(R.id.edit_amount);
        editDescription = findViewById(R.id.edit_description);
        btnSubmit = findViewById(R.id.btnSubmitInvoice);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);

        // Get rescue request ID from intent
        rescueReqId = getIntent().getStringExtra("RESCUE_REQUEST_ID");
        if (rescueReqId == null) {
            Toast.makeText(this, "No rescue request ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        tvRescueReqId.setText(rescueReqId);

        // Initialize service
        rescueSvc = new RescueSvc(this);

        // Button listeners
        btnSubmit.setOnClickListener(v -> submitInvoice());
        btnBack.setOnClickListener(v -> finish());
    }

    private void submitInvoice() {
        String amountStr = editAmount.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(amountStr)) {
            editAmount.setError("Please enter an amount");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            editDescription.setError("Please enter a description");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                editAmount.setError("Amount must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            editAmount.setError("Invalid amount format");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);

        // Create invoice object
        RescueInvoiceMetadata metadata = new RescueInvoiceMetadata(description);
        RescueInvoiceReq invoiceReq = new RescueInvoiceReq(rescueReqId, amount, metadata);

        // Call API
        rescueSvc.createRescueInvoice(invoiceReq, new ApiCallback<BaseResp<RescueInvoice>>() {
            @Override
            public void onSuccess(BaseResp<RescueInvoice> response) {
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                Toast.makeText(CreateInvoiceActivity.this, "Invoice created successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateInvoiceActivity.this, InvoiceDetailActivity.class);
                intent.putExtra("INVOICE_ID", response.getData().getId());
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                btnSubmit.setEnabled(true);
                Toast.makeText(CreateInvoiceActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }
}