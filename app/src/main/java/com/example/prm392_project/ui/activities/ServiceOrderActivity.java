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
import com.example.prm392_project.data.external.response.RescueReqBody;
import com.example.prm392_project.data.external.services.RescueSvc;
import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.data.models.RescueReqMetadata;
import com.example.prm392_project.utils.StringUtils;

public class ServiceOrderActivity extends AppCompatActivity {

    private EditText editDescription, editPhone, editAddress, editVehicleBrand, editVehicleType, editVehicleInfo;
    private Button btnCall, btnBack;
    private ProgressBar progressBar;
    private String rescueTitle;
    private RescueSvc rescueSvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order);

        rescueTitle = StringUtils.decodeUnicode(getIntent().getStringExtra("RESCUE_TITLE"));
        if (rescueTitle.isEmpty()) {
            rescueTitle = "Yêu cầu cứu hộ";
        }

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(rescueTitle);

        // Ánh xạ các view
        editDescription = findViewById(R.id.editDescription);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        editVehicleBrand = findViewById(R.id.editVehicleBrand);
        editVehicleType = findViewById(R.id.editVehicleType);
        editVehicleInfo = findViewById(R.id.editVehicleInfo);
        btnCall = findViewById(R.id.btnCall);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);

        rescueSvc = new RescueSvc(ServiceOrderActivity.this);

        // Xử lý khi nhấn nút gọi cứu hộ
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRescueRequest();
            }
        });

        // Xử lý khi nhấn nút quay trở về
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về trang Home
                Intent intent = new Intent(ServiceOrderActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Kết thúc Activity hiện tại
            }
        });

    }

    private void handleRescueRequest() {
        // Lấy dữ liệu từ EditText
        String description = editDescription.getText().toString();
        String phone = editPhone.getText().toString();
        String address = editAddress.getText().toString();
        String vehicleBrand = editVehicleBrand.getText().toString();
        String vehicleType = editVehicleType.getText().toString();
        String vehicleInfo = editVehicleInfo.getText().toString();

        // Kiểm tra dữ liệu nhập vào
        if (TextUtils.isEmpty(description)) {
            editDescription.setError("Vui lòng nhập mô tả vấn đề");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            editPhone.setError("Vui lòng nhập số điện thoại");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            editAddress.setError("Vui lòng nhập địa chỉ");
            return;
        }

        if (TextUtils.isEmpty(vehicleBrand)) {
            editVehicleBrand.setError("Vui lòng nhập hãng xe");
            return;
        }

        if (TextUtils.isEmpty(vehicleType)) {
            editVehicleType.setError("Vui lòng nhập loại xe");
            return;
        }

        if (TextUtils.isEmpty(vehicleInfo)) {
            editVehicleInfo.setError("Vui lòng nhập thông tin xe");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Gửi yêu cầu cứu hộ
        RescueReqMetadata metadata = new RescueReqMetadata(rescueTitle, vehicleBrand, vehicleType, vehicleInfo);
        RescueReqBody rescueBody = new RescueReqBody(rescueTitle, description, 10.875073, 106.800732, address, phone, metadata);

        rescueSvc.createRescueReq(rescueBody, new ApiCallback<BaseResp<RescueReq>>() {
            @Override
            public void onSuccess(BaseResp<RescueReq> response) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ServiceOrderActivity.this, "Yêu cầu cứu hộ đã được gửi: " + response.getData().getId(), Toast.LENGTH_SHORT).show();
                resetForm();
                Intent intent = new Intent(ServiceOrderActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ServiceOrderActivity.this, "Gửi yêu cầu cứu hộ thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetForm() {
        editDescription.setText("");
        editPhone.setText("");
        editAddress.setText("");
        editVehicleBrand.setText("");
        editVehicleType.setText("");
        editVehicleInfo.setText("");
    }
}
