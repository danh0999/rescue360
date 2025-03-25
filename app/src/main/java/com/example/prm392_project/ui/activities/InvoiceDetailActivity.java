package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.RescueInvoice;
import com.example.prm392_project.data.external.services.RescueSvc;
import com.example.prm392_project.utils.DateUtils; // Assuming you have this for date formatting
import com.example.prm392_project.utils.StringUtils;

import java.text.DecimalFormat;

public class InvoiceDetailActivity extends AppCompatActivity {

    private TextView tvInvoiceId, tvRescueReqId, tvCreatedBy, tvAmount, tvStatus, tvDescription, tvCreatedAt;
    private Button btnMarkAsPaid, btnBack;
    private ProgressBar progressBar;
    private String invoiceId;
    private RescueSvc rescueSvc;
    private RescueInvoice invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);

        // Initialize views
        tvInvoiceId = findViewById(R.id.tvInvoiceId);
        tvRescueReqId = findViewById(R.id.tvRescueReqId);
        tvCreatedBy = findViewById(R.id.tvCreatedBy);
        tvAmount = findViewById(R.id.tvAmount);
        tvStatus = findViewById(R.id.tvStatus);
        tvDescription = findViewById(R.id.tvDescription);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        btnMarkAsPaid = findViewById(R.id.btnMarkAsPaid);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);

        // Get invoice ID from intent
        invoiceId = getIntent().getStringExtra("INVOICE_ID");
        if (invoiceId == null) {
            Toast.makeText(this, "No invoice ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize service
        rescueSvc = new RescueSvc(this);

        // Load invoice details
        loadInvoiceDetails();

        // Button listeners
        btnMarkAsPaid.setOnClickListener(v -> markAsPaid());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadInvoiceDetails() {
        progressBar.setVisibility(View.VISIBLE);
        rescueSvc.getRescueInvoiceById(invoiceId, new ApiCallback<BaseResp<RescueInvoice>>() {
            @Override
            public void onSuccess(BaseResp<RescueInvoice> response) {
                progressBar.setVisibility(View.GONE);
                invoice = response.getData();
                if (invoice != null) {
                    updateUI();
                } else {
                    Toast.makeText(InvoiceDetailActivity.this, "Invoice not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(InvoiceDetailActivity.this, "Failed to load invoice: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        tvInvoiceId.setText(invoice.getId());
        tvRescueReqId.setText(invoice.getRescueReqId());
//        tvCreatedBy.setText(invoice.getCreatedByUser() != null ? invoice.getCreatedByUser().getUsername() : invoice.getCreatedBy());
        if (invoice.getCreatedByUser() != null) {
            tvCreatedBy.setText(invoice.getCreatedByUser().getFullName());
        } else {
            tvCreatedBy.setText(invoice.getCreatedBy());
        }
        tvAmount.setText(StringUtils.moneySplitter(String.valueOf(invoice.getAmount())) + " VND");

//        tvStatus.setText(invoice.isPaid() ? "Paid" : "Unpaid");
        if (invoice.isPaid()) {
            tvStatus.setText("Paid");
        } else {
            tvStatus.setText("Unpaid");
        }
        setStatusColor(invoice.isPaid());

//        tvDescription.setText(invoice.getMetadata() != null ? invoice.getMetadata().getDescription() : "N/A");
        if (invoice.getMetadata() != null) {
            tvDescription.setText(invoice.getMetadata().getDescription());
        } else {
            tvDescription.setText("N/A");
        }
        tvCreatedAt.setText(formatDate(invoice.getCreatedAt()));

        // Show "Mark as Paid" button only if unpaid
//        btnMarkAsPaid.setVisibility(invoice.isPaid() ? View.GONE : View.VISIBLE);
        if (invoice.isPaid()) {
            btnMarkAsPaid.setVisibility(View.GONE);
        } else {
            btnMarkAsPaid.setVisibility(View.VISIBLE);
        }
    }


    private void setStatusColor(boolean isPaid) {
        int colorResId = isPaid ? R.color.status_completed : R.color.status_pending;
        tvStatus.setTextColor(getResources().getColor(colorResId, null));
    }

    private String formatDate(String date) {
        if (date == null || date.isEmpty()) return "N/A";
        try {
            return DateUtils.formatDateTime(DateUtils.parseDateTime(date, "yyyy-MM-dd HH:mm:ss"), "dd/MM/yyyy HH:mm");
        } catch (Exception e) {
            return date;
        }
    }

    private void markAsPaid() {
        progressBar.setVisibility(View.VISIBLE);
        btnMarkAsPaid.setEnabled(false);

        rescueSvc.updateRescueInvoicePaid(invoiceId, new ApiCallback<BaseResp<RescueInvoice>>() {
            @Override
            public void onSuccess(BaseResp<RescueInvoice> response) {
                progressBar.setVisibility(View.GONE);
                btnMarkAsPaid.setEnabled(true);
                Toast.makeText(InvoiceDetailActivity.this, "Invoice marked as paid", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InvoiceDetailActivity.this, InvoiceSuccessActivity.class);

                // Pass invoice ID to success activity
                intent.putExtra("INVOICE_ID", invoiceId);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                btnMarkAsPaid.setEnabled(true);
                Toast.makeText(InvoiceDetailActivity.this, "Failed to mark as paid: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}