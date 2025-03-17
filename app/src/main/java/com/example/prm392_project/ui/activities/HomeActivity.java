package com.example.prm392_project.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.services.AuthSvc;
import com.example.prm392_project.data.internal.UserManager;
import com.example.prm392_project.data.models.User;
import com.example.prm392_project.data.internal.TokenManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;

    private AuthSvc authSvc;
    private TokenManager tokenManager;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        authSvc = new AuthSvc(HomeActivity.this);
        tokenManager = new TokenManager(HomeActivity.this);
        userManager = new UserManager(HomeActivity.this);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Kiểm tra quyền trước khi khởi tạo map
        checkLocationPermission();

        loadUserData();

        // Xử lý chuyển trang bằng BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_message) {
                startActivity(new Intent(HomeActivity.this, ChatActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Xử lý click cho các dịch vụ
        setupServiceClick(R.id.lnOutOfFuel, "Hết xăng");
        setupServiceClick(R.id.imgOutOfFuel, "Hết xăng");
        setupServiceClick(R.id.tvOutOfFuel, "Hết xăng");

        setupServiceClick(R.id.lnFlatTire, "Thủng lốp");
        setupServiceClick(R.id.imgFlatTire, "Thủng lốp");
        setupServiceClick(R.id.tvFlatTire, "Thủng lốp");

        setupServiceClick(R.id.lnEngineFailure, "Hỏng máy");
        setupServiceClick(R.id.imgEngineFailure, "Hỏng máy");
        setupServiceClick(R.id.tvEngineFailure, "Hỏng máy");

        setupServiceClick(R.id.lnAccident, "Tai nạn");
        setupServiceClick(R.id.imgAccident, "Tai nạn");
        setupServiceClick(R.id.tvAccident, "Tai nạn");

        setupServiceClick(R.id.lnOther, "Khác");
        setupServiceClick(R.id.imgOther, "Khác");
        setupServiceClick(R.id.tvOther, "Khác");

        // Chuyển trang sang ListFragment khi click nút (Giả sử nút có id btnViewRequests)
        Button btnViewRequests = findViewById(R.id.btn_payment);
        btnViewRequests.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RequestListActivity.class);
            startActivity(intent);
        });
    }

    private void setupServiceClick(int viewId, String title) {
        View view = findViewById(viewId);
        view.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ServiceOrderActivity.class);
            intent.putExtra("RESCUE_TITLE", title);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        tokenManager.clearToken();
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initMap();
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            LatLng myLocation = new LatLng(10.875073, 106.800732);
            mMap.addMarker(new MarkerOptions().position(myLocation).title("Vị trí mẫu"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền truy cập vị trí được cấp", Toast.LENGTH_SHORT).show();
                initMap();
            } else {
                Toast.makeText(this, "Quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadUserData() {
        authSvc.getProfile(new ApiCallback<BaseResp<User>>() {
            @Override
            public void onSuccess(BaseResp<User> response) {
                User user = response.getData();
                if (user != null) {
                    Log.d("HomeActivity", "User: " + user);

                    // check if admin then navigate to admin activity
                    if (user.isAdmin()) {
                        userManager.saveIsAdmin(true);
                        userManager.saveUser(user);
                        Intent intent = new Intent(HomeActivity.this, AdminDashboardActivity.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onError(String message) {
                Log.e("ProfileActivity", "Failed to get user profile: " + message);
                Toast.makeText(HomeActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
