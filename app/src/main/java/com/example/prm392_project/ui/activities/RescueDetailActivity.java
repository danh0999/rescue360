package com.example.prm392_project.ui.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.services.RescueSvc;
import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.utils.StringUtils;

public class RescueDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvPhone, tvAddress, tvVehicleBrand, tvVehicleType, tvVehicleInfo;
    private Button btnBack;

    private RescueSvc rescueSvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvVehicleBrand = findViewById(R.id.tvVehicleBrand);
        tvVehicleType = findViewById(R.id.tvVehicleType);
        tvVehicleInfo = findViewById(R.id.tvVehicleInfo);
        btnBack = findViewById(R.id.btnBack);


        // Retrieve data from intent
        String rescueReqId = (String) getIntent().getSerializableExtra("RESCUE_REQUEST_ID");

        rescueSvc = new RescueSvc(RescueDetailActivity.this);

        // Get rescue request by ID
        rescueSvc.getRescueReqById(rescueReqId, new ApiCallback<BaseResp<RescueReq>>() {
            @Override
            public void onSuccess(BaseResp<RescueReq> response) {
                if (response.getData() != null) {
                    RescueReq rescueReq = response.getData();
                    tvTitle.setText(StringUtils.decodeUnicode(rescueReq.getTitle()));
                    tvDescription.setText(rescueReq.getDescription());
                    tvPhone.setText(rescueReq.getContact());
                    tvAddress.setText(rescueReq.getAddress());
                    tvVehicleBrand.setText(rescueReq.getMetadata().getVehicleBrand());
                    tvVehicleType.setText(rescueReq.getMetadata().getVehicleType());
                    tvVehicleInfo.setText(rescueReq.getMetadata().getVehicleInfo());
                }
            }

            @Override
            public void onError(String message) {
                // Show error message
                tvTitle.setText("Error");
                tvDescription.setText(message);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity
            }
        });
    }
}
