package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_project.R;
import com.example.prm392_project.constants.RescueStatus;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.RescueUpdate;
import com.example.prm392_project.data.external.services.RescueStaffSvc;
import com.example.prm392_project.data.external.services.RescueSvc;
import com.example.prm392_project.data.internal.UserManager;
import com.example.prm392_project.data.models.RescueAssign;
import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.data.models.RescueReqMetadata;
import com.example.prm392_project.data.models.RescueStaff;
import com.example.prm392_project.ui.adapters.RescueStaffAdapter;
import com.example.prm392_project.utils.StringUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class RescueDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private TextView tvTitle, tvDescription, tvPhone, tvAddress, tvVehicleBrand, tvVehicleType, tvVehicleInfo, tvStatus;
    private Button btnBack, btnProcess, btnComplete, btnInProgress;
    private LinearLayout imageContainer;
    private RescueSvc rescueSvc;
    private RescueStaffSvc rescueStaffSvc;
    private UserManager userManager;
    private RescueStaffAdapter staffAdapter;
    private RecyclerView recyclerView;
    private String rescueReqId;
    private List<RescueStaff> staffList;
    private RescueReq rescueReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvVehicleBrand = findViewById(R.id.tvVehicleBrand);
        tvVehicleType = findViewById(R.id.tvVehicleType);
        tvVehicleInfo = findViewById(R.id.tvVehicleInfo);
        btnBack = findViewById(R.id.btnBack);
        btnProcess = findViewById(R.id.btnProcess);
        recyclerView = findViewById(R.id.recyclerViewRescueStaff);
        tvStatus = findViewById(R.id.textStatus);
        btnComplete = findViewById(R.id.btnComplete);
        btnInProgress = findViewById(R.id.btnInProgress);
        imageContainer = findViewById(R.id.imageContainer);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rescueReqId = (String) getIntent().getSerializableExtra("RESCUE_REQUEST_ID");

        rescueSvc = new RescueSvc(RescueDetailActivity.this);
        rescueStaffSvc = new RescueStaffSvc(RescueDetailActivity.this);
        userManager = new UserManager(RescueDetailActivity.this);

        initMap();
        loadData();

        btnBack.setOnClickListener(v -> finish());

        btnProcess.setOnClickListener(v -> {
            Intent intent = new Intent(RescueDetailActivity.this, SelectRescueStaffActivity.class);
            intent.putExtra("RESCUE_REQUEST_ID", rescueReqId);
            startActivity(intent);
            finish();
        });

        btnComplete.setOnClickListener(v -> completeRescue());

        btnInProgress.setOnClickListener(v -> updateInProgress());
    }

    private void setupRescueStaffAssign() {
        rescueStaffSvc.getRescueAssigns(rescueReqId, new ApiCallback<BaseResp<List<RescueAssign>>>() {
            @Override
            public void onSuccess(BaseResp<List<RescueAssign>> response) {
                var data = response.getData();
                var user = userManager.getUser();
                var rescueStaffList = new ArrayList<RescueStaff>();
                var isStaff = false;
                for (var assign : data) {
                    if (assign.getStaff().getUser().getId().equals(user.getId())) {
                        isStaff = true;
                    }
                    rescueStaffList.add(assign.getStaff());
                }
                if (isStaff) {
                    if (rescueReq.getStatus() == RescueStatus.IN_PROGRESS.getValue()) {
                        btnComplete.setVisibility(View.VISIBLE);
                        btnInProgress.setVisibility(View.GONE);

                    } else if (rescueReq.getStatus() == RescueStatus.PENDING.getValue() || rescueReq.getStatus() == RescueStatus.APPROVED.getValue()) {
                        btnInProgress.setVisibility(View.VISIBLE);
                        btnComplete.setVisibility(View.GONE);
                    }
                }

                staffList = rescueStaffList;
                staffAdapter = new RescueStaffAdapter(rescueStaffList);
                recyclerView.setAdapter(staffAdapter);
            }

            @Override
            public void onError(String message) {
                staffAdapter = new RescueStaffAdapter(new ArrayList<>());
                recyclerView.setAdapter(staffAdapter);
                Log.e("RescueStaffActivity", message);
            }
        });
    }


    private void loadData() {
        rescueSvc.getRescueReqById(rescueReqId, new ApiCallback<BaseResp<RescueReq>>() {
            @Override
            public void onSuccess(BaseResp<RescueReq> response) {
                if (response.getData() != null) {
                    rescueReq = response.getData();
                    tvTitle.setText(StringUtils.decodeUnicode(rescueReq.getTitle()));
                    tvDescription.setText(rescueReq.getDescription());
                    tvPhone.setText(rescueReq.getContact());
                    tvAddress.setText(rescueReq.getAddress());
                    tvVehicleBrand.setText(rescueReq.getMetadata().getVehicleBrand());
                    tvVehicleType.setText(rescueReq.getMetadata().getVehicleType());
                    tvVehicleInfo.setText(rescueReq.getMetadata().getVehicleInfo());

                    // Set status with formatting and color
                    RescueStatus status = RescueStatus.values()[rescueReq.getStatus()];
                    tvStatus.setText(formatStatus(status.name()));
                    setStatusColor(rescueReq.getStatus());

                    if (rescueReq.getStatus() == RescueStatus.PENDING.getValue() && userManager.isAdmin()) {
                        btnProcess.setVisibility(View.VISIBLE);
                    }

                    updateMapLocation(
                            new LatLng(rescueReq.getLatitude(), rescueReq.getLongitude()),
                            StringUtils.decodeUnicode(rescueReq.getTitle())
                    );
                    renderImagesFromMetadata(rescueReq.getMetadata());
                }
            }

            @Override
            public void onError(String message) {
                tvTitle.setText("Error");
                tvDescription.setText(message);
            }
        });

        setupRescueStaffAssign();
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

        // Set initial map position (Vietnam)
        LatLng vietnam = new LatLng(16.047079, 108.206230);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vietnam, 5));
    }

    private void updateMapLocation(LatLng location, String title) {
        if (mMap != null && location != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(location).title(title));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }


    private void renderImagesFromMetadata(RescueReqMetadata metadata) {
        List<String> imageList = metadata.getImageList();
        if (imageList != null && !imageList.isEmpty()) {
            imageContainer.removeAllViews();
            for (String imageUrl : imageList) {
                addImageToContainer(imageUrl);
            }
            imageContainer.setVisibility(View.VISIBLE);
            findViewById(R.id.imagesCardView).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.imagesCardView).setVisibility(View.GONE);
        }
    }

    private void addImageToContainer(String imageUrl) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                500
        );
        layoutParams.setMargins(0, 0, 0, 16);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView);

        imageContainer.addView(imageView);
    }

    private void completeRescue() {
        rescueStaffSvc.completeRescueStaff(rescueReqId, new ApiCallback<BaseResp>() {
            @Override
            public void onSuccess(BaseResp response) {
                Toast.makeText(RescueDetailActivity.this, "Rescue request has been completed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RescueDetailActivity.this, CreateInvoiceActivity.class);
                intent.putExtra("RESCUE_REQUEST_ID", rescueReqId);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                Log.e("RescueDetailActivity", message);
            }
        });
    }

    private void updateInProgress() {
        RescueUpdate rescueReq = new RescueUpdate(RescueStatus.IN_PROGRESS.getValue());

        rescueSvc.updateRescueReq(rescueReqId, rescueReq, new ApiCallback<BaseResp<RescueReq>>() {
            @Override
            public void onSuccess(BaseResp<RescueReq> response) {
                Toast.makeText(RescueDetailActivity.this, "Rescue request is in progress", Toast.LENGTH_SHORT).show();
                loadData();
            }

            @Override
            public void onError(String message) {
                Log.e("RescueDetailActivity", message);
            }
        });
    }

    // New method to set status color
    private void setStatusColor(int status) {
        int colorResId;
        if (status == RescueStatus.PENDING.getValue()) {
            colorResId = R.color.status_pending;
        } else if (status == RescueStatus.IN_PROGRESS.getValue()) {
            colorResId = R.color.status_in_progress;
        } else if (status == RescueStatus.APPROVED.getValue()) {
            colorResId = R.color.status_approved;
        } else if (status == RescueStatus.COMPLETED.getValue()) {
            colorResId = R.color.status_completed;
        } else if (status == RescueStatus.CANCELLED.getValue()) {
            colorResId = R.color.status_cancelled;
        } else {
            colorResId = R.color.status_default;
        }

        tvStatus.setTextColor(getResources().getColor(colorResId, null));
    }

    // New method to format status text
    private String formatStatus(String status) {
        if (status == null) return "Unknown";

        switch (status.toLowerCase()) {
            case "pending":
                return "Pending";
            case "in_progress":
                return "In Progress";
            case "completed":
                return "Completed";
            case "cancelled":
                return "Cancelled";
            case "approved":
                return "Approved";
            default:
                return status;
        }
    }
}