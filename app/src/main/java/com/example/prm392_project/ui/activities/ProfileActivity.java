package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.services.AuthSvc;
import com.example.prm392_project.data.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtEmail;
    private Button btnLogout;

    private AuthSvc authSvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        edtName = findViewById(R.id.userName);
        edtPhone = findViewById(R.id.userPhoneNumber);
        edtEmail = findViewById(R.id.userEmail);
        btnLogout = findViewById(R.id.btnLogout);

        authSvc = new AuthSvc(ProfileActivity.this);

        loadUserData();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_message) {
                startActivity(new Intent(ProfileActivity.this, ChatActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }

    private void loadUserData() {
        authSvc.getProfile(new ApiCallback<BaseResp<User>>() {
            @Override
            public void onSuccess(BaseResp<User> response) {
                User user = response.getData();
                if (user != null) {
                    edtName.setText(user.getFullName());
                    edtPhone.setText(user.getPhone());
                    edtEmail.setText(user.getEmail());
                }
            }

            @Override
            public void onError(String message) {
                Log.e("ProfileActivity", "Failed to get user profile: " + message);
                Toast.makeText(ProfileActivity.this, "Failed to get user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Logout
    private void logoutUser() {
        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
