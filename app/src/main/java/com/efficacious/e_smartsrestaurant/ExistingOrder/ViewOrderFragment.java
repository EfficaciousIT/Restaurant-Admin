package com.efficacious.e_smartsrestaurant.ExistingOrder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.HomeFragment;
import com.efficacious.e_smartsrestaurant.Dashboard.DashboardFragment;
import com.efficacious.e_smartsrestaurant.Kitchen.KitchenHomeFragment;
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
import com.efficacious.e_smartsrestaurant.WaiterRole.WaiterHomeFragment;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrderFragment extends Fragment {

    List<GetExistingOrderDetail> getExistingOrderDetails;
    ExistingOrderMenuDeatilsAdapter existingOrderMenuDeatilsAdapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    LottieAnimationView lottieAnimationView;
    SharedPreferences sharedPreferences,OrderIdSharedPref;
    FloatingActionButton btnVertMenu;
    int employeeId,OrderId;
    String TableName;

    private static String SHARED_PREF_NAME = "OrderIdData";
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_order, container, false);
        btnVertMenu = view.findViewById(R.id.vertMenu);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Existing Order");

        sharedPreferences = getContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        employeeId = sharedPreferences.getInt(WebConstant.EMPLOYEE_ID,0);

        Bundle bundle = this.getArguments();

        if(bundle != null){
            OrderId = bundle.getInt("OrderId");
            TableName = bundle.getString("TableName");
        }


        btnVertMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.btnAddFood:
                                OrderIdSharedPref = getContext().getSharedPreferences(WebConstant.ORDER_ID_DATA_SHARED_PREF,0);
                                editor = OrderIdSharedPref.edit();
                                editor.putInt(WebConstant.ORDER_ID,OrderId);
                                editor.putString(WebConstant.TABLE_NAME,TableName);
                                editor.commit();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new ExistingOrderMenuCategoryFragment())
                                        .addToBackStack(null)
                                        .commit();
                                break;
                            case R.id.btnSendToBill:
                                updateStatus();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.inflate(R.menu.vert_menu);
                popupMenu.show();
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
            existingOrderDetails(OrderId);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        return view;
    }

    private void updateStatus() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are sure to create bill ?");
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

                        UpdateStatusDetails updateStatusDetails = new UpdateStatusDetails(OrderId, WebConstant.BILL_STATUS);

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
                                            .getFCMToken("getFCM","1",WebConstant.MANAGER_STATUS);
                                    FCM_CALL.enqueue(new Callback<GetFCMTokenResponse>() {
                                        @Override
                                        public void onResponse(Call<GetFCMTokenResponse> call, Response<GetFCMTokenResponse> response) {

                                            List<GetFCM> getFCM = response.body().getGetFCM();
                                            String FCMToken = getFCM.get(0).getVchFcmToken();
                                            String Title = "Bill is generated against #" + OrderId;
                                            String Msg = "Tap to view";
                                            String flag = WebConstant.BILL;
                                            sendNotification(FCMToken,Title,Msg,flag);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("Sending to billing !!");
                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    sharedPreferences = getContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
                                                    String Role = sharedPreferences.getString(WebConstant.USER_TYPE,null);
                                                    if (Role.equalsIgnoreCase("Manager")){
                                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment())
                                                                .disallowAddToBackStack().commit();
                                                    }else {
                                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewOrderFragment())
                                                                .disallowAddToBackStack().commit();
                                                    }
                                                }
                                            });
                                            builder.show();

                                        }

                                        @Override
                                        public void onFailure(Call<GetFCMTokenResponse> call, Throwable t) {

                                        }
                                    });
                                }catch (Exception e){

                                }

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