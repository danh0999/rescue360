package com.example.prm392_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DrawerLayout drawerLayout;
    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_management);

        // Ánh xạ DrawerLayout và nút menu
        drawerLayout = findViewById(R.id.main);
        btnMenu = findViewById(R.id.btnMenu);

        // Xử lý sự kiện mở menu
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(findViewById(R.id.navigation_view))) {
                    drawerLayout.closeDrawer(findViewById(R.id.navigation_view));
                } else {
                    drawerLayout.openDrawer(findViewById(R.id.navigation_view));
                }
            }
        });

        // Ánh xạ RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách
        userList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            userList.add(new User("Trần Minh Tiến " + i, "0335486688"));
        }

        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);
    }
}
