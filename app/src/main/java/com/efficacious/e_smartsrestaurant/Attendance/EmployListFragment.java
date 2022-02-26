package com.efficacious.e_smartsrestaurant.Attendance;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Dashboard.DashboardFragment;
import com.efficacious.e_smartsrestaurant.Menu.MenuCategoryAdapter;
import com.efficacious.e_smartsrestaurant.Model.CategoryResponse;
import com.efficacious.e_smartsrestaurant.Model.EmployeeDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.GetEmployeeDetail;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployListFragment extends Fragment {

    EmployListAdapter employListAdapter;
    List<GetEmployeeDetail> getEmployeeDetails;
    CheckInternetConnection checkInternetConnection;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employ_list, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Employ List");

        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {
            getEmployeeDetails = new ArrayList<>();
            employList();
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }


        return view;
    }

    private void employList() {
        try {
            Call<EmployeeDetailsResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getEmployDetails("select","1");

            call.enqueue(new Callback<EmployeeDetailsResponse>() {
                @Override
                public void onResponse(Call<EmployeeDetailsResponse> call, Response<EmployeeDetailsResponse> response) {
                    if (response.isSuccessful()){
                        getEmployeeDetails = response.body().getGetEmployeeDetails();
                        employListAdapter = new EmployListAdapter(getEmployeeDetails);
                        recyclerView.setAdapter(employListAdapter);
                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EmployeeDetailsResponse> call, Throwable t) {

                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment())
                        .addToBackStack(null).commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}