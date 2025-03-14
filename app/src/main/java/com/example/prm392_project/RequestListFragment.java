package com.example.prm392_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RequestListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private List<Request> requestList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Giả lập danh sách yêu cầu
        requestList = new ArrayList<>();
        requestList.add(new Request("Yêu cầu 1", false));
        requestList.add(new Request("Yêu cầu 2", true));

        // Thiết lập Adapter
        adapter = new RequestAdapter(getContext(), requestList, request -> {
            // Chuyển sang màn hình phương thức thanh toán
            Intent intent = new Intent(getContext(), PaymentMethodActivity.class);
            intent.putExtra("request_title", request.getTitle());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Xử lý sự kiện nút "Trở về Home"
        Button btnBackToHome = view.findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HomeActivity.class); // Thay HomeActivity bằng activity trang chủ của bạn
            startActivity(intent);
            requireActivity().finish(); // Đảm bảo đóng Fragment hiện tại nếu cần
        });

        return view;
    }
}
