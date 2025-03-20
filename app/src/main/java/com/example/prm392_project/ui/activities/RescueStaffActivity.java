package com.example.prm392_project.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.services.RescueStaffSvc;
import com.example.prm392_project.data.models.RescueStaff;
import com.example.prm392_project.ui.adapters.RescueStaffAdapter;

import java.util.ArrayList;
import java.util.List;

public class RescueStaffActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RescueStaffAdapter adapter;
    private List<RescueStaff> rescueStaffList;
    private Button btnBack;

    private RescueStaffSvc rescueStaffSvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_staff);
        rescueStaffSvc = new RescueStaffSvc(this);

        recyclerView = findViewById(R.id.recyclerViewRescueStaff);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        setupRescueStaffList();
    }

    private void setupRescueStaffList() {
        rescueStaffSvc.getRescueStaff(new ApiCallback<BaseResp<List<RescueStaff>>>() {
            @Override
            public void onSuccess(BaseResp<List<RescueStaff>> response) {
                rescueStaffList = response.getData();
                adapter = new RescueStaffAdapter(rescueStaffList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                rescueStaffList = new ArrayList<>();
                adapter = new RescueStaffAdapter(rescueStaffList);
                recyclerView.setAdapter(adapter);

                // Show error message
                Log.e("RescueStaffActivity", message);
            }
        });
    }
}
