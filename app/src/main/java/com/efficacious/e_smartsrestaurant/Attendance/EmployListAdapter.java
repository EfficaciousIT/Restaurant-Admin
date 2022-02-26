package com.efficacious.e_smartsrestaurant.Attendance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Model.GetEmployeeDetail;
import com.efficacious.e_smartsrestaurant.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployListAdapter extends RecyclerView.Adapter<EmployListAdapter.ViewHolder>{

    List<GetEmployeeDetail> employeeDetails;
    Context context;

    public EmployListAdapter(List<GetEmployeeDetail> employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employ_detail_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.employName.setText(employeeDetails.get(position).getFirstName());
            holder.employType.setText(employeeDetails.get(position).getMobileNo());

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new MarkAttendanceFragment();
                    AppCompatActivity activity = (AppCompatActivity) context;
                    Bundle bundle = new Bundle();
                    bundle.putString("eName",employeeDetails.get(position).getFirstName());
                    bundle.putString("eMobile",employeeDetails.get(position).getMobileNo());
                    bundle.putString("empId",employeeDetails.get(position).getEmployeeId().toString());
                    fragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container,fragment)
                            .addToBackStack(null).commit();
                }
            });

    }

    @Override
    public int getItemCount() {
        return employeeDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        CircleImageView profileImg;
        TextView employName,employType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImg = itemView.findViewById(R.id.profileImg);
            employName = itemView.findViewById(R.id.employName);
            employType = itemView.findViewById(R.id.employType);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

        }
    }
}
