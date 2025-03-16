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
import com.example.prm392_project.data.external.response.UpdateProfileReq;
import com.example.prm392_project.data.external.services.AuthSvc;
import com.example.prm392_project.data.models.User;
import com.example.prm392_project.utils.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtEmail;
    private Button btnLogout, btnUpdate;

    private AuthSvc authSvc;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        edtName = findViewById(R.id.userName);
        edtPhone = findViewById(R.id.userPhoneNumber);
        edtEmail = findViewById(R.id.userEmail);
        btnLogout = findViewById(R.id.btnLogout);
        btnUpdate = findViewById(R.id.btnUpdate);

        authSvc = new AuthSvc(ProfileActivity.this);
        tokenManager = new TokenManager(ProfileActivity.this);

        loadUserData();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                String email = edtEmail.getText().toString();
                if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                UpdateProfileReq req = new UpdateProfileReq(name, phone, "");

                authSvc.updateProfile(req, new ApiCallback<BaseResp>() {
                    @Override
                    public void onSuccess(BaseResp response) {
                        Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        loadUserData();
                    }

                    @Override
                    public void onError(String message) {
                        Log.e("ProfileActivity", "Failed to update user profile: " + message);
                        Toast.makeText(ProfileActivity.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
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
                Toast.makeText(ProfileActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Logout
    private void logoutUser() {
        tokenManager.clearToken();
        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
