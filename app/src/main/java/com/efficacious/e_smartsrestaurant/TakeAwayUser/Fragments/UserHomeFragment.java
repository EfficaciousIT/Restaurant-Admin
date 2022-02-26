package com.efficacious.e_smartsrestaurant.TakeAwayUser.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Menu.MenuCategoryAdapter;
import com.efficacious.e_smartsrestaurant.Model.CategoryResponse;
import com.efficacious.e_smartsrestaurant.Model.CustomerDetailsResponse;
import com.efficacious.e_smartsrestaurant.Model.GetCustomer;
import com.efficacious.e_smartsrestaurant.Model.MenuCategoryDetail;
import com.efficacious.e_smartsrestaurant.Model.MenuDetail;
import com.efficacious.e_smartsrestaurant.Model.MenuResponse;
import com.efficacious.e_smartsrestaurant.NewOrder.SelectMenuAdapter;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.MenuDatabase;
import com.efficacious.e_smartsrestaurant.Room.TableBookDatabase;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Adapter.CategoryAdapter;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Adapter.FullViewMenuAdapter;
import com.efficacious.e_smartsrestaurant.TakeAwayUser.Adapter.MenuAdapter;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeFragment extends Fragment {

    SharedPreferences sharedPreferences;
    String title;

    List<MenuCategoryDetail> menuCategoryDetails;
    CategoryAdapter adapter;
    RecyclerView recyclerView;
    CheckInternetConnection checkInternetConnection;

    List<GetCustomer> getCustomers;

    List<MenuDetail> menuDetail;
    List<MenuDetail> menuDetailForYou;
    MenuAdapter menuAdapter;
    FullViewMenuAdapter fullViewMenuAdapter;
    RecyclerView recyclerView2,recyclerView3;
    String CategoryId,CategoryIdForYou, CategoryName;
    MenuDatabase menuDatabase;
    TableBookDatabase tableBookDatabase;
    String[] CategoryIdArray;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    SharedPreferences.Editor editor;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = getContext().getSharedPreferences(WebConstant.FIREBASE_LOGIN_DATA,0);
        String UserName = sharedPreferences.getString("name",null);
        TextView userTxt = view.findViewById(R.id.userName);
        userTxt.setText("Hello, "+ UserName);
        dayStatus(view);

        view.findViewById(R.id.btnProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new UserProfileFragment())
                        .addToBackStack(null).commit();
            }
        });

        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {

            //for something new
            menuCategoryDetails = new ArrayList<>();
            categoryList();
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

            //for recommended food
//            CategoryId = getArguments().getString("CategoryId");
//            CategoryName = getArguments().getString("CategoryName");
            CategoryId = "22";
            menuDetail = new ArrayList<>();
            if (!CategoryId.isEmpty()){
//                menuList(CategoryId);
            }
            recyclerView2 = view.findViewById(R.id.recyclerView2);
            recyclerView2.setHasFixedSize(true);
            recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

            CategoryIdForYou = "4";
            menuDetailForYou = new ArrayList<>();
            if (!CategoryId.isEmpty()){
//                Random random = new Random();
//                String ran = CategoryIdArray[random.nextInt(CategoryIdArray.length-1)];
//                Toast.makeText(getContext(), ran, Toast.LENGTH_SHORT).show();
//                menuListForYou(CategoryIdForYou);
            }
            recyclerView3 = view.findViewById(R.id.recyclerView3);
            recyclerView3.setHasFixedSize(true);
            recyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));

        }

//        if (!checkInternetConnection.isConnectingToInternet()){
//            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
//            builder.setTitle("Internet not Available !!");
//            builder.setNegativeButton("Close",null);
//            builder.show();
//        }else {
//            firebaseFirestore.collection("UserData")
//                    .document(firebaseAuth.getCurrentUser().getUid())
//                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()){
//                        String mobileNumber = task.getResult().getString("WithoutCCMobile");
//
//                        try {
//                            Call<CustomerDetailsResponse> call = RetrofitClient
//                                    .getInstance()
//                                    .getApi()
//                                    .getCustomerDetails("select","1",mobileNumber);
//
//
//                            call.enqueue(new Callback<CustomerDetailsResponse>() {
//                                @SuppressLint("SetTextI18n")
//                                @Override
//                                public void onResponse(Call<CustomerDetailsResponse> call, Response<CustomerDetailsResponse> response) {
//                                    if (response.isSuccessful()){
//                                        getCustomers = response.body().getGetCustomer();
//                                        sharedPreferences = getContext().getSharedPreferences(WebConstant.FIREBASE_LOGIN_DATA,0);
//                                        editor = sharedPreferences.edit();
//                                        editor.putInt("RegisterId",getCustomers.get(0).getRegisterId());
//                                        editor.putString("MobileNumber",getCustomers.get(0).getMobileNo());
//                                        editor.commit();
//                                        editor.apply();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<CustomerDetailsResponse> call, Throwable t) {
//                                    Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }catch (Exception e){
//                            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            });
//        }

        return view;
    }

    private void dayStatus(View view) {
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

    private void categoryList() {
        try {
            Call<CategoryResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .categories("Category","1");

            call.enqueue(new Callback<CategoryResponse>() {
                @Override
                public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {

                    if (response.isSuccessful()){
                        menuCategoryDetails = response.body().getMenuCategoryDetails();
                        CategoryIdArray = new String[menuCategoryDetails.size()];
                        for (int i=0;i<menuCategoryDetails.size();i++){
                            CategoryIdArray[i] = menuCategoryDetails.get(i).getCatId().toString();
                        }
                        adapter = new CategoryAdapter(menuCategoryDetails);
                        recyclerView.setAdapter(adapter);
                        Random random = new Random();
                        String CategoryIdForMenuList = CategoryIdArray[random.nextInt(CategoryIdArray.length-1)];
                        menuList(CategoryIdForMenuList);

                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CategoryResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void menuList(String categoryId) {
        try {
            Call<MenuResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getMenu("CategoryDrp",categoryId,"1");

            call.enqueue(new Callback<MenuResponse>() {
                @Override
                public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                    if (response.isSuccessful()){
                        menuDetail = response.body().getMenuDetails();
                        menuAdapter = new MenuAdapter(menuDetail);
                        recyclerView2.setAdapter(menuAdapter);
                        Random random = new Random();
                        String CategoryIdForMenuListForYou = CategoryIdArray[random.nextInt(CategoryIdArray.length-1)];
                        menuListForYou(CategoryIdForMenuListForYou);
                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MenuResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error occur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void menuListForYou(String categoryId) {
        try {
            Call<MenuResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .getMenu("CategoryDrp",categoryId,"1");

            call.enqueue(new Callback<MenuResponse>() {
                @Override
                public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                    if (response.isSuccessful()){
                        menuDetailForYou = response.body().getMenuDetails();
                        fullViewMenuAdapter = new FullViewMenuAdapter(menuDetailForYou);
                        recyclerView3.setAdapter(fullViewMenuAdapter);
                    }else {
                        Toast.makeText(getContext(), "Failed to fetch data..", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MenuResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "API Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            Toast.makeText(getContext(), "Error occur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.restaurant_logo)
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
}