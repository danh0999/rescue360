package com.example.prm392_project.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_project.R;
import com.example.prm392_project.data.models.RescueStaff;
import java.util.List;

public class RescueStaffAdapter extends RecyclerView.Adapter<RescueStaffAdapter.ViewHolder> {
    private List<RescueStaff> rescueStaffList;
    private OnItemClickListener onItemClickListener;
    private RescueStaff selectedStaff;

    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(RescueStaff staff);
    }

    public RescueStaffAdapter(List<RescueStaff> rescueStaffList) {
        this.rescueStaffList = rescueStaffList;
    }

    public RescueStaffAdapter(List<RescueStaff> rescueStaffList, OnItemClickListener listener) {
        this.rescueStaffList = rescueStaffList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rescue_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RescueStaff staff = rescueStaffList.get(position);
        holder.positionText.setText(staff.getPosition());
        holder.nameText.setText(staff.getUser().getFullName());  // Ensure correct method
        holder.availabilityText.setText(staff.isAvailable() ? "Available" : "Not Available");
        holder.availabilityText.setTextColor(ContextCompat.getColor(
                holder.itemView.getContext(),
                staff.isAvailable() ? R.color.green : R.color.red
        ));
        holder.availabilityIndicator.setBackgroundResource(staff.isAvailable()
                ? R.drawable.circle_indicator
                : R.drawable.circle_indicator_red
        );

        // Highlight selected item
        holder.itemView.setBackgroundColor(
                staff == selectedStaff ? ContextCompat.getColor(holder.itemView.getContext(), R.color.light_gray)
                        : ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white)
        );

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            selectedStaff = staff;
            notifyDataSetChanged();  // Refresh UI
            onItemClickListener.onItemClick(staff);
        });
    }

    @Override
    public int getItemCount() {
        return rescueStaffList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView positionText, nameText, availabilityText;
        View availabilityIndicator;

        public ViewHolder(View itemView) {
            super(itemView);
            positionText = itemView.findViewById(R.id.positionText);
            nameText = itemView.findViewById(R.id.nameText);
            availabilityText = itemView.findViewById(R.id.availabilityText);
            availabilityIndicator = itemView.findViewById(R.id.availabilityIndicator);
        }
    }
}
