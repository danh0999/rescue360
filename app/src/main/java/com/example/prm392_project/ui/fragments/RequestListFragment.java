package com.example.prm392_project.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.services.RescueSvc;
import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.ui.activities.HomeActivity;
import com.example.prm392_project.ui.adapters.RescueReqAdapter;

import java.util.List;

public class RequestListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RescueReqAdapter adapter;
    private RescueSvc rescueSvc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        rescueSvc = new RescueSvc(RequestListFragment.this.getContext());

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lấy danh sách yêu cầu từ API
        rescueSvc.getAdminRescueReq(new ApiCallback<BaseResp<List<RescueReq>>>() {
            @Override
            public void onSuccess(BaseResp<List<RescueReq>> response) {
                if (response.getData() != null) {
                    List<RescueReq> rescueReqList = response.getData();
                    adapter = new RescueReqAdapter(getContext(), rescueReqList, rescueReq -> {
                        Toast.makeText(getContext(), "Click on " + rescueReq.getTitle(), Toast.LENGTH_SHORT).show();
                    });

                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện nút "Trở về Home"
        Button btnBackToHome = view.findViewById(R.id.btnBackToHome);
        btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}
