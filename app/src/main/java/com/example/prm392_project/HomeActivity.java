package com.example.prm392_project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Kiểm tra quyền trước khi khởi tạo map
        checkLocationPermission();

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
        setupServiceClick(R.id.lnOutOfFuel);
        setupServiceClick(R.id.imgOutOfFuel);
        setupServiceClick(R.id.tvOutOfFuel);

        setupServiceClick(R.id.lnFlatTire);
        setupServiceClick(R.id.imgFlatTire);
        setupServiceClick(R.id.tvFlatTire);

        setupServiceClick(R.id.lnEngineFailure);
        setupServiceClick(R.id.imgEngineFailure);
        setupServiceClick(R.id.tvEngineFailure);

        setupServiceClick(R.id.lnAccident);
        setupServiceClick(R.id.imgAccident);
        setupServiceClick(R.id.tvAccident);

        setupServiceClick(R.id.lnOther);
        setupServiceClick(R.id.imgOther);
        setupServiceClick(R.id.tvOther);

        // Chuyển trang sang ListFragment khi click nút (Giả sử nút có id btnViewRequests)
        Button btnViewRequests = findViewById(R.id.btn_payment);
        btnViewRequests.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RequestListActivity.class);
            startActivity(intent);
        });
    }

    private void setupServiceClick(int viewId) {
        View view = findViewById(viewId);
        view.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ServiceOrderActivity.class);
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
}
