package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.R;
import com.example.prm392_project.constants.RescueStatus;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.services.RescueStaffSvc;
import com.example.prm392_project.data.external.services.RescueSvc;
import com.example.prm392_project.data.internal.UserManager;
import com.example.prm392_project.data.models.RescueAssign;
import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.data.models.RescueStaff;
import com.example.prm392_project.ui.adapters.RescueStaffAdapter;
import com.example.prm392_project.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RescueDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvPhone, tvAddress, tvVehicleBrand, tvVehicleType, tvVehicleInfo, tvStatus;
    private Button btnBack, btnProcess;

    private RescueSvc rescueSvc;
    private RescueStaffSvc rescueStaffSvc;
    private UserManager userManager;
    private RescueStaffAdapter staffAdapter;
    private RecyclerView recyclerView;
    private String rescueReqId;

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
        btnProcess = findViewById(R.id.btnProcess);
        recyclerView = findViewById(R.id.recyclerViewRescueStaff);
        tvStatus = findViewById(R.id.textStatus);

        // Set LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve data from intent
        rescueReqId = (String) getIntent().getSerializableExtra("RESCUE_REQUEST_ID");

        rescueSvc = new RescueSvc(RescueDetailActivity.this);
        rescueStaffSvc = new RescueStaffSvc(RescueDetailActivity.this);
        userManager = new UserManager(RescueDetailActivity.this);

        loadData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity
            }
        });

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to SelectRescueStaffActivity
                Intent intent = new Intent(RescueDetailActivity.this, SelectRescueStaffActivity.class);
                intent.putExtra("RESCUE_REQUEST_ID", rescueReqId);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void setupRescueStaffAssign() {
        rescueStaffSvc.getRescueAssigns(rescueReqId, new ApiCallback<BaseResp<List<RescueAssign>>>() {
            @Override
            public void onSuccess(BaseResp<List<RescueAssign>> response) {
                var data = response.getData();
                var rescueStaffList = new ArrayList<RescueStaff>();
                for (var assign : data) {
                    rescueStaffList.add(assign.getStaff());
                }
                Log.d("RescueStaffActivity", "RescueStaffList: " + rescueStaffList.size());
                staffAdapter = new RescueStaffAdapter(rescueStaffList);
                recyclerView.setAdapter(staffAdapter);
            }

            @Override
            public void onError(String message) {
                staffAdapter = new RescueStaffAdapter(new ArrayList<>());
                recyclerView.setAdapter(staffAdapter);

                // Show error message
                Log.e("RescueStaffActivity", message);
            }
        });
    }

    private void loadData() {
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

                    // Set status
                    RescueStatus status = RescueStatus.values()[rescueReq.getStatus()];
                    tvStatus.setText(status.toString());

                    if (rescueReq.getStatus() == RescueStatus.PENDING.getValue() && userManager.isAdmin()) {
                        btnProcess.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(String message) {
                // Show error message
                tvTitle.setText("Error");
                tvDescription.setText(message);
            }
        });

        setupRescueStaffAssign();
    }
}
