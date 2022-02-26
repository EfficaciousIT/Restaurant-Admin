package com.efficacious.e_smartsrestaurant.Kitchen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.ExistingOrder.ViewOrderFragment;
import com.efficacious.e_smartsrestaurant.Model.KitchenNewOrderDetail;
import com.efficacious.e_smartsrestaurant.R;

import java.util.List;

public class KitchenNewOrderAdapter extends RecyclerView.Adapter<KitchenNewOrderAdapter.ViewHolder>{

    List<KitchenNewOrderDetail> kitchenNewOrderDetails;
    Context context;

    public KitchenNewOrderAdapter(List<KitchenNewOrderDetail> kitchenNewOrderDetails) {
        this.kitchenNewOrderDetails = kitchenNewOrderDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_order_kitchen,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.orderId.setText("#" + kitchenNewOrderDetails.get(position).getOrderId() + "      "  +kitchenNewOrderDetails.get(position).getTableName());
        holder.btnViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment fragment = new KitchenViewOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("OrderId",kitchenNewOrderDetails.get(position).getOrderId());
                bundle.putString("OrderType",kitchenNewOrderDetails.get(position).getTableName());
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
        return kitchenNewOrderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderId;
        Button btnViewOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            btnViewOrder = itemView.findViewById(R.id.btnViewOrder);
        }
    }
}
