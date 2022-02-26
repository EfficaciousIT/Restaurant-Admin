package com.efficacious.e_smartsrestaurant.ExistingOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Model.GetExistingOrderDetail;
import com.efficacious.e_smartsrestaurant.R;

import java.util.List;

public class ExistingOrderMenuDeatilsAdapter extends RecyclerView.Adapter<ExistingOrderMenuDeatilsAdapter.ViewHolder> {

    List<GetExistingOrderDetail> getExistingOrderDetails;
    Context context;

    public ExistingOrderMenuDeatilsAdapter(List<GetExistingOrderDetail> getExistingOrderDetails) {
        this.getExistingOrderDetails = getExistingOrderDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.existing_order_menu_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.foodQty.setText(getExistingOrderDetails.get(position).getQty() + " QTY");
        holder.foodName.setText(getExistingOrderDetails.get(position).getMenuName());

    }

    @Override
    public int getItemCount() {
        return getExistingOrderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImg;
        TextView foodName,foodQty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImg = itemView.findViewById(R.id.foodImg);
            foodName = itemView.findViewById(R.id.foodName);
            foodQty = itemView.findViewById(R.id.qyt);

        }
    }
}
