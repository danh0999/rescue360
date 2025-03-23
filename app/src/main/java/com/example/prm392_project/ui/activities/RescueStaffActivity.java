package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.services.RescueStaffSvc;
import com.example.prm392_project.data.models.RescueStaff;
import com.example.prm392_project.ui.adapters.RescueStaffAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class RescueStaffActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RescueStaffAdapter adapter;
    private List<RescueStaff> rescueStaffList;

    private RescueStaffSvc rescueStaffSvc;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_staff);
        rescueStaffSvc = new RescueStaffSvc(this);

        recyclerView = findViewById(R.id.recyclerViewRescueStaff);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_users);

        setupRescueStaffList();
        setupBottomNavigation();
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

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    startActivity(new Intent(RescueStaffActivity.this, AdminDashboardActivity.class));
                    return true;
                } else if (id == R.id.nav_request) {
                    startActivity(new Intent(RescueStaffActivity.this, RequestListActivity.class));
                    return true;
                } else if (id == R.id.nav_users) {
                    Toast.makeText(RescueStaffActivity.this, "Rescue Staff", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_chat) {
                    startActivity(new Intent(RescueStaffActivity.this, ConversationListActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
