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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Model.MenuDetail;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.MenuDatabase;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments.AddToCartFragmentFragment;

import java.util.List;

public class FullViewMenuAdapter extends  RecyclerView.Adapter<FullViewMenuAdapter.ViewHolder>{

    List<MenuDetail> menuDetail;
    Context context;
    String TableName,OrderId;
    MenuDatabase menuDatabase;
    int EmployeeId;
    SharedPreferences orderDataSharedPref,userDataSharedPref;
    CheckInternetConnection checkInternetConnection;
    public FullViewMenuAdapter(List<MenuDetail> menuDetail) {
        this.menuDetail = menuDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_full_menu_view,parent,false);
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

        categoryImg(holder.foodImg,categoryName);


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

    private void categoryImg(ImageView categoryImg, String categoryName) {
        if (categoryName.equalsIgnoreCase("South Indian")){
            categoryImg.setImageResource(R.drawable.south_indian);
        }else if (categoryName.equalsIgnoreCase("Maharashtrian")){
            categoryImg.setImageResource(R.drawable.maharashtrian);
        }else if (categoryName.equalsIgnoreCase("Pizzas")){
            categoryImg.setImageResource(R.drawable.pizza);
        }else if (categoryName.equalsIgnoreCase("Sandwich")){
            categoryImg.setImageResource(R.drawable.sandwich);
        }else if (categoryName.equalsIgnoreCase("Beverages")){
            categoryImg.setImageResource(R.drawable.beverages);
        }else if (categoryName.equalsIgnoreCase("Refreshers")){
            categoryImg.setImageResource(R.drawable.refreshers);
        }else if (categoryName.equalsIgnoreCase("Faloodas")){
            categoryImg.setImageResource(R.drawable.faloodas);
        }else if (categoryName.equalsIgnoreCase("Fresh-N-Juicy")){
            categoryImg.setImageResource(R.drawable.fresh_n_juicy);
        }else if (categoryName.equalsIgnoreCase("Shaken Up")){
            categoryImg.setImageResource(R.drawable.shaken_up);
        }else if (categoryName.equalsIgnoreCase("Salad/Raita/Papad")){
            categoryImg.setImageResource(R.drawable.salad_raita_papad);
        }else if (categoryName.equalsIgnoreCase("Soups")){
            categoryImg.setImageResource(R.drawable.soup);
        }else if (categoryName.equalsIgnoreCase("Starter")){
            categoryImg.setImageResource(R.drawable.starter);
        }else if (categoryName.equalsIgnoreCase("Main Course")){
            categoryImg.setImageResource(R.drawable.main_course);
        }else if (categoryName.equalsIgnoreCase("Chinese")){
            categoryImg.setImageResource(R.drawable.chinese);
        }else if (categoryName.equalsIgnoreCase("Rice")){
            categoryImg.setImageResource(R.drawable.rice);
        }else if (categoryName.equalsIgnoreCase("Continental")){
            categoryImg.setImageResource(R.drawable.continental);
        }else if (categoryName.equalsIgnoreCase("Ice Creams")){
            categoryImg.setImageResource(R.drawable.ice_cream);
        }else if (categoryName.equalsIgnoreCase("Dessert")){
            categoryImg.setImageResource(R.drawable.dessert);
        }else{
            categoryImg.setImageResource(R.drawable.pasta);
        }
    }

}
