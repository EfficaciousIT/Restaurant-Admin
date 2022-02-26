package com.efficacious.e_smartsrestaurant.Dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.TableReport;
import com.efficacious.e_smartsrestaurant.Room.TableReportList;

import java.util.List;

public class TableReportAdapter extends RecyclerView.Adapter<TableReportAdapter.ViewHolder> {

    List<TableReport> tableReports;

    public TableReportAdapter(List<TableReport> tableReports) {
        this.tableReports = tableReports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.datewise_report_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.orderId.setText("#" + tableReports.get(position).getOrderId().toString());
        holder.tableName.setText(tableReports.get(position).getTableName() + "(" + tableReports.get(position).getStatus() + ")");
        holder.total.setText("Total : ₹ " +tableReports.get(position).getTotal().toString());
        holder.date.setText(tableReports.get(position).getCreatedDate());

    }

    @Override
    public int getItemCount() {
        return tableReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderId,tableName,total,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.orderId);
            tableName = itemView.findViewById(R.id.tableName);
            total = itemView.findViewById(R.id.total);
            date = itemView.findViewById(R.id.createdDate);
        }
    }
}
