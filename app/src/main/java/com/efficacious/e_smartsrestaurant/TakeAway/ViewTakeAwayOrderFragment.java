package com.efficacious.e_smartsrestaurant.TakeAway;

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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.HomeFragment;
import com.efficacious.e_smartsrestaurant.Dashboard.DashboardFragment;
import com.efficacious.e_smartsrestaurant.ExistingOrder.ExistingOrderMenuDeatilsAdapter;
import com.efficacious.e_smartsrestaurant.Model.ExistingOrderDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.GetExistingOrderDetail;
import com.efficacious.e_smartsrestaurant.Model.GetFCM;
import com.efficacious.e_smartsrestaurant.Model.GetFCMTokenResponse;
import com.efficacious.e_smartsrestaurant.Model.UpdateStatusDetails;
import com.efficacious.e_smartsrestaurant.NewOrder.NewOrderFragment;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTakeAwayOrderFragment extends Fragment {

    List<GetExistingOrderDetail> getExistingOrderDetails;
    List<GetFCM> getFCM;
    ExistingOrderMenuDeatilsAdapter existingOrderMenuDeatilsAdapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    ExtendedFloatingActionButton btnAccept;
    int OrderId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_take_away_order, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Takeaway Order");

        Bundle bundle = this.getArguments();

        if(bundle != null){
            OrderId = bundle.getInt("OrderId");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        btnAccept = view.findViewById(R.id.btnAccept);
        checkInternetConnection = new CheckInternetConnection(getContext());

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            getExistingOrderDetails = new ArrayList<>();
            existingOrderDetails(OrderId);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Accept Request");
                builder.setMessage("After accept request order send to kitchen");
                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            updateStatus();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.show();
            }
        });

        return view;
    }

    private void updateStatus() {
        if (!checkInternetConnection.isConnectingToInternet()){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            try {

                UpdateStatusDetails updateStatusDetails = new UpdateStatusDetails(OrderId, WebConstant.KITCHEN_STATUS);

                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .updateStatus("update",updateStatusDetails);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        try {
                            Call<GetFCMTokenResponse> FCM_CALL = RetrofitClient
                                    .getInstance()
                                    .getApi()
                                    .getFCMToken("getFCM","1",WebConstant.KITCHEN_STATUS);

                            FCM_CALL.enqueue(new Callback<GetFCMTokenResponse>() {
                                @Override
                                public void onResponse(Call<GetFCMTokenResponse> call, Response<GetFCMTokenResponse> response) {
                                    getFCM = response.body().getGetFCM();
                                    String FCMToken = getFCM.get(0).getVchFcmToken();
                                    String Title = "New Takeaway order #" + OrderId + " arrived";
                                    String Msg = "Tap to view";
                                    String flag = WebConstant.KITCHEN_NEW_ORDER;
                                    sendNotification(FCMToken,Title,Msg,flag);
                                }

                                @Override
                                public void onFailure(Call<GetFCMTokenResponse> call, Throwable t) {

                                }
                            });
                        }catch (Exception e){

                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Request Accepted !!");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("OrderId",OrderId);
                                map.put("TimeStamp",System.currentTimeMillis());
                                map.put("Status","Accept");
                                firebaseFirestore.collection("Orders").document(String.valueOf(OrderId))
                                        .collection("OrderStatus")
                                        .add(map);

                                //sending order accept notification to user
                                firebaseFirestore.collection("TakeAway").document(String.valueOf(OrderId))
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            String token = task.getResult().getString("FCMToken");
                                            String title = "Your order #"+OrderId+" accepted";
                                            String msg = "Tap to view order status";
                                            String flag = WebConstant.USER_HISTORY;
                                            sendNotification(token,title,msg,flag);
                                        }
                                    }
                                });



                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment())
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

    private void existingOrderDetails(int orderId) {
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
                        existingOrderMenuDeatilsAdapter = new ExistingOrderMenuDeatilsAdapter(getExistingOrderDetails);
                        recyclerView.setAdapter(existingOrderMenuDeatilsAdapter);
                    }else {
//                        lottieAnimationView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ExistingOrderDetailsResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    lottieAnimationView.setVisibility(View.VISIBLE);
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            lottieAnimationView.setVisibility(View.VISIBLE);
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