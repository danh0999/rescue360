package com.example.prm392_project.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.R;
import com.example.prm392_project.constants.RescueStatus;
import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.utils.DateUtils;

import java.util.List;

/**
 * RecyclerView adapter for displaying a list of RescueReq objects
 */
public class RescueReqAdapter extends RecyclerView.Adapter<RescueReqAdapter.RescueReqViewHolder> {

    private final List<RescueReq> rescueReqList;
    private final Context context;
    private final OnRescueReqClickListener clickListener;

    /**
     * Interface for handling item click events
     */
    public interface OnRescueReqClickListener {
        void onRescueReqClick(RescueReq rescueReq);
    }

    /**
     * Constructor for the adapter
     *
     * @param context The context
     * @param rescueReqList List of rescue requests to display
     * @param clickListener Listener for item click events
     */
    public RescueReqAdapter(Context context, List<RescueReq> rescueReqList, OnRescueReqClickListener clickListener) {
        this.context = context;
        this.rescueReqList = rescueReqList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RescueReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rescue_req, parent, false);
        return new RescueReqViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RescueReqViewHolder holder, int position) {
        RescueReq rescueReq = rescueReqList.get(position);
        holder.bind(rescueReq);
    }

    @Override
    public int getItemCount() {
        return rescueReqList.size();
    }

    /**
     * Updates the adapter with a new list of rescue requests
     *
     * @param newRescueReqList The new list of rescue requests
     */
    public void updateRescueReqList(List<RescueReq> newRescueReqList) {
        this.rescueReqList.clear();
        this.rescueReqList.addAll(newRescueReqList);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for RescueReq items
     */
    class RescueReqViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvStatus;
        private final TextView tvAddress;
        private final TextView tvAccidentType;
        private final TextView tvCreatedAt;

        public RescueReqViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_rescue_title);
            tvStatus = itemView.findViewById(R.id.tv_rescue_status);
            tvAddress = itemView.findViewById(R.id.tv_rescue_address);
            tvAccidentType = itemView.findViewById(R.id.tv_accident_type);
            tvCreatedAt = itemView.findViewById(R.id.tv_created_at);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    clickListener.onRescueReqClick(rescueReqList.get(position));
                }
            });
        }

        /**
         * Binds a RescueReq object to the ViewHolder
         *
         * @param rescueReq The rescue request to bind
         */
        public void bind(RescueReq rescueReq) {
            tvTitle.setText(rescueReq.getTitle());

            // Set status with appropriate formatting
            tvStatus.setText(formatStatus(RescueStatus.values()[rescueReq.getStatus()].name()));
            setStatusColor(rescueReq.getStatus());

            // Set address or coordinates if address is not available
            if (rescueReq.getAddress() != null && !rescueReq.getAddress().isEmpty()) {
                tvAddress.setText(rescueReq.getAddress());
            } else {
                tvAddress.setText(String.format("Lat: %.6f, Long: %.6f",
                        rescueReq.getLatitude(), rescueReq.getLongitude()));
            }

            // Set accident type from metadata if available
            if (rescueReq.getMetadata() != null && rescueReq.getMetadata().getAccidentType() != null) {
                tvAccidentType.setText(rescueReq.getMetadata().getAccidentType());
                tvAccidentType.setVisibility(View.VISIBLE);
            } else {
                tvAccidentType.setVisibility(View.GONE);
            }

            // Set created time
            tvCreatedAt.setText(formatTimestamp(rescueReq.getCreatedAt()));
        }

        /**
         * Sets the color of the status TextView based on the status value
         *
         * @param status The status value
         */
        private void setStatusColor(int status) {
            int colorResId;
            if (status == RescueStatus.PENDING.getValue()) {
                colorResId = R.color.status_pending;
            } else if (status == RescueStatus.IN_PROGRESS.getValue()) {
                colorResId = R.color.status_in_progress;
            } else if (status == RescueStatus.COMPLETED.getValue()) {
                colorResId = R.color.status_completed;
            } else if (status == RescueStatus.CANCELLED.getValue()) {
                colorResId = R.color.status_cancelled;
            } else {
                colorResId = R.color.status_default;
            }

            tvStatus.setTextColor(context.getResources().getColor(colorResId, null));
        }
    }

    /**
     * Formats the status to a display-friendly string
     *
     * @param status The status value
     * @return A formatted status string
     */
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
            default:
                return status;
        }
    }

    /**
     * Formats a timestamp string into a readable format
     *
     * @param timestamp The timestamp string
     * @return A formatted date/time string
     */
    private String formatTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return "";
        }

        try {
            // Here you can implement your preferred date formatting
            // This is just a placeholder - implement based on your timestamp format
            return DateUtils.formatDateTime(DateUtils.parseDateTime(timestamp, "yyyy-MM-dd HH:mm:ss"), "dd/MM/yyyy HH:mm");
        } catch (Exception e) {
            return timestamp;
        }
    }
}