package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.interfaces.IAuthSvc;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.LoginResp;
import com.example.prm392_project.data.external.services.AuthSvc;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister, btnGoogleLogin;
    private AuthSvc authSvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        authSvc = new AuthSvc(LoginActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Google login is not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show();
            return;
        }

        authSvc.login(email, password, new ApiCallback<BaseResp<LoginResp>>() {
            @Override
            public void onSuccess(BaseResp<LoginResp> response) {
                LoginResp data = response.getData();

                if (data != null) {
                    Toast.makeText(LoginActivity.this, "Login successful UserId: " + data.getUserId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                Log.e("LoginActivity", message);
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
