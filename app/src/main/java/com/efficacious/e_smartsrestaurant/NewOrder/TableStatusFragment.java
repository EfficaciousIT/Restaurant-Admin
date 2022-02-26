package com.efficacious.e_smartsrestaurant.NewOrder;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Model.TableStatusDetail;
import com.efficacious.e_smartsrestaurant.Model.TableStatusResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableStatusFragment extends Fragment {

    List<TableStatusDetail> tableStatusDetails;
    TableStatusAdapter tableStatusAdapter;

    RecyclerView recyclerView;
    LottieAnimationView emptyState,error;
    TextView emptyTxt;
    CheckInternetConnection checkInternetConnection;
    LottieAnimationView lottieAnimationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_status, container, false);



        recyclerView = view.findViewById(R.id.recyclerView);
        lottieAnimationView = view.findViewById(R.id.error404);
        checkInternetConnection = new CheckInternetConnection(getContext());
        emptyState = view.findViewById(R.id.emptyState);
        emptyTxt = view.findViewById(R.id.emptyTxt);
        error = view.findViewById(R.id.error404);

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            tableStatusDetails = new ArrayList<>();
            tableStatus();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }

        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
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
                        tableStatusAdapter = new TableStatusAdapter(tableStatusDetails);
                        recyclerView.setAdapter(tableStatusAdapter);
                        lottieAnimationView.setVisibility(View.GONE);
                        int size = tableStatusDetails.size();
                        if (size==0){
                            emptyState.setVisibility(View.VISIBLE);
                            emptyTxt.setVisibility(View.VISIBLE);
                        }
                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                        error.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<TableStatusResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    error.setVisibility(View.VISIBLE);
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            error.setVisibility(View.VISIBLE);
        }
    }
}