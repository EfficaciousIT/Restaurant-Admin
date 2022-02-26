package com.efficacious.e_smartsrestaurant.ExistingOrder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Base.HomeFragment;
import com.efficacious.e_smartsrestaurant.Model.MenuDetail;
import com.efficacious.e_smartsrestaurant.Model.OrderDetails;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExistingOrderMenuAdapter extends  RecyclerView.Adapter<ExistingOrderMenuAdapter.ViewHolder>{

    List<MenuDetail> menuDetail;
    Context context;
    int countNo=1;
    SharedPreferences orderIdDataSharedPref,userDataSharedPref;
    int OrderId,EmployeeId;
    String TableName;
    private SharedPreferences.Editor editor;
    public ExistingOrderMenuAdapter(List<MenuDetail> menuDetail) {
        this.menuDetail = menuDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_view,parent,false);
        context = parent.getContext();
        orderIdDataSharedPref = context.getSharedPreferences(WebConstant.ORDER_ID_DATA_SHARED_PREF,0);
        userDataSharedPref = context.getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        OrderId = orderIdDataSharedPref.getInt(WebConstant.ORDER_ID,0);
        TableName = orderIdDataSharedPref.getString(WebConstant.TABLE_NAME,null);
        EmployeeId = userDataSharedPref.getInt(WebConstant.EMPLOYEE_ID,0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        Picasso.get().load(menuData[position].getFoodImg()).into(holder.foodImg);
        holder.foodName.setText(menuDetail.get(position).getMenuName());
        holder.foodPrice.setText("₹" + menuDetail.get(position).getPrice());

        String menuType = menuDetail.get(position).getMenuType();
        if (menuType.equalsIgnoreCase("Veg")){
            holder.veg_nonVegImg.setImageResource(R.drawable.veg);
        }

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.select_qty_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Button btnCancel = dialog.findViewById(R.id.btnCancel);
                TextView btnAdd = dialog.findViewById(R.id.btnAdd);
                ImageView btnMinus = dialog.findViewById(R.id.btnMinus);
                Button btnAddMenu = dialog.findViewById(R.id.btnAddMenu);

                btnAddMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OrderDetails orderDetailsData = new OrderDetails(OrderId,menuDetail.get(position).getCatName(),
                                menuDetail.get(position).getMenuName(),TableName,
                                1,EmployeeId,menuDetail.get(position).getPrice(),countNo
                                ,null,"kitchen","kitchen",String.valueOf(System.currentTimeMillis()));

                        try {
                            Call<ResponseBody> call = RetrofitClient
                                    .getInstance()
                                    .getApi()
                                    .getOrderDetails("insert",orderDetailsData);

                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Added successfully !!");
                                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialog.dismiss();
                                            AppCompatActivity activity = (AppCompatActivity) context;
                                            orderIdDataSharedPref.edit().clear();
//                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ExistingOrderFragment()).disallowAddToBackStack()
//                                                    .commit();
                                        }
                                    });
                                    builder.show();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(context, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){ }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (countNo==0){
                            Toast.makeText(context, "select at least one item", Toast.LENGTH_SHORT).show();
                        }else if (countNo>1){
                            countNo--;
                            btnAdd.setText(String.valueOf(countNo));
                        }
                    }
                });

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (countNo<50){
                            countNo++;
                            btnAdd.setText(String.valueOf(countNo));
                        }else {
//                            Toast.makeText(context, "You have already choose 8 people", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
