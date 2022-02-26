package com.efficacious.e_smartsrestaurant.NewOrder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.HomeFragment;
import com.efficacious.e_smartsrestaurant.Dashboard.DashboardFragment;
import com.efficacious.e_smartsrestaurant.Model.TableStatusDetail;
import com.efficacious.e_smartsrestaurant.Model.TableStatusResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrderFragment extends Fragment {

    List<TableStatusDetail> tableStatusDetails;
    NewOrderAdapter newOrderAdapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_order, container, false);
        checkInternetConnection = new CheckInternetConnection(getContext());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("New Order");
        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            tableStatusDetails = new ArrayList<>();
            tableStatus();
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }


        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment())
                .addToBackStack(null).commit();
            }
        });


        return view;
    }

    private void tableStatus() {
        try {
            Call<TableStatusResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getTableStatus("selectAllTAble","1");

            call.enqueue(new Callback<TableStatusResponse>() {
                @Override
                public void onResponse(Call<TableStatusResponse> call, Response<TableStatusResponse> response) {
                    if (response.isSuccessful()){
                        tableStatusDetails = response.body().getTableStatusDetails();
                        newOrderAdapter = new NewOrderAdapter(tableStatusDetails);
                        recyclerView.setAdapter(newOrderAdapter);
                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TableStatusResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment())
//                        .disallowAddToBackStack().commit();
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
//    }
}