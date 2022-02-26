package com.efficacious.e_smartsrestaurant.NewOrder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Model.TableStatusDetail;
import com.efficacious.e_smartsrestaurant.R;

import java.util.List;

public class TableStatusAdapter extends RecyclerView.Adapter<TableStatusAdapter.ViewHolder> {

    List<TableStatusDetail> tableStatusDetails;
    Context context;

    public TableStatusAdapter(List<TableStatusDetail> tableStatusDetails) {
        this.tableStatusDetails = tableStatusDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tableNo.setText(tableStatusDetails.get(position).getTableName());
        String status = tableStatusDetails.get(position).getTableStatus();
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (status.equalsIgnoreCase("free")){
//                    tableName = tableStatusDetail.get(position).getTableName();
//                    showDialog(tableName);
//                }else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setMessage("Sorry,Table Occupied");
//                    builder.setNegativeButton("Close",null);
//                    builder.show();
//                }
//            }
//        });

        if (status.equalsIgnoreCase("free")){
            holder.bookOrNot.setImageResource(R.drawable.available);
            holder.tableImg.setImageResource(R.drawable.empty_table);
        }

    }

    @Override
    public int getItemCount() {
        return tableStatusDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView tableImg;
        TextView tableNo;
        ImageView bookOrNot;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tableImg = itemView.findViewById(R.id.tableImg);
            bookOrNot = itemView.findViewById(R.id.bookOrNot);
            tableNo = itemView.findViewById(R.id.tableNo);
            relativeLayout = itemView.findViewById(R.id.layoutBg);
        }
    }
}
