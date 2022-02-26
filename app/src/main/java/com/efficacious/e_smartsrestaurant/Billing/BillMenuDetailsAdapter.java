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
import com.efficacious.e_smartsrestaurant.Model.GetExistingOrderDetail;
import com.efficacious.e_smartsrestaurant.R;

import java.util.List;

public class BillMenuDetailsAdapter extends RecyclerView.Adapter<BillMenuDetailsAdapter.ViewHolder> {

    List<GetBillDetail> getBillDetails;
    Context context;

    public BillMenuDetailsAdapter(List<GetBillDetail> getBillDetails) {
        this.getBillDetails = getBillDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_menu_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.foodQty.setText(getBillDetails.get(position).getQty() + " QTY");
        holder.foodName.setText(getBillDetails.get(position).getMenuName());
        holder.foodPrice.setText(String.valueOf(getBillDetails.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return getBillDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImg;
        TextView foodName,foodQty,foodPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImg = itemView.findViewById(R.id.foodImg);
            foodName = itemView.findViewById(R.id.foodName);
            foodQty = itemView.findViewById(R.id.qyt);
            foodPrice = itemView.findViewById(R.id.foodPrice);

        }
    }
}
