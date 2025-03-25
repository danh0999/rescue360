package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.RescueReqBody;
import com.example.prm392_project.data.external.response.UploadResp;
import com.example.prm392_project.data.external.services.RescueSvc;
import com.example.prm392_project.data.external.services.UploadSvc;
import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.data.models.RescueReqMetadata;
import com.example.prm392_project.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceOrderActivity extends AppCompatActivity {

    private EditText editDescription, editPhone, editAddress, editVehicleBrand, editVehicleType, editVehicleInfo;
    private Button btnCall, btnBack, btnPickImage;
    private ImageView imagePreview;
    private ProgressBar progressBar;
    private String rescueTitle;
    private RescueSvc rescueSvc;
    private UploadSvc uploadSvc;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

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
        btnPickImage = findViewById(R.id.btnPickImage);
        imagePreview = findViewById(R.id.imagePreview);
        progressBar = findViewById(R.id.progressBar);

        rescueSvc = new RescueSvc(ServiceOrderActivity.this);
        uploadSvc = new UploadSvc(ServiceOrderActivity.this);

        // Xử lý khi nhấn nút chọn hình ảnh
        btnPickImage.setOnClickListener(v -> openImagePicker());

        // Xử lý khi nhấn nút gọi cứu hộ
        btnCall.setOnClickListener(v -> handleRescueRequest());

        // Xử lý khi nhấn nút quay trở về
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ServiceOrderActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imagePreview.setImageURI(selectedImageUri);
            imagePreview.setVisibility(View.VISIBLE);
        }
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

        if (selectedImageUri != null) {
            uploadImage(description, phone, address, vehicleBrand, vehicleType, vehicleInfo);
        } else {
            createRescueRequest(description, phone, address, vehicleBrand, vehicleType, vehicleInfo, null);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        try {
            // For newer Android versions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try (Cursor cursor = getContentResolver().query(contentUri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        return cursor.getString(columnIndex);
                    }
                }
            }

            // Fallback for older versions
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                cursor.close();
                return path;
            }
        } catch (Exception e) {
            Log.e("ServiceOrderActivity", "Error getting file path", e);
        }
        return null;
    }

    private void uploadImage(String description, String phone, String address, String vehicleBrand, String vehicleType, String vehicleInfo) {
        try {
            // Get InputStream directly from URI
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            if (inputStream == null) {
                Toast.makeText(this, "Cannot open file input stream", Toast.LENGTH_SHORT).show();
                return;
            }

            // Determine MIME type
            String mimeType = getContentResolver().getType(selectedImageUri);
            MediaType mediaType = MediaType.parse(mimeType != null ? mimeType : "image/jpeg");

            // Convert InputStream to byte array
            byte[] fileBytes = getBytes(inputStream);
            inputStream.close(); // Close the stream after use

            // Create RequestBody
            RequestBody requestFile = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    fileBytes
            );

            // Create MultipartBody.Part (use a generic filename if needed)
            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

            // Upload the image
            uploadSvc.uploadImage(body, new ApiCallback<BaseResp<UploadResp>>() {
                @Override
                public void onSuccess(BaseResp<UploadResp> response) {
                    String imageUrl = response.getData().getUrl();
                    createRescueRequest(description, phone, address, vehicleBrand, vehicleType, vehicleInfo, imageUrl);
                }

                @Override
                public void onError(String message) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ServiceOrderActivity.this, "Upload image failed: " + message, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            Log.e("ServiceOrderActivity", "Upload error", e);
            Toast.makeText(this, "Error preparing file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }
    // Utility method to convert InputStream to byte array
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    private void createRescueRequest(String description, String phone, String address, String vehicleBrand, String vehicleType, String vehicleInfo, String imageUrl) {
        RescueReqMetadata metadata = new RescueReqMetadata(rescueTitle, vehicleBrand, vehicleType, vehicleInfo);
        if (imageUrl != null) {
            List<String> imageList = new ArrayList<>();
            imageList.add(imageUrl);

            metadata.setImageList(imageList);
        }
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
                Toast.makeText(ServiceOrderActivity.this, "Gửi yêu cầu cứu hộ thất bại: " + message, Toast.LENGTH_SHORT).show();
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
        imagePreview.setVisibility(View.GONE);
        selectedImageUri = null;
    }
}