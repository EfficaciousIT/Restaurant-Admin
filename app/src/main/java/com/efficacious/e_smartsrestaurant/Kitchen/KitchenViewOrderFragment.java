package com.efficacious.e_smartsrestaurant.Kitchen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.ExistingOrder.ExistingOrderMenuCategoryFragment;
import com.efficacious.e_smartsrestaurant.ExistingOrder.ExistingOrderMenuDeatilsAdapter;
import com.efficacious.e_smartsrestaurant.Model.ExistingOrderDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.GetExistingOrderDetail;
import com.efficacious.e_smartsrestaurant.Model.GetFCM;
import com.efficacious.e_smartsrestaurant.Model.GetFCMTokenResponse;
import com.efficacious.e_smartsrestaurant.Model.UpdateStatusDetails;
import com.efficacious.e_smartsrestaurant.Notification.APIService;
import com.efficacious.e_smartsrestaurant.Notification.Client;
import com.efficacious.e_smartsrestaurant.Notification.Data;
import com.efficacious.e_smartsrestaurant.Notification.NotificationSender;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KitchenViewOrderFragment extends Fragment {

    List<GetExistingOrderDetail> getExistingOrderDetails;
    List<GetFCM> getFCM;
    ExistingOrderMenuDeatilsAdapter existingOrderMenuDeatilsAdapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    LottieAnimationView lottieAnimationView;
    SharedPreferences sharedPreferences;
    ExtendedFloatingActionButton btnDispatchOrder;
    int OrderId,employeeId;
    String OrderType,FCMToken;
    LottieAnimationView emptyState,error;
    TextView emptyTxt;
    UpdateStatusDetails updateStatusDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitchen_view_order, container, false);

        btnDispatchOrder = view.findViewById(R.id.btnDispatchOrder);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order Details");

        sharedPreferences = getContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        employeeId = sharedPreferences.getInt(WebConstant.EMPLOYEE_ID,0);
        emptyState = view.findViewById(R.id.emptyState);
        emptyTxt = view.findViewById(R.id.emptyTxt);
        error = view.findViewById(R.id.error404);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            OrderId = bundle.getInt("OrderId");
            OrderType = bundle.getString("OrderType");
        }

        btnDispatchOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are sure to dispatch order ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!checkInternetConnection.isConnectingToInternet()){
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                            builder.setTitle("Internet not Available !!");
                            builder.setNegativeButton("Close",null);
                            builder.show();
                        }else {
                            try {
                                if (OrderType.equalsIgnoreCase(WebConstant.TAKEAWAY)){
                                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                    HashMap<String,Object> map = new HashMap<>();
                                    map.put("OrderId",OrderId);
                                    map.put("TimeStamp",System.currentTimeMillis());
                                    map.put("Status","Order Prepared");
                                    firebaseFirestore.collection("Orders").document(String.valueOf(OrderId))
                                            .collection("OrderStatus")
                                            .add(map);
                                    firebaseFirestore.collection("TakeAway").document(String.valueOf(OrderId))
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                String token = task.getResult().getString("FCMToken");
                                                String title = "Your order #"+OrderId+" prepared";
                                                String msg = "Tap to view order status";
                                                String flag = WebConstant.USER_HISTORY;
                                                sendNotification(token,title,msg,flag);
                                            }
                                        }
                                    });

                                    try {
                                        Call<GetFCMTokenResponse> FCM_CALL = RetrofitClient
                                                .getInstance()
                                                .getApi()
                                                .getFCMToken("getFCM","1","Manager");

                                        FCM_CALL.enqueue(new Callback<GetFCMTokenResponse>() {
                                            @Override
                                            public void onResponse(Call<GetFCMTokenResponse> call, Response<GetFCMTokenResponse> response) {
                                                getFCM = response.body().getGetFCM();
                                                String FCMToken = getFCM.get(0).getVchFcmToken();
                                                String Title = "Takeaway order #" + OrderId + " Prepared";
                                                String Msg = "Tap to view";
                                                String flag = WebConstant.TAKEAWAY_DISPATCH;
                                                sendNotification(FCMToken,Title,Msg,flag);

                                            }

                                            @Override
                                            public void onFailure(Call<GetFCMTokenResponse> call, Throwable t) {

                                            }
                                        });
                                    }catch (Exception e){

                                    }
                                    updateStatusDetails = new UpdateStatusDetails(OrderId, WebConstant.BILL_STATUS);

                                }else {
                                    updateStatusDetails = new UpdateStatusDetails(OrderId, WebConstant.DISPATCH_STATUS);
                                }

                                Call<ResponseBody> call = RetrofitClient
                                        .getInstance()
                                        .getApi()
                                        .updateStatus("update",updateStatusDetails);

                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Order Dispatch Successfully !!");
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String title = "Order ID #" + OrderId + " dispatch successfully";
                                                String msg = "Check order here";
                                                String flag = WebConstant.WAITER_DISPATCH;
                                                sendNotification(FCMToken,title,msg,flag);
                                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new KitchenHomeFragment())
                                                        .addToBackStack(null).commit();
                                            }
                                        });
                                        builder.show();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(getContext(), "Api Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }catch (Exception e){
                                Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.show();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
//        lottieAnimationView = view.findViewById(R.id.error404);
        checkInternetConnection = new CheckInternetConnection(getContext());

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            getExistingOrderDetails = new ArrayList<>();
            OrderDetails(OrderId);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        return view;
    }

    private void OrderDetails(int orderId) {
        try {
            Call<ExistingOrderDetailsResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getExistingOrderDetails("select","1",String.valueOf(orderId));

            call.enqueue(new Callback<ExistingOrderDetailsResponse>() {
                @Override
                public void onResponse(Call<ExistingOrderDetailsResponse> call, Response<ExistingOrderDetailsResponse> response) {
                    if (response.isSuccessful()){
                        getExistingOrderDetails = response.body().getGetExistingOrderDetails();
                        FCMToken = getExistingOrderDetails.get(0).getFcmToken();
                        existingOrderMenuDeatilsAdapter = new ExistingOrderMenuDeatilsAdapter(getExistingOrderDetails);
                        recyclerView.setAdapter(existingOrderMenuDeatilsAdapter);
                        int size = getExistingOrderDetails.size();
                        if (size==0){
                            emptyState.setVisibility(View.VISIBLE);
                            emptyTxt.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ExistingOrderDetailsResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    error.setVisibility(View.VISIBLE);
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            error.setVisibility(View.VISIBLE);
        }
    }

    private void sendNotification(String token, String title, String msg,String flag) {
        Data data = new Data(title,msg,flag);
        NotificationSender notificationSender = new NotificationSender(data,token);

        APIService apiService = Client.getRetrofit().create(APIService.class);

        apiService.sendNotification(notificationSender).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}