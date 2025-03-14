package com.example.prm392_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<Request> requestList;
    private Context context;
    private OnPayClickListener listener;

    public interface OnPayClickListener {
        void onPayClick(Request request);
    }

    public RequestAdapter(Context context, List<Request> requestList, OnPayClickListener listener) {
        this.context = context;
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_service, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requestList.get(position);
        holder.tvRequestTitle.setText(request.getTitle());
        holder.btnPay.setOnClickListener(v -> listener.onPayClick(request));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvRequestTitle;
        Button btnPay;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequestTitle = itemView.findViewById(R.id.tvRequestTitle);
            btnPay = itemView.findViewById(R.id.btnPay);
        }
    }
}
