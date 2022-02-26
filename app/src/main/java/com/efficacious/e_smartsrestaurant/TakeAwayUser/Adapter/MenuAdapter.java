package com.efficacious.e_smartsrestaurant.TakeAwayUser.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Model.MenuDetail;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.MenuData;
import com.efficacious.e_smartsrestaurant.Room.MenuDatabase;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments.AddToCartFragmentFragment;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.List;

public class MenuAdapter extends  RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    List<MenuDetail> menuDetail;
    Context context;
    String TableName,OrderId;
    MenuDatabase menuDatabase;
    int EmployeeId;
    SharedPreferences orderDataSharedPref,userDataSharedPref;
    CheckInternetConnection checkInternetConnection;
    public MenuAdapter(List<MenuDetail> menuDetail) {
        this.menuDetail = menuDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_menu_view,parent,false);
        context = parent.getContext();
        orderDataSharedPref = context.getSharedPreferences("OrderData",0);
        userDataSharedPref = context.getSharedPreferences("UserData",0);
        checkInternetConnection = new CheckInternetConnection(context);

        TableName = orderDataSharedPref.getString("TableName",null);
        OrderId = orderDataSharedPref.getString("OrderId",null);

        EmployeeId = userDataSharedPref.getInt("Employee_Id",0);

        setUpDB();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        Picasso.get().load(menuData[position].getFoodImg()).into(holder.foodImg);
        holder.foodName.setText(menuDetail.get(position).getMenuName());
        holder.foodPrice.setText("₹" + menuDetail.get(position).getPrice());
        String categoryName = menuDetail.get(position).getCatName();
        String menuName = menuDetail.get(position).getMenuName();
        Integer price = menuDetail.get(position).getPrice();
        Integer resId = menuDetail.get(position).getResId();

        String menuType = menuDetail.get(position).getMenuType();
        if (menuType.equalsIgnoreCase("Veg")){
            holder.veg_nonVegImg.setImageResource(R.drawable.veg);
        }

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("MenuName",menuName);
                bundle.putString("Category",categoryName);
                bundle.putString("Price", String.valueOf(price));
                bundle.putInt("ResId",resId);

                Fragment fragment = new AddToCartFragmentFragment();
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null).commit();
            }
        });


    }

    @Override
    public int getItemCount() {
        return menuDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImg;
        TextView foodName,foodPrice;
        ImageView veg_nonVegImg;
        RelativeLayout btnAdd;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.foodImg);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            veg_nonVegImg = itemView.findViewById(R.id.vegImg);
            btnAdd = itemView.findViewById(R.id.relativeLayout);
        }
    }

    private void setUpDB(){
        menuDatabase = Room.databaseBuilder(context,MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
    }

}
