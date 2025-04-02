package com.example.jeevanjyotandroidapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.PatientViewHolder> {
    private Context context;
    private List<Patient> patientList;

    public DoctorAdapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patientList.get(position);
        holder.tvPatientName.setText(patient.getName());

        // ✅ Navigate to add_info.class when an item is clicked
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context,PatientFormActivity.class);
            context.startActivity(intent);
        });

        // ✅ Call Functionality
        holder.ivCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + patient.getPhoneNumber()));
            context.startActivity(intent);
        });

        // ✅ Message Functionality
        holder.ivMessage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:" + patient.getPhoneNumber()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientName;
        ImageView ivCall, ivMessage;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            ivCall = itemView.findViewById(R.id.ivCall);
            ivMessage = itemView.findViewById(R.id.ivMessage);
        }
    }
}
