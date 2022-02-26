package com.efficacious.e_smartsrestaurant.TakeAway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efficacious.e_smartsrestaurant.Model.GetUserDetailResponse;
import com.efficacious.e_smartsrestaurant.Model.GetUserDetails;
import com.efficacious.e_smartsrestaurant.Model.TakeAwayOrderData;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeAwayHistoryOrderAdapter extends RecyclerView.Adapter<TakeAwayHistoryOrderAdapter.Holder>{

    List<TakeAwayOrderData> takeAwayOrderData;
    Context context;
    List<GetUserDetails> getUserDetails;

    public TakeAwayHistoryOrderAdapter(List<TakeAwayOrderData> takeAwayOrderData) {
        this.takeAwayOrderData = takeAwayOrderData;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_takeaway_order_view,parent,false);
        context =  parent.getContext();
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.status.setText(takeAwayOrderData.get(position).getStatus());
        holder.totalAmount.setText("₹" + String.valueOf(Math.round(Float.parseFloat(takeAwayOrderData.get(position).getTotalBill()))));
        holder.mobileNumber.setText(takeAwayOrderData.get(position).getMobileNumber());
        holder.userName.setText(takeAwayOrderData.get(position).getUserName());
        try {
            Call<GetUserDetailResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getUserDetails("select","1",takeAwayOrderData.get(position).getMobileNumber());
            call.enqueue(new Callback<GetUserDetailResponse>() {
                @Override
                public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                    if (response.isSuccessful()){
                        getUserDetails = response.body().getGetUserDetails();
                        String Address = getUserDetails.get(0).getAddress1() + ", " + getUserDetails.get(0).getAddress2() + ", " + getUserDetails.get(0).getAddress3();
                        holder.deliveryAddress.setText(Address);
                    }
                }

                @Override
                public void onFailure(Call<GetUserDetailResponse> call, Throwable t) {
                    Toast.makeText(context, "Api Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(context, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return takeAwayOrderData.size();
    }


    protected class Holder extends RecyclerView.ViewHolder {

        CircleImageView profileImg;
        TextView userName,mobileNumber,status;
        TextView deliveryAddress;
        TextView totalAmount;
        Button btnAccept;

        public Holder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profileImg);
            userName = itemView.findViewById(R.id.userName);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);
            deliveryAddress = itemView.findViewById(R.id.deliveryAddress);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            btnAccept = itemView.findViewById(R.id.btnViewOrder);
            status = itemView.findViewById(R.id.status);
        }
    }
}
