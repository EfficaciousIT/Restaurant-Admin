package com.efficacious.e_smartsrestaurant.NewOrder;

import static com.airbnb.lottie.L.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Model.TableStatusDetail;
import com.efficacious.e_smartsrestaurant.Model.TakeOrderDetail;
import com.efficacious.e_smartsrestaurant.Model.GetOrderId;
import com.efficacious.e_smartsrestaurant.Model.TakeOrderIdResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.MenuDatabase;
import com.efficacious.e_smartsrestaurant.Room.TableBook;
import com.efficacious.e_smartsrestaurant.Room.TableBookDatabase;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrderAdapter extends  RecyclerView.Adapter<NewOrderAdapter.ViewHolder>{

    List<TableStatusDetail> tableStatusDetail;
    Context context;
    int countNo=2;
    String tableName;
    int EmployeeId;
    int ResId;

    SharedPreferences sharedPreferences,sharedPreferences2;
    CheckInternetConnection checkInternetConnection;
    TableBookDatabase tableBookDatabase;

    private static String SHARED_PREF_NAME = "OrderData";
    SharedPreferences.Editor editor;

    public NewOrderAdapter(List<TableStatusDetail> tableStatusDetail) {
        this.tableStatusDetail = tableStatusDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_view,parent,false);
        context = parent.getContext();
        sharedPreferences2 = context.getSharedPreferences(SHARED_PREF_NAME,0);
        checkInternetConnection = new CheckInternetConnection(context);
        sharedPreferences = context.getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        editor = sharedPreferences2.edit();
        EmployeeId = sharedPreferences.getInt(WebConstant.EMPLOYEE_ID,0);
        ResId = sharedPreferences.getInt(WebConstant.RESTAURANT_ID,0);
        setUpDB();
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tableNo.setText(tableStatusDetail.get(position).getTableName());
        String status = tableStatusDetail.get(position).getTableStatus();
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equalsIgnoreCase("free")){
                    tableName = tableStatusDetail.get(position).getTableName();
                    showDialog(tableName);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Sorry,Table Occupied");
                    builder.setNegativeButton("Close",null);
                    builder.show();
                }
            }
        });

        if (status.equalsIgnoreCase("free")){
            holder.bookOrNot.setImageResource(R.drawable.available);
            holder.tableImg.setImageResource(R.drawable.empty_table);
        }
    }

    @Override
    public int getItemCount() {
        return tableStatusDetail.size();
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

    private void showDialog(String tableName) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.select_table_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                ImageView btnCancel = dialog.findViewById(R.id.btnCancel);
                ImageView tableImg = dialog.findViewById(R.id.tableImg);
                TextView btnAdd = dialog.findViewById(R.id.btnAdd);
                TextView table = dialog.findViewById(R.id.text);
                ImageView btnMinus = dialog.findViewById(R.id.btnMinus);
                Button btnBookTable = dialog.findViewById(R.id.btnBookTable);

                table.setText("Book a " + tableName);

                btnBookTable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!checkInternetConnection.isConnectingToInternet()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Internet not Available !!");
                            builder.setNegativeButton("Close", null);
                            builder.show();
                        }else {
                            String val = "0";
                            editor.putString("OrderId",val);
                            editor.commit();
                            TakeOrderDetail takeOrderDetail = new TakeOrderDetail(tableName,1,EmployeeId,null,1,countNo,ResId,null,"No","kitchen",String.valueOf(System.currentTimeMillis()));
                            Log.d(TAG, "onClick: " + tableName);

                            TableBook tableBook = new TableBook(tableName,1,EmployeeId,null,1,countNo,ResId,null,"No", WebConstant.KITCHEN_STATUS);

                            tableBookDatabase.dao_tableBook().tableBookData(tableBook);

                            Fragment fragment = new SelectMenuCategoryFragment();
                            AppCompatActivity activity = (AppCompatActivity) context;
                            activity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container,fragment)
                                    .addToBackStack(null)
                                    .commit();
                            dialog.dismiss();

                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        countNo=2;
                    }
                });

                btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (countNo==0){
                            Toast.makeText(context, "First add people.", Toast.LENGTH_SHORT).show();
                        }else if (countNo>1){
                            countNo--;
                            btnAdd.setText(String.valueOf(countNo));
                            showTable(countNo,tableImg,null);
                        }
                    }
                });

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (countNo<8){
                            countNo++;
                            btnAdd.setText(String.valueOf(countNo));
                            showTable(countNo, tableImg,null);
                        }else {
//                            Toast.makeText(context, "You have already choose 8 people", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void setUpDB(){
        tableBookDatabase = Room.databaseBuilder(context, TableBookDatabase.class,"TableBook")
                .allowMainThreadQueries().build();
    }

}
