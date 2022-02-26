package com.efficacious.e_smartsrestaurant.ExistingOrder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Model.ExistingOrder;
import com.efficacious.e_smartsrestaurant.Model.KitchenNewOrderDetail;
import com.efficacious.e_smartsrestaurant.Model.TableStatusDetail;
import com.efficacious.e_smartsrestaurant.R;

import java.util.List;

public class ExistingTableStatusAdapter extends RecyclerView.Adapter<ExistingTableStatusAdapter.ViewHolder> {

    List<ExistingOrder> existingOrders;
    Context context;

    public ExistingTableStatusAdapter(List<ExistingOrder> existingOrders) {
        this.existingOrders = existingOrders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tableNo.setText(existingOrders.get(position).getTableName());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, String.valueOf(existingOrders.get(position).getOrderId()), Toast.LENGTH_SHORT).show();
                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment fragment = new ViewOrderFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("OrderId",existingOrders.get(position).getOrderId());
                bundle.putString("TableName",existingOrders.get(position).getTableName());
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        showTable(existingOrders.get(position).getPersonCont(),holder.tableImg, holder.bookOrNot);

    }

    @Override
    public int getItemCount() {
        return existingOrders.size();
    }

    private void showTable(int countNo, ImageView imageView, ImageView bookOrNot) {

        if (countNo==2 || countNo==1){
            imageView.setImageResource(R.drawable.two_chair);
        }else if(countNo==3){
            imageView.setImageResource(R.drawable.three_chair);
        }else if(countNo==4){
            imageView.setImageResource(R.drawable.four_chair);
        }else if(countNo==5){
            imageView.setImageResource(R.drawable.five_chair);
        }else if(countNo==6){
            imageView.setImageResource(R.drawable.six_chair);
        }else if(countNo==7){
            imageView.setImageResource(R.drawable.seven_chair);
        }else if(countNo==8){
            imageView.setImageResource(R.drawable.eight_chair);
        }else if (countNo==0){
            imageView.setImageResource(R.drawable.empty_table);
            bookOrNot.setImageResource(R.drawable.available);
        }
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
