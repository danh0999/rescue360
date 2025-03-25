package com.example.prm392_project.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.RescueReqBody;
import com.example.prm392_project.data.external.response.UploadResp;
import com.example.prm392_project.data.external.services.RescueSvc;
import com.example.prm392_project.data.external.services.UploadSvc;
import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.data.models.RescueReqMetadata;
import com.example.prm392_project.utils.StringUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServiceOrderActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private GoogleMap mMap;

    private TextInputEditText editDescription, editPhone, editVehicleBrand, editVehicleType, editVehicleInfo;
    private MaterialAutoCompleteTextView editAddress;
    private MaterialButton btnCall, btnBack, btnPickImage;
    private ImageButton btnCurrentLocation;
    private ImageView imagePreview;
    private CircularProgressIndicator progressBar;
    private String rescueTitle;
    private RescueSvc rescueSvc;
    private UploadSvc uploadSvc;

    private FusedLocationProviderClient fusedLocationClient;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    // Places API
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api";
    private static final String TYPE_AUTOCOMPLETE = "/place/autocomplete/json";
    private static final String TYPE_DETAILS = "/place/details/json";
    private static final String API_KEY = "AIzaSyDjgywNFOTSo5iWGB2B3mm_c4Qsn8FFlR0"; // Replace with your actual API key

    // Debounce settings
    private static final long DEBOUNCE_DELAY_MS = 250; // Delay in milliseconds
    private Runnable autocompleteRunnable;
    private android.os.Handler handler;

    private String sessionToken;
    private OkHttpClient client;
    private List<String> placeIds;
    private List<String> descriptions;
    private LatLng selectedLocation;
    private ArrayAdapter<String> adapter;

    // Flag to track if address has been validated
    private boolean addressValidated = false;
    private String lastValidatedAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order);

        rescueTitle = StringUtils.decodeUnicode(getIntent().getStringExtra("RESCUE_TITLE"));
        if (rescueTitle.isEmpty()) {
            rescueTitle = "Yêu cầu cứu hộ";
        }

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize OkHttpClient for Places API requests
        client = new OkHttpClient();
        placeIds = new ArrayList<>();
        descriptions = new ArrayList<>();

        // Initialize handler for debouncing
        handler = new android.os.Handler();

        // Generate session token for Places API
        refreshSessionToken();

        // Initialize map
        initMap();

        // Ánh xạ các view
        editDescription = findViewById(R.id.editDescription);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        editVehicleBrand = findViewById(R.id.editVehicleBrand);
        editVehicleType = findViewById(R.id.editVehicleType);
        editVehicleInfo = findViewById(R.id.editVehicleInfo);
        btnCall = findViewById(R.id.btnCall);
        btnBack = findViewById(R.id.btnBack);
        btnPickImage = findViewById(R.id.btnPickImage);
        btnCurrentLocation = findViewById(R.id.btnCurrentLocation);
        imagePreview = findViewById(R.id.imagePreview);
        progressBar = findViewById(R.id.progressBar);

        rescueSvc = new RescueSvc(ServiceOrderActivity.this);
        uploadSvc = new UploadSvc(ServiceOrderActivity.this);

        // Create a custom adapter that forces the dropdown to show
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>()) {
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        results.values = descriptions;
                        results.count = descriptions.size();
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        clear();
                        if (results.count > 0) {
                            addAll((List<String>) results.values);
                            notifyDataSetChanged();
                        } else {
                            notifyDataSetInvalidated();
                        }
                    }
                };
            }
        };

        editAddress.setAdapter(adapter);
        editAddress.setThreshold(1);

        // Add TextWatcher with address validation tracking
        editAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (autocompleteRunnable != null) {
                    handler.removeCallbacks(autocompleteRunnable);
                }

                final String input = s.toString().trim();

                if (input.length() > 2) {
                    autocompleteRunnable = () -> getAutocompletePredictions(input);
                    handler.postDelayed(autocompleteRunnable, DEBOUNCE_DELAY_MS);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Force dropdown to show on click
        editAddress.setOnClickListener(v -> {
            if (editAddress.getText().length() > 0 && !descriptions.isEmpty()) {
                editAddress.showDropDown();
            }
        });

        // Set current location button click listener
        btnCurrentLocation.setOnClickListener(v -> {
            getCurrentLocation();
        });

        // Setup item click listener for address suggestions - update to track
        // validation
        editAddress.setOnItemClickListener((parent, view, position, id) -> {
            if (position < placeIds.size()) {
                String placeId = placeIds.get(position);
                getPlaceDetails(placeId);
                addressValidated = true;

                // Refresh session token after selection is made
                refreshSessionToken();
            }
        });

        // Xử lý khi nhấn nút chọn hình ảnh
        btnPickImage.setOnClickListener(v -> openImagePicker());

        // Xử lý khi nhấn nút gọi cứu hộ
        btnCall.setOnClickListener(v -> handleRescueRequest());

        // Xử lý khi nhấn nút quay trở về
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(ServiceOrderActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
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

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        // We got a location
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        selectedLocation = currentLatLng;
                        addressValidated = true;

                        // Update map with current location
                        updateMapLocation(currentLatLng, "Vị trí hiện tại");

                        // Get address from location coordinates
                        getAddressFromLocation(location);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Không thể lấy vị trí hiện tại. Vui lòng thử lại.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Lỗi lấy vị trí: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, try again
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Cần quyền truy cập vị trí để sử dụng tính năng này",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up handler callbacks when activity is destroyed
        if (handler != null && autocompleteRunnable != null) {
            handler.removeCallbacks(autocompleteRunnable);
        }
    }

    private void refreshSessionToken() {
        sessionToken = UUID.randomUUID().toString();
    }

    private void getAutocompletePredictions(String input) {
        // Start showing progress or indicator if needed

        String url = PLACES_API_BASE + TYPE_AUTOCOMPLETE +
                "?input=" + Uri.encode(input) +
                "&components=country:vn" +
                "&language=vi" +
                "&key=" + API_KEY +
                "&sessiontoken=" + sessionToken;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Places API", "Error getting autocomplete predictions", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }

                try {
                    String responseData = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseData);

                    if (!jsonResponse.getString("status").equals("OK")) {
                        return;
                    }

                    // Clear existing data
                    placeIds.clear();
                    descriptions.clear();

                    // Parse new data
                    JSONArray predictions = jsonResponse.getJSONArray("predictions");
                    for (int i = 0; i < predictions.length(); i++) {
                        JSONObject prediction = predictions.getJSONObject(i);
                        placeIds.add(prediction.getString("place_id"));
                        descriptions.add(prediction.getString("description"));
                    }

                    // Update UI on main thread
                    runOnUiThread(() -> {
                        // Force update the adapter and show dropdown
                        adapter.notifyDataSetChanged();
                        editAddress.setAdapter(null);
                        editAddress.setAdapter(adapter);

                        // This is crucial - need to post with a delay to ensure dropdown shows
                        new android.os.Handler().postDelayed(() -> {
                            if (editAddress.isFocused() && descriptions.size() > 0) {
                                editAddress.showDropDown();
                            }
                        }, 100);
                    });

                } catch (JSONException e) {
                    Log.e("Places API", "JSON parsing error", e);
                }
            }
        });
    }

    private void getPlaceDetails(String placeId) {
        String url = PLACES_API_BASE + TYPE_DETAILS +
                "?place_id=" + placeId +
                "&fields=geometry,formatted_address" +
                "&key=" + API_KEY +
                "&sessiontoken=" + sessionToken;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Places API", "Error getting place details", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseData);

                        if (jsonResponse.getString("status").equals("OK")) {
                            JSONObject result = jsonResponse.getJSONObject("result");
                            String formattedAddress = result.getString("formatted_address");

                            JSONObject location = result.getJSONObject("geometry")
                                    .getJSONObject("location");
                            double lat = location.getDouble("lat");
                            double lng = location.getDouble("lng");

                            selectedLocation = new LatLng(lat, lng);

                            runOnUiThread(() -> {
                                editAddress.setText(formattedAddress);
                                lastValidatedAddress = formattedAddress; // Store validated address
                                addressValidated = true; // Mark as validated
                                editAddress.setError(null);
                                editAddress.clearFocus();

                                // Update map with the selected location
                                updateMapLocation(selectedLocation, formattedAddress);
                            });
                        }
                    } catch (JSONException e) {
                        Log.e("Places API", "JSON parsing error", e);
                    }
                }
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imagePreview.setImageURI(selectedImageUri);
            imagePreview.setVisibility(View.VISIBLE);
        }
    }

    private void handleRescueRequest() {
        // Get data from EditText
        String description = editDescription.getText().toString();
        String phone = editPhone.getText().toString();
        String address = editAddress.getText().toString();
        String vehicleBrand = editVehicleBrand.getText().toString();
        String vehicleType = editVehicleType.getText().toString();
        String vehicleInfo = editVehicleInfo.getText().toString();

        // Check form data
        if (TextUtils.isEmpty(description)) {
            editDescription.setError("Vui lòng nhập mô tả vấn đề");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            editPhone.setError("Vui lòng nhập số điện thoại");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            editAddress.setError("Vui lòng nhập địa chỉ");
            return;
        }
        if (TextUtils.isEmpty(vehicleBrand)) {
            editVehicleBrand.setError("Vui lòng nhập hãng xe");
            return;
        }
        if (TextUtils.isEmpty(vehicleType)) {
            editVehicleType.setError("Vui lòng nhập loại xe");
            return;
        }
        if (TextUtils.isEmpty(vehicleInfo)) {
            editVehicleInfo.setError("Vui lòng nhập thông tin xe");
            return;
        }

        // Validate that the address is confirmed by checking validation state
        if (!addressValidated || !address.equals(lastValidatedAddress)) {
            editAddress.setError("Vui lòng chọn địa chỉ từ danh sách gợi ý hoặc sử dụng vị trí hiện tại");
            Toast.makeText(this,
                    "Địa chỉ đã bị chỉnh sửa. Vui lòng chọn từ danh sách gợi ý hoặc sử dụng vị trí hiện tại",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Check if we have a valid location
        if (selectedLocation == null) {
            Toast.makeText(this, "Vui lòng chọn địa chỉ từ danh sách gợi ý hoặc sử dụng vị trí hiện tại",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if (selectedImageUri != null) {
            uploadImage(description, phone, address, vehicleBrand, vehicleType, vehicleInfo);
        } else {
            createRescueRequest(description, phone, address, vehicleBrand, vehicleType, vehicleInfo, null);
        }
    }

    private void createRescueRequest(String description, String phone, String address, String vehicleBrand,
            String vehicleType, String vehicleInfo, String imageUrl) {
        RescueReqMetadata metadata = new RescueReqMetadata(rescueTitle, vehicleBrand, vehicleType, vehicleInfo);
        if (imageUrl != null) {
            List<String> imageList = new ArrayList<>();
            imageList.add(imageUrl);

            metadata.setImageList(imageList);
        }

        // Use the lat/lng from the selected place
        double latitude = selectedLocation != null ? selectedLocation.latitude : 10.875073;
        double longitude = selectedLocation != null ? selectedLocation.longitude : 106.800732;

        RescueReqBody rescueBody = new RescueReqBody(rescueTitle, description, latitude, longitude, address, phone,
                metadata);

        rescueSvc.createRescueReq(rescueBody, new ApiCallback<BaseResp<RescueReq>>() {
            @Override
            public void onSuccess(BaseResp<RescueReq> response) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ServiceOrderActivity.this, "Yêu cầu cứu hộ đã được gửi: " + response.getData().getId(),
                        Toast.LENGTH_SHORT).show();
                resetForm();
                Intent intent = new Intent(ServiceOrderActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ServiceOrderActivity.this, "Gửi yêu cầu cứu hộ thất bại: " + message, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void uploadImage(String description, String phone, String address, String vehicleBrand, String vehicleType,
            String vehicleInfo) {
        try {
            // Get InputStream directly from URI
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            if (inputStream == null) {
                Toast.makeText(this, "Cannot open file input stream", Toast.LENGTH_SHORT).show();
                return;
            }

            // Determine MIME type
            String mimeType = getContentResolver().getType(selectedImageUri);
            MediaType mediaType = MediaType.parse(mimeType != null ? mimeType : "image/jpeg");

            // Convert InputStream to byte array
            byte[] fileBytes = getBytes(inputStream);
            inputStream.close(); // Close the stream after use

            // Create RequestBody
            RequestBody requestFile = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    fileBytes);

            // Create MultipartBody.Part (use a generic filename if needed)
            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

            // Upload the image
            uploadSvc.uploadImage(body, new ApiCallback<BaseResp<UploadResp>>() {
                @Override
                public void onSuccess(BaseResp<UploadResp> response) {
                    String imageUrl = response.getData().getUrl();
                    createRescueRequest(description, phone, address, vehicleBrand, vehicleType, vehicleInfo, imageUrl);
                }

                @Override
                public void onError(String message) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ServiceOrderActivity.this, "Upload image failed: " + message, Toast.LENGTH_SHORT)
                            .show();
                }
            });

        } catch (IOException e) {
            Log.e("ServiceOrderActivity", "Upload error", e);
            Toast.makeText(this, "Error preparing file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    // Utility method to convert InputStream to byte array
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void resetForm() {
        editDescription.setText("");
        editPhone.setText("");
        editAddress.setText("");
        editVehicleBrand.setText("");
        editVehicleType.setText("");
        editVehicleInfo.setText("");
        imagePreview.setVisibility(View.GONE);
        selectedImageUri = null;
        selectedLocation = null;
        addressValidated = false;
        lastValidatedAddress = "";
    }

    private void getAddressFromLocation(Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, new Locale("vi", "VN"));
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressText = new StringBuilder();

                // Build address string
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    if (i > 0) {
                        addressText.append(", ");
                    }
                    addressText.append(address.getAddressLine(i));
                }

                // If no address line is available, construct from components
                if (addressText.length() == 0) {
                    String street = address.getThoroughfare();
                    String subArea = address.getSubLocality();
                    String city = address.getLocality();
                    String subAdminArea = address.getSubAdminArea();
                    String adminArea = address.getAdminArea();
                    String country = address.getCountryName();

                    if (street != null)
                        addressText.append(street).append(", ");
                    if (subArea != null)
                        addressText.append(subArea).append(", ");
                    if (city != null)
                        addressText.append(city).append(", ");
                    if (subAdminArea != null)
                        addressText.append(subAdminArea).append(", ");
                    if (adminArea != null)
                        addressText.append(adminArea).append(", ");
                    if (country != null)
                        addressText.append(country);

                    // Remove trailing comma and space if they exist
                    if (addressText.length() > 2 && addressText.substring(addressText.length() - 2).equals(", ")) {
                        addressText.setLength(addressText.length() - 2);
                    }
                }

                final String finalAddress = addressText.length() > 0 ? addressText.toString() : "Vị trí hiện tại";
                lastValidatedAddress = finalAddress;

                // Update UI with address
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    editAddress.setText(finalAddress);
                    editAddress.setError(null);
                    updateMapLocation(selectedLocation, finalAddress);
                });
            } else {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    editAddress.setText("Vị trí hiện tại");
                    editAddress.setError(null);
                });
            }
        } catch (IOException e) {
            Log.e("Geocoder", "Error getting address: " + e.getMessage());
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                editAddress.setText("Vị trí hiện tại");
                editAddress.setError(null);
            });
        }
    }
}
