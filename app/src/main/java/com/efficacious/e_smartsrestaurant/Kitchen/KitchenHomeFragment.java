package com.efficacious.e_smartsrestaurant.Kitchen;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.SharedPrefManger;
import com.efficacious.e_smartsrestaurant.Base.SplashScreenActivity;
import com.efficacious.e_smartsrestaurant.Model.KitchenNewOrderDetail;
import com.efficacious.e_smartsrestaurant.Model.KitchenNewOrderResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KitchenHomeFragment extends Fragment {

    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    KitchenNewOrderAdapter kitchenNewOrderAdapter;
    List<KitchenNewOrderDetail> kitchenNewOrderDetails;
    LottieAnimationView emptyState,error;
    TextView emptyTxt;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitchen_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("New Order");
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyState = view.findViewById(R.id.emptyState);
        emptyTxt = view.findViewById(R.id.emptyTxt);
        error = view.findViewById(R.id.error404);
        checkInternetConnection = new CheckInternetConnection(getContext());

        if (!checkInternetConnection.isConnectingToInternet()){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            kitchenNewOrderDetails = new ArrayList<>();
            kitchenNewOrder();
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.bg_img)
                        .setTitle(R.string.app_name)
                        .setMessage("Are you sure to exit ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                                getActivity().moveTaskToBack(true);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void dayStatus(View view) {
        String title = null;
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            title="Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            title="Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            title="Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            title="Good Night";
        }
        TextView textView = view.findViewById(R.id.text);
        textView.setText(title);
    }

    private void kitchenNewOrder() {

        try {
            Call<KitchenNewOrderResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getKitchenNewOrder("select","1");

            call.enqueue(new Callback<KitchenNewOrderResponse>() {
                @Override
                public void onResponse(Call<KitchenNewOrderResponse> call, Response<KitchenNewOrderResponse> response) {
                    if (response.isSuccessful()){
                        kitchenNewOrderDetails = response.body().getGetKitchenOrderList();
                        kitchenNewOrderAdapter = new KitchenNewOrderAdapter(kitchenNewOrderDetails);
                        recyclerView.setAdapter(kitchenNewOrderAdapter);
                        int size = kitchenNewOrderDetails.size();
                        if (size==0){
                            emptyState.setVisibility(View.VISIBLE);
                            emptyTxt.setVisibility(View.VISIBLE);
                        }
                    }else {
                        error.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<KitchenNewOrderResponse> call, Throwable t) {
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