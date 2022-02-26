package com.efficacious.e_smartsrestaurant.Billing;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
import com.efficacious.e_smartsrestaurant.ExistingOrder.ExistingTableStatusAdapter;
import com.efficacious.e_smartsrestaurant.Model.ExistingOrderResponse;
import com.efficacious.e_smartsrestaurant.Model.GetBillForTable;
import com.efficacious.e_smartsrestaurant.Model.GetBillForTableResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillTableStatusFragment extends Fragment {

    List<GetBillForTable> getBillForTables;
    BillTableAdapter billTableAdapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    LottieAnimationView lottieAnimationView;
    SharedPreferences sharedPreferences;
    LottieAnimationView emptyState,error;
    TextView emptyTxt;
    int employeeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_table_status, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Bill");
        sharedPreferences = getContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);

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
            getBillForTables = new ArrayList<>();
            tableStatus();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        return view;
    }

    private void tableStatus() {
        try {
            Call<GetBillForTableResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getTableBill("selectTableForBill","1");

            call.enqueue(new Callback<GetBillForTableResponse>() {
                @Override
                public void onResponse(Call<GetBillForTableResponse> call, Response<GetBillForTableResponse> response) {
                    if (response.isSuccessful()){
                        getBillForTables = response.body().getGetBillForTable();
                        billTableAdapter = new BillTableAdapter(getBillForTables);
                        recyclerView.setAdapter(billTableAdapter);
                        int size = getBillForTables.size();
                        if (size==0){
                            emptyState.setVisibility(View.VISIBLE);
                            emptyTxt.setVisibility(View.VISIBLE);
                        }
                    }else {
                        error.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<GetBillForTableResponse> call, Throwable t) {
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