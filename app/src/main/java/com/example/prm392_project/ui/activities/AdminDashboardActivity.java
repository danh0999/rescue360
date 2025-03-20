package com.example.prm392_project.ui.activities;

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
import java.util.List;

import com.example.prm392_project.ui.adapters.DashboardAdapter;
import com.example.prm392_project.R;
import com.example.prm392_project.data.internal.TokenManager;

public class AdminDashboardActivity extends AppCompatActivity {

    private CardView cardActiveRequests, cardAvailableStaff, cardConversations;
    private DashboardAdapter adapter;
    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        // Ánh xạ view
        cardActiveRequests = findViewById(R.id.card_active_requests);
        cardAvailableStaff = findViewById(R.id.card_available_staff);
        cardConversations = findViewById(R.id.card_conversations);

        cardActiveRequests.setOnClickListener(v -> {
            Intent intent = new Intent(this, RequestListActivity.class);
            startActivity(intent);
        });
        cardAvailableStaff.setOnClickListener(v -> {
            Intent intent = new Intent(this, RescueStaffActivity.class);
            startActivity(intent);
        });
        cardConversations.setOnClickListener(v -> {
            Intent intent = new Intent(this, ConversationListActivity.class);
            startActivity(intent);
        });

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
            TokenManager tokenManager = new TokenManager(this);
            tokenManager.clearToken();

            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Đảm bảo quay lại login
            return true;
        } else {
            return false;
        }
    }

}
