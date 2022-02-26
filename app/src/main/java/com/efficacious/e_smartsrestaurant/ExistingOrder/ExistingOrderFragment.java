package com.efficacious.e_smartsrestaurant.ExistingOrder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.HomeFragment;
import com.efficacious.e_smartsrestaurant.Base.SharedPrefManger;
import com.efficacious.e_smartsrestaurant.Dashboard.DashboardFragment;
import com.efficacious.e_smartsrestaurant.Kitchen.KitchenNewOrderAdapter;
import com.efficacious.e_smartsrestaurant.Model.ExistingOrder;
import com.efficacious.e_smartsrestaurant.Model.ExistingOrderResponse;
import com.efficacious.e_smartsrestaurant.Model.KitchenNewOrderDetail;
import com.efficacious.e_smartsrestaurant.Model.KitchenNewOrderResponse;
import com.efficacious.e_smartsrestaurant.Model.TableStatusDetail;
import com.efficacious.e_smartsrestaurant.Model.TableStatusResponse;
import com.efficacious.e_smartsrestaurant.NewOrder.TableStatusAdapter;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExistingOrderFragment extends Fragment {

    List<ExistingOrder> existingOrders;
    ExistingTableStatusAdapter existingTableStatusAdapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    LottieAnimationView emptyState,error;
    TextView emptyTxt;
    SharedPreferences sharedPreferences;
    int employeeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_existing_order, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Existing Order");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

        sharedPreferences = getContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        employeeId = sharedPreferences.getInt(WebConstant.EMPLOYEE_ID,0);

        recyclerView = view.findViewById(R.id.recyclerView);
        emptyState = view.findViewById(R.id.emptyState);
        emptyTxt = view.findViewById(R.id.emptyTxt);
        error = view.findViewById(R.id.error404);
        checkInternetConnection = new CheckInternetConnection(getContext());

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            existingOrders = new ArrayList<>();
            tableStatus();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }

        return view;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment())
//                        .disallowAddToBackStack().commit();
//
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
//    }

    private void tableStatus() {
        try {
            Call<ExistingOrderResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getExistingOrder("select","1", String.valueOf(employeeId));

            call.enqueue(new Callback<ExistingOrderResponse>() {
                @Override
                public void onResponse(Call<ExistingOrderResponse> call, Response<ExistingOrderResponse> response) {
                    if (response.isSuccessful()){
                        existingOrders = response.body().getExistingOrder();
                        existingTableStatusAdapter = new ExistingTableStatusAdapter(existingOrders);
                        recyclerView.setAdapter(existingTableStatusAdapter);
                        int size = existingOrders.size();
                        if (size==0){
                            emptyState.setVisibility(View.VISIBLE);
                            emptyTxt.setVisibility(View.VISIBLE);
                        }
                    }else {
                        error.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ExistingOrderResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    error.setVisibility(View.VISIBLE);
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            error.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new New())
//                        .disallowAddToBackStack().commit();
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
//    }
}