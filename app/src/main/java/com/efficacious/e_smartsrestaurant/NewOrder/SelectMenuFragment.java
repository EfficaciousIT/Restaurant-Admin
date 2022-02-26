package com.efficacious.e_smartsrestaurant.NewOrder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficacious.e_smartsrestaurant.Base.CheckInternetConnection;
import com.efficacious.e_smartsrestaurant.Base.HomeFragment;
import com.efficacious.e_smartsrestaurant.Base.MainActivity;
import com.efficacious.e_smartsrestaurant.Menu.MenuAdapter;
import com.efficacious.e_smartsrestaurant.Model.MenuDetail;
import com.efficacious.e_smartsrestaurant.Model.MenuResponse;
import com.efficacious.e_smartsrestaurant.Model.OrderDetails;
import com.efficacious.e_smartsrestaurant.R;
import com.efficacious.e_smartsrestaurant.Room.MenuData;
import com.efficacious.e_smartsrestaurant.Room.MenuDatabase;
import com.efficacious.e_smartsrestaurant.Room.TableBookDatabase;
import com.efficacious.e_smartsrestaurant.WaiterRole.WaiterHomeFragment;
import com.efficacious.e_smartsrestaurant.WebService.RetrofitClient;
import com.efficacious.e_smartsrestaurant.WebService.WebConstant;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectMenuFragment extends Fragment {

    List<MenuDetail> menuDetail;
    SelectMenuAdapter menuAdapter;
    RecyclerView recyclerView;
    String CategoryId,CategoryName;
    CheckInternetConnection checkInternetConnection;
    MenuDatabase menuDatabase;
    TableBookDatabase tableBookDatabase;
    SharedPreferences sharedPreferences;
    OnCartCountListener listener;
    FloatingActionButton btnCart;
    EditText btnSearch;
    @SuppressLint("UnsafeExperimentalUsageError")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_menu, container, false);
        setUpDB();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Choose menu");
        btnCart = view.findViewById(R.id.floatingCartBtn);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new CartFragment())
                        .addToBackStack(null).commit();
            }
        });

        btnCart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onGlobalLayout() {

                BadgeDrawable badgeDrawable = BadgeDrawable.create(getContext());
                listener = new OnCartCountListener() {
                    @Override
                    public void onAddToCart(int count) {
                        badgeDrawable.setNumber(count);
                    }
                };
                //Important to change the position of the Badge
                List<MenuData> Data = menuDatabase.dao().getMenuListData();
                int badgeCount = Data.size();
                badgeDrawable.setNumber(badgeCount);
                badgeDrawable.setHorizontalOffset(30);
                badgeDrawable.setVerticalOffset(20);

                BadgeUtils.attachBadgeDrawable(badgeDrawable, btnCart, null);

                btnCart.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


        checkInternetConnection = new CheckInternetConnection(getContext());
        if (!checkInternetConnection.isConnectingToInternet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Internet not Available !!");
            builder.setNegativeButton("Close", null);
            builder.show();
        }else {
            CategoryId = getArguments().getString("CategoryId");
            CategoryName = getArguments().getString("CategoryName");
            menuDetail = new ArrayList<>();
            if (!CategoryId.isEmpty()){
                menuList(CategoryId);
            }
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }

        btnSearch = view.findViewById(R.id.search);

        btnSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString(),view);
            }
        });

        return view;
    }

    private void filter(String text, View view) {
        ImageView imageView = view.findViewById(R.id.empty);
        TextView textView = view.findViewById(R.id.emptyTxt);

        List<MenuDetail> temp = new ArrayList();

        for(MenuDetail menuCat: menuDetail){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(menuCat.getMenuName().contains(text)){
                temp.add(menuCat);
            }
        }
        //update recyclerview
        menuAdapter.updateList(temp);
        if (temp.size() == 0){
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private void cartBadgeCountShow(int badgeCount ) {
        btnCart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onGlobalLayout() {
                BadgeDrawable badgeDrawable = BadgeDrawable.create(getContext());
                badgeDrawable.setNumber(badgeCount);
                //Important to change the position of the Badge
                badgeDrawable.setHorizontalOffset(30);
                badgeDrawable.setVerticalOffset(20);

                BadgeUtils.attachBadgeDrawable(badgeDrawable, btnCart, null);

                btnCart.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

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
                        menuAdapter = new SelectMenuAdapter(menuDetail,listener);
                        recyclerView.setAdapter(menuAdapter);
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

    private void setUpDB(){
        menuDatabase = Room.databaseBuilder(getContext(), MenuDatabase.class,"MenuDB")
                .allowMainThreadQueries().build();
        tableBookDatabase = Room.databaseBuilder(getContext(), TableBookDatabase.class,"TableBook")
                .allowMainThreadQueries().build();
    }

}