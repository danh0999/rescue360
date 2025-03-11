package com.example.prm392_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etAddress, etEmail, etPassword, etPhone;
    private CheckBox cbUser, cbRescueDriver;
    private Button btnRegister;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        cbUser = findViewById(R.id.cbUser);
        cbRescueDriver = findViewById(R.id.cbRescueDriver);
        btnRegister = findViewById(R.id.btnRegister);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        String userType = "";
        if (cbUser.isChecked()) {
            userType = "User";
        } else if (cbRescueDriver.isChecked()) {
            userType = "RescueDriver";
        }

        if (fullName.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || userType.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put("fullName", fullName);
        values.put("address", address);
        values.put("email", email);
        values.put("password", password);
        values.put("phone", phone);
        values.put("userType", userType);

        long result = database.insert("users", null, values);

        if (result != -1) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
