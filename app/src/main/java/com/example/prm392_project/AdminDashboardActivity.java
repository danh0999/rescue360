package com.example.prm392_project;

import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.PopupMenu;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;
import com.example.prm392_project.R;

public class AdminDashboardActivity extends AppCompatActivity {

    private CardView cardActiveRequests, cardAvailableStaff, cardCompletedMissions;
    private RecyclerView rvActiveRequests, rvAvailableStaff, rvCompletedMissions;
    private DashboardAdapter adapter;
    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        // Ánh xạ view
        cardActiveRequests = findViewById(R.id.card_active_requests);
        cardAvailableStaff = findViewById(R.id.card_available_staff);
        cardCompletedMissions = findViewById(R.id.card_completed_missions);

        rvActiveRequests = findViewById(R.id.rv_active_requests);
        rvAvailableStaff = findViewById(R.id.rv_available_staff);
        rvCompletedMissions = findViewById(R.id.rv_completed_missions);

        setupRecyclerView(rvActiveRequests, Arrays.asList("Request 1", "Request 2"));
        setupRecyclerView(rvAvailableStaff, Arrays.asList("Staff 1", "Staff 2"));
        setupRecyclerView(rvCompletedMissions, Arrays.asList("Mission 1", "Mission 2"));

        cardActiveRequests.setOnClickListener(v -> toggleRecyclerView(rvActiveRequests));
        cardAvailableStaff.setOnClickListener(v -> toggleRecyclerView(rvAvailableStaff));
        cardCompletedMissions.setOnClickListener(v -> toggleRecyclerView(rvCompletedMissions));

        btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(v -> showPopupMenu(v));
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<String> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DashboardAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    private void toggleRecyclerView(RecyclerView recyclerView) {
        recyclerView.setVisibility(recyclerView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.admin_nav_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
        popupMenu.show();
    }

    private boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            Toast.makeText(this, "Dashboard clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AdminDashboardActivity.class));
            return true;

        } else if (id == R.id.nav_users) {
            Toast.makeText(this, "Users clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, UserManagementActivity.class)); // Chuyển sang trang Users
            return true;

//    } else if (id == R.id.nav_settings) {
//        Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, SettingsActivity.class)); // Chuyển sang trang Settings
//        return true;

        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Đảm bảo quay lại login
            return true;
        } else {
            return false;
        }
    }

}
