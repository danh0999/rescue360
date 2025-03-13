package com.example.prm392_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public class UserManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.admin_user_management);

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Dữ liệu mẫu
            //         Thiết lập RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            userList = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                userList.add(new User("Trần Minh Tiến", "0335486688"));
            }

            userAdapter = new UserAdapter(userList);
            recyclerView.setAdapter(userAdapter);
        }
    }

