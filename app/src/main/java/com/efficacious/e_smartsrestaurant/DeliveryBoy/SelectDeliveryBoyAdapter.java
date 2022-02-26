package com.efficacious.e_smartsrestaurant.DeliveryBoy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Billing.BillTableStatusFragment;
import com.efficacious.e_smartsrestaurant.Billing.TotalBillFragment;
import com.efficacious.e_smartsrestaurant.Dashboard.DashboardFragment;
import com.efficacious.e_smartsrestaurant.Model.BillUpdateDetails;
import com.efficacious.e_smartsrestaurant.Model.DeliveryBoyData;
import com.efficacious.e_smartsrestaurant.Notification.APIService;
import com.efficacious.e_smartsrestaurant.Notification.Client;
import com.efficacious.e_smartsrestaurant.Notification.Data;
import com.efficacious.e_smartsrestaurant.Notification.NotificationSender;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectDeliveryBoyAdapter extends RecyclerView.Adapter<SelectDeliveryBoyAdapter.ViewHolder>{

    List<DeliveryBoyData> deliveryBoyData;
    FirebaseFirestore firebaseFirestore;
    Context context;

    public SelectDeliveryBoyAdapter(List<DeliveryBoyData> deliveryBoyData) {
        this.deliveryBoyData = deliveryBoyData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_boy_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.deliveryBoyName.setText(deliveryBoyData.get(position).getName());
        holder.mobileNumber.setText(deliveryBoyData.get(position).getMobileNumber());
        String status = deliveryBoyData.get(position).getStatus();
        if (!TextUtils.isEmpty(status)){
            if (status.equalsIgnoreCase("true")){
                holder.status.setTextColor(Color.GREEN);
                holder.status.setText("Online");

            }else {
                holder.status.setText("Offline");
            }
        }else {
            holder.status.setText("Offline");
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equalsIgnoreCase("true")){
                    holder.status.setTextColor(Color.GREEN);
                    holder.status.setText("Online");

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Are you are to shift order ?");
                    builder.setMessage("this order to assign delivery boy, they will be delivered order and notify you.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Assign", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            try {
                                BillUpdateDetails billUpdateDetails = new BillUpdateDetails(Integer.parseInt(deliveryBoyData.get(position).AttachOrderId),
                                        WebConstant.REQUEST,
                                        Math.round(Float.parseFloat(deliveryBoyData.get(position).TotalBill)));

                                Call<ResponseBody> call = RetrofitClient
                                        .getInstance()
                                        .getApi()
                                        .updateBillDetails("update",billUpdateDetails);

                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()){
                                            HashMap<String,Object> map = new HashMap<>();
                                            map.put("OrderId",deliveryBoyData.get(position).AttachOrderId);
                                            map.put("TotalBill",deliveryBoyData.get(position).TotalBill);
                                            map.put("MobileNumber",deliveryBoyData.get(position).MobileNo);
                                            map.put("UserName",deliveryBoyData.get(position).UserName);
                                            map.put("RegisterId",deliveryBoyData.get(position).RegisterId);
                                            map.put("DeliveryBoyId",deliveryBoyData.get(position).getMobileNumber());
                                            map.put("TimeStamp",System.currentTimeMillis());
                                            map.put("Status",WebConstant.REQUEST);

                                            firebaseFirestore.collection("DeliveryBoy").document(deliveryBoyData.get(position).getMobileNumber())
                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        String token = task.getResult().getString("FCMToken");
                                                        String title = "New task request !!";
                                                        String msg = "Tap to view";
                                                        String flag = WebConstant.DELIVERY_BOY;
                                                        sendNotification(token,title,msg,flag);
                                                    }
                                                }
                                            });

                                            firebaseFirestore.collection("Orders")
                                                    .document(deliveryBoyData.get(position).AttachOrderId).set(map)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(context, "Task assign to delivery boy", Toast.LENGTH_SHORT).show();
                                                                AppCompatActivity activity = (AppCompatActivity) context;
                                                                activity.getSupportFragmentManager().beginTransaction()
                                                                        .replace(R.id.fragment_container,new DashboardFragment())
                                                                        .commit();
                                                            }
                                                        }
                                                    });

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(context, "Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }catch (Exception e){
                                Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel",null);
                    builder.show();

                }else {
                    holder.status.setText("Offline");
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delivery boy offline now !!");
                    builder.setMessage("If delivery boy online then notify you.");
                    builder.setNegativeButton("Ok",null);
                    builder.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return deliveryBoyData.size();
    }

    private void sendNotification(String token, String title, String msg,String flag) {
        Data data = new Data(title,msg,flag);
        NotificationSender notificationSender = new NotificationSender(data,token);

        APIService apiService = Client.getRetrofit().create(APIService.class);

        apiService.sendNotification(notificationSender).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImg;
        TextView deliveryBoyName,status,mobileNumber;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profileImg);
            deliveryBoyName = itemView.findViewById(R.id.deliveryBoyName);
            status = itemView.findViewById(R.id.status);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
