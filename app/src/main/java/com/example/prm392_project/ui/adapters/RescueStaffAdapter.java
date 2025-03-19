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

    public RescueStaffAdapter(List<RescueStaff> rescueStaffList) {
        this.rescueStaffList = rescueStaffList;
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
        holder.nameText.setText(staff.getUser().getFullName());

        if (staff.isAvailable()) {
            holder.availabilityText.setText("Available");
            holder.availabilityText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
            holder.availabilityIndicator.setBackgroundResource(R.drawable.circle_indicator);
        } else {
            holder.availabilityText.setText("Not Available");
            holder.availabilityText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
            holder.availabilityIndicator.setBackgroundResource(R.drawable.circle_indicator_red);
        }
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
