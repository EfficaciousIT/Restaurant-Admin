package com.efficacious.e_smartsrestaurant.NewOrder;

import static com.airbnb.lottie.L.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.HomeFragment;
import com.efficacious.e_smartsrestaurant.Dashboard.DashboardFragment;
import com.efficacious.e_smartsrestaurant.Model.CartData;
import com.efficacious.e_smartsrestaurant.Model.GetFCM;
import com.efficacious.e_smartsrestaurant.Model.GetFCMTokenResponse;
import com.efficacious.e_smartsrestaurant.Model.GetOrderId;
import com.efficacious.e_smartsrestaurant.Model.OrderDetails;
import com.efficacious.e_smartsrestaurant.Model.TakeOrderDetail;
import com.efficacious.e_smartsrestaurant.Model.TakeOrderIdResponse;
import com.efficacious.e_smartsrestaurant.Notification.APIService;
import com.efficacious.e_smartsrestaurant.Notification.Client;
import com.efficacious.e_smartsrestaurant.Notification.Data;
import com.efficacious.e_smartsrestaurant.Notification.NotificationSender;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.MenuData;
import com.efficacious.e_smartsrestaurant.Room.MenuDatabase;
import com.efficacious.e_smartsrestaurant.Room.TableBook;
import com.efficacious.e_smartsrestaurant.Room.TableBookDatabase;
import com.efficacious.e_smartsrestaurant.WaiterRole.WaiterHomeFragment;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    MenuDatabase menuDatabase;
    TableBookDatabase tableBookDatabase;
    CheckInternetConnection checkInternetConnection;
    ProgressBar progressBar;
    LottieAnimationView lottieAnimationView;
    TextView emptyTxt;
    String tableName;
    List<GetOrderId> takeOrderIdDetails;
    List<GetFCM> getFCM;
    int OrderId;
    List<TableBook> tableBooks;
    List<MenuData> menuData;
    SharedPreferences sharedPreferences;
    FloatingActionButton btnOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cart");
        checkInternetConnection = new CheckInternetConnection(getContext());
        progressBar = view.findViewById(R.id.loader);
        btnOrder = view.findViewById(R.id.floatingOrderBtn);
        emptyTxt = view.findViewById(R.id.emptyTxt);
        lottieAnimationView = view.findViewById(R.id.lottie);
        takeOrderIdDetails = new ArrayList<>();
        setUpDB();
        menuData = menuDatabase.dao().getMenuListData();
        tableBooks = tableBookDatabase.dao_tableBook().getTableData();
        boolean empty = menuData.isEmpty();
        if (empty){
            btnOrder.setVisibility(View.GONE);
        }else {
            lottieAnimationView.setVisibility(View.GONE);
            emptyTxt.setVisibility(View.GONE);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            CartAdapter adapter = new CartAdapter(menuData);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookTable();
            }
        });

        return view;
    }

    private void bookTable() {
        int EmployeeId = tableBooks.get(0).getEmployeeId();
        int personCount = tableBooks.get(0).getIntPersonCount();
        int ResId = tableBooks.get(0).getResId();
        tableName = tableBooks.get(0).getTableName();

        TakeOrderDetail takeOrderDetail = new TakeOrderDetail(tableName,1,EmployeeId,null,1,personCount,ResId,null,"No", WebConstant.KITCHEN_STATUS,String.valueOf(System.currentTimeMillis()));
        Log.d(TAG, "onClick: " + tableName);

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {
            try {
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .getOrder("insert",takeOrderDetail);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.d(TAG, "onResponse: " + response.body().toString());
                            TakeOrderId();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }catch (Exception e){
                Toast.makeText(getContext(), "Api Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void TakeOrderId() {
        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {
            try {
                Call<TakeOrderIdResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .getOrderId("GetOrderId","1");

                call.enqueue(new Callback<TakeOrderIdResponse>() {
                    @Override
                    public void onResponse(Call<TakeOrderIdResponse> call, Response<TakeOrderIdResponse> response) {
                        if (response.isSuccessful()){
                            takeOrderIdDetails = response.body().getGetOrderId();
                            OrderId = takeOrderIdDetails.get(0).getOrderId();
                            Log.d(TAG, "OrderId: " + OrderId);
                            confirmOrder(checkInternetConnection);
                        }
                    }

                    @Override
                    public void onFailure(Call<TakeOrderIdResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }catch (Exception e){
                Toast.makeText(getContext(), "API Error : " + e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void confirmOrder(CheckInternetConnection checkInternetConnection) {

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {
            menuData = menuDatabase.dao().getMenuListData();
            int listSize = menuData.size();
            for (int i=0;i<menuData.size();i++){
                btnOrder.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                OrderDetails orderDetailsData = new OrderDetails(OrderId,menuData.get(i).getCategoryName(),
                        menuData.get(i).getMenuName(),tableName,
                        0,menuData.get(i).getEmployeeId(),menuData.get(i).getPrice(),menuData.get(i).getQty()
                        ,null,WebConstant.KITCHEN_STATUS,WebConstant.KITCHEN_STATUS,String.valueOf(System.currentTimeMillis()));
                Log.d(TAG, "Qty : " + menuData.get(i).getQty());

                try {
                    Call<ResponseBody> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .getOrderDetails("insert",orderDetailsData);

                    int finalI = i;

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (finalI == listSize-1){

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
                                            String Title = "New #" + OrderId + " Order arrived";
                                            String Msg = "Tap to view";
                                            String flag = WebConstant.KITCHEN_NEW_ORDER;
                                            sendNotification(FCMToken,Title,Msg,flag);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                            builder.setTitle("Order confirm !!");
                                            builder.setCancelable(false);
                                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
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
                                            progressBar.setVisibility(View.GONE);
                                            btnOrder.setVisibility(View.VISIBLE);
                                            menuDatabase.dao().deleteAllData();
                                            tableBookDatabase.dao_tableBook().deleteAllData();
                                        }

                                        @Override
                                        public void onFailure(Call<GetFCMTokenResponse> call, Throwable t) {

                                        }
                                    });
                                }catch (Exception e){

                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btnOrder.setVisibility(View.VISIBLE);
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getContext(), "API Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    btnOrder.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void setUpDB(){
        menuDatabase = Room.databaseBuilder(getContext(), MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
        tableBookDatabase = Room.databaseBuilder(getContext(), TableBookDatabase.class,"TableBook")
                .allowMainThreadQueries().build();

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