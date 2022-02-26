package com.efficacious.e_smartsrestaurant.TakeAwayUser.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Billing.BillDetailFragment;
import com.efficacious.e_smartsrestaurant.Model.GetUserWiseTakeAwayOrder;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    List<GetUserWiseTakeAwayOrder> userWiseTakeAwayOrders;
    Context context;

    public OrderHistoryAdapter(List<GetUserWiseTakeAwayOrder> userWiseTakeAwayOrders) {
        this.userWiseTakeAwayOrders = userWiseTakeAwayOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.takeaway_order_history_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.mOrderId.setText("#" + userWiseTakeAwayOrders.get(position).getOrderId());
        holder.mDate.setText(userWiseTakeAwayOrders.get(position).getCreatedDate());
        String status = userWiseTakeAwayOrders.get(position).getStatus();

        if (status.equalsIgnoreCase(WebConstant.TAKEAWAY)){
            holder.mStatus.setText("Status : Request Pending");
        }else if (status.equalsIgnoreCase(WebConstant.KITCHEN_STATUS)){
            holder.mStatus.setText("Status : Accept");
        }else if (status.equalsIgnoreCase(WebConstant.DISPATCH_STATUS)){
            holder.mStatus.setText("Status : Dispatch Order");
        }else if (status.equalsIgnoreCase(WebConstant.BILL_STATUS)){
            holder.mStatus.setText("Status : Billing");
        }else if (status.equalsIgnoreCase(WebConstant.CLOSE_STATUS)){
            holder.mStatus.setText("Status : Close");
        }

        holder.mBill.setText("Total Bill : ₹" + userWiseTakeAwayOrders.get(position).getTotal().toString());
    }

    @Override
    public int getItemCount() {
        return userWiseTakeAwayOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mOrderId,mDate,mStatus,mBill;
        Button mBtnViewBill;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mOrderId = itemView.findViewById(R.id.orderId);
            mBill = itemView.findViewById(R.id.bill);
            mDate = itemView.findViewById(R.id.date);
            mStatus = itemView.findViewById(R.id.status);
            mBtnViewBill = itemView.findViewById(R.id.btnViewBill);

        }
    }
}
