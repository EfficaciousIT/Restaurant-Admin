package com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Model.MenuDetail;
import com.efficacious.e_smartsrestaurant.Model.MenuResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Adapter.FullViewMenuAdapter;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Adapter.MenuAdapter;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodViewFragment extends Fragment {

    CheckInternetConnection checkInternetConnection;
    List<MenuDetail> menuDetail;
    FullViewMenuAdapter fullViewMenuAdapter;
    RecyclerView recyclerView;
    String CategoryId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_view, container, false);

        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {

            //for recommended food
            CategoryId = getArguments().getString("CategoryId");
            menuDetail = new ArrayList<>();
            if (!CategoryId.isEmpty()){
                menuList(CategoryId);
            }
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        }

        return view;
    }

    private void menuList(String categoryId) {
        try {
            Call<MenuResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getMenu("CategoryDrp",categoryId,"1");

            call.enqueue(new Callback<MenuResponse>() {
                @Override
                public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                    if (response.isSuccessful()){
                        menuDetail = response.body().getMenuDetails();
                        fullViewMenuAdapter = new FullViewMenuAdapter(menuDetail);
                        recyclerView.setAdapter(fullViewMenuAdapter);

                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MenuResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error occur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}