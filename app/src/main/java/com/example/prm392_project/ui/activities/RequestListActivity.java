package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;
import com.example.prm392_project.data.internal.UserManager;
import com.example.prm392_project.ui.fragments.RequestListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RequestListActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private UserManager userManager;
    private boolean isStaff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        userManager = new UserManager(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_request);

        if (!userManager.isAdmin()) {
            bottomNavigationView.setVisibility(View.GONE);
        }
        setupBottomNavigation();

        isStaff = getIntent().getBooleanExtra("STAFF", false);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RequestListFragment(isStaff))
                    .commit();
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
//                    Toast.makeText(AdminDashboardActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RequestListActivity.this, AdminDashboardActivity.class));
                    return true;
                } else if (id == R.id.nav_request) {
//                    startActivity(new Intent(RequestListActivity.this, RequestListActivity.class));
                    Toast.makeText(RequestListActivity.this, "Request", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_users) {
                    startActivity(new Intent(RequestListActivity.this, RescueStaffActivity.class));
                    return true;
                } else if (id == R.id.nav_chat) {
                    startActivity(new Intent(RequestListActivity.this, ConversationListActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}