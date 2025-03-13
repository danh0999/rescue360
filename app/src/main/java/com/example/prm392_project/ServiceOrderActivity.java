package com.example.prm392_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ServiceOrderActivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtCarBrand, edtCarCode, edtCarColor, edtLicensePlate, edtIssue;
    private Button btnCallRescue, btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order);

        // Ánh xạ các view
        edtName = findViewById(R.id.edt_owner_name);
        edtPhone = findViewById(R.id.edt_owner_phone);
        edtCarBrand = findViewById(R.id.edt_vehicle_brand);
        edtCarCode = findViewById(R.id.edt_vehicle_code);
        edtCarColor = findViewById(R.id.edt_vehicle_color);
        edtLicensePlate = findViewById(R.id.edt_vehicle_plate);
        edtIssue = findViewById(R.id.edt_issue);

        btnCallRescue = findViewById(R.id.btn_call_rescue);
        btnGoBack = findViewById(R.id.btn_go_back);

        // Xử lý khi nhấn nút gọi cứu hộ
        btnCallRescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRescueRequest();
            }
        });

        // Xử lý khi nhấn nút quay trở về
        btnGoBack.setOnClickListener(new View.OnClickListener() {
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
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String carBrand = edtCarBrand.getText().toString().trim();
        String carCode = edtCarCode.getText().toString().trim();
        String carColor = edtCarColor.getText().toString().trim();
        String licensePlate = edtLicensePlate.getText().toString().trim();
        String issue = edtIssue.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Vui lòng nhập họ và tên");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Vui lòng nhập số điện thoại");
            return;
        }
        if (phone.length() != 10) {
            edtPhone.setError("Số điện thoại phải đủ 10 số");
            return;
        }
        if (TextUtils.isEmpty(carBrand)) {
            edtCarBrand.setError("Vui lòng nhập hãng xe");
            return;
        }
        if (TextUtils.isEmpty(carCode)) {
            edtCarCode.setError("Vui lòng nhập mã xe");
            return;
        }
        if (TextUtils.isEmpty(carColor)) {
            edtCarColor.setError("Vui lòng nhập màu xe");
            return;
        }
        if (TextUtils.isEmpty(licensePlate)) {
            edtLicensePlate.setError("Vui lòng nhập biển số xe");
            return;
        }
        if (TextUtils.isEmpty(issue)) {
            edtIssue.setError("Vui lòng nhập loại sự cố");
            return;
        }

        // Nếu tất cả dữ liệu đều hợp lệ, hiển thị thông báo thành công
        Toast.makeText(this, "Yêu cầu cứu hộ đã được gửi thành công!", Toast.LENGTH_LONG).show();

        // (Tùy chọn) Thực hiện điều hướng hoặc lưu dữ liệu
        // Intent intent = new Intent(this, AnotherActivity.class);
        // startActivity(intent);

        // (Tùy chọn) Reset các trường nhập sau khi gửi thành công
        resetForm();
    }

    private void resetForm() {
        edtName.setText("");
        edtPhone.setText("");
        edtCarBrand.setText("");
        edtCarCode.setText("");
        edtCarColor.setText("");
        edtLicensePlate.setText("");
        edtIssue.setText("");
    }
}
