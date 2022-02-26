package com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments;

import static com.airbnb.lottie.L.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import com.efficacious.e_smartsrestaurant.Model.GetOrderId;
import com.efficacious.e_smartsrestaurant.Model.GetTakeAwayOrderId;
import com.efficacious.e_smartsrestaurant.Model.OrderDetails;
import com.efficacious.e_smartsrestaurant.Model.TakeAwayOrderIdResponse;
import com.efficacious.e_smartsrestaurant.Model.TakeOrderDetail;
import com.efficacious.e_smartsrestaurant.NewOrder.CartAdapter;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.MenuData;
import com.efficacious.e_smartsrestaurant.Room.MenuDatabase;
import com.efficacious.e_smartsrestaurant.Room.TableBook;
import com.efficacious.e_smartsrestaurant.Room.TableBookDatabase;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserOrderFragment extends Fragment {

    MenuDatabase menuDatabase;
    TableBookDatabase tableBookDatabase;
    CheckInternetConnection checkInternetConnection;
    ProgressBar progressBar;
    Button btnOrder;
    LottieAnimationView lottieAnimationView;
    TextView emptyTxt;
    String tableName;
    long OrderId;
    String TimeStamp;
    List<TableBook> tableBooks;
    List<MenuData> menuData;
    SharedPreferences sharedPreferences,sharedPrefTimeStamp;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_order, container, false);

        checkInternetConnection = new CheckInternetConnection(getContext());
        sharedPreferences = getContext().getSharedPreferences(WebConstant.FIREBASE_LOGIN_DATA,0);
        sharedPrefTimeStamp = getContext().getSharedPreferences("TIMESTAMP_SHARED_PREF",0);
        editor = sharedPreferences.edit();
        progressBar = view.findViewById(R.id.loader);
        btnOrder = view.findViewById(R.id.btnOrder);
        emptyTxt = view.findViewById(R.id.emptyTxt);
        lottieAnimationView = view.findViewById(R.id.lottie);
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


        btnOrder.findViewById(R.id.btnOrder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int RegisterId = sharedPreferences.getInt("RegisterId",0);
                String timeStamp = String.valueOf(System.currentTimeMillis());
                Log.d(TAG, "timeStamp: " + timeStamp);
                TakeOrderDetail takeOrderDetail = new TakeOrderDetail(WebConstant.TAKEAWAY,RegisterId,0,null,1,0,1,null,"No", WebConstant.TAKEAWAY,timeStamp);
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
                                    TakeAwayOrderId(timeStamp);
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
        });

        return view;
    }

    private void TakeAwayOrderId(String timeStamp) {
        Log.d(TAG, "timeStamp2: " + timeStamp);
        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {
            try {
                Call<TakeAwayOrderIdResponse> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .getTakeAwayOrderId("select","1",timeStamp);

                call.enqueue(new Callback<TakeAwayOrderIdResponse>() {
                    @Override
                    public void onResponse(Call<TakeAwayOrderIdResponse> call, Response<TakeAwayOrderIdResponse> response) {
                        if (response.isSuccessful()){
                            List<GetTakeAwayOrderId> takeAwayOrderId = response.body().getGetTakeAwayOrder();
                            int size = takeAwayOrderId.size();
                            if (size>0){
                                OrderId = takeAwayOrderId.get(0).getOrderId();
                                TimeStamp = takeAwayOrderId.get(0).getTimeStamp();
                                Log.d(TAG, "OrderId: " + OrderId);
                                confirmOrder(menuData,checkInternetConnection);
                            }else {
                                Toast.makeText(getContext(), "Empty..", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<TakeAwayOrderIdResponse> call, Throwable t) {

                    }
                });

            }catch (Exception e){
                Toast.makeText(getContext(), "API Error : " + e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void confirmOrder(List<MenuData> menuData, CheckInternetConnection checkInternetConnection) {

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {
            String takeAwayOrderId = String.valueOf(OrderId);
            int listSize = menuData.size();
            for (int i=0;i<menuData.size();i++){
                btnOrder.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                OrderDetails orderDetailsData = new OrderDetails(Integer.valueOf(takeAwayOrderId),menuData.get(i).getCategoryName(),
                        menuData.get(i).getMenuName(),WebConstant.TAKEAWAY,
                        0,menuData.get(i).getEmployeeId(),menuData.get(i).getPrice(),menuData.get(i).getQty()
                        ,null,WebConstant.TAKEAWAY,WebConstant.TAKEAWAY,TimeStamp);

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
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Order confirm !!");
                                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new UserHomeFragment()).disallowAddToBackStack()
                                                .commit();
                                    }
                                });
                                builder.show();
                                progressBar.setVisibility(View.GONE);
                                btnOrder.setVisibility(View.VISIBLE);
                                menuDatabase.dao().deleteAllData();
                                tableBookDatabase.dao_tableBook().deleteAllData();
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

}