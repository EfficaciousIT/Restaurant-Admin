package com.efficacious.e_smartsrestaurant.Billing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Model.GetBillDetail;
import com.efficacious.e_smartsrestaurant.R;

import java.util.List;

public class BillDetailsAdapter extends RecyclerView.Adapter<BillDetailsAdapter.ViewHolder> {

    List<GetBillDetail> getBillDetail;
    Context context;

    public BillDetailsAdapter(List<GetBillDetail> getBillDetail) {
        this.getBillDetail = getBillDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_menu_detail_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.qyt.setText(getBillDetail.get(position).getQty().toString());
        holder.foodName.setText(getBillDetail.get(position).getMenuName());
        holder.foodPrice.setText("₹ " + String.valueOf(getBillDetail.get(position).getPrice()));
        float total = getBillDetail.get(position).getPrice() * getBillDetail.get(position).getQty();
        holder.totalPrice.setText("₹ " + String.valueOf(total) );

    }

    @Override
    public int getItemCount() {
        return getBillDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView foodName,qyt,foodPrice,totalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            qyt = itemView.findViewById(R.id.qyt);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            totalPrice = itemView.findViewById(R.id.foodTotalPrice);

        }
    }
}
