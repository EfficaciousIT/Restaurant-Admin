package com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.SplashScreenActivity;
import com.efficacious.e_smartsrestaurant.Model.CustomerDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.GetCustomer;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfileFragment extends Fragment {

    CheckInternetConnection checkInternetConnection;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    List<GetCustomer> getCustomers;
    String mobileNumber,mobileNumberWithCC,userName;
    TextView mUserName,mMobileNumber,mAddress;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        checkInternetConnection = new CheckInternetConnection(getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mUserName = view.findViewById(R.id.userName);
        mMobileNumber = view.findViewById(R.id.mobileNumber);
        mAddress = view.findViewById(R.id.address);

        view.findViewById(R.id.btnEditProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new UpdateProfileFragment())
                        .addToBackStack(null).commit();
            }
        });

        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are sure to logout ?");
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPreferences = getContext().getSharedPreferences(WebConstant.FIREBASE_LOGIN_DATA,0);
                        editor = sharedPreferences.edit();
                        editor.putBoolean("logged",false);
                        editor.commit();
                        editor.apply();
                        startActivity(new Intent(getContext(), SplashScreenActivity.class));
                        getActivity().finish();
                    }
                });
                builder.show();
            }
        });

        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Internet not Available !!");
            builder.setNegativeButton("Close",null);
            builder.show();
        }else {
            firebaseFirestore.collection("UserData")
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        mobileNumber = task.getResult().getString("WithoutCCMobile");
                        mobileNumberWithCC = task.getResult().getString("MobileNumber");
                        userName = task.getResult().getString("Name");
                        mUserName.setText(userName);
                        mMobileNumber.setText(mobileNumberWithCC);

                        try {
                            Call<CustomerDetailsResponse> call = RetrofitClient
                                    .getInstance()
                                    .getApi()
                                    .getCustomerDetails("select","1",mobileNumber);


                            call.enqueue(new Callback<CustomerDetailsResponse>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(Call<CustomerDetailsResponse> call, Response<CustomerDetailsResponse> response) {
                                    if (response.isSuccessful()){
                                        getCustomers = response.body().getGetCustomer();
                                        sharedPreferences = getContext().getSharedPreferences(WebConstant.FIREBASE_LOGIN_DATA,0);
                                        editor = sharedPreferences.edit();
                                        editor.putInt("RegisterId",getCustomers.get(0).getRegisterId());
                                        editor.putString("MobileNumber",getCustomers.get(0).getMobileNo());
                                        editor.commit();
                                        editor.apply();
                                        mUserName.setText(getCustomers.get(0).getFirstName() + " " + getCustomers.get(0).getLastName());
                                        mAddress.setText(getCustomers.get(0).getAddress1() + ", " + getCustomers.get(0).getAddress2() + ", " + getCustomers.get(0).getAddress3());
                                    }
                                }

                                @Override
                                public void onFailure(Call<CustomerDetailsResponse> call, Throwable t) {
                                    Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){
                            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        return view;
    }
}