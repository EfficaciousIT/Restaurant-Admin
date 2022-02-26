package com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Model.GetUserWiseTakeAwayOrder;
import com.efficacious.e_smartsrestaurant.Model.GetUserWiseTakeAwayOrderResponse;
import com.efficacious.e_smartsrestaurant.Model.MenuDetail;
import com.efficacious.e_smartsrestaurant.Model.MenuResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Adapter.FullViewMenuAdapter;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Adapter.OrderHistoryAdapter;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    CheckInternetConnection checkInternetConnection;
    List<GetUserWiseTakeAwayOrder> userWiseTakeAwayOrders;
    OrderHistoryAdapter orderHistoryAdapter;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        sharedPreferences = getContext().getSharedPreferences(WebConstant.FIREBASE_LOGIN_DATA,0);
        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {

            userWiseTakeAwayOrders = new ArrayList<>();
            historyData();
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        return view;
    }

    private void historyData() {
        try {
            String RegisterId = String.valueOf(sharedPreferences.getInt("RegisterId",0));
            Call<GetUserWiseTakeAwayOrderResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getOrderHistory("select","1",RegisterId);

            call.enqueue(new Callback<GetUserWiseTakeAwayOrderResponse>() {
                @Override
                public void onResponse(Call<GetUserWiseTakeAwayOrderResponse> call, Response<GetUserWiseTakeAwayOrderResponse> response) {
                    if (response.isSuccessful()){
                        userWiseTakeAwayOrders = response.body().getGetUserWiseTakeAwayOrder();
                        orderHistoryAdapter = new OrderHistoryAdapter(userWiseTakeAwayOrders);
                        recyclerView.setAdapter(orderHistoryAdapter);

                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetUserWiseTakeAwayOrderResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error occur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}