package com.efficacious.e_smartsrestaurant.Billing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.HomeFragment;
import com.efficacious.e_smartsrestaurant.Dashboard.DashboardFragment;
import com.efficacious.e_smartsrestaurant.Model.BillDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.GetBillDetail;
import com.efficacious.e_smartsrestaurant.Model.RestaurantDetailsResponse;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TotalBillFragment extends Fragment {

    List<GetBillDetail> getBillDetail;
    BillDetailsAdapter billDetailsAdapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;
    String OrderId,TotalBill,Date,TableName,EmpId,SubTotal,GST;
    TextView totalBill,resName,resEmail,resMobile,resAddress,resType,date,orderId,tableNumber,waiterId,subTotal,cgst,sgst,GSTNO;
    SharedPreferences sharedPreferences;
    int resId;
    RelativeLayout billLayout;
    Bitmap bitmap;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_bill, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Bill");

        recyclerView = view.findViewById(R.id.recyclerView);
        date = view.findViewById(R.id.date);
        orderId = view.findViewById(R.id.orderId);
        totalBill = view.findViewById(R.id.totalBill);
        tableNumber = view.findViewById(R.id.tableNo);
        waiterId = view.findViewById(R.id.waiterId);
        subTotal = view.findViewById(R.id.subTotal);
        cgst = view.findViewById(R.id.CGST);
        sgst = view.findViewById(R.id.SGST);
        GSTNO = view.findViewById(R.id.GstNO);
        resName = view.findViewById(R.id.restaurantName);
        resMobile = view.findViewById(R.id.restaurantMobile);
        resEmail = view.findViewById(R.id.restaurantEmail);
        resAddress = view.findViewById(R.id.restaurantAddress);
        resType = view.findViewById(R.id.restaurantType);
        billLayout = view.findViewById(R.id.billLayout);

        Bundle bundle = this.getArguments();
        sharedPreferences = getContext().getSharedPreferences(WebConstant.USER_DATA_SHARED_PREF,0);
        resId = sharedPreferences.getInt(WebConstant.RESTAURANT_ID,0);

        if(bundle != null){
            OrderId = bundle.getString("OrderId");
            TotalBill = bundle.getString("TotalBill");
            TableName = bundle.getString("TableName");
            SubTotal = bundle.getString("SubTotal");
            GST = bundle.getString("GST");
            EmpId = bundle.getString("EmpId");
        }

        long timeStamp = System.currentTimeMillis();
        Date d = new Date(timeStamp);
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat1 = new SimpleDateFormat("dd/mm/yyyy");
        Date = dateFormat1.format(d.getTime());

        totalBill.setText("₹ " + TotalBill);
        date.setText(Date);
        orderId.setText("Order Id : #" + OrderId);
        tableNumber.setText(TableName);
        waiterId.setText("W. No : "  + EmpId);
        subTotal.setText("₹ " + SubTotal);

        Double gst = Double.parseDouble(GST);
        Double gstDivide =  gst/ 2;

        cgst.setText("₹ " + String.valueOf(gstDivide));
        sgst.setText("₹ " + String.valueOf(gstDivide));

//        lottieAnimationView = view.findViewById(R.id.error404);
        checkInternetConnection = new CheckInternetConnection(getContext());

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            getBillDetail = new ArrayList<>();
            billDetails(OrderId);
            restaurantDetails(resId);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        view.findViewById(R.id.backToHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment())
                        .commit();
            }
        });

        view.findViewById(R.id.btnShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billLayout.setDrawingCacheEnabled(true);
                Bitmap bitmap = billLayout.getDrawingCache();


                String file = getFilename();

                try {

                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                    billLayout.invalidate();
                    Toast.makeText(getContext(), "Download bill", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    billLayout.setDrawingCacheEnabled(false);
                }
            }
        });

        return view;
    }


    private String getFilename() {
        File path = Environment.getExternalStorageDirectory();
            File file = new File(path + "/DCIM");
            if (!file.exists()) {
                file.mkdirs();
            }
            String uriSting = (file.getAbsolutePath() + "/"
                    + "Bill#"+OrderId + ".jpg");
            return uriSting;
        }

    private void restaurantDetails(int resId) {
        try {
            Call<RestaurantDetailsResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getRestaurantDetails("Select", String.valueOf(resId));
            call.enqueue(new Callback<RestaurantDetailsResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<RestaurantDetailsResponse> call, Response<RestaurantDetailsResponse> response) {
                    if (response.isSuccessful()){
                        String name = response.body().getGetRestaurantDetails().get(0).getResName();
                        String email = response.body().getGetRestaurantDetails().get(0).getEmail();
                        String mobile = response.body().getGetRestaurantDetails().get(0).getMobile();
                        String address = response.body().getGetRestaurantDetails().get(0).getAddress();
                        String type = response.body().getGetRestaurantDetails().get(0).getResType();
                        String gst = response.body().getGetRestaurantDetails().get(0).getGstin();

                        resName.setText(name);
                        resEmail.setText(email);
                        resMobile.setText(mobile);
//                        resAddress.setText(address);
                        resType.setText(type);
                        GSTNO.setText(gst);
                    }
                }

                @Override
                public void onFailure(Call<RestaurantDetailsResponse> call, Throwable t) {

                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            lottieAnimationView.setVisibility(View.VISIBLE);
        }

    }

    private void billDetails(String orderId) {

        try {
            Call<BillDetailsResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getBillDetails("getBillDetails",orderId);

            call.enqueue(new Callback<BillDetailsResponse>() {
                @Override
                public void onResponse(Call<BillDetailsResponse> call, Response<BillDetailsResponse> response) {
                    if (response.isSuccessful()){
                        getBillDetail = response.body().getGetBillDetails();
                        billDetailsAdapter = new BillDetailsAdapter(getBillDetail);
                        recyclerView.setAdapter(billDetailsAdapter);
                    }else {
//                        lottieAnimationView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<BillDetailsResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    lottieAnimationView.setVisibility(View.VISIBLE);
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            lottieAnimationView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.restaurant_logo)
                        .setTitle(R.string.app_name)
                        .setMessage("Are you sure to close?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment())
                                        .disallowAddToBackStack().commit();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}