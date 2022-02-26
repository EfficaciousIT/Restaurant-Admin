package com.efficacious.e_smartsrestaurant.NewOrder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Model.MenuDetail;
import com.efficacious.e_smartsrestaurant.Model.OrderDetails;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.MenuData;
import com.efficacious.e_smartsrestaurant.Room.MenuDatabase;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectMenuAdapter extends  RecyclerView.Adapter<SelectMenuAdapter.ViewHolder>{

    List<MenuDetail> menuDetail;
    Context context;
    String TableName,OrderId;
    MenuDatabase menuDatabase;
    int EmployeeId;
    SharedPreferences orderDataSharedPref,userDataSharedPref;
    CheckInternetConnection checkInternetConnection;
    OnCartCountListener onCartCountListener;

    public SelectMenuAdapter(List<MenuDetail> menuDetail, OnCartCountListener onCartCountListener) {
        this.menuDetail = menuDetail;
        this.onCartCountListener = onCartCountListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_view,parent,false);
        context = parent.getContext();
        orderDataSharedPref = context.getSharedPreferences("OrderData",0);
        userDataSharedPref = context.getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        checkInternetConnection = new CheckInternetConnection(context);

        TableName = orderDataSharedPref.getString("TableName",null);
        OrderId = orderDataSharedPref.getString("OrderId",null);

        EmployeeId = userDataSharedPref.getInt(WebConstant.EMPLOYEE_ID,0);

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



        String menuType = menuDetail.get(position).getMenuType();
        if (menuType.equalsIgnoreCase("Veg")){
            holder.veg_nonVegImg.setImageResource(R.drawable.veg);
        }

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add to cart successfully !!");
                builder.setMessage("continue to order..");
                builder.setNegativeButton("Add more food",null);
                builder.setPositiveButton("Open Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppCompatActivity activity = (AppCompatActivity) context;
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,new CartFragment())
                                .addToBackStack(null).commit();
                    }
                });
                builder.show();

                MenuData menuData = new MenuData(Integer.valueOf(OrderId),categoryName,
                        menuName,TableName,1,EmployeeId,menuDetail.get(position).getPrice(),
                        1,null,WebConstant.KITCHEN_STATUS, WebConstant.KITCHEN_STATUS);
                menuDatabase.dao().menuDataList(menuData);
//                Toast.makeText(context, "Add to cart", Toast.LENGTH_SHORT).show();
                menuDetail.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,menuDetail.size());
                List<MenuData> Data = menuDatabase.dao().getMenuListData();
                int badgeCount = Data.size();
                onCartCountListener.onAddToCart(badgeCount);
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
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.foodImg);
            foodName = itemView.findViewById(R.id.foodName);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            veg_nonVegImg = itemView.findViewById(R.id.vegImg);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnAdded = itemView.findViewById(R.id.btnAdded);
            progressBar = itemView.findViewById(R.id.loader);
        }
    }

    private void setUpDB(){
        menuDatabase = Room.databaseBuilder(context,MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
    }

}
