package com.efficacious.e_smartsrestaurant.Billing;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.DeliveryBoy.SelectDeliveryBoyFragment;
import com.efficacious.e_smartsrestaurant.ExistingOrder.ExistingOrderMenuCategoryFragment;
import com.efficacious.e_smartsrestaurant.ExistingOrder.ExistingOrderMenuDeatilsAdapter;
import com.efficacious.e_smartsrestaurant.Model.BillDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.BillUpdateDetails;
import com.efficacious.e_smartsrestaurant.Model.ExistingOrderDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.GetBillDetail;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillDetailFragment extends Fragment {

    List<GetBillDetail> getBillDetails;
    BillMenuDetailsAdapter billMenuDetailsAdapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    LottieAnimationView emptyState,error;
    TextView emptyTxt;
    SharedPreferences sharedPreferences;
    String tableName,empId;
    ExtendedFloatingActionButton btnCreateBill,btnAssign;
    int OrderId;
    float total=0,gst,totalBill;
    FirebaseFirestore firebaseFirestore;
    String UserName,MobileNumber,RegisterId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_detail, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Bill");

        btnCreateBill = view.findViewById(R.id.btnCreateBill);
        btnAssign = view.findViewById(R.id.btnAssignTask);


        sharedPreferences = getContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            OrderId = bundle.getInt("OrderId");
            UserName = bundle.getString("UserName");
            MobileNumber = bundle.getString("MobileNumber");
            RegisterId = bundle.getString("RegisterId");
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TakeAway").document(String.valueOf(OrderId))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String DeliveryStatus = task.getResult().getString("DeliveryStatus");
                    if (!TextUtils.isEmpty(DeliveryStatus)){
                        if (DeliveryStatus.equalsIgnoreCase(WebConstant.DOOR_DELIVERY)){
                            btnCreateBill.setVisibility(View.GONE);
                            btnAssign.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }
        });


        emptyState = view.findViewById(R.id.emptyState);
        emptyTxt = view.findViewById(R.id.emptyTxt);
        error = view.findViewById(R.id.error404);
        recyclerView = view.findViewById(R.id.recyclerView);
        checkInternetConnection = new CheckInternetConnection(getContext());

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
            btnCreateBill.setVisibility(View.GONE);
        }else {
//            Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
            getBillDetails = new ArrayList<>();
            billDetails(OrderId);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            btnCreateBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i=0;i<getBillDetails.size();i++){
                        tableName = getBillDetails.get(i).getTableName();
                        empId = String.valueOf(getBillDetails.get(i).getEmployeeId());
                        total += getBillDetails.get(i).getPrice() * getBillDetails.get(i).getQty();
                        if (i == getBillDetails.size()-1){
//                            btnCreateBill.setText("Total Bill : " + String.valueOf(total));
                            gst = ((total*5)/100);
                            totalBill = total + gst;
                            updateBill(totalBill,total,OrderId);
                        }
                    }
                }
            });

            btnAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i=0;i<getBillDetails.size();i++){
                        tableName = getBillDetails.get(i).getTableName();
                        empId = String.valueOf(getBillDetails.get(i).getEmployeeId());
                        total += getBillDetails.get(i).getPrice() * getBillDetails.get(i).getQty();
                        if (i == getBillDetails.size()-1){
//                            btnCreateBill.setText("Total Bill : " + String.valueOf(total));
                            gst = ((total*5)/100);
                            totalBill = total + gst;

                            Fragment fragment = new SelectDeliveryBoyFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("OrderId",String.valueOf(OrderId));
                            bundle.putString("TotalBill",String.valueOf(totalBill));
                            bundle.putString("RegisterId",RegisterId);
                            bundle.putString("MobileNumber",MobileNumber);
                            bundle.putString("UserName",UserName);
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
                                    .addToBackStack(null).commit();

                        }
                    }

                }
            });

        }

        return view;
    }

    private void updateBill(float totalBill, float total, int orderId) {
        try {
            BillUpdateDetails billUpdateDetails = new BillUpdateDetails(orderId,WebConstant.CLOSE_STATUS,Math.round(total));
//            Toast.makeText(getContext(), String.valueOf(total), Toast.LENGTH_SHORT).show();
            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updateBillDetails("update",billUpdateDetails);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        Fragment fragment = new TotalBillFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("TotalBill",String.valueOf(totalBill));
                        bundle.putString("SubTotal",String.valueOf(total));
                        bundle.putString("GST",String.valueOf(gst));
                        bundle.putString("OrderId", String.valueOf(OrderId));
                        bundle.putString("TableName",String.valueOf(tableName));
                        bundle.putString("EmpId",String.valueOf(empId));
                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
                                .addToBackStack(null).commit();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void billDetails(int orderId) {
        try {
            Call<BillDetailsResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getBillDetails("getBillDetails",String.valueOf(orderId));

            call.enqueue(new Callback<BillDetailsResponse>() {
                @Override
                public void onResponse(Call<BillDetailsResponse> call, Response<BillDetailsResponse> response) {
                    if (response.isSuccessful()){
                        getBillDetails = response.body().getGetBillDetails();
                        billMenuDetailsAdapter = new BillMenuDetailsAdapter(getBillDetails);
                        recyclerView.setAdapter(billMenuDetailsAdapter);
                        int size = getBillDetails.size();
//                        Toast.makeText(getContext(), String.valueOf(size), Toast.LENGTH_SHORT).show();
                        if (size==0){
                            emptyState.setVisibility(View.VISIBLE);
                            emptyTxt.setVisibility(View.VISIBLE);
                        }
                    }else {
                        error.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<BillDetailsResponse> call, Throwable t) {
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