package com.example.prm392_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_management);

        // Ánh xạ nút menu và RecyclerView
        btnMenu = findViewById(R.id.btnMenu);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách user mẫu
        userList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            userList.add(new User("Trần Minh Tiến " + i, "0335486688"));
        }

        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);

        // Xử lý khi nhấn nút Menu
        btnMenu.setOnClickListener(v -> showPopupMenu(v));
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.admin_nav_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int itemId = menuItem.getItemId();

            if (itemId == R.id.nav_dashboard) {
                Toast.makeText(this, "Dashboard clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AdminDashboardActivity.class));
                return true;
            } else if (itemId == R.id.nav_users) {
                Toast.makeText(this, "User Management clicked", Toast.LENGTH_SHORT).show();
                // Ở lại trang hiện tại
                return true;
//            } else if (itemId == R.id.nav_settings) {
//                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
//                return true;
            } else if (itemId == R.id.nav_logout) {
                logout();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void logout() {
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
