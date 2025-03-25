package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_project.R;
import com.example.prm392_project.constants.RescueStatus;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.AssignReq;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.RescueReqBody;
import com.example.prm392_project.data.external.response.RescueUpdate;
import com.example.prm392_project.data.external.services.RescueStaffSvc;
import com.example.prm392_project.data.external.services.RescueSvc;
import com.example.prm392_project.data.models.RescueAssign;
import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.data.models.RescueStaff;
import com.example.prm392_project.ui.adapters.RescueStaffAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectRescueStaffActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSelectStaff;
    private Button btnConfirmSelection, btnBack;
    private RescueStaffAdapter adapter;
    private List<RescueStaff> rescueStaffList;
    private RescueStaff selectedStaff;
    private RescueStaffSvc rescueStaffSvc;
    private RescueSvc rescueSvc;
    private String rescueId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_rescue_staff);

        rescueStaffSvc = new RescueStaffSvc(this);
        rescueSvc = new RescueSvc(this);

        recyclerViewSelectStaff = findViewById(R.id.recyclerViewSelectStaff);
        btnConfirmSelection = findViewById(R.id.btnConfirmSelection);
        btnBack = findViewById(R.id.btnBack);

        recyclerViewSelectStaff.setLayoutManager(new LinearLayoutManager(this));

        rescueId = getIntent().getStringExtra("RESCUE_REQUEST_ID");

        setupRescueStaffList();

        btnConfirmSelection.setOnClickListener(view -> {
            if (selectedStaff != null) {
                updateRescueStatus(selectedStaff.getId());
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selected_staff_id", selectedStaff.getId());
                setResult(RESULT_OK, resultIntent);
                finish(); // Go back to RescueDetailActivity
            }
        });

        btnBack.setOnClickListener(view -> finish());

    }

    private void setupRescueStaffList() {
        rescueStaffSvc.getRescueStaff(new ApiCallback<BaseResp<List<RescueStaff>>>() {
            @Override
            public void onSuccess(BaseResp<List<RescueStaff>> response) {
                rescueStaffList = response.getData();
                adapter = new RescueStaffAdapter(rescueStaffList, new RescueStaffAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RescueStaff staff) {
                        if (staff.isAvailable()) {
                            selectedStaff = staff;
                            btnConfirmSelection.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(SelectRescueStaffActivity.this, "Staff is not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                recyclerViewSelectStaff.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                rescueStaffList = new ArrayList<>();
                adapter = new RescueStaffAdapter(rescueStaffList, new RescueStaffAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RescueStaff staff) {
                        selectedStaff = staff;
                        btnConfirmSelection.setVisibility(View.VISIBLE);
                    }
                });
                recyclerViewSelectStaff.setAdapter(adapter);

                // Show error message
                Log.e("RescueStaffActivity", message);
            }
        });
    }


    private void updateRescueStatus(String staffId) {
        // TODO: Call API to update status
        Toast.makeText(this, "Rescue assigned to Staff ID: " + staffId, Toast.LENGTH_SHORT).show();
        rescueStaffSvc.assignRescueStaff(new AssignReq(rescueId, staffId), new ApiCallback<BaseResp<RescueAssign>>() {
            @Override
            public void onSuccess(BaseResp<RescueAssign> response) {
                Toast.makeText(SelectRescueStaffActivity.this, "Rescue assigned to Staff ID: " + staffId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(SelectRescueStaffActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        RescueUpdate body = new RescueUpdate(RescueStatus.APPROVED.getValue());

        // update rescue status
        rescueSvc.updateRescueReq(rescueId, body, new ApiCallback<BaseResp<RescueReq>>() {
            @Override
            public void onSuccess(BaseResp<RescueReq> response) {
                Toast.makeText(SelectRescueStaffActivity.this, "Rescue assigned to Staff ID: " + staffId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(SelectRescueStaffActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
