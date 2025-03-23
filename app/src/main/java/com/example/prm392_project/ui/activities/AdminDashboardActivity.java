package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.prm392_project.R;
import com.example.prm392_project.data.internal.TokenManager;
import com.example.prm392_project.ui.adapters.DashboardAdapter;

public class AdminDashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        btnMenu = findViewById(R.id.btnMenu);

        setupPopupMenu();
        setupBottomNavigation();
    }

    private void setupPopupMenu() {
        btnMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(AdminDashboardActivity.this, btnMenu);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_admin, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.nav_dashboard) {
                    Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.nav_users) {
                    startActivity(new Intent(this, UserManagementActivity.class));
                } else if (item.getItemId() == R.id.nav_logout) {
                    logout();
                }
                return true;
            });

            popupMenu.show();
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Toast.makeText(AdminDashboardActivity.this, "Dashboard", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_request) {
                    startActivity(new Intent(AdminDashboardActivity.this, RequestListActivity.class));
                    return true;
                } else if (id == R.id.nav_users) {
                    startActivity(new Intent(AdminDashboardActivity.this, RescueStaffActivity.class));
                    return true;
                } else if (id == R.id.nav_chat) {
                    startActivity(new Intent(AdminDashboardActivity.this, ConversationListActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    private void logout() {
        TokenManager tokenManager = new TokenManager(this);
        tokenManager.clearToken();

        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
