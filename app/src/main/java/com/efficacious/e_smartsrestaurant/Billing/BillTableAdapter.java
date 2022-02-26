package com.efficacious.e_smartsrestaurant.Billing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.ExistingOrder.ViewOrderFragment;
import com.efficacious.e_smartsrestaurant.Model.ExistingOrder;
import com.efficacious.e_smartsrestaurant.Model.GetBillForTable;
import com.efficacious.e_smartsrestaurant.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BillTableAdapter extends RecyclerView.Adapter<BillTableAdapter.ViewHolder> {

    List<GetBillForTable> getBillForTables;
    Context context;

    public BillTableAdapter(List<GetBillForTable> getBillForTables) {
        this.getBillForTables = getBillForTables;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tableNo.setText(getBillForTables.get(position).getTableName());
        holder.orderId.setText("#" +getBillForTables.get(position).getOrderId().toString());
        holder.bookOrNot.setVisibility(View.GONE);
        if (getBillForTables.get(position).getTableName().equalsIgnoreCase("TakeAway")){
            holder.tableImg.setVisibility(View.INVISIBLE);
            holder.takeAwayImg.setVisibility(View.VISIBLE);
            holder.userName.setText(getBillForTables.get(position).getFirstName() + " " + getBillForTables.get(position).getLastName());
            holder.mobileNumber.setText(getBillForTables.get(position).getMobileNo());
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, String.valueOf(existingOrders.get(position).getOrderId()), Toast.LENGTH_SHORT).show();
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment fragment = new BillDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("OrderId",getBillForTables.get(position).getOrderId());
                bundle.putString("TableName",getBillForTables.get(position).getTableName());
                bundle.putString("RegisterId",getBillForTables.get(position).getRegisterId().toString());
                bundle.putString("MobileNumber",getBillForTables.get(position).getMobileNo());
                bundle.putString("UserName",getBillForTables.get(position).getFirstName() +" "+ getBillForTables.get(position).getLastName());
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return getBillForTables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView tableImg,takeAwayImg;
        TextView tableNo,orderId,userName,mobileNumber,status;
        ImageView bookOrNot;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tableImg = itemView.findViewById(R.id.tableImg);
            takeAwayImg = itemView.findViewById(R.id.takeAwayImg);
            bookOrNot = itemView.findViewById(R.id.bookOrNot);
            tableNo = itemView.findViewById(R.id.tableNo);
            orderId = itemView.findViewById(R.id.orderId);
            userName = itemView.findViewById(R.id.userName);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);
            status = itemView.findViewById(R.id.status);
            relativeLayout = itemView.findViewById(R.id.layoutBg);
        }
    }
}
