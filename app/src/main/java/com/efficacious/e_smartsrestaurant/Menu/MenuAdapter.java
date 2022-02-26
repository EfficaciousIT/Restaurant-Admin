package com.efficacious.e_smartsrestaurant.Menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Model.MenuDetail;
import com.efficacious.e_smartsrestaurant.R;

import java.util.List;

public class MenuAdapter extends  RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    List<MenuDetail> menuDetail;
    Context context;
    public MenuAdapter(List<MenuDetail> menuDetail) {
        this.menuDetail = menuDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_view,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        Picasso.get().load(menuData[position].getFoodImg()).into(holder.foodImg);
        holder.foodName.setText(menuDetail.get(position).getMenuName());
        holder.foodPrice.setText("₹" + menuDetail.get(position).getPrice());

        holder.btnAdd.setVisibility(View.GONE);

        String menuType = menuDetail.get(position).getMenuType();
        if (menuType.equalsIgnoreCase("Veg")){
            holder.veg_nonVegImg.setImageResource(R.drawable.veg);
        }

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnAdded.setVisibility(View.VISIBLE);
                holder.btnAdd.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuDetail.size();
    }

    public void updateList(List<MenuDetail> temp) {
        menuDetail = temp;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImg;
        TextView foodName,foodPrice;
        ImageView veg_nonVegImg;
        Button btnAdd,btnAdded;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.foodImg);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            veg_nonVegImg = itemView.findViewById(R.id.vegImg);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnAdded = itemView.findViewById(R.id.btnAdded);
        }
    }

}
